package me.gentleman.messenger;

import me.gentleman.messenger.module.MessengerSettings;
import me.gentleman.messenger.windows.MessengerWindow;
import me.gentleman.messenger.windows.OnlineFriendsWindow;
import org.rusherhack.client.api.RusherHackAPI;

/**
 * Messenger rusherhack plugin
 *
 * @author Gentleman
 */
public class Plugin extends org.rusherhack.client.api.plugin.Plugin {
	
	@Override
	public void onLoad() {
		
		//logger
		this.getLogger().info("Messenger plugin loaded!");

		final MessengerWindow messengerWindow = new MessengerWindow();
		final OnlineFriendsWindow onlineFriendsWindow= new OnlineFriendsWindow();
		final MessengerSettings messengerSettings = new MessengerSettings();

		RusherHackAPI.getWindowManager().registerFeature(messengerWindow);
		RusherHackAPI.getWindowManager().registerFeature(onlineFriendsWindow);
		RusherHackAPI.getModuleManager().registerFeature(messengerSettings);
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("Messenger plugin unloaded!");
	}
	
}