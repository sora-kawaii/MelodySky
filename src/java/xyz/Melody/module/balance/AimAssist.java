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
    public void onUpdate(EventPreUpdate event) {
        if (((Boolean)this.ca.getValue()).booleanValue() && !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        Entity entity = null;
        double maxDistance = 360.0;
        double maxAngle = (Double)this.amax.getValue();
        double minAngle = (Double)this.amin.getValue();
        for (Entity e : this.mc.theWorld.getLoadedEntityList()) {
            double d;
            Entity en = e;
            if (en == this.mc.thePlayer || !this.isValid(en)) continue;
            double yawdistance = this.getDistanceBetweenAngles(this.getAngles(en)[1], this.mc.thePlayer.rotationYaw);
            if (maxDistance <= d) continue;
            entity = en;
            maxDistance = yawdistance;
        }
        if (entity != null) {
            float yaw2 = this.getAngles(entity)[1];
            float pitch = this.getAngles(entity)[0];
            double yawdistance = this.getDistanceBetweenAngles(yaw2, this.mc.thePlayer.rotationYaw);
            double pitchdistance = this.getDistanceBetweenAngles(pitch, this.mc.thePlayer.rotationPitch);
            if (pitchdistance <= maxAngle && yawdistance >= minAngle && yawdistance <= maxAngle) {
                double horizontalSpeed = (Double)this.h.getValue() * 3.0 + ((Double)this.h.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                double verticalSpeed = (Double)this.v.getValue() * 3.0 + ((Double)this.v.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                if (((Boolean)this.str.getValue()).booleanValue() && this.mc.thePlayer.moveStrafing != 0.0f) {
                    horizontalSpeed *= 1.25;
                }
                if (this.getEntity(24.0) != null && this.getEntity(24.0).equals(entity)) {
                    horizontalSpeed *= ((Double)this.s.getValue()).doubleValue();
                    verticalSpeed *= ((Double)this.s.getValue()).doubleValue();
                }
                this.faceTarget(entity, 0.0f, (float)verticalSpeed);
                this.faceTarget(entity, (float)horizontalSpeed, 0.0f);
            }
        }
    }

    protected float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return currentRotation + deltaAngle / 2.0f;
    }

    private void faceTarget(Entity target, float yawspeed, float pitchspeed) {
        EntityPlayerSP player = this.mc.thePlayer;
        float yaw = this.getAngles(target)[1];
        float pitch = this.getAngles(target)[0];
        player.rotationYaw = this.getRotation(player.rotationYaw, yaw, yawspeed);
        player.rotationPitch = this.getRotation(player.rotationPitch, pitch, pitchspeed);
    }

    public float[] getAngles(Entity entity) {
        double x = entity.posX - this.mc.thePlayer.posX;
        double z = entity.posZ - this.mc.thePlayer.posZ;
        double y = entity instanceof EntityEnderman ? entity.posY - this.mc.thePlayer.posY : entity.posY + ((double)entity.getEyeHeight() - 1.9) - this.mc.thePlayer.posY + ((double)this.mc.thePlayer.getEyeHeight() - 1.9);
        double helper = MathHelper.sqrt_double(x * x + z * z);
        float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
        float newPitch = (float)(-Math.toDegrees(Math.atan(y / helper)));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z / x)));
        } else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return new float[]{newPitch, newYaw};
    }

    public double getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    public Object[] getEntity(double distance, double expand, float partialTicks) {
        Entity var2 = this.mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 == null || this.mc.theWorld == null) {
            return null;
        }
        this.mc.mcProfiler.startSection("pick");
        double var3 = distance;
        double var4 = distance;
        Vec3 var5 = var2.getPositionEyes(0.0f);
        Vec3 var6 = var2.getLook(0.0f);
        Vec3 var7 = var5.addVector(var6.xCoord * var3, var6.yCoord * var3, var6.zCoord * var3);
        Vec3 var8 = null;
        float var9 = 1.0f;
        List<Entity> var10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var6.xCoord * var3, var6.yCoord * var3, var6.zCoord * var3).expand(var9, var9, var9));
        double var11 = var4;
        for (int var12 = 0; var12 < var10.size(); ++var12) {
            double var17;
            Entity var13 = var10.get(var12);
            if (!var13.canBeCollidedWith()) continue;
            float var14 = var13.getCollisionBorderSize();
            AxisAlignedBB var15 = var13.getEntityBoundingBox().expand(var14, var14, var14);
            var15 = var15.expand(expand, expand, expand);
            MovingObjectPosition var16 = var15.calculateIntercept(var5, var7);
            if (var15.isVecInside(var5)) {
                if (!(0.0 < var11) && var11 != 0.0) continue;
                entity = var13;
                var8 = var16 == null ? var5 : var16.hitVec;
                var11 = 0.0;
                continue;
            }
            if (var16 == null || !((var17 = var5.distanceTo(var16.hitVec)) < var11) && var11 != 0.0) continue;
            entity = var13;
            var8 = var16.hitVec;
            var11 = var17;
        }
        if (var11 < var4 && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        this.mc.mcProfiler.endSection();
        if (entity == null || var8 == null) {
            return null;
        }
        return new Object[]{entity, var8};
    }

    public Entity getEntity(double distance) {
        if (this.getEntity(distance, 0.0, 0.0f) == null) {
            return null;
        }
        return (Entity)this.getEntity(distance, 0.0, 0.0f)[0];
    }

    public boolean isValid(Entity e) {
        boolean flag1 = true;
        AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (ab.isEnabled()) {
            if (ab.isEnabled()) {
                flag1 = ab.isEntityBot(e);
            }
        } else {
            flag1 = true;
        }
        return e instanceof EntityLivingBase && !(e instanceof EntityArmorStand) && !(e instanceof EntityAnimal) && !(e instanceof EntityMob) && e != this.mc.thePlayer && !(e instanceof EntityVillager) && (double)this.mc.thePlayer.getDistanceToEntity(e) <= (Double)this.r.getValue() && !e.getName().contains("#") && ((Boolean)this.team.getValue() == false || !e.getDisplayName().getFormattedText().startsWith("\ufffd\ufffd" + this.mc.thePlayer.getDisplayName().getFormattedText().charAt(1))) && flag1;
    }
}

