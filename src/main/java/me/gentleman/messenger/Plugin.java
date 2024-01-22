package me.gentleman.messenger;

import me.gentleman.messenger.windows.MessengerWindow;
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

		//register the window
		final MessengerWindow messengerWindow = new MessengerWindow();
		RusherHackAPI.getWindowManager().registerFeature(messengerWindow);
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("Messenger plugin unloaded!");
	}
	
}