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
    private final Minecraft mc = Minecraft.func_71410_x();
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
            return this.mc.field_71439_g.func_70678_g(f);
        }
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        return this.prevSwingProgress + f2 * f;
    }

    private int getArmSwingAnimationEnd(EntityPlayerSP entityPlayerSP) {
        return entityPlayerSP.func_70644_a(Potion.field_76422_e) ? 5 - entityPlayerSP.func_70660_b(Potion.field_76422_e).func_76458_c() : (entityPlayerSP.func_70644_a(Potion.field_76419_f) ? 8 + entityPlayerSP.func_70660_b(Potion.field_76419_f).func_76458_c() * 2 : 6);
    }

    private void updateSwingProgress() {
        EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        this.prevSwingProgress = this.swingProgress;
        int n = this.getArmSwingAnimationEnd(entityPlayerSP);
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.punching.getValue()).booleanValue() && this.mc.field_71474_y.field_74312_F.func_151470_d() && this.mc.field_71476_x != null && this.mc.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && (!this.isSwingInProgress || this.swingProgressInt >= n >> 1 || this.swingProgressInt < 0)) {
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
        Item item = itemStack.func_77973_b();
        if (item == Items.field_151098_aY) {
            return false;
        }
        EnumAction enumAction = itemStack.func_77975_n();
        if (item == Items.field_151112_aM && (Boolean)oldAnimations.oldRod.getValue() == false || enumAction == EnumAction.NONE && (Boolean)oldAnimations.oldModel.getValue() == false || enumAction == EnumAction.BLOCK && (Boolean)oldAnimations.oldBlockhitting.getValue() == false || enumAction == EnumAction.BOW && !((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            return false;
        }
        EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
        float f3 = entityPlayerSP.field_70127_C + (entityPlayerSP.field_70125_A - entityPlayerSP.field_70127_C) * f2;
        GlStateManager.func_179094_E();
        GlStateManager.func_179114_b((float)f3, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(entityPlayerSP.field_70126_B + (entityPlayerSP.field_70177_z - entityPlayerSP.field_70126_B) * f2), (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179121_F();
        float f4 = entityPlayerSP.field_71164_i + (entityPlayerSP.field_71155_g - entityPlayerSP.field_71164_i) * f2;
        float f5 = entityPlayerSP.field_71163_h + (entityPlayerSP.field_71154_f - entityPlayerSP.field_71163_h) * f2;
        GlStateManager.func_179114_b((float)((entityPlayerSP.field_70125_A - f4) * 0.1f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((entityPlayerSP.field_70177_z - f5) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179091_B();
        if (item instanceof ItemCloth) {
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        }
        int n = this.mc.field_71441_e.func_175626_b(new BlockPos(entityPlayerSP.field_70165_t, entityPlayerSP.field_70163_u + (double)entityPlayerSP.func_70047_e(), entityPlayerSP.field_70161_v), 0);
        float f6 = n & 0xFFFF;
        float f7 = n >> 16;
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)f6, (float)f7);
        int n2 = item.func_82790_a(itemStack, 0);
        float f8 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f10 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)f8, (float)f9, (float)f10, (float)1.0f);
        GlStateManager.func_179094_E();
        int n3 = entityPlayerSP.func_71052_bv();
        float f11 = this.getSwingProgress(f2);
        boolean bl = false;
        if (((Boolean)oldAnimations.punching.getValue()).booleanValue() && n3 <= 0 && this.mc.field_71474_y.field_74313_G.func_151470_d()) {
            boolean bl2;
            boolean bl3 = enumAction == EnumAction.BLOCK;
            boolean bl4 = false;
            if (item instanceof ItemFood && entityPlayerSP.func_71043_e(bl2 = ((ItemFoodAccessor)((Object)item)).getAlwaysEdible())) {
                boolean bl5 = bl4 = enumAction == EnumAction.EAT || enumAction == EnumAction.DRINK;
            }
            if (bl3 || bl4) {
                bl = true;
            }
        }
        GlStateManager.func_179137_b((double)((Double)oldAnimations.handX.getValue()), (double)((Double)oldAnimations.handY.getValue()), (double)((Double)oldAnimations.handZ.getValue()));
        if ((n3 > 0 || bl) && enumAction != EnumAction.NONE && this.mc.field_71439_g.func_71052_bv() > 0) {
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
        if (item.func_77629_n_()) {
            GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (this.doFirstPersonTransform(itemStack)) {
            itemRenderer.func_178099_a(entityPlayerSP, itemStack, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else {
            itemRenderer.func_178099_a(entityPlayerSP, itemStack, ItemCameraTransforms.TransformType.NONE);
        }
        GlStateManager.func_179121_F();
        if (item instanceof ItemCloth) {
            GlStateManager.func_179084_k();
        }
        GlStateManager.func_179101_C();
        RenderHelper.func_74518_a();
        return true;
    }

    public void doSwordBlock3rdPersonTransform() {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.oldBlockhitting.getValue()).booleanValue()) {
            GlStateManager.func_179109_b((float)-0.15f, (float)-0.2f, (float)0.0f);
            GlStateManager.func_179114_b((float)70.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179109_b((float)0.119f, (float)0.2f, (float)-0.024f);
        }
    }

    private boolean doFirstPersonTransform(ItemStack itemStack) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        switch (itemStack.func_77975_n()) {
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
        GlStateManager.func_179109_b((float)0.58800083f, (float)0.36999986f, (float)-0.77000016f);
        GlStateManager.func_179109_b((float)0.0f, (float)-0.3f, (float)0.0f);
        GlStateManager.func_179152_a((float)1.5f, (float)1.5f, (float)1.5f);
        GlStateManager.func_179114_b((float)50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179109_b((float)-0.9375f, (float)-0.0625f, (float)0.0f);
        GlStateManager.func_179152_a((float)-2.0f, (float)2.0f, (float)-2.0f);
        if (this.mc.func_175599_af().func_175050_a(itemStack)) {
            GlStateManager.func_179152_a((float)0.58823526f, (float)0.58823526f, (float)0.58823526f);
            GlStateManager.func_179114_b((float)-25.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.func_179114_b((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179109_b((float)0.0f, (float)-0.25f, (float)-0.125f);
            GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
            return true;
        }
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        return false;
    }

    private void doConsumeAnimation(ItemStack itemStack, int n, float f) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (((Boolean)oldAnimations.oldEating.getValue()).booleanValue()) {
            float f2 = (float)n - f + 1.0f;
            float f3 = 1.0f - f2 / (float)itemStack.func_77988_m();
            float f4 = 1.0f - f3;
            f4 = f4 * f4 * f4;
            f4 = f4 * f4 * f4;
            f4 = f4 * f4 * f4;
            float f5 = 1.0f - f4;
            GlStateManager.func_179109_b((float)0.0f, (float)(MathHelper.func_76135_e((float)(MathHelper.func_76134_b((float)(f2 / 4.0f * (float)Math.PI)) * 0.1f)) * (float)((double)f3 > 0.2 ? 1 : 0)), (float)0.0f);
            GlStateManager.func_179109_b((float)(f5 * 0.6f), (float)(-f5 * 0.5f), (float)0.0f);
            GlStateManager.func_179114_b((float)(f5 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(f5 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(f5 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        } else {
            float f6 = (float)n - f + 1.0f;
            float f7 = f6 / (float)itemStack.func_77988_m();
            float f8 = MathHelper.func_76135_e((float)(MathHelper.func_76134_b((float)(f6 / 4.0f * (float)Math.PI)) * 0.1f));
            if (f7 >= 0.8f) {
                f8 = 0.0f;
            }
            GlStateManager.func_179109_b((float)0.0f, (float)f8, (float)0.0f);
            float f9 = 1.0f - (float)Math.pow(f7, 27.0);
            GlStateManager.func_179109_b((float)(f9 * 0.6f), (float)(f9 * -0.5f), (float)(f9 * 0.0f));
            GlStateManager.func_179114_b((float)(f9 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(f9 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(f9 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
    }

    private void doSwingTranslation(float f) {
        float f2 = MathHelper.func_76126_a((float)(f * (float)Math.PI));
        float f3 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f) * (float)Math.PI));
        GlStateManager.func_179109_b((float)(-f3 * 0.4f), (float)(MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f) * (float)Math.PI * 2.0f)) * 0.2f), (float)(-f2 * 0.2f));
    }

    private void doEquipAndSwingTransform(float f, float f2) {
        GlStateManager.func_179109_b((float)0.56f, (float)(-0.52f - (1.0f - f) * 0.6f), (float)-0.72f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(-f3 * 20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-f4 * 20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(-f4 * 80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
    }

    private void doSwordBlockAnimation() {
        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void doBowAnimation(ItemStack itemStack, int n, float f) {
        GlStateManager.func_179114_b((float)-18.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)-12.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-8.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)-0.9f, (float)0.2f, (float)0.0f);
        float f2 = (float)itemStack.func_77988_m() - ((float)n - f + 1.0f);
        float f3 = f2 / 20.0f;
        f3 = (f3 * f3 + f3 * 2.0f) / 3.0f;
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f3 > 0.1f) {
            GlStateManager.func_179109_b((float)0.0f, (float)(MathHelper.func_76126_a((float)((f2 - 0.1f) * 1.3f)) * 0.01f * (f3 - 0.1f)), (float)0.0f);
        }
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)(f3 * 0.1f));
        if (((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            GlStateManager.func_179114_b((float)-335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.func_179114_b((float)-50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179109_b((float)0.0f, (float)0.5f, (float)0.0f);
        }
        float f4 = 1.0f + f3 * 0.2f;
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)f4);
        if (((Boolean)oldAnimations.oldBow.getValue()).booleanValue()) {
            GlStateManager.func_179109_b((float)0.0f, (float)-0.5f, (float)0.0f);
            GlStateManager.func_179114_b((float)50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        }
    }

    public Vec3 getOffset() {
        double d = Minecraft.func_71410_x().field_71474_y.field_74334_X;
        double d2 = d / 110.0;
        return new Vec3(-d2 + d2 / 2.5 - d2 / 8.0 + 0.16, 0.0, 0.4);
    }
}

