package me.gentleman.messenger.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class RegexUtils {

    // LivemessageUtil's patterns
    private static final List<Pattern> FROM_PATTERNS = LivemessageUtil.FROM_PATTERNS;
    private static final List<Pattern> TO_PATTERNS = LivemessageUtil.TO_PATTERNS;

    public static ChatMessageInfo extractPlayerAndMessage(String chatPacketContent) {
        // Use LivemessageUtil's FROM_PATTERNS and TO_PATTERNS
        for (Pattern pattern : FROM_PATTERNS) {
            Matcher matcher = pattern.matcher(chatPacketContent);
            if (matcher.find()) {
                String playerName = matcher.group(1);
                String message = matcher.group(2);
                return new ChatMessageInfo(playerName, message);
            }
        }

        for (Pattern pattern : TO_PATTERNS) {
            Matcher matcher = pattern.matcher(chatPacketContent);
            if (matcher.find()) {
                String playerName = matcher.group(1);
                String message = matcher.group(2);
                return new ChatMessageInfo(playerName, message);
            }
        }

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
