/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AimDragonCrystals
extends Module {
    private EntityEnderCrystal curCrystal = null;
    private TimerUtil timer = new TimerUtil();

    public AimDragonCrystals() {
        super("AimCrystals", new String[]{"shootcs"}, ModuleType.QOL);
        this.setModInfo("Auto Aim Crystals in Dragon Fucking.");
    }

    @EventHandler
    private void onTick(EventPreUpdate eventPreUpdate) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.The_End) {
            return;
        }
        if (this.curCrystal == null || !this.curCrystal.isEntityAlive() || this.timer.hasReached(500.0)) {
            this.curCrystal = this.getCrystal();
            this.timer.reset();
        }
        ItemStack itemStack = this.mc.thePlayer.getHeldItem();
        if (this.curCrystal != null && itemStack != null && itemStack.getItem() instanceof ItemBow && this.curCrystal.isEntityAlive()) {
            float f = 1.0f;
            double d = f * 3.0f;
            double d2 = 0.05f;
            double d3 = this.curCrystal.posX - this.mc.thePlayer.posX + (this.curCrystal.posX - this.curCrystal.lastTickPosX) * (double)(f * 10.0f);
            double d4 = this.curCrystal.posZ - this.mc.thePlayer.posZ + (this.curCrystal.posZ - this.curCrystal.lastTickPosZ) * (double)(f * 10.0f);
            float f2 = (float)(Math.atan2(d4, d3) * 180.0 / Math.PI) - 90.0f;
            float f3 = (float)((double)((float)(-Math.toDegrees(this.getLaunchAngle(this.curCrystal, d, d2)))) - 3.8);
            if (f2 <= 360.0f && f3 <= 360.0f) {
                eventPreUpdate.setYaw(f2);
                eventPreUpdate.setPitch(f3);
            }
        }
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        if (this.curCrystal != null && this.curCrystal.isEntityAlive()) {
            RenderUtil.drawFilledESP(this.curCrystal, Color.PINK, eventRender3D, 3.0f);
        }
    }

    private EntityEnderCrystal getCrystal() {
        ArrayList<EntityEnderCrystal> arrayList = new ArrayList<EntityEnderCrystal>();
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            arrayList.add((EntityEnderCrystal)entity);
        }
        arrayList.sort(Comparator.comparingDouble(entityEnderCrystal -> this.mc.thePlayer.getDistanceToEntity((Entity)entityEnderCrystal)));
        if (!arrayList.isEmpty()) {
            return (EntityEnderCrystal)arrayList.get(0);
        }
        return null;
    }

    private float getLaunchAngle(EntityEnderCrystal entityEnderCrystal, double d, double d2) {
        double d3 = entityEnderCrystal.posY + (double)(entityEnderCrystal.getEyeHeight() / 2.0f) - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight());
        double d4 = entityEnderCrystal.posX - this.mc.thePlayer.posX;
        double d5 = entityEnderCrystal.posZ - this.mc.thePlayer.posZ;
        double d6 = Math.sqrt(d4 * d4 + d5 * d5);
        return this.theta(d + 2.0, d2, d6, d3);
    }

    private float theta(double d, double d2, double d3, double d4) {
        double d5 = 2.0 * d4 * (d * d);
        double d6 = d2 * (d3 * d3);
        double d7 = d2 * (d6 + d5);
        double d8 = d * d * d * d - d7;
        double d9 = Math.sqrt(d8);
        double d10 = d * d + d9;
        double d11 = d * d - d9;
        double d12 = Math.atan2(d10, d2 * d3);
        double d13 = Math.atan2(d11, d2 * d3);
        return (float)Math.min(d12, d13);
    }
}

