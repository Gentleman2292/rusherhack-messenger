package me.gentleman.messenger.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static ChatMessageInfo extractPlayerAndMessage(String chatPacketContent) {
        String regex = "^<([^>]+)>\\s*(.*)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(chatPacketContent);

        String playerName = null;
        String message = null;

        if (matcher.find()) {
            playerName = matcher.group(1);
            message = matcher.group(2);
        }

        return new ChatMessageInfo(playerName, message);
    }

    public static class ChatMessageInfo {
        private final String playerName;
        private final String message;

        public ChatMessageInfo(String playerName, String message) {
            this.playerName = playerName;
            this.message = message;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getMessage() {
            return message;
        }
    }
}
