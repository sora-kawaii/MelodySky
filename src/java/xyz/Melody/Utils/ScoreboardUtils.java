/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;
import xyz.Melody.Utils.other.StringUtil;

public final class ScoreboardUtils {
    public static final String cleanSB(String string) {
        char[] cArray = StringUtils.stripControlCodes(string).toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : cArray) {
            if (c <= '\u0014' || c >= '\u007f') continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static final List<String> getScoreboard() {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (Minecraft.getMinecraft().theWorld == null) {
            return arrayList;
        }
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) {
            return arrayList;
        }
        ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreObjective == null) {
            return arrayList;
        }
        Collection<Score> collection = scoreboard.getSortedScores(scoreObjective);
        List list = collection.stream().filter(score -> score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
        collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
        for (Score score2 : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score2.getPlayerName());
            arrayList.add(ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score2.getPlayerName()));
        }
        return arrayList;
    }

    public static final int getLinesNumber() {
        return ScoreboardUtils.getScoreboard().size();
    }

    public static final boolean scoreboardContains(String string) {
        boolean bl = false;
        List<String> list = ScoreboardUtils.getScoreboard();
        for (String string2 : list) {
            string2 = ScoreboardUtils.cleanSB(string2);
            if (!(string2 = StringUtil.removeFormatting(string2)).contains(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public static final boolean scoreboardLowerContains(String string) {
        boolean bl = false;
        List<String> list = ScoreboardUtils.getScoreboard();
        for (String string2 : list) {
            string2 = ScoreboardUtils.cleanSB(string2).toLowerCase();
            if (!(string2 = StringUtil.removeFormatting(string2)).contains(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public static final String getLineThatContains(String string) {
        String string2 = null;
        List<String> list = ScoreboardUtils.getScoreboard();
        for (String string3 : list) {
            if (!(string3 = ScoreboardUtils.cleanSB(string3)).contains(string)) continue;
            string2 = string3;
            break;
        }
        return string2;
    }
}

