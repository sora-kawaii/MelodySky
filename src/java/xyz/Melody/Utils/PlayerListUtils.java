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
    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());
    public static final Ordering<NetworkPlayerInfo> playerInfoOrdering2 = new Ordering<NetworkPlayerInfo>(){

        @Override
        public int compare(NetworkPlayerInfo networkPlayerInfo, NetworkPlayerInfo networkPlayerInfo2) {
            ScorePlayerTeam scorePlayerTeam = networkPlayerInfo.getPlayerTeam();
            ScorePlayerTeam scorePlayerTeam2 = networkPlayerInfo2.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(scorePlayerTeam != null ? scorePlayerTeam.getRegisteredName() : "")), (Comparable<?>)((Object)(scorePlayerTeam2 != null ? scorePlayerTeam2.getRegisteredName() : ""))).compare((Comparable<?>)((Object)networkPlayerInfo.getGameProfile().getName()), (Comparable<?>)((Object)networkPlayerInfo2.getGameProfile().getName())).result();
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
        return PlayerListUtils.getTabEntries().stream().map(networkPlayerInfo -> Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName((NetworkPlayerInfo)networkPlayerInfo)).collect(Collectors.toList());
    }

    public static boolean tabContains(String string) {
        List<NetworkPlayerInfo> list = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo networkPlayerInfo : list) {
            String string2 = StringUtils.stripControlCodes(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkPlayerInfo));
            if (!string2.contains(string)) continue;
            return true;
        }
        return false;
    }

    public static String copyContainsLine(String string) {
        List<NetworkPlayerInfo> list = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo networkPlayerInfo : list) {
            String string2 = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkPlayerInfo);
            if (!string2.contains(string)) continue;
            return string2;
        }
        return null;
    }

    @SideOnly(value=Side.CLIENT)
    static class PlayerComparator
    implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        @Override
        public int compare(NetworkPlayerInfo networkPlayerInfo, NetworkPlayerInfo networkPlayerInfo2) {
            ScorePlayerTeam scorePlayerTeam = networkPlayerInfo.getPlayerTeam();
            ScorePlayerTeam scorePlayerTeam2 = networkPlayerInfo2.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(scorePlayerTeam != null ? scorePlayerTeam.getRegisteredName() : "")), (Comparable<?>)((Object)(scorePlayerTeam2 != null ? scorePlayerTeam2.getRegisteredName() : ""))).compare((Comparable<?>)((Object)networkPlayerInfo.getGameProfile().getName()), (Comparable<?>)((Object)networkPlayerInfo2.getGameProfile().getName())).result();
        }
    }
}

