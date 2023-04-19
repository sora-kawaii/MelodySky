/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.Utils.game.ChatUtils;

public final class Helper {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessageOLD(String msg) {
        Object[] arrobject = new Object[2];
        Client.instance.getClass();
        arrobject[0] = (Object)((Object)EnumChatFormatting.BLUE) + "Melody" + (Object)((Object)EnumChatFormatting.GRAY) + ": ";
        arrobject[1] = msg;
        Helper.mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", arrobject)));
    }

    public static void sendMessage(Object message) {
        new ChatUtils.ChatMessageBuilder(true, true).appendText(message + "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessageWithoutPrefix(Object message) {
        new ChatUtils.ChatMessageBuilder(false, true).appendText(message + "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static boolean onServer(String server) {
        return !mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
    }
}

