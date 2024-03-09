package me.gentleman.messenger.module;

import org.rusherhack.client.api.feature.module.Module;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.core.setting.BooleanSetting;

public class MessengerSettings extends Module {

    /**
     * Settings
     */
    public BooleanSetting Notifications = new BooleanSetting("in-game notifications", true);
    public BooleanSetting showOfflineFriends = new BooleanSetting("Show offline friends", false);

    public MessengerSettings() {
        super("Messenger settings", "settings for the messenger plugin", ModuleCategory.CLIENT);

        this.registerSettings(
                this.Notifications,
                this.showOfflineFriends
        );
    }
}
