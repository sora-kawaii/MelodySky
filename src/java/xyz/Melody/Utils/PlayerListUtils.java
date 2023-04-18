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
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());
    public static final Ordering<NetworkPlayerInfo> playerInfoOrdering2 = new Ordering<NetworkPlayerInfo>(){

        @Override
        public int compare(NetworkPlayerInfo networkPlayerInfo, NetworkPlayerInfo networkPlayerInfo2) {
            ScorePlayerTeam scorePlayerTeam = networkPlayerInfo.func_178850_i();
            ScorePlayerTeam scorePlayerTeam2 = networkPlayerInfo2.func_178850_i();
            return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.func_178848_b() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.func_178848_b() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(scorePlayerTeam != null ? scorePlayerTeam.func_96661_b() : "")), (Comparable<?>)((Object)(scorePlayerTeam2 != null ? scorePlayerTeam2.func_96661_b() : ""))).compare((Comparable<?>)((Object)networkPlayerInfo.func_178845_a().getName()), (Comparable<?>)((Object)networkPlayerInfo2.func_178845_a().getName())).result();
        }
    };

    public static GuiPlayerTabOverlay getTabList() {
        return PlayerListUtils.mc.field_71456_v.func_175181_h();
    }

    public static List<NetworkPlayerInfo> getTabEntries() {
        if (Minecraft.func_71410_x().field_71439_g == null) {
            return Collections.emptyList();
        }
        return playerInfoOrdering2.sortedCopy(Minecraft.func_71410_x().field_71439_g.field_71174_a.func_175106_d());
    }

    public static List<String> getTabListListStr() {
        return PlayerListUtils.getTabEntries().stream().map(networkPlayerInfo -> Minecraft.func_71410_x().field_71456_v.func_175181_h().func_175243_a((NetworkPlayerInfo)networkPlayerInfo)).collect(Collectors.toList());
    }

    public static boolean tabContains(String string) {
        List list = playerOrdering.sortedCopy(Minecraft.func_71410_x().field_71439_g.field_71174_a.func_175106_d());
        for (NetworkPlayerInfo networkPlayerInfo : list) {
            String string2 = StringUtils.func_76338_a((String)Minecraft.func_71410_x().field_71456_v.func_175181_h().func_175243_a(networkPlayerInfo));
            if (!string2.contains(string)) continue;
            return true;
        }
        return false;
    }

    public static String copyContainsLine(String string) {
        List list = playerOrdering.sortedCopy(Minecraft.func_71410_x().field_71439_g.field_71174_a.func_175106_d());
        for (NetworkPlayerInfo networkPlayerInfo : list) {
            String string2 = Minecraft.func_71410_x().field_71456_v.func_175181_h().func_175243_a(networkPlayerInfo);
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
            ScorePlayerTeam scorePlayerTeam = networkPlayerInfo.func_178850_i();
            ScorePlayerTeam scorePlayerTeam2 = networkPlayerInfo2.func_178850_i();
            return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.func_178848_b() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.func_178848_b() != WorldSettings.GameType.SPECTATOR).compare((Comparable<?>)((Object)(scorePlayerTeam != null ? scorePlayerTeam.func_96661_b() : "")), (Comparable<?>)((Object)(scorePlayerTeam2 != null ? scorePlayerTeam2.func_96661_b() : ""))).compare((Comparable<?>)((Object)networkPlayerInfo.func_178845_a().getName()), (Comparable<?>)((Object)networkPlayerInfo2.func_178845_a().getName())).result();
        }
    }
}

