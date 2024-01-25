package me.gentleman.messenger.util;

import org.rusherhack.client.api.Globals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class RegexUtils {

    // LivemessageUtil's patterns
    private static final List<Pattern> FROM_PATTERNS = LivemessageUtil.FROM_PATTERNS;

    public static ChatMessageInfo extractPlayerAndMessage(String chatPacketContent) {
        for (Pattern pattern : FROM_PATTERNS) {
            Matcher matcher = pattern.matcher(chatPacketContent);
            if (matcher.find()) {
                String playerName = matcher.group(1);
                String message = matcher.group(2);

                if (playerName.equals(Globals.mc.player.getName())) {
                    return new ChatMessageInfo(null, null);
                }

                return new ChatMessageInfo(playerName, message);
            }
        }

        // If no patterns matched, return a default ChatMessageInfo
        return new ChatMessageInfo(null, null);
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
