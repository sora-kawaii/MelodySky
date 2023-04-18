/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import xyz.Melody.Utils.fakemc.FakeNetHandlerPlayClient;
import xyz.Melody.Utils.fakemc.FakeWorld;
import xyz.Melody.injection.mixins.render.ERA;

public final class DrawEntity {
    private Minecraft mc = Minecraft.func_71410_x();
    public WorldClient world;
    public EntityPlayerSP player;

    public void draw(int n, int n2, int n3, float f, float f2) {
        GlStateManager.func_179094_E();
        try {
            if (this.player == null || this.player.field_70170_p == null) {
                this.init();
            }
            if (this.mc.func_175598_ae().field_78722_g == null || this.mc.func_175598_ae().field_78734_h == null) {
                this.mc.func_175598_ae().func_180597_a(this.world, this.mc.field_71466_p, this.player, this.player, this.mc.field_71474_y, 0.0f);
            }
            if (this.world != null && this.player != null) {
                this.mc.field_71439_g = this.player;
                this.mc.field_71441_e = this.world;
                this.drawEntityOnScreen(n, n2, n3, f, f2, this.player);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            this.player = null;
            this.world = null;
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        this.mc.field_71439_g = null;
        this.mc.field_71441_e = null;
    }

    private void init() {
        try {
            boolean bl = this.world == null;
            WorldSettings worldSettings = new WorldSettings(0L, WorldSettings.GameType.NOT_SET, true, false, WorldType.field_77137_b);
            FakeNetHandlerPlayClient fakeNetHandlerPlayClient = new FakeNetHandlerPlayClient(this.mc);
            if (bl) {
                this.world = new FakeWorld(worldSettings, fakeNetHandlerPlayClient);
            }
            if (bl || this.player == null) {
                this.player = new EntityPlayerSP(this.mc, this.world, fakeNetHandlerPlayClient, null);
                int n = 0;
                for (EnumPlayerModelParts enumPlayerModelParts : this.mc.field_71474_y.func_178876_d()) {
                    n |= enumPlayerModelParts.func_179327_a();
                }
                this.player.func_70096_w().func_75692_b(10, (byte)n);
                this.player.field_71093_bK = 0;
                this.player.field_71158_b = new MovementInputFromOptions(this.mc.field_71474_y);
            }
            this.updateLightmap(this.mc, this.world);
            this.mc.func_175598_ae().func_180597_a(this.world, this.mc.field_71466_p, this.player, this.player, this.mc.field_71474_y, 0.0f);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            this.player = null;
            this.world = null;
        }
    }

    private void drawEntityOnScreen(int n, int n2, float f, float f2, float f3, EntityLivingBase entityLivingBase) {
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179141_d();
        GlStateManager.func_179142_g();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GuiInventory.func_147046_a((int)n, (int)n2, (int)((int)f), (float)f2, (float)f3, (EntityLivingBase)entityLivingBase);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)20.0f);
    }

    private void updateLightmap(Minecraft minecraft, World world) {
        float f = world.func_72971_b(1.0f);
        float f2 = f * 0.95f + 0.05f;
        for (int i = 0; i < 256; ++i) {
            float f3 = world.field_73011_w.func_177497_p()[i / 16] * f2;
            float f4 = world.field_73011_w.func_177497_p()[i % 16] * (((ERA)((Object)minecraft.field_71460_t)).getTorchFlickerX() * 0.1f + 1.5f);
            float f5 = f3 * (f * 0.65f + 0.35f);
            float f6 = f3 * (f * 0.65f + 0.35f);
            float f7 = f4 * ((f4 * 0.6f + 0.4f) * 0.6f + 0.4f);
            float f8 = f4 * (f4 * f4 * 0.6f + 0.4f);
            float f9 = f5 + f4;
            float f10 = f6 + f7;
            float f11 = f3 + f8;
            f9 = f9 * 0.96f + 0.03f;
            f10 = f10 * 0.96f + 0.03f;
            f11 = f11 * 0.96f + 0.03f;
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            if (f10 > 1.0f) {
                f10 = 1.0f;
            }
            if (f11 > 1.0f) {
                f11 = 1.0f;
            }
            float f12 = minecraft.field_71474_y.field_74333_Y;
            float f13 = 1.0f - f9;
            float f14 = 1.0f - f10;
            float f15 = 1.0f - f11;
            f13 = 1.0f - f13 * f13 * f13 * f13;
            f14 = 1.0f - f14 * f14 * f14 * f14;
            f15 = 1.0f - f15 * f15 * f15 * f15;
            f9 = f9 * (1.0f - f12) + f13 * f12;
            f10 = f10 * (1.0f - f12) + f14 * f12;
            f11 = f11 * (1.0f - f12) + f15 * f12;
            f9 = f9 * 0.96f + 0.03f;
            f10 = f10 * 0.96f + 0.03f;
            f11 = f11 * 0.96f + 0.03f;
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            if (f10 > 1.0f) {
                f10 = 1.0f;
            }
            if (f11 > 1.0f) {
                f11 = 1.0f;
            }
            if (f9 < 0.0f) {
                f9 = 0.0f;
            }
            if (f10 < 0.0f) {
                f10 = 0.0f;
            }
            if (f11 < 0.0f) {
                f11 = 0.0f;
            }
            int n = (int)(f9 * 255.0f);
            int n2 = (int)(f10 * 255.0f);
            int n3 = (int)(f11 * 255.0f);
            ((ERA)((Object)minecraft.field_71460_t)).getLightmapColors()[i] = 0xFF000000 | n << 16 | n2 << 8 | n3;
        }
        ((ERA)((Object)minecraft.field_71460_t)).getLightmapTexture().func_110564_a();
    }
}

