/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.render.ColorUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.Scissor;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.Utils.shader.LayerBlur;
import xyz.Melody.module.balance.Aura;

public final class TargetHUD
extends HUDApi {
    private TimerUtil timer = new TimerUtil();
    public EntityLivingBase curTarget;
    private double healthBarWidth;
    public double hue;
    private GaussianBlur gblur = new GaussianBlur();
    private LayerBlur lblur = new LayerBlur();

    public TargetHUD() {
        super("TargetHUD", 100, 150);
        this.setEnabled(true);
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        Aura aura = (Aura)Client.instance.getModuleManager().getModuleByClass(Aura.class);
        if (!((Boolean)aura.targetHud.getValue()).booleanValue()) {
            return;
        }
        if (aura.isEnabled()) {
            this.curTarget = aura.curTarget;
        }
        if (this.curTarget != null && !this.curTarget.isEntityAlive() && this.curTarget.isDead || !aura.isEnabled()) {
            this.curTarget = null;
        }
        if (this.curTarget == null) {
            this.healthBarWidth = 0.0;
        }
    }

    @EventHandler
    public void onRender(EventRender2D eventRender2D) {
        if (!Client.instance.getModuleManager().getModuleByClass(Aura.class).isEnabled() && !(Minecraft.getMinecraft().currentScreen instanceof HUDScreen)) {
            return;
        }
        if (this.y < 75) {
            this.y = 75;
        }
        if (this.curTarget != null) {
            this.Melody();
        }
    }

    private void Melody() {
        FontRenderer fontRenderer = this.mc.fontRendererObj;
        float f = this.curTarget.getHealth();
        double d = f / this.curTarget.getMaxHealth();
        d = MathHelper.clamp_double(d, 0.0, 1.0);
        double d2 = 92.0 * d;
        int n = ColorUtils.getHealthColor(this.curTarget.getHealth(), this.curTarget.getMaxHealth()).getRGB();
        int n2 = ColorUtils.getArmorColor(this.curTarget.getTotalArmorValue(), this.curTarget.getMaxHealth()).getRGB();
        float f2 = this.x + 20;
        float f3 = this.y + 80;
        if (this.curTarget != null) {
            Object object;
            if (this.timer.hasReached(15.0)) {
                this.healthBarWidth = AnimationUtil.animate(d2, this.healthBarWidth, 0.153f);
                this.timer.reset();
            }
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int n3 = scaledResolution.getScaleFactor();
            RenderUtil.drawBorderedRect(f2 - 12.0f, f3 - 90.0f, f2 + 130.0f, f3 - 50.0f, 3.0f, new Color(10, 10, 10, 110).getRGB(), new Color(10, 10, 10, 50).getRGB());
            Scissor.start((f2 - 12.0f) * (float)n3, (f3 - 90.0f) * (float)n3, (f2 + 130.0f) * (float)n3, (f3 - 50.0f) * (float)n3);
            this.gblur.renderBlur(25.0f);
            Scissor.end();
            RenderUtil.drawFastRoundedRect(f2 - 12.0f, this.y - 10, f2 + 130.0f, this.y + 30, 0.0f, new Color(10, 10, 10, 20).getRGB());
            fontRenderer.drawString(this.curTarget.getName(), (int)f2 + 30, (int)f3 - 87, 0xFFFFFF);
            fontRenderer.drawString("HP:" + (int)this.curTarget.getHealth() + "/" + (int)this.curTarget.getMaxHealth() + " Hurt:" + (this.curTarget.hurtTime > 0), (int)f2 + 30, (int)f3 - 69, new Color(255, 255, 255).getRGB());
            fontRenderer.drawString("Dist: " + this.mc.thePlayer.getDistanceToEntity(this.curTarget), (int)f2 + 30, (int)f3 - 60, new Color(255, 255, 255).getRGB());
            if (this.curTarget instanceof EntityPlayer) {
                object = (EntityPlayer)this.curTarget;
                ResourceLocation resourceLocation = ((AbstractClientPlayer)object).getLocationSkin();
                this.mc.getTextureManager().bindTexture(resourceLocation);
                Gui.drawScaledCustomSizeModalRect((int)(f2 - 11.0f), (int)(f3 - 89.0f), 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
            } else {
                GuiInventory.drawEntityOnScreen((int)(f2 + 9.0f), (int)(f3 - 54.0f), 15, 2.0f, 15.0f, this.curTarget);
            }
            RenderUtil.drawFastRoundedRect(f2 + 30.0f, f3 - 78.0f, f2 + 30.0f + (float)this.healthBarWidth, f3 - 71.0f, 0.0f, n);
            object = new Color(n2);
            Color color = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue(), 130);
            RenderUtil.drawFastRoundedRect(f2 - 12.0f, f3 - 48.5f, f2 + 130.0f, f3 - 50.5f, 0.0f, color.getRGB());
            RenderUtil.drawFastRoundedRect(f2 - 1.0f, f3 - 49.0f, f2 + 131.0f, f3 - 91.0f, 0.0f, new Color(255, 0, 0, 0).getRGB());
            GlStateManager.resetColor();
        }
    }

    @Override
    public void InScreenRender() {
        float f = this.x;
        float f2 = this.y + 70;
        RenderUtil.drawFastRoundedRect((int)f - 12, (int)f2 - 50, (int)f + 130, (int)f2 - 90, 0.0f, new Color(10, 10, 10, 130).getRGB());
        FontLoaders.NMSL18.drawString("TargetHUD", this.x + 2, this.y + 5, new Color(0, 0, 0).getRGB());
    }
}

