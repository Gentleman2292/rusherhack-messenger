/*
 * Copyright (c) 2023-2024 Rusher Development LLC. All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited, proprietary, and confidential.
 */

package me.gentleman.messenger.windows;

import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.Globals;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.render.graphic.VectorGraphic;
import org.rusherhack.client.api.ui.window.ResizeableWindow;
import org.rusherhack.client.api.ui.window.Window;
import org.rusherhack.client.api.ui.window.content.ListItemContent;
import org.rusherhack.client.api.ui.window.view.ListView;
import org.rusherhack.client.api.ui.window.view.TabbedView;
import org.rusherhack.client.api.ui.window.view.WindowView;

import java.util.ArrayList;
import java.util.List;

public class OnlineFriendsWindow extends ResizeableWindow {

	public static OnlineFriendsWindow INSTANCE;

	private final TabbedView tabView;
	private final List<FriendItem> friendItems = new ArrayList<>();
	private String selectedFriend;

	public OnlineFriendsWindow() {
		super("Online Friends", 100, 325, 150, 100);
		INSTANCE = this;

		try {
			this.setIcon(new VectorGraphic("rusherhack/graphics/windows/relations_window.svg", 64, 64));
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setMinWidth(100);
		this.setMinHeight(100);

		// Initialize friendItems with current online friends
		updateFriendItems();

		this.tabView = new TabbedView(this, List.of(new FriendListView("Online Friends", this, this.friendItems)));
	}

	@Override
	public WindowView getRootView() {
		return this.tabView;
	}

	// Update friendItems based on current online friends
	private void updateFriendItems() {
		this.friendItems.clear();

		if (Globals.mc != null && Globals.mc.level != null && RusherHackAPI.getRelationManager() != null) {
			for (Player player : Globals.mc.level.players()) {
				if (player != null && RusherHackAPI.getRelationManager().isFriend(player.getName().getString())) {
					friendItems.add(new FriendItem(player.getName().getString()));
				}
			}
		}
	}

	public void refreshFriendItems() {
		updateFriendItems();
	}

	public String getSelectedFriend() {
		return selectedFriend;
	}

	class FriendItem extends ListItemContent {

		private final String playerName;

		public FriendItem(String playerName) {
			super(OnlineFriendsWindow.this, null);
			this.playerName = playerName;
		}

		@Override
		public String getAsString(ListView<?>.Column column) {
			if (column.getName().equalsIgnoreCase("username")) {
				return playerName;
			}
			return "null";
		}

		@Override
		protected void onDoubleClick() {
			if (isSelected()) {
				// Store the selected friend in the variable
				selectedFriend = playerName;
				System.out.println("Selected friend: " + selectedFriend);
			}
		}
	}

	class FriendListView extends ListView<FriendItem> {

		public FriendListView(String name, Window window, List<FriendItem> items) {
			super(name, window, items);

			this.addColumn("Username");
		}
	}
}