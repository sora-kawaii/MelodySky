/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import java.util.List;
import java.util.Random;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public final class AimAssist
extends Module {
    private Random rand = new Random();
    private Numbers<Double> h = new Numbers<Double>("Horizontal", 4.2, 0.0, 10.0, 0.1);
    private Numbers<Double> v = new Numbers<Double>("Vertical", 2.4, 0.0, 10.0, 0.1);
    private Numbers<Double> s = new Numbers<Double>("Speed", 0.2, 0.0, 1.5, 0.01);
    private Numbers<Double> r = new Numbers<Double>("AimRange", 4.2, 1.0, 8.1, 0.1);
    private Numbers<Double> amin = new Numbers<Double>("MinAngle", 0.0, 0.0, 1.0, 1.0);
    private Numbers<Double> amax = new Numbers<Double>("MaxAngle", 100.0, 20.0, 360.0, 1.0);
    private Option<Boolean> ca = new Option<Boolean>("ClickAim", false);
    private Option<Boolean> str = new Option<Boolean>("MoveStrafing", false);
    private Option<Boolean> team = new Option<Boolean>("Team", true);

    public AimAssist() {
        super("AimAssist", new String[]{"AimAssist"}, ModuleType.Balance);
        this.addValues(this.h, this.v, this.s, this.r, this.amin, this.amax, this.ca, this.str, this.team);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate eventPreUpdate) {
        if (((Boolean)this.ca.getValue()).booleanValue() && !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        Entity entity = null;
        double d = 360.0;
        double d2 = (Double)this.amax.getValue();
        double d3 = (Double)this.amin.getValue();
        for (Entity entity2 : this.mc.theWorld.getLoadedEntityList()) {
            double d4;
            Entity entity3 = entity2;
            if (entity3 == this.mc.thePlayer || !this.isValid(entity3)) continue;
            double d5 = this.getDistanceBetweenAngles(this.getAngles(entity3)[1], this.mc.thePlayer.rotationYaw);
            if (d <= d4) continue;
            entity = entity3;
            d = d5;
        }
        if (entity != null) {
            float f = this.getAngles(entity)[1];
            float f2 = this.getAngles(entity)[0];
            double d6 = this.getDistanceBetweenAngles(f, this.mc.thePlayer.rotationYaw);
            double d7 = this.getDistanceBetweenAngles(f2, this.mc.thePlayer.rotationPitch);
            if (d7 <= d2 && d6 >= d3 && d6 <= d2) {
                double d8 = (Double)this.h.getValue() * 3.0 + ((Double)this.h.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                double d9 = (Double)this.v.getValue() * 3.0 + ((Double)this.v.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                if (((Boolean)this.str.getValue()).booleanValue() && this.mc.thePlayer.moveStrafing != 0.0f) {
                    d8 *= 1.25;
                }
                if (this.getEntity(24.0) != null && this.getEntity(24.0).equals(entity)) {
                    d8 *= ((Double)this.s.getValue()).doubleValue();
                    d9 *= ((Double)this.s.getValue()).doubleValue();
                }
                this.faceTarget(entity, 0.0f, (float)d9);
                this.faceTarget(entity, (float)d8, 0.0f);
            }
        }
    }

    protected float getRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4 / 2.0f;
    }

    private void faceTarget(Entity entity, float f, float f2) {
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        float f3 = this.getAngles(entity)[1];
        float f4 = this.getAngles(entity)[0];
        entityPlayerSP.rotationYaw = this.getRotation(entityPlayerSP.rotationYaw, f3, f);
        entityPlayerSP.rotationPitch = this.getRotation(entityPlayerSP.rotationPitch, f4, f2);
    }

    public float[] getAngles(Entity entity) {
        double d = entity.posX - this.mc.thePlayer.posX;
        double d2 = entity.posZ - this.mc.thePlayer.posZ;
        double d3 = entity instanceof EntityEnderman ? entity.posY - this.mc.thePlayer.posY : entity.posY + ((double)entity.getEyeHeight() - 1.9) - this.mc.thePlayer.posY + ((double)this.mc.thePlayer.getEyeHeight() - 1.9);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)Math.toDegrees(-Math.atan(d / d2));
        float f2 = (float)(-Math.toDegrees(Math.atan(d3 / d4)));
        if (d2 < 0.0 && d < 0.0) {
            f = (float)(90.0 + Math.toDegrees(Math.atan(d2 / d)));
        } else if (d2 < 0.0 && d > 0.0) {
            f = (float)(-90.0 + Math.toDegrees(Math.atan(d2 / d)));
        }
        return new float[]{f2, f};
    }

    public double getDistanceBetweenAngles(float f, float f2) {
        float f3 = Math.abs(f - f2) % 360.0f;
        if (f3 > 180.0f) {
            f3 = 360.0f - f3;
        }
        return f3;
    }

    public Object[] getEntity(double d, double d2, float f) {
        Entity entity = this.mc.getRenderViewEntity();
        Entity entity2 = null;
        if (entity == null || this.mc.theWorld == null) {
            return null;
        }
        this.mc.mcProfiler.startSection("pick");
        double d3 = d;
        double d4 = d;
        Vec3 vec3 = entity.getPositionEyes(0.0f);
        Vec3 vec32 = entity.getLook(0.0f);
        Vec3 vec33 = vec3.addVector(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3);
        Vec3 vec34 = null;
        float f2 = 1.0f;
        List<Entity> list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(f2, f2, f2));
        double d5 = d4;
        for (int i = 0; i < list.size(); ++i) {
            double d6;
            Entity entity3 = list.get(i);
            if (!entity3.canBeCollidedWith()) continue;
            float f3 = entity3.getCollisionBorderSize();
            AxisAlignedBB axisAlignedBB = entity3.getEntityBoundingBox().expand(f3, f3, f3);
            axisAlignedBB = axisAlignedBB.expand(d2, d2, d2);
            MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec33);
            if (axisAlignedBB.isVecInside(vec3)) {
                if (!(0.0 < d5) && d5 != 0.0) continue;
                entity2 = entity3;
                vec34 = movingObjectPosition == null ? vec3 : movingObjectPosition.hitVec;
                d5 = 0.0;
                continue;
            }
            if (movingObjectPosition == null || !((d6 = vec3.distanceTo(movingObjectPosition.hitVec)) < d5) && d5 != 0.0) continue;
            entity2 = entity3;
            vec34 = movingObjectPosition.hitVec;
            d5 = d6;
        }
        if (d5 < d4 && !(entity2 instanceof EntityLivingBase) && !(entity2 instanceof EntityItemFrame)) {
            entity2 = null;
        }
        this.mc.mcProfiler.endSection();
        if (entity2 == null || vec34 == null) {
            return null;
        }
        return new Object[]{entity2, vec34};
    }

    public Entity getEntity(double d) {
        if (this.getEntity(d, 0.0, 0.0f) == null) {
            return null;
        }
        return (Entity)this.getEntity(d, 0.0, 0.0f)[0];
    }

    public boolean isValid(Entity entity) {
        boolean bl = true;
        AntiBot antiBot = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (antiBot.isEnabled()) {
            if (antiBot.isEnabled()) {
                bl = antiBot.isEntityBot(entity);
            }
        } else {
            bl = true;
        }
        return entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityAnimal) && !(entity instanceof EntityMob) && entity != this.mc.thePlayer && !(entity instanceof EntityVillager) && (double)this.mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.r.getValue() && !entity.getName().contains("#") && ((Boolean)this.team.getValue() == false || !entity.getDisplayName().getFormattedText().startsWith("\ufffd\ufffd" + this.mc.thePlayer.getDisplayName().getFormattedText().charAt(1))) && bl;
    }
}

