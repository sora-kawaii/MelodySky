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
    public static final String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (char c : nvString) {
            if (c <= '\u0014' || c >= '\u007f') continue;
            cleaned.append(c);
        }
        return cleaned.toString();
    }

    public static final List<String> getScoreboard() {
        ArrayList<String> lines = new ArrayList<String>();
        if (Minecraft.getMinecraft().theWorld == null) {
            return lines;
        }
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return lines;
        }
        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List list = scores.stream().filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#")).collect(Collectors.toList());
        scores = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, scores.size() - 15)) : list;
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }
        return lines;
    }

    public static final int getLinesNumber() {
        return ScoreboardUtils.getScoreboard().size();
    }

    public static final boolean scoreboardContains(String string) {
        boolean result = false;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for (String line : scoreboard) {
            line = ScoreboardUtils.cleanSB(line);
            if (!(line = StringUtil.removeFormatting(line)).contains(string)) continue;
            result = true;
            break;
        }
        return result;
    }

    public static final boolean scoreboardLowerContains(String string) {
        boolean result = false;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for (String line : scoreboard) {
            line = ScoreboardUtils.cleanSB(line).toLowerCase();
            if (!(line = StringUtil.removeFormatting(line)).contains(string)) continue;
            result = true;
            break;
        }
        return result;
    }

    public static final String getLineThatContains(String string) {
        String result = null;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for (String line : scoreboard) {
            if (!(line = ScoreboardUtils.cleanSB(line)).contains(string)) continue;
            result = line;
            break;
        }
        return result;
    }
}

