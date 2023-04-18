/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Slayer;

import java.awt.Color;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Utils.MethodReflectionHelper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class BlazeDagger
extends Module {
    private long lastClickTime = 0L;
    private MethodReflectionHelper CONTROLLER = new MethodReflectionHelper(PlayerControllerMP.class, "func_78750_j", "syncCurrentPlayItem", new Class[0]);

    public BlazeDagger() {
        super("AutoBlazeDagger", new String[]{"cb"}, ModuleType.Nether);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Auto Swap Dagger Mode.");
    }

    @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> pre) {
        if (pre.entity instanceof EntityArmorStand) {
            EntityArmorStand entityArmorStand = (EntityArmorStand)pre.entity;
            if (!entityArmorStand.func_145818_k_()) {
                return;
            }
            String string = StringUtils.func_76338_a((String)entityArmorStand.func_95999_t());
            double d = pre.entity.field_70165_t;
            double d2 = pre.entity.field_70163_u;
            double d3 = pre.entity.field_70161_v;
            if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && this.mc.field_71462_r == null && this.shouldClick()) {
                if (string.startsWith("CRYSTAL")) {
                    if (this.isFacingAABB(new AxisAlignedBB(d - 0.5, d2 - 3.0, d3 - 0.5, d + 0.5, d2 + 1.0, d3 + 0.5), 5.0f)) {
                        this.swapToCrystal();
                    }
                    return;
                }
                if (string.startsWith("ASHEN")) {
                    if (this.isFacingAABB(new AxisAlignedBB(d - 0.5, d2 - 3.0, d3 - 0.5, d + 0.5, d2 + 1.0, d3 + 0.5), 5.0f)) {
                        this.swapToAshen();
                    }
                    return;
                }
                if (string.startsWith("AURIC")) {
                    if (this.isFacingAABB(new AxisAlignedBB(d - 0.5, d2 - 3.0, d3 - 0.5, d + 0.5, d2 + 1.0, d3 + 0.5), 5.0f)) {
                        this.swapToAuric();
                    }
                    return;
                }
                if (string.startsWith("SPIRIT")) {
                    if (this.isFacingAABB(new AxisAlignedBB(d - 0.5, d2 - 3.0, d3 - 0.5, d + 0.5, d2 + 1.0, d3 + 0.5), 5.0f)) {
                        this.swapToSprit();
                    }
                    return;
                }
            }
        }
    }

    public void swapToCrystal() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || !(string = itemStack.func_82833_r()).contains("Deathripper Dagger") && !string.contains("Mawdredge Dagger") && !string.contains("Twilight Dagger")) continue;
            this.mc.field_71439_g.field_71071_by.field_70461_c = i;
            if (itemStack.func_77973_b() != Items.field_151048_u) {
                this.CONTROLLER.invoke(this.mc.field_71442_b);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToSprit() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || !(string = itemStack.func_82833_r()).contains("Deathripper Dagger") && !string.contains("Mawdredge Dagger") && !string.contains("Twilight Dagger")) continue;
            this.mc.field_71439_g.field_71071_by.field_70461_c = i;
            if (itemStack.func_77973_b() != Items.field_151040_l) {
                this.CONTROLLER.invoke(this.mc.field_71442_b);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAshen() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || !(string = itemStack.func_82833_r()).contains("Pyrochaos Dagger") && !string.contains("Kindlebane Dagger") && !string.contains("Firedust Dagger")) continue;
            this.mc.field_71439_g.field_71071_by.field_70461_c = i;
            if (itemStack.func_77973_b() != Items.field_151052_q) {
                this.CONTROLLER.invoke(this.mc.field_71442_b);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAuric() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || !(string = itemStack.func_82833_r()).contains("Pyrochaos Dagger") && !string.contains("Kindlebane Dagger") && !string.contains("Firedust Dagger")) continue;
            this.mc.field_71439_g.field_71071_by.field_70461_c = i;
            if (itemStack.func_77973_b() != Items.field_151010_B) {
                this.CONTROLLER.invoke(this.mc.field_71442_b);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public boolean shouldClick() {
        return System.currentTimeMillis() - this.lastClickTime >= 500L;
    }

    public boolean isFacingAABB(AxisAlignedBB axisAlignedBB, float f) {
        return this.isInterceptable(axisAlignedBB, f);
    }

    public Vec3 getPositionEyes() {
        return new Vec3(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u + (double)this.fastEyeHeight(), this.mc.field_71439_g.field_70161_v);
    }

    public float fastEyeHeight() {
        return this.mc.field_71439_g.func_70093_af() ? 1.54f : 1.62f;
    }

    public boolean isInterceptable(AxisAlignedBB axisAlignedBB, float f) {
        Vec3 vec3 = this.getPositionEyes();
        Vec3 vec32 = this.getVectorForRotation();
        return this.isInterceptable(vec3, vec3.func_72441_c(vec32.field_72450_a * (double)f, vec32.field_72448_b * (double)f, vec32.field_72449_c * (double)f), axisAlignedBB);
    }

    private Vec3 getVectorForRotation() {
        float f = -MathHelper.func_76134_b((float)(-this.mc.field_71439_g.field_70125_A * ((float)Math.PI / 180)));
        return new Vec3(MathHelper.func_76126_a((float)(-this.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180) - (float)Math.PI)) * f, MathHelper.func_76126_a((float)(-this.mc.field_71439_g.field_70125_A * ((float)Math.PI / 180))), MathHelper.func_76134_b((float)(-this.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180) - (float)Math.PI)) * f);
    }

    public boolean isInterceptable(Vec3 vec3, Vec3 vec32, AxisAlignedBB axisAlignedBB) {
        return this.isVecInYZ(vec3.func_72429_b(vec32, axisAlignedBB.field_72340_a), axisAlignedBB) || this.isVecInYZ(vec3.func_72429_b(vec32, axisAlignedBB.field_72336_d), axisAlignedBB) || this.isVecInXZ(vec3.func_72435_c(vec32, axisAlignedBB.field_72338_b), axisAlignedBB) || this.isVecInXZ(vec3.func_72435_c(vec32, axisAlignedBB.field_72337_e), axisAlignedBB) || this.isVecInXY(vec3.func_72434_d(vec32, axisAlignedBB.field_72339_c), axisAlignedBB) || this.isVecInXY(vec3.func_72434_d(vec32, axisAlignedBB.field_72334_f), axisAlignedBB);
    }

    public boolean isVecInYZ(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.field_72448_b >= axisAlignedBB.field_72338_b && vec3.field_72448_b <= axisAlignedBB.field_72337_e && vec3.field_72449_c >= axisAlignedBB.field_72339_c && vec3.field_72449_c <= axisAlignedBB.field_72334_f;
    }

    public boolean isVecInXZ(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.field_72450_a >= axisAlignedBB.field_72340_a && vec3.field_72450_a <= axisAlignedBB.field_72336_d && vec3.field_72449_c >= axisAlignedBB.field_72339_c && vec3.field_72449_c <= axisAlignedBB.field_72334_f;
    }

    public boolean isVecInXY(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.field_72450_a >= axisAlignedBB.field_72340_a && vec3.field_72450_a <= axisAlignedBB.field_72336_d && vec3.field_72448_b >= axisAlignedBB.field_72338_b && vec3.field_72448_b <= axisAlignedBB.field_72337_e;
    }
}

