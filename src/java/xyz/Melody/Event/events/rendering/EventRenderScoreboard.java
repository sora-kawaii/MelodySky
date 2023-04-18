/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.rendering;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import xyz.Melody.Event.Event;

public class EventRenderScoreboard
extends Event {
    private ScoreObjective objective;
    private ScaledResolution scaledRes;

    public EventRenderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {
        this.objective = scoreObjective;
        this.scaledRes = scaledResolution;
    }

    public ScoreObjective getObjective() {
        return this.objective;
    }

    public ScaledResolution getScaledRes() {
        return this.scaledRes;
    }
}

