/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.Math.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public final class FixedEntityLookAtClosest
extends EntityAIWatchClosest {
    public FixedEntityLookAtClosest(EntityLiving entity, Class cz, float distance) {
        super(entity, cz, distance);
    }

    public FixedEntityLookAtClosest(EntityLiving entity, Class cz, float distance, float updateRndTrigger) {
        super(entity, cz, distance, updateRndTrigger);
    }
}

