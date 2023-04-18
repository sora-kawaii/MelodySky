/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Animations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.Melody.Client;
import xyz.Melody.injection.mixins.item.ItemFoodAccessor;
import xyz.Melody.module.modules.others.OldAnimations;

public final class AnimationHandler {
    private static final AnimationHandler INSTANCE = new AnimationHandler();
    private final Minecraft mc = Minecraft.getMinecraft();
    public float prevSwingProgress;
    public float swingProgress;
    private int swingProgressInt;
    private boolean isSwingInProgress;

    public static AnimationHandler getInstance() {
        return INSTANCE;
    }

    public float getSwingProgress(float f) {
        float f2 = this.swingProgress - this.prevSwingProgress;
        if (!this.isSwingInProgress) {
            return this.mc.thePlayer.getSwingProgress(f);
        }
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        return this.prevSwingProgress + f2 * f;
    }

    private int getArmSwingAnimationEnd(EntityPlayerSP entityPlayerSP) {
        return entityPlayerSP.isPotionActive(Potion.digSpeed) ? 5 - entityPlayerSP.getActivePotionEffect(Potion.digSpeed).getAmplifier() : (entityPlayerSP.isPotionActive(Potion.digSlowdown) ? 8 + entityPlayerSP.getActivePotionEffect(Potion.digSlowdown).getAmplifier() * 2 : 6);
    }

