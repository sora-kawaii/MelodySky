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

    private ChatUtils(ChatComponentText chatComponentText) {
        this.message = chatComponentText;
    }

    public static String addFormat(String string, String string2) {
        return string.replaceAll("(?i)" + string2 + "([0-9a-fklmnor])", "\u00a7$1");
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

        public ChatMessageBuilder(boolean bl, boolean bl2) {
            if (bl) {
                this.theMessage.appendSibling(new ChatMessageBuilder(false, false).appendText(((StringBuilder)((Object)((Object)((Object)EnumChatFormatting.AQUA) + "Melody > "))).toString()).setColor(EnumChatFormatting.RED).build().getChatComponent());
            }
            this.useDefaultMessageColor = bl2;
        }

        public ChatMessageBuilder() {
        }

        public ChatMessageBuilder appendText(String string) {
            this.appendSibling();
            this.workerMessage = new ChatComponentText(string);
            this.workingStyle = new ChatStyle();
            if (this.useDefaultMessageColor) {
                this.setColor(defaultMessageColor);
            }
            return this;
        }

        public ChatMessageBuilder setColor(EnumChatFormatting enumChatFormatting) {
            this.workingStyle.setColor(enumChatFormatting);
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

