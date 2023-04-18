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
    public void onPacket(EventPacketRecieve eventPacketRecieve) {
        S02PacketChat s02PacketChat;
        String string;
        if (eventPacketRecieve.getPacket() instanceof S02PacketChat && !(string = this.getSubString((s02PacketChat = (S02PacketChat)eventPacketRecieve.getPacket()).getChatComponent().toString(), "style=Style{hasParent=true, color=\ufffd\ufffdb, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=RUN_COMMAND, value='/play ", "'},")).contains("TextComponent") && !string.equalsIgnoreCase("")) {
            this.next(string);
            this.mc.thePlayer.sendChatMessage("GG");
        }
    }

    private String getSubString(String string, String string2, String string3) {
        int n;
        String string4 = "";
        n = string2 == null || string2.isEmpty() ? 0 : ((n = string.indexOf(string2)) > -1 ? (n += string2.length()) : 0);
        int n2 = string.indexOf(string3, n);
        if (n2 < 0 || string3 == null || string3.isEmpty()) {
            n2 = string.length();
        }
        string4 = string.substring(n, n2);
        return string4;
    }

    private void next(final String string) {
        NotificationPublisher.queue("AutoPlay", "Sending You to a New Game In 3s.", NotificationType.INFO, 3000, true);
        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                AutoGG.this.mc.thePlayer.sendChatMessage("/play " + string);
            }
        }, 3000L);
    }
}

