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
    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) {
        FastTrig.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent entityJoinWorldEvent) {
        Entity entity = entityJoinWorldEvent.entity;
        if (entity instanceof EntityLiving) {
            Object object;
            EntityLiving entityLiving = (EntityLiving)entity;
            Iterator<EntityAITasks.EntityAITaskEntry> iterator = entityLiving.tasks.taskEntries.iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                if (!(object instanceof EntityAITasks.EntityAITaskEntry)) continue;
                EntityAITasks.EntityAITaskEntry entityAITaskEntry = (EntityAITasks.EntityAITaskEntry)object;
                if (entityAITaskEntry.action instanceof EntityAIWatchClosest) {
                    iterator.remove();
                    continue;
                }
                if (!(entityAITaskEntry.action instanceof EntityAILookIdle)) continue;
                iterator.remove();
            }
            if (entityLiving.getLookHelper() == null || entityLiving.getLookHelper().getClass() == EntityLookHelper.class) {
                object = entityLiving.getLookHelper();
                ((EntityLivingAccessor)((Object)entityLiving)).setLookHelper(new FixedEntityLookHelper(entityLiving));
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setPosX(((EntityLookHelperAccessor)object).getPosX());
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setPosY(((EntityLookHelperAccessor)object).getPosY());
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setPosZ(((EntityLookHelperAccessor)object).getPosZ());
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setLooking(((EntityLookHelperAccessor)object).isLooking());
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setDeltaLookPitch(((EntityLookHelperAccessor)object).getDeltaLookPitch());
                ((EntityLookHelperAccessor)((Object)entityLiving.getLookHelper())).setDeltaLookYaw(((EntityLookHelperAccessor)object).getDeltaLookYaw());
            }
        }
    }
}

