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
    public static Minecraft mc = Minecraft.func_71410_x();

    public static void sendMessageOLD(String string) {
        Object[] objectArray = new Object[2];
        Client.instance.getClass();
        objectArray[0] = (Object)((Object)EnumChatFormatting.BLUE) + "Melody" + (Object)((Object)EnumChatFormatting.GRAY) + ": ";
        objectArray[1] = string;
        Helper.mc.field_71439_g.func_145747_a(new ChatComponentText(String.format("%s%s", objectArray)));
    }

    public static void sendMessage(Object object) {
        new ChatUtils.ChatMessageBuilder(true, true).appendText(object + "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessageWithoutPrefix(Object object) {
        new ChatUtils.ChatMessageBuilder(false, true).appendText(object + "").setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static boolean onServer(String string) {
        return !mc.func_71356_B() && Helper.mc.func_147104_D().field_78845_b.toLowerCase().contains(string);
    }
}