    private void updateSwingProgress() {
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        if (entityPlayerSP == null) {
            return;
        }
        this.prevSwingProgress = this.swingProgress;
        int n = this.getArmSwingAnimationEnd(entityPlayerSP);
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.punching.getValue()).booleanValue() && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (!this.isSwingInProgress || this.swingProgressInt >= n >> 1 || this.swingProgressInt < 0)) {
            this.isSwingInProgress = true;
            this.swingProgressInt = -1;
        }
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= n) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = (float)this.swingProgressInt / (float)n;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            this.updateSwingProgress();
        }
    }

    public boolean renderItemInFirstPerson(ItemRenderer itemRenderer, ItemStack itemStack, float f, float f2) {
        if (itemStack == null) {
            return false;
        }
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        Item item = itemStack.getItem();
        if (item == Items.filled_map) {
            return false;
        }
        EnumAction enumAction = itemStack.getItemUseAction();
        if (item == Items.fishing_rod && (Boolean)oldAnimations.oldRod.getValue() == false || enumAction == EnumAction.NONE && (Boolean)oldAnimations.oldModel.getValue() == false || enumAction == EnumAction.BLOCK && (Boolean)oldAnimations.oldBlockhitting.getValue() == false || enumAction == EnumAction.BOW && !((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            return false;
        }
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        float f3 = entityPlayerSP.prevRotationPitch + (entityPlayerSP.rotationPitch - entityPlayerSP.prevRotationPitch) * f2;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(f3, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(entityPlayerSP.prevRotationYaw + (entityPlayerSP.rotationYaw - entityPlayerSP.prevRotationYaw) * f2, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        float f4 = entityPlayerSP.prevRenderArmPitch + (entityPlayerSP.renderArmPitch - entityPlayerSP.prevRenderArmPitch) * f2;
        float f5 = entityPlayerSP.prevRenderArmYaw + (entityPlayerSP.renderArmYaw - entityPlayerSP.prevRenderArmYaw) * f2;
        GlStateManager.rotate((entityPlayerSP.rotationPitch - f4) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityPlayerSP.rotationYaw - f5) * 0.1f, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        if (item instanceof ItemCloth) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        int n = this.mc.theWorld.getCombinedLight(new BlockPos(entityPlayerSP.posX, entityPlayerSP.posY + (double)entityPlayerSP.getEyeHeight(), entityPlayerSP.posZ), 0);
        float f6 = n & 0xFFFF;
        float f7 = n >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f6, f7);
        int n2 = item.getColorFromItemStack(itemStack, 0);
        float f8 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f10 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.color(f8, f9, f10, 1.0f);
        GlStateManager.pushMatrix();
        int n3 = entityPlayerSP.getItemInUseCount();
        float f11 = this.getSwingProgress(f2);
        boolean bl = false;
        if (((Boolean)oldAnimations.punching.getValue()).booleanValue() && n3 <= 0 && this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean bl2;
            boolean bl3 = enumAction == EnumAction.BLOCK;
            boolean bl4 = false;
            if (item instanceof ItemFood && entityPlayerSP.canEat(bl2 = ((ItemFoodAccessor)((Object)item)).getAlwaysEdible())) {
                boolean bl5 = bl4 = enumAction == EnumAction.EAT || enumAction == EnumAction.DRINK;
            }
            if (bl3 || bl4) {
                bl = true;
            }
        }
        GlStateManager.translate((Double)oldAnimations.handX.getValue(), (Double)oldAnimations.handY.getValue(), (Double)oldAnimations.handZ.getValue());
        if ((n3 > 0 || bl) && enumAction != EnumAction.NONE && this.mc.thePlayer.getItemInUseCount() > 0) {
            switch (enumAction) {
                case EAT: 
                case DRINK: {
                    this.doConsumeAnimation(itemStack, n3, f2);
                    this.doEquipAndSwingTransform(f, (Boolean)oldAnimations.oldBlockhitting.getValue() != false ? f11 : 0.0f);
                    break;
                }
                case BLOCK: {
                    this.doEquipAndSwingTransform(f, (Boolean)oldAnimations.oldBlockhitting.getValue() != false ? f11 : 0.0f);
                    this.doSwordBlockAnimation();
                    break;
                }
                case BOW: {
                    this.doEquipAndSwingTransform(f, (Boolean)oldAnimations.oldBlockhitting.getValue() != false ? f11 : 0.0f);
                    this.doBowAnimation(itemStack, n3, f2);
                }
            }
        } else {
            this.doSwingTranslation(f11);
            this.doEquipAndSwingTransform(f, f11);
        }
        if (item.shouldRotateAroundWhenRendering()) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        }
        if (this.doFirstPersonTransform(itemStack)) {
            itemRenderer.renderItem(entityPlayerSP, itemStack, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else {
            itemRenderer.renderItem(entityPlayerSP, itemStack, ItemCameraTransforms.TransformType.NONE);
        }
        GlStateManager.popMatrix();
        if (item instanceof ItemCloth) {
            GlStateManager.disableBlend();
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        return true;
    }

    public void doSwordBlock3rdPersonTransform() {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.oldBlockhitting.getValue()).booleanValue()) {
            GlStateManager.translate(-0.15f, -0.2f, 0.0f);
            GlStateManager.rotate(70.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.119f, 0.2f, -0.024f);
        }
    }

    private boolean doFirstPersonTransform(ItemStack itemStack) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        switch (itemStack.getItemUseAction()) {
            case BOW: {
                if (((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) break;
                return true;
            }
            case EAT: 
            case DRINK: {
                if (((Boolean)oldAnimations.oldEating.getValue()).booleanValue()) break;
                return true;
            }
            case BLOCK: {
                if (((Boolean)oldAnimations.oldBlockhitting.getValue()).booleanValue()) break;
                return true;
            }
            case NONE: {
                if (((Boolean)oldAnimations.oldModel.getValue()).booleanValue()) break;
                return true;
            }
        }
        GlStateManager.translate(0.58800083f, 0.36999986f, -0.77000016f);
        GlStateManager.translate(0.0f, -0.3f, 0.0f);
        GlStateManager.scale(1.5f, 1.5f, 1.5f);
        GlStateManager.rotate(50.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(335.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.9375f, -0.0625f, 0.0f);
        GlStateManager.scale(-2.0f, 2.0f, -2.0f);
        if (this.mc.getRenderItem().shouldRenderItemIn3D(itemStack)) {
            GlStateManager.scale(0.58823526f, 0.58823526f, 0.58823526f);
            GlStateManager.rotate(-25.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.25f, -0.125f);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            return true;
        }
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        return false;
    }

    private void doConsumeAnimation(ItemStack itemStack, int n, float f) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.oldEating.getValue()).booleanValue()) {
            float f2 = (float)n - f + 1.0f;
            float f3 = 1.0f - f2 / (float)itemStack.getMaxItemUseDuration();
            float f4 = 1.0f - f3;
            f4 = f4 * f4 * f4;
            f4 = f4 * f4 * f4;
            f4 = f4 * f4 * f4;
            float f5 = 1.0f - f4;
            GlStateManager.translate(0.0f, MathHelper.abs(MathHelper.cos(f2 / 4.0f * (float)Math.PI) * 0.1f) * (float)((double)f3 > 0.2 ? 1 : 0), 0.0f);
            GlStateManager.translate(f5 * 0.6f, -f5 * 0.5f, 0.0f);
            GlStateManager.rotate(f5 * 90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f5 * 10.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f5 * 30.0f, 0.0f, 0.0f, 1.0f);
        } else {
            float f6 = (float)n - f + 1.0f;
            float f7 = f6 / (float)itemStack.getMaxItemUseDuration();
            float f8 = MathHelper.abs(MathHelper.cos(f6 / 4.0f * (float)Math.PI) * 0.1f);
            if (f7 >= 0.8f) {
                f8 = 0.0f;
            }
            GlStateManager.translate(0.0f, f8, 0.0f);
            float f9 = 1.0f - (float)Math.pow(f7, 27.0);
            GlStateManager.translate(f9 * 0.6f, f9 * -0.5f, f9 * 0.0f);
            GlStateManager.rotate(f9 * 90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f9 * 10.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f9 * 30.0f, 0.0f, 0.0f, 1.0f);
        }
    }

    private void doSwingTranslation(float f) {
        float f2 = MathHelper.sin(f * (float)Math.PI);
        float f3 = MathHelper.sin(MathHelper.sqrt_float(f) * (float)Math.PI);
        GlStateManager.translate(-f3 * 0.4f, MathHelper.sin(MathHelper.sqrt_float(f) * (float)Math.PI * 2.0f) * 0.2f, -f2 * 0.2f);
    }

    private void doEquipAndSwingTransform(float f, float f2) {
        GlStateManager.translate(0.56f, -0.52f - (1.0f - f) * 0.6f, -0.72f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f3 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.rotate(-f3 * 20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f4 * 20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-f4 * 80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void doSwordBlockAnimation() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }

    private void doBowAnimation(ItemStack itemStack, int n, float f) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        float f2 = (float)itemStack.getMaxItemUseDuration() - ((float)n - f + 1.0f);
        float f3 = f2 / 20.0f;
        f3 = (f3 * f3 + f3 * 2.0f) / 3.0f;
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f3 > 0.1f) {
            GlStateManager.translate(0.0f, MathHelper.sin((f2 - 0.1f) * 1.3f) * 0.01f * (f3 - 0.1f), 0.0f);
        }
        GlStateManager.translate(0.0f, 0.0f, f3 * 0.1f);
        if (((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            GlStateManager.rotate(-335.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-50.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, 0.5f, 0.0f);
        }
        float f4 = 1.0f + f3 * 0.2f;
        GlStateManager.scale(1.0f, 1.0f, f4);
        if (((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            GlStateManager.translate(0.0f, -0.5f, 0.0f);
            GlStateManager.rotate(50.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(335.0f, 0.0f, 0.0f, 1.0f);
        }
    }

    public Vec3 getOffset() {
        double d = Minecraft.getMinecraft().gameSettings.fovSetting;
        double d2 = d / 110.0;
        return new Vec3(-d2 + d2 / 2.5 - d2 / 8.0 + 0.16, 0.0, 0.4);
    }
}

