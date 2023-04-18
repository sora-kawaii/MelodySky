/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Dungeons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.System.Managers.Dungeons.DungeonTypes;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.TimerUtil;

public final class DungeonListener
implements Manager {
    private Minecraft mc = Minecraft.getMinecraft();
    public DungeonTypes floor = DungeonTypes.NULL;
    public boolean inBoss = false;
    public ArrayList<EntityPlayer> teammates = new ArrayList();
    public int secretsFound = 0;
    public int cryptsFound = 0;
    public boolean foundMimic = false;
    public int deaths = 0;
    public boolean activeRun = false;
    public int score = 0;
    public Pattern scorePattern = Pattern.compile("Cleared: [0-9]{1,3}% \\((?<score>[0-9]+)\\)");
    public Thread dungeonThread;
    public TimerUtil daemonTimer = new TimerUtil();
    public List<String> entryMessages = Arrays.asList("[BOSS] Bonzo:", "[BOSS] Scarf:", "[BOSS] The Professor:", "[BOSS] Thorn:", "[BOSS] Livid:", "[BOSS] Sadan:", "[BOSS] Maxor:");
    private Pattern numberPattern = Pattern.compile("[^0-9.]");

    @Override
    public void init() {
        Client.instance.logger.info("[Melody] [CONSOLE] Initializing Melody -> Dungeon Listener.");
        EventBus.getInstance().register(this);
    }

    public void initDungeonThread() {
        this.dungeonThread = new Thread(() -> {
            block2: while (true) {
                try {
                    while (true) {
                        Matcher matcher;
                        String string;
                        Thread.sleep(1500L);
                        if (this.mc.theWorld == null || this.mc.thePlayer == null) continue;
                        if (!Client.inDungeons) {
                            this.reset();
                            break block2;
                        }
                        this.updateFloor();
                        List<String> list = this.getTabList();
                        if (list != null) {
                            this.updateStats(list);
                        }
                        if (list != null && this.teammates.isEmpty()) {
                            this.updateTeammates(list);
                        }
                        if ((string = ScoreboardUtils.getLineThatContains("Cleared: ")) == null || !(matcher = this.scorePattern.matcher(string)).matches()) continue;
                        String string2 = matcher.group("score");
                        this.score = Integer.parseInt(string2);
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                break;
            }
        });
        this.dungeonThread.setName("Melody -> Dungeon Thread");
        this.dungeonThread.start();
    }

    public void reset() {
        this.floor = DungeonTypes.NULL;
        this.inBoss = false;
        this.secretsFound = 0;
        this.cryptsFound = 0;
        this.foundMimic = false;
        this.deaths = 0;
        this.activeRun = false;
        this.score = 0;
        this.teammates.clear();
    }

    @EventHandler
    public void onChatPacket(EventPacketRecieve eventPacketRecieve) {
        String string;
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (Client.inDungeons && eventPacketRecieve.getPacket() instanceof S02PacketChat && ((S02PacketChat)eventPacketRecieve.getPacket()).getType() != 2 && "[NPC] Mort: Here, I found this map when I first entered the dungeon.".equals(string = StringUtils.stripControlCodes(((S02PacketChat)eventPacketRecieve.getPacket()).getChatComponent().getUnformattedText()))) {
            this.updateTeammates(this.getTabList());
        }
    }

    public void updateTeammates(List<String> list) {
        this.teammates.clear();
        for (int i = 0; i < 5; ++i) {
            String string = StringUtils.stripControlCodes(list.get(1 + i * 4)).trim();
            String string2 = string.split(" ")[0];
            if (Objects.equals(string2, "")) continue;
            for (EntityPlayer entityPlayer : this.mc.theWorld.playerEntities) {
                if (!entityPlayer.getName().contains(string2)) continue;
                this.teammates.add(entityPlayer);
            }
        }
    }

    public void updateFloor() {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (Client.inDungeons) {
            if (ScoreboardUtils.scoreboardContains("(F1)")) {
                this.floor = DungeonTypes.F1;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F2)")) {
                this.floor = DungeonTypes.F2;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F3)")) {
                this.floor = DungeonTypes.F3;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F4)")) {
                this.floor = DungeonTypes.F4;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F5)")) {
                this.floor = DungeonTypes.F5;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F6)")) {
                this.floor = DungeonTypes.F6;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F7)")) {
                this.floor = DungeonTypes.F7;
                Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(M1)")) {
                this.floor = DungeonTypes.M1;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M2)")) {
                this.floor = DungeonTypes.M2;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M3)")) {
                this.floor = DungeonTypes.M3;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M4)")) {
                this.floor = DungeonTypes.M4;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M5)")) {
                this.floor = DungeonTypes.M5;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M6)")) {
                this.floor = DungeonTypes.M6;
                Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M7)")) {
                this.floor = DungeonTypes.M7;
                Client.isMMD = true;
            } else if ((ScoreboardUtils.scoreboardContains("Dragon") || ScoreboardUtils.scoreboardContains("No Alive Dragons")) && ScoreboardUtils.scoreboardContains("Time Elapsed:") && ScoreboardUtils.scoreboardContains("Cleard: ")) {
                this.floor = DungeonTypes.M7;
                Client.isMMD = true;
            } else {
                this.floor = DungeonTypes.NULL;
                Client.isMMD = false;
            }
        } else {
            this.floor = DungeonTypes.NULL;
            Client.isMMD = false;
        }
    }

    public void updateStats(List<String> list) {
        try {
            for (String string : list) {
                String string2;
                if (string.contains("Deaths: ")) {
                    string2 = this.removeAllExceptNumbersAndPeriods(string = StringUtils.stripControlCodes(string));
                    if (string2.isEmpty()) continue;
                    this.deaths = Integer.parseInt(string2);
                    continue;
                }
                if (string.contains("Secrets Found: ") && !string.contains("%")) {
                    string2 = this.removeAllExceptNumbersAndPeriods(string = StringUtils.stripControlCodes(string));
                    if (string2.isEmpty()) continue;
                    this.secretsFound = Integer.parseInt(string2);
                    continue;
                }
                if (!string.contains("Crypts: ") || (string2 = this.removeAllExceptNumbersAndPeriods(string = StringUtils.stripControlCodes(string))).isEmpty()) continue;
                this.cryptsFound = Integer.parseInt(string2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Client.instance.logger.error("Exception in class DungeonListener.");
        }
    }

    public List<String> getTabList() {
        List<String> list = PlayerListUtils.getTabListListStr();
        if (!StringUtils.stripControlCodes(list.get(0)).contains("Party")) {
            return null;
        }
        return list;
    }

    public boolean inFloor(DungeonTypes ... dungeonTypesArray) {
        for (DungeonTypes dungeonTypes : dungeonTypesArray) {
            if (dungeonTypes != this.floor) continue;
            return true;
        }
        return false;
    }

    public void debug() {
        if (Client.inDungeons) {
            Helper.sendMessage("Floor: " + this.floor.name());
            Helper.sendMessage("In Boss: " + this.inBoss);
            Helper.sendMessage("Teams:");
            for (EntityPlayer entityPlayer : this.teammates) {
                Helper.sendMessage("- " + entityPlayer.getName());
            }
        } else {
            Helper.sendMessage("You must be in a dungeon to debug a dungeon!");
        }
    }

    public String removeAllExceptNumbersAndPeriods(String string) {
        return this.numberPattern.matcher(string).replaceAll("");
    }
}

