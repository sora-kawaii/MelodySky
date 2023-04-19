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
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand)event.entity;
            if (!entity.hasCustomName()) {
                return;
            }
            String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
            double x = event.entity.posX;
            double y = event.entity.posY;
            double z = event.entity.posZ;
            if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && this.mc.currentScreen == null && this.shouldClick()) {
                if (entityName.startsWith("CRYSTAL")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        this.swapToCrystal();
                    }
                    return;
                }
                if (entityName.startsWith("ASHEN")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        this.swapToAshen();
                    }
                    return;
                }
                if (entityName.startsWith("AURIC")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        this.swapToAuric();
                    }
                    return;
                }
                if (entityName.startsWith("SPIRIT")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        this.swapToSprit();
                    }
                    return;
                }
            }
        }
    }

    public void swapToCrystal() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = this.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Deathripper Dagger") && !name.contains("Mawdredge Dagger") && !name.contains("Twilight Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.diamond_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToSprit() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = this.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Deathripper Dagger") && !name.contains("Mawdredge Dagger") && !name.contains("Twilight Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.iron_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAshen() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = this.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Pyrochaos Dagger") && !name.contains("Kindlebane Dagger") && !name.contains("Firedust Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.stone_sword) {
                this.CONTROLLER.invoke(this.mc.playerController);
                Client.rightClick();
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAuric() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = this.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Pyrochaos Dagger") && !name.contains("Kindlebane Dagger") && !name.contains("Firedust Dagger")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.golden_sword) {
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

    public boolean isFacingAABB(AxisAlignedBB aabb, float range) {
        return this.isInterceptable(aabb, range);
    }

    public Vec3 getPositionEyes() {
        return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.fastEyeHeight(), this.mc.thePlayer.posZ);
    }

    public float fastEyeHeight() {
        return this.mc.thePlayer.isSneaking() ? 1.54f : 1.62f;
    }

    public boolean isInterceptable(AxisAlignedBB aabb, float range) {
        Vec3 position = this.getPositionEyes();
        Vec3 look = this.getVectorForRotation();
        return this.isInterceptable(position, position.addVector(look.xCoord * (double)range, look.yCoord * (double)range, look.zCoord * (double)range), aabb);
    }

    private Vec3 getVectorForRotation() {
        float f2 = -MathHelper.cos(-this.mc.thePlayer.rotationPitch * ((float)Math.PI / 180));
        return new Vec3(MathHelper.sin(-this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2, MathHelper.sin(-this.mc.thePlayer.rotationPitch * ((float)Math.PI / 180)), MathHelper.cos(-this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2);
    }

    public boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb) {
        return this.isVecInYZ(start.getIntermediateWithXValue(goal, aabb.minX), aabb) || this.isVecInYZ(start.getIntermediateWithXValue(goal, aabb.maxX), aabb) || this.isVecInXZ(start.getIntermediateWithYValue(goal, aabb.minY), aabb) || this.isVecInXZ(start.getIntermediateWithYValue(goal, aabb.maxY), aabb) || this.isVecInXY(start.getIntermediateWithZValue(goal, aabb.minZ), aabb) || this.isVecInXY(start.getIntermediateWithZValue(goal, aabb.maxZ), aabb);
    }

    public boolean isVecInYZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public boolean isVecInXZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public boolean isVecInXY(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY;
    }
}

