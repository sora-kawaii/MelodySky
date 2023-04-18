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
        if (((Boolean)this.ca.getValue()).booleanValue() && !this.mc.field_71474_y.field_74312_F.func_151470_d()) {
            return;
        }
        Entity entity = null;
        double d = 360.0;
        double d2 = (Double)this.amax.getValue();
        double d3 = (Double)this.amin.getValue();
        for (Object e : this.mc.field_71441_e.func_72910_y()) {
            double d4;
            Entity entity2 = (Entity)e;
            if (entity2 == this.mc.field_71439_g || !this.isValid(entity2)) continue;
            double d5 = this.getDistanceBetweenAngles(this.getAngles(entity2)[1], this.mc.field_71439_g.field_70177_z);
            if (d <= d4) continue;
            entity = entity2;
            d = d5;
        }
        if (entity != null) {
            float f = this.getAngles(entity)[1];
            float f2 = this.getAngles(entity)[0];
            double d6 = this.getDistanceBetweenAngles(f, this.mc.field_71439_g.field_70177_z);
            double d7 = this.getDistanceBetweenAngles(f2, this.mc.field_71439_g.field_70125_A);
            if (d7 <= d2 && d6 >= d3 && d6 <= d2) {
                double d8 = (Double)this.h.getValue() * 3.0 + ((Double)this.h.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                double d9 = (Double)this.v.getValue() * 3.0 + ((Double)this.v.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                if (((Boolean)this.str.getValue()).booleanValue() && this.mc.field_71439_g.field_70702_br != 0.0f) {
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
        float f4 = MathHelper.func_76142_g((float)(f2 - f));
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4 / 2.0f;
    }

    private void faceTarget(Entity entity, float f, float f2) {
        EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
        float f3 = this.getAngles(entity)[1];
        float f4 = this.getAngles(entity)[0];
        entityPlayerSP.field_70177_z = this.getRotation(entityPlayerSP.field_70177_z, f3, f);
        entityPlayerSP.field_70125_A = this.getRotation(entityPlayerSP.field_70125_A, f4, f2);
    }

    public float[] getAngles(Entity entity) {
        double d = entity.field_70165_t - this.mc.field_71439_g.field_70165_t;
        double d2 = entity.field_70161_v - this.mc.field_71439_g.field_70161_v;
        double d3 = entity instanceof EntityEnderman ? entity.field_70163_u - this.mc.field_71439_g.field_70163_u : entity.field_70163_u + ((double)entity.func_70047_e() - 1.9) - this.mc.field_71439_g.field_70163_u + ((double)this.mc.field_71439_g.func_70047_e() - 1.9);
        double d4 = MathHelper.func_76133_a((double)(d * d + d2 * d2));
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
        Entity entity = this.mc.func_175606_aa();
        Entity entity2 = null;
        if (entity == null || this.mc.field_71441_e == null) {
            return null;
        }
        this.mc.field_71424_I.func_76320_a("pick");
        double d3 = d;
        double d4 = d;
        Vec3 vec3 = entity.func_174824_e(0.0f);
        Vec3 vec32 = entity.func_70676_i(0.0f);
        Vec3 vec33 = vec3.func_72441_c(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3);
        Vec3 vec34 = null;
        float f2 = 1.0f;
        List list = this.mc.field_71441_e.func_72839_b(entity, entity.func_174813_aQ().func_72321_a(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3).func_72314_b(f2, f2, f2));
        double d5 = d4;
        for (int i = 0; i < list.size(); ++i) {
            double d6;
            Entity entity3 = (Entity)list.get(i);
            if (!entity3.func_70067_L()) continue;
            float f3 = entity3.func_70111_Y();
            AxisAlignedBB axisAlignedBB = entity3.func_174813_aQ().func_72314_b(f3, f3, f3);
            axisAlignedBB = axisAlignedBB.func_72314_b(d2, d2, d2);
            MovingObjectPosition movingObjectPosition = axisAlignedBB.func_72327_a(vec3, vec33);
            if (axisAlignedBB.func_72318_a(vec3)) {
                if (!(0.0 < d5) && d5 != 0.0) continue;
                entity2 = entity3;
                vec34 = movingObjectPosition == null ? vec3 : movingObjectPosition.field_72307_f;
                d5 = 0.0;
                continue;
            }
            if (movingObjectPosition == null || !((d6 = vec3.func_72438_d(movingObjectPosition.field_72307_f)) < d5) && d5 != 0.0) continue;
            entity2 = entity3;
            vec34 = movingObjectPosition.field_72307_f;
            d5 = d6;
        }
        if (d5 < d4 && !(entity2 instanceof EntityLivingBase) && !(entity2 instanceof EntityItemFrame)) {
            entity2 = null;
        }
        this.mc.field_71424_I.func_76319_b();
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
        return entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityAnimal) && !(entity instanceof EntityMob) && entity != this.mc.field_71439_g && !(entity instanceof EntityVillager) && (double)this.mc.field_71439_g.func_70032_d(entity) <= (Double)this.r.getValue() && !entity.func_70005_c_().contains("#") && ((Boolean)this.team.getValue() == false || !entity.func_145748_c_().func_150254_d().startsWith("\ufffd\ufffd" + this.mc.field_71439_g.func_145748_c_().func_150254_d().charAt(1))) && bl;
    }
}

