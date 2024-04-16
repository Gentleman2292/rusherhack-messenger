package me.gentleman.messenger.windows;

import me.gentleman.messenger.module.MessengerSettings;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.rusherhack.client.api.Globals;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.render.graphic.VectorGraphic;
import org.rusherhack.client.api.system.IRelationManager;
import org.rusherhack.client.api.feature.window.ResizeableWindow;
import org.rusherhack.client.api.feature.window.Window;
import org.rusherhack.client.api.ui.window.content.ComboContent;
import org.rusherhack.client.api.ui.window.content.ListItemContent;
import org.rusherhack.client.api.ui.window.content.component.ButtonComponent;
import org.rusherhack.client.api.ui.window.view.ListView;
import org.rusherhack.client.api.ui.window.view.TabbedView;
import org.rusherhack.client.api.ui.window.view.WindowView;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.client.api.utils.objects.PlayerRelation;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OnlineFriendsWindow extends ResizeableWindow {

	public static OnlineFriendsWindow INSTANCE;

	private final TabbedView tabView;
	public final FriendListView friendsView;

	public final List<FriendItem> friendItems = new ArrayList<>();
	private boolean selectedFriendLoaded = false;
	private FriendItem selectedFriend; // Add a field to store the selected friend

	public OnlineFriendsWindow() {
		super("Online friends", 100, 325, 150, 100);
		INSTANCE = this;

		try {
			this.setIcon(new VectorGraphic("rusherhack/graphics/windows/relations_window.svg", 64, 64));
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setMinWidth(100);
		this.setMinHeight(100);
		final ComboContent comboContent = new ComboContent(this);


		this.friendsView = new FriendListView("Online friends", this, this.friendItems);

		ButtonComponent refreshButton = new ButtonComponent(this, "Refresh", () -> {
			resyncList();
		});
		comboContent.addContent(refreshButton, ComboContent.AnchorSide.RIGHT);

		this.tabView = new TabbedView(this, List.of(comboContent, this.friendsView));

		RusherHackAPI.getEventBus().subscribe(this);
	}

	public void resyncList() {
		this.selectedFriend = this.friendsView.getSelectedItem();
		this.friendItems.clear();

		if (Globals.mc.player != null && Globals.mc.level != null) {
			List<String> friendNamesList = new ArrayList<>();

			// Retrieve online friends
			for (PlayerInfo playerInfo : Globals.mc.player.connection.getOnlinePlayers()) {
				if (Globals.mc.level != null && !Globals.mc.player.getName().getString().equals(playerInfo.getProfile().getName()) && RusherHackAPI.getRelationManager().isFriend(playerInfo.getProfile().getName())) {
					friendNamesList.add(playerInfo.getProfile().getName());
				}
			}

			// Retrieve offline friends
			//WHY IS THIS NOT WORKING
			if (MessengerSettings.INSTANCE.showOfflineFriends.getValue()){
				ChatUtils.print("hi");
				IRelationManager relationManager = RusherHackAPI.getRelationManager();
				for (PlayerRelation relation : relationManager.getFriends()) {
					String friendName = relation.username();
				if (!friendNamesList.contains(friendName)) {
						ChatUtils.print(friendName);
						friendNamesList.add(friendName);
					}
				}
			}


			for (String friendName : friendNamesList) {
				this.friendItems.add(new FriendItem(friendName, this.friendsView));
			}
		}

		this.friendsView.resort();

		if (this.selectedFriend != null && this.friendItems.contains(this.selectedFriend)) {
			this.friendsView.setSelectedItem(this.selectedFriend);
		}
	}

	@Subscribe
	public void onUpdate(EventUpdate event) {
		if (!selectedFriendLoaded && !friendItems.isEmpty()) {
			OnlineFriendsWindow.FriendItem firstFriend = friendItems.get(0);

			if (firstFriend != null) {
				this.friendsView.setSelectedItem(firstFriend);
				firstFriend.reloadMessageHistory(firstFriend.playerName);
				selectedFriendLoaded = true;  // Set a flag to ensure this happens only once
			}
		}
	}

	@Override
	public WindowView getRootView() {
		return this.tabView;
	}

	class FriendItem extends ListItemContent {
		public final String playerName;
		private final String messageHistoryDirectory = "rusherhack/message_history/";
		public String messageHistoryFile = "";

		public FriendItem(String playerName, ListView<FriendItem> view) {
			super(OnlineFriendsWindow.this, view);
			this.playerName = playerName;

			File directory = new File(messageHistoryDirectory);
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					System.out.println("Message history directory created successfully");
				} else {
					System.err.println("Failed to create message history directory");
				}
			}
		}

		@Override
		public String getAsString(ListView<?>.Column column) {
			if (column.getName().equalsIgnoreCase("username")) {
				return this.playerName;
			}
			return "null";
		}

		private void loadMessages(String filename) {
			if (!new File(filename).exists()) {
				return;
			}

			try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
				String line;
				while ((line = reader.readLine()) != null) {
					displayMessage(line, line.startsWith("To: "));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void addMessage(String message, boolean isYourMessage) {
			this.messageHistoryFile = messageHistoryDirectory + "/" + playerName + ".txt";
			String formattedMessage = (isYourMessage ? "To: " : "From: " + playerName + ": ") + message;
			saveMessageToFile(formattedMessage);
			displayMessage(formattedMessage, isYourMessage);
		}

		private void saveMessageToFile(String message) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(messageHistoryFile), true))) {
				writer.write(message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void displayMessage(String formattedMessage, boolean isYourMessage) {
			int color = isYourMessage ? Color.white.getRGB() : Color.lightGray.getRGB();
			MessengerWindow.INSTANCE.getMessageView().add(formattedMessage, color);
		}

		public void reloadMessageHistory(String filename) {
			MessengerWindow.INSTANCE.getMessageView().clear();
			loadMessages(messageHistoryDirectory + filename + ".txt"); // Pass the filename based on the friend's name
		}
	}



	class FriendListView extends ListView<FriendItem> {

		public FriendListView(String name, Window window, List<FriendItem> items) {
			super(name, window, items);

			this.addColumn("Username");
		}
	}
}