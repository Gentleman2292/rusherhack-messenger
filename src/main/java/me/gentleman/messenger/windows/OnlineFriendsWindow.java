package me.gentleman.messenger.windows;

import net.minecraft.client.multiplayer.PlayerInfo;
import org.rusherhack.client.api.Globals;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.render.graphic.VectorGraphic;
import org.rusherhack.client.api.ui.window.ResizeableWindow;
import org.rusherhack.client.api.ui.window.Window;
import org.rusherhack.client.api.ui.window.content.ComboContent;
import org.rusherhack.client.api.ui.window.content.ListItemContent;
import org.rusherhack.client.api.ui.window.content.component.ButtonComponent;
import org.rusherhack.client.api.ui.window.view.ListView;
import org.rusherhack.client.api.ui.window.view.TabbedView;
import org.rusherhack.client.api.ui.window.view.WindowView;

import java.util.ArrayList;
import java.util.List;

public class OnlineFriendsWindow extends ResizeableWindow {

	public static OnlineFriendsWindow INSTANCE;

	private final TabbedView tabView;
	public final RelationListView friendsView;

	private final List<RelationItem> friendItems = new ArrayList<>();

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


		this.friendsView = new RelationListView("Online friends", this, this.friendItems);

		ButtonComponent refreshButton = new ButtonComponent(this, "Refresh", this::resyncList);
		comboContent.addContent(refreshButton, ComboContent.AnchorSide.RIGHT);

		this.tabView = new TabbedView(this, List.of(comboContent, this.friendsView));

		RusherHackAPI.getEventBus().subscribe(this);
	}

	public void resyncList() {
		this.friendItems.clear();

		if (Globals.mc.player != null && Globals.mc.level != null) {
			List<String> friendNamesList = new ArrayList<>();

			for (PlayerInfo playerInfo : Globals.mc.player.connection.getOnlinePlayers()) {
				if (Globals.mc.level != null && RusherHackAPI.getRelationManager().isFriend(playerInfo.getProfile().getName())) {
					friendNamesList.add(playerInfo.getProfile().getName());
				}
			}

			for (String friendName : friendNamesList) {
				this.friendItems.add(new RelationItem(friendName, this.friendsView));
			}
		}

		this.friendsView.resort();
	}


	@Override
	public WindowView getRootView() {
		return this.tabView;
	}

	class RelationItem extends ListItemContent {
		public final String playerName;

		public RelationItem(String playerName, ListView<RelationItem> view) {
			super(OnlineFriendsWindow.this, view);
			this.playerName = playerName;
		}

		@Override
		public String getAsString(ListView<?>.Column column) {
			if (column.getName().equalsIgnoreCase("username")) {
				return this.playerName;
			}
			return "null";
		}
	}

	class RelationListView extends ListView<RelationItem> {

		public RelationListView(String name, Window window, List<RelationItem> items) {
			super(name, window, items);

			this.addColumn("Username");
		}
	}
}