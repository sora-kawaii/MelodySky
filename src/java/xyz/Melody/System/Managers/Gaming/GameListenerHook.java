/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Gaming;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventDrawText;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.other.StringUtil;

public final class GameListenerHook
implements Manager {
    private Minecraft mc = Minecraft.getMinecraft();
    private Thread clientMainThread;

    @Override
    public void init() {
        Client.instance.logger.info("[Melody] [CONSOLE] Initializing Melody -> Main Thread.");
        this.initMainThread();
        EventBus.getInstance().register(this);
    }

    @EventHandler
    private void onFR(EventDrawText event) {
        if (Client.playerName != null) {
            event.setText(event.getText().replaceAll(this.mc.getSession().getUsername(), Client.playerName));
        }
        if (event.getText().contains("Wither Impact")) {
            event.setText(event.getText().replaceAll("Wither Impact", "Genshin Impact"));
        }
    }

    private void initMainThread() {
        this.clientMainThread = new Thread(() -> {
            while (true) {
                try {
                    while (true) {
                        Thread.sleep(1000L);
                        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) continue;
                        ScoreObjective scoreboardObj = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                        if (scoreboardObj != null) {
                            Client.inSkyblock = StringUtil.removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
                        }
                        if ((Client.inDungeons = ScoreboardUtils.scoreboardContains("The Catacombs") ? true : ScoreboardUtils.scoreboardContains("Time Elapsed:") && ScoreboardUtils.scoreboardContains("Cleared: ")) && !Client.instance.dungeonUtils.dungeonThread.isAlive()) {
                            Client.instance.dungeonUtils.initDungeonThread();
                        }
                        Client.instance.sbArea.updateCurrentArea();
                        if (ScoreboardUtils.scoreboardContains("Slay the boss!")) {
                            Client.instance.slayerBossSpawned = true;
                            continue;
                        }
                        Client.instance.slayerBossSpawned = false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                break;
            }
        });
        this.clientMainThread.setName("Melody -> Main Thread");
        this.clientMainThread.start();
    }
}

