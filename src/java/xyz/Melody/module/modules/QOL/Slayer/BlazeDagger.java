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
            if (!entityArmorStand.hasCustomName()) {
                return;
            }
            String string = StringUtils.stripControlCodes(entityArmorStand.getCustomNameTag());
            double d = pre.entity.posX;
            double d2 = pre.entity.posY;
            double d3 = pre.entity.posZ;
            if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && this.mc.currentScreen == null && this.shouldClick()) {
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
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !(string = itemStack.getDisplayName()).contains("Deathripper Dagger") && !string.contains("Mawdredge Dagger") && !string.contains("Twilight Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (itemStack.getItem() != Items.diamond_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToSprit() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !(string = itemStack.getDisplayName()).contains("Deathripper Dagger") && !string.contains("Mawdredge Dagger") && !string.contains("Twilight Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (itemStack.getItem() != Items.iron_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAshen() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !(string = itemStack.getDisplayName()).contains("Pyrochaos Dagger") && !string.contains("Kindlebane Dagger") && !string.contains("Firedust Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (itemStack.getItem() != Items.stone_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAuric() {
        for (int i = 0; i < 8; ++i) {
            String string;
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !(string = itemStack.getDisplayName()).contains("Pyrochaos Dagger") && !string.contains("Kindlebane Dagger") && !string.contains("Firedust Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (itemStack.getItem() != Items.golden_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
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
        return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.fastEyeHeight(), this.mc.thePlayer.posZ);
    }

    public float fastEyeHeight() {
        return this.mc.thePlayer.isSneaking() ? 1.54f : 1.62f;
    }

    public boolean isInterceptable(AxisAlignedBB axisAlignedBB, float f) {
        Vec3 vec3 = this.getPositionEyes();
        Vec3 vec32 = this.getVectorForRotation();
        return this.isInterceptable(vec3, vec3.addVector(vec32.xCoord * (double)f, vec32.yCoord * (double)f, vec32.zCoord * (double)f), axisAlignedBB);
    }

    private Vec3 getVectorForRotation() {
        float f = -MathHelper.cos(-this.mc.thePlayer.rotationPitch * ((float)Math.PI / 180));
        return new Vec3(MathHelper.sin(-this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f, MathHelper.sin(-this.mc.thePlayer.rotationPitch * ((float)Math.PI / 180)), MathHelper.cos(-this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f);
    }

    public boolean isInterceptable(Vec3 vec3, Vec3 vec32, AxisAlignedBB axisAlignedBB) {
        return this.isVecInYZ(vec3.getIntermediateWithXValue(vec32, axisAlignedBB.minX), axisAlignedBB) || this.isVecInYZ(vec3.getIntermediateWithXValue(vec32, axisAlignedBB.maxX), axisAlignedBB) || this.isVecInXZ(vec3.getIntermediateWithYValue(vec32, axisAlignedBB.minY), axisAlignedBB) || this.isVecInXZ(vec3.getIntermediateWithYValue(vec32, axisAlignedBB.maxY), axisAlignedBB) || this.isVecInXY(vec3.getIntermediateWithZValue(vec32, axisAlignedBB.minZ), axisAlignedBB) || this.isVecInXY(vec3.getIntermediateWithZValue(vec32, axisAlignedBB.maxZ), axisAlignedBB);
    }

    public boolean isVecInYZ(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.yCoord >= axisAlignedBB.minY && vec3.yCoord <= axisAlignedBB.maxY && vec3.zCoord >= axisAlignedBB.minZ && vec3.zCoord <= axisAlignedBB.maxZ;
    }

    public boolean isVecInXZ(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.xCoord >= axisAlignedBB.minX && vec3.xCoord <= axisAlignedBB.maxX && vec3.zCoord >= axisAlignedBB.minZ && vec3.zCoord <= axisAlignedBB.maxZ;
    }

    public boolean isVecInXY(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        return vec3 != null && vec3.xCoord >= axisAlignedBB.minX && vec3.xCoord <= axisAlignedBB.maxX && vec3.yCoord >= axisAlignedBB.minY && vec3.yCoord <= axisAlignedBB.maxY;
    }
}

