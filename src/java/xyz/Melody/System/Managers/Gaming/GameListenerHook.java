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
    private Minecraft mc = Minecraft.func_71410_x();
    private Thread clientMainThread;

    @Override
    public void init() {
        Client.instance.logger.info("[Melody] [CONSOLE] Initializing Melody -> Main Thread.");
        this.initMainThread();
        EventBus.getInstance().register(this);
    }

    @EventHandler
    private void onFR(EventDrawText eventDrawText) {
        if (Client.playerName != null) {
            eventDrawText.setText(eventDrawText.getText().replaceAll(this.mc.func_110432_I().func_111285_a(), Client.playerName));
        }
        if (eventDrawText.getText().contains("Wither Impact")) {
            eventDrawText.setText(eventDrawText.getText().replaceAll("Wither Impact", "Genshin Impact"));
        }
    }

    private void initMainThread() {
        this.clientMainThread = new Thread(() -> {
            while (true) {
                try {
                    while (true) {
                        Thread.sleep(1000L);
                        if (Minecraft.func_71410_x().field_71439_g == null || Minecraft.func_71410_x().field_71441_e == null) continue;
                        ScoreObjective scoreObjective = Minecraft.func_71410_x().field_71441_e.func_96441_U().func_96539_a(1);
                        if (scoreObjective != null) {
                            Client.inSkyblock = StringUtil.removeFormatting(scoreObjective.func_96678_d()).contains("SKYBLOCK");
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
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                break;
            }
        });
        this.clientMainThread.setName("Melody -> Main Thread");
        this.clientMainThread.start();
    }
}

