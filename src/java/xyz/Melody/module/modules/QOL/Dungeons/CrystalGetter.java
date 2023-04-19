/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CrystalGetter
extends Module {
    private TimerUtil timer = new TimerUtil();
    private EntityEnderCrystal crystal;
    private ArrayList<EntityEnderCrystal> crystalList = new ArrayList();
    private Numbers<Double> range = new Numbers<Double>("Range", 6.0, 1.0, 6.0, 1.0);

    public CrystalGetter() {
        super("CrystalGetter", new String[]{"cr"}, ModuleType.Dungeons);
        this.addValues(this.range);
        this.setModInfo("Auto Get Crystal in Range in F7/M7.");
    }

    @EventHandler
    public void onTick(EventTick event) {
        this.crystal = this.getClosestCrystal();
    }

    @EventHandler
    public void onRightClick(EventPreUpdate event) {
        List<Entity> armorStand;
        if (this.crystal == null) {
            return;
        }
        if ((double)this.mc.thePlayer.getDistanceToEntity(this.crystal) > (Double)this.range.getValue()) {
            return;
        }
        if (this.crystal != null && this.timer.hasReached(200.0) && !(armorStand = this.mc.theWorld.getEntitiesInAABBexcluding(this.crystal, this.crystal.getEntityBoundingBox(), entity -> entity instanceof EntityArmorStand && entity.getCustomNameTag().contains("CLICK HERE"))).isEmpty() && armorStand.get(0) != null) {
            this.mc.playerController.interactWithEntitySendPacket(this.mc.thePlayer, armorStand.get(0));
            this.timer.reset();
        }
    }

    private EntityEnderCrystal getClosestCrystal() {
        this.crystalList.clear();
        for (Entity ent : this.mc.theWorld.loadedEntityList) {
            if (!(ent instanceof EntityEnderCrystal)) continue;
            this.crystalList.add((EntityEnderCrystal)ent);
        }
        this.crystalList.sort(this::lambda$getClosestCrystal$1);
        if (this.crystalList.size() > 0) {
            return this.crystalList.get(0);
        }
        return null;
    }

    private int lambda$getClosestCrystal$1(EntityEnderCrystal o1, EntityEnderCrystal o2) {
        return (int)(o1.getDistanceToEntity(this.mc.thePlayer) - o2.getDistanceToEntity(this.mc.thePlayer));
    }
}

