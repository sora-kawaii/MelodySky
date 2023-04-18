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
            Iterator iterator = entityLiving.field_70714_bg.field_75782_a.iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                if (!(object instanceof EntityAITasks.EntityAITaskEntry)) continue;
                EntityAITasks.EntityAITaskEntry entityAITaskEntry = (EntityAITasks.EntityAITaskEntry)object;
                if (entityAITaskEntry.field_75733_a instanceof EntityAIWatchClosest) {
                    iterator.remove();
                    continue;
                }
                if (!(entityAITaskEntry.field_75733_a instanceof EntityAILookIdle)) continue;
                iterator.remove();
            }
            if (entityLiving.func_70671_ap() == null || entityLiving.func_70671_ap().getClass() == EntityLookHelper.class) {
                object = entityLiving.func_70671_ap();
                ((EntityLivingAccessor)((Object)entityLiving)).setLookHelper(new FixedEntityLookHelper(entityLiving));
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setPosX(((EntityLookHelperAccessor)object).getPosX());
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setPosY(((EntityLookHelperAccessor)object).getPosY());
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setPosZ(((EntityLookHelperAccessor)object).getPosZ());
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setLooking(((EntityLookHelperAccessor)object).isLooking());
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setDeltaLookPitch(((EntityLookHelperAccessor)object).getDeltaLookPitch());
                ((EntityLookHelperAccessor)((Object)entityLiving.func_70671_ap())).setDeltaLookYaw(((EntityLookHelperAccessor)object).getDeltaLookYaw());
            }
        }
    }
}

