/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.Math;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Performance.Math.FastTrig;
import xyz.Melody.Performance.Math.FixedEntityLookHelper;
import xyz.Melody.injection.mixins.entity.EntityLivingAccessor;
import xyz.Melody.injection.mixins.entity.EntityLookHelperAccessor;

public final class AIImprovements {
    public void postInit(FMLPostInitializationEvent event) {
        FastTrig.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving)entity;
            Iterator<EntityAITasks.EntityAITaskEntry> it = living.tasks.taskEntries.iterator();
            while (it.hasNext()) {
                EntityAITasks.EntityAITaskEntry obj = it.next();
                if (!(obj instanceof EntityAITasks.EntityAITaskEntry)) continue;
                EntityAITasks.EntityAITaskEntry task = obj;
                if (task.action instanceof EntityAIWatchClosest) {
                    it.remove();
                    continue;
                }
                if (!(task.action instanceof EntityAILookIdle)) continue;
                it.remove();
            }
            if (living.getLookHelper() == null || living.getLookHelper().getClass() == EntityLookHelper.class) {
                EntityLookHelper oldHelper = living.getLookHelper();
                ((EntityLivingAccessor)((Object)living)).setLookHelper(new FixedEntityLookHelper(living));
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setPosX(((EntityLookHelperAccessor)((Object)oldHelper)).getPosX());
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setPosY(((EntityLookHelperAccessor)((Object)oldHelper)).getPosY());
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setPosZ(((EntityLookHelperAccessor)((Object)oldHelper)).getPosZ());
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setLooking(((EntityLookHelperAccessor)((Object)oldHelper)).isLooking());
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setDeltaLookPitch(((EntityLookHelperAccessor)((Object)oldHelper)).getDeltaLookPitch());
                ((EntityLookHelperAccessor)((Object)living.getLookHelper())).setDeltaLookYaw(((EntityLookHelperAccessor)((Object)oldHelper)).getDeltaLookYaw());
            }
        }
    }
}

