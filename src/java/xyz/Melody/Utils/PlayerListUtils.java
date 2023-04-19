/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.StringUtils;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerListUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator(null));
    public static final Ordering<NetworkPlayerInfo> playerInfoOrdering2 = new Ordering<NetworkPlayerInfo>(){

        @Override
        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "")), (Comparable<?>)((Object)(scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : ""))).compare((Comparable<?>)((Object)p_compare_1_.getGameProfile().getName()), (Comparable<?>)((Object)p_compare_2_.getGameProfile().getName())).result();
        }

        @Override
        public int compare(Object object, Object object2) {
            return this.compare((NetworkPlayerInfo)object, (NetworkPlayerInfo)object2);
        }
    };

    public static GuiPlayerTabOverlay getTabList() {
        return PlayerListUtils.mc.ingameGUI.getTabList();
    }

    public static List<NetworkPlayerInfo> getTabEntries() {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return Collections.emptyList();
        }
        return playerInfoOrdering2.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
    }

    public static List<String> getTabListListStr() {
        return PlayerListUtils.getTabEntries().stream().map(playerInfo -> Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName((NetworkPlayerInfo)playerInfo)).collect(Collectors.toList());
    }

    public static boolean tabContains(String str) {
        List<NetworkPlayerInfo> players = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo info : players) {
            String name = StringUtils.stripControlCodes(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info));
            if (!name.contains(str)) continue;
            return true;
        }
        return false;
    }

    public static String copyContainsLine(String str) {
        List<NetworkPlayerInfo> players = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo info : players) {
            String name = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info);
            if (!name.contains(str)) continue;
            return name;
        }
        return null;
    }

    @SideOnly(value=Side.CLIENT)
    static class PlayerComparator
    implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        @Override
        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            ScorePlayerTeam team1 = o1.getPlayerTeam();
            ScorePlayerTeam team2 = o2.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(o1.getGameType() != WorldSettings.GameType.SPECTATOR, o2.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(team1 != null ? team1.getRegisteredName() : "")), (Comparable<?>)((Object)(team2 != null ? team2.getRegisteredName() : ""))).compare((Comparable<?>)((Object)o1.getGameProfile().getName()), (Comparable<?>)((Object)o2.getGameProfile().getName())).result();
        }

        @Override
        public int compare(Object object, Object object2) {
            return this.compare((NetworkPlayerInfo)object, (NetworkPlayerInfo)object2);
        }

        PlayerComparator(lI x0) {
            this();
        }
    }
}

