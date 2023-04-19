/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.network.play.server.S02PacketChat;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoGG
extends Module {
    public AutoGG() {
        super("AutoPlay", new String[]{"AutoPlay", "AutoGG"}, ModuleType.Others);
    }

    @EventHandler
    public void onPacket(EventPacketRecieve event) {
        S02PacketChat packet;
        String game;
        if (event.getPacket() instanceof S02PacketChat && !(game = this.getSubString((packet = (S02PacketChat)event.getPacket()).getChatComponent().toString(), "style=Style{hasParent=true, color=\ufffd\ufffdb, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=RUN_COMMAND, value='/play ", "'},")).contains("TextComponent") && !game.equalsIgnoreCase("")) {
            this.next(game);
            this.mc.thePlayer.sendChatMessage("GG");
        }
    }

    private String getSubString(String text, String left, String right) {
        int zLen;
        String result = "";
        zLen = left == null || left.isEmpty() ? 0 : ((zLen = text.indexOf(left)) > -1 ? (zLen += left.length()) : 0);
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    private void next(final String game) {
        NotificationPublisher.queue("AutoPlay", "Sending You to a New Game In 3s.", NotificationType.INFO, 3000, true);
        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                AutoGG.this.mc.thePlayer.sendChatMessage("/play " + game);
            }
        }, 3000L);
    }
}

