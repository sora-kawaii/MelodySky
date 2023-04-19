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
    private void onTick(EventPreUpdate event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.The_End) {
            return;
        }
        if (this.curCrystal == null || !this.curCrystal.isEntityAlive() || this.timer.hasReached(500.0)) {
            this.curCrystal = this.getCrystal();
            this.timer.reset();
        }
        ItemStack is = this.mc.thePlayer.getHeldItem();
        if (this.curCrystal != null && is != null && is.getItem() instanceof ItemBow && this.curCrystal.isEntityAlive()) {
            float bowVelocity = 1.0f;
            double v = bowVelocity * 3.0f;
            double g = 0.05f;
            double xDistance = this.curCrystal.posX - this.mc.thePlayer.posX + (this.curCrystal.posX - this.curCrystal.lastTickPosX) * (double)(bowVelocity * 10.0f);
            double zDistance = this.curCrystal.posZ - this.mc.thePlayer.posZ + (this.curCrystal.posZ - this.curCrystal.lastTickPosZ) * (double)(bowVelocity * 10.0f);
            float trajectoryTheta90 = (float)(Math.atan2(zDistance, xDistance) * 180.0 / Math.PI) - 90.0f;
            float bowTrajectory = (float)((double)((float)(-Math.toDegrees(this.getLaunchAngle(this.curCrystal, v, g)))) - 3.8);
            if (trajectoryTheta90 <= 360.0f && bowTrajectory <= 360.0f) {
                event.setYaw(trajectoryTheta90);
                event.setPitch(bowTrajectory);
            }
        }
    }

    @EventHandler
    private void on3D(EventRender3D event) {
        if (this.curCrystal != null && this.curCrystal.isEntityAlive()) {
            RenderUtil.drawFilledESP(this.curCrystal, Color.PINK, event, 3.0f);
        }
    }

    private EntityEnderCrystal getCrystal() {
        ArrayList<EntityEnderCrystal> cs = new ArrayList<EntityEnderCrystal>();
        for (Entity e : this.mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            cs.add((EntityEnderCrystal)e);
        }
        cs.sort(Comparator.comparingDouble(this::lambda$getCrystal$0));
        if (!cs.isEmpty()) {
            return (EntityEnderCrystal)cs.get(0);
        }
        return null;
    }

    private float getLaunchAngle(EntityEnderCrystal e, double v, double g) {
        double yDif = e.posY + (double)(e.getEyeHeight() / 2.0f) - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight());
        double xDif = e.posX - this.mc.thePlayer.posX;
        double zDif = e.posZ - this.mc.thePlayer.posZ;
        double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
        return this.theta(v + 2.0, g, xCoord, yDif);
    }

    private float theta(double v, double g, double x, double y) {
        double yv = 2.0 * y * (v * v);
        double gx = g * (x * x);
        double g2 = g * (gx + yv);
        double insqrt = v * v * v * v - g2;
        double sqrt = Math.sqrt(insqrt);
        double numerator = v * v + sqrt;
        double numerator2 = v * v - sqrt;
        double atan1 = Math.atan2(numerator, g * x);
        double atan2 = Math.atan2(numerator2, g * x);
        return (float)Math.min(atan1, atan2);
    }

    private double lambda$getCrystal$0(EntityEnderCrystal e) {
        return this.mc.thePlayer.getDistanceToEntity(e);
    }
}

