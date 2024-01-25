package me.gentleman.messenger.util;

import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.Globals;
import org.rusherhack.client.api.RusherHackAPI;

import java.util.ArrayList;
import java.util.List;

public class FriendUtils {

    // Will need this later for the online friends list
    public static boolean isPlayerFriend(String targetPlayerName) {

        List<String> friendNamesList = new ArrayList<>();

        for (Player player : Globals.mc.level.players()) {
            if (Globals.mc.level != null && RusherHackAPI.getRelationManager().isFriend(player.getName().getString())) {
                // Player is a friend, add the name to the list
                friendNamesList.add(player.getName().getString());
            }
        }

        // Check if the target player name is in the list of friends
        return friendNamesList.contains(targetPlayerName);
    }
}
