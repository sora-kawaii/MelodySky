/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.game;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public final class ChatUtils {
    private final ChatComponentText message;

    private ChatUtils(ChatComponentText message) {
        this.message = message;
    }

    public static String addFormat(String message, String regex) {
        return message.replaceAll("(?i)" + regex + "([0-9a-fklmnor])", "\u00a7$1");
    }

    public void displayClientSided() {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(this.message);
        }
    }

    private ChatComponentText getChatComponent() {
        return this.message;
    }

    ChatUtils(ChatComponentText chatComponentText, ChatUtils chatUtils) {
        this(chatComponentText);
    }

    public static class ChatMessageBuilder {
        private static final EnumChatFormatting defaultMessageColor = EnumChatFormatting.WHITE;
        private ChatComponentText theMessage = new ChatComponentText("");
        private boolean useDefaultMessageColor = false;
        private ChatStyle workingStyle = new ChatStyle();
        private ChatComponentText workerMessage = new ChatComponentText("");

        public ChatMessageBuilder(boolean prependDefaultPrefix, boolean useDefaultMessageColor) {
            if (prependDefaultPrefix) {
                this.theMessage.appendSibling(new ChatMessageBuilder(false, false).appendText(((StringBuilder)((Object)((Object)((Object)EnumChatFormatting.AQUA) + "Melody > "))).toString()).setColor(EnumChatFormatting.RED).build().getChatComponent());
            }
            this.useDefaultMessageColor = useDefaultMessageColor;
        }

        public ChatMessageBuilder() {
        }

        public ChatMessageBuilder appendText(String text) {
            this.appendSibling();
            this.workerMessage = new ChatComponentText(text);
            this.workingStyle = new ChatStyle();
            if (this.useDefaultMessageColor) {
                this.setColor(defaultMessageColor);
            }
            return this;
        }

        public ChatMessageBuilder setColor(EnumChatFormatting color) {
            this.workingStyle.setColor(color);
            return this;
        }

        public ChatMessageBuilder bold() {
            this.workingStyle.setBold(true);
            return this;
        }

        public ChatMessageBuilder italic() {
            this.workingStyle.setItalic(true);
            return this;
        }

        public ChatMessageBuilder strikethrough() {
            this.workingStyle.setStrikethrough(true);
            return this;
        }

        public ChatMessageBuilder underline() {
            this.workingStyle.setUnderlined(true);
            return this;
        }

        public ChatUtils build() {
            this.appendSibling();
            return new ChatUtils(this.theMessage, null);
        }

        private void appendSibling() {
            this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
        }
    }
}

