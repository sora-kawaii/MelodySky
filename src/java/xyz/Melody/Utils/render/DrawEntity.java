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
    private Minecraft mc = Minecraft.getMinecraft();
    public WorldClient world;
    public EntityPlayerSP player;

    public void draw(int x, int y, int scale, float mouseX, float mouseY) {
        GlStateManager.pushMatrix();
        try {
            if (this.player == null || this.player.worldObj == null) {
                this.init();
            }
            if (this.mc.getRenderManager().worldObj == null || this.mc.getRenderManager().livingPlayer == null) {
                this.mc.getRenderManager().cacheActiveRenderInfo(this.world, this.mc.fontRendererObj, this.player, this.player, this.mc.gameSettings, 0.0f);
            }
            if (this.world != null && this.player != null) {
                this.mc.thePlayer = this.player;
                this.mc.theWorld = this.world;
                this.drawEntityOnScreen(x, y, scale, mouseX, mouseY, this.player);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.player = null;
            this.world = null;
        }
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.mc.thePlayer = null;
        this.mc.theWorld = null;
    }

    private void init() {
        try {
            boolean createNewWorld = this.world == null;
            WorldSettings worldSettings = new WorldSettings(0L, WorldSettings.GameType.NOT_SET, true, false, WorldType.DEFAULT);
            FakeNetHandlerPlayClient netHandler = new FakeNetHandlerPlayClient(this.mc);
            if (createNewWorld) {
                this.world = new FakeWorld(worldSettings, netHandler);
            }
            if (createNewWorld || this.player == null) {
                this.player = new EntityPlayerSP(this.mc, this.world, netHandler, null);
                int ModelParts = 0;
                for (EnumPlayerModelParts enumplayermodelparts : this.mc.gameSettings.getModelParts()) {
                    ModelParts |= enumplayermodelparts.getPartMask();
                }
                this.player.getDataWatcher().updateObject(10, (byte)ModelParts);
                this.player.dimension = 0;
                this.player.movementInput = new MovementInputFromOptions(this.mc.gameSettings);
            }
            this.updateLightmap(this.mc, this.world);
            this.mc.getRenderManager().cacheActiveRenderInfo(this.world, this.mc.fontRendererObj, this.player, this.player, this.mc.gameSettings, 0.0f);
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.player = null;
            this.world = null;
        }
    }

    private void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableColorMaterial();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiInventory.drawEntityOnScreen(posX, posY, (int)scale, mouseX, mouseY, ent);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.translate(0.0f, 0.0f, 20.0f);
    }

    private void updateLightmap(Minecraft mc, World world) {
        float f = world.getSunBrightness(1.0f);
        float f2 = f * 0.95f + 0.05f;
        for (int i = 0; i < 256; ++i) {
            float f3 = world.provider.getLightBrightnessTable()[i / 16] * f2;
            float f4 = world.provider.getLightBrightnessTable()[i % 16] * (((ERA)((Object)mc.entityRenderer)).getTorchFlickerX() * 0.1f + 1.5f);
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
            float f12 = mc.gameSettings.gammaSetting;
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
            int k = (int)(f9 * 255.0f);
            int n = (int)(f10 * 255.0f);
            int i2 = (int)(f11 * 255.0f);
            ((ERA)((Object)mc.entityRenderer)).getLightmapColors()[i] = 0xFF000000 | k << 16 | n << 8 | i2;
        }
        ((ERA)((Object)mc.entityRenderer)).getLightmapTexture().updateDynamicTexture();
    }
}

