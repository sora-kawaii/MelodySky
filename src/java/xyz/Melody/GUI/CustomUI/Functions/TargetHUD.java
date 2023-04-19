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
    public void onTick(EventTick e) {
        Aura a = (Aura)Client.instance.getModuleManager().getModuleByClass(Aura.class);
        if (!((Boolean)a.targetHud.getValue()).booleanValue()) {
            return;
        }
        if (a.isEnabled()) {
            this.curTarget = a.curTarget;
        }
        if (this.curTarget != null && !this.curTarget.isEntityAlive() && this.curTarget.isDead || !a.isEnabled()) {
            this.curTarget = null;
        }
        if (this.curTarget == null) {
            this.healthBarWidth = 0.0;
        }
    }

    @EventHandler
    public void onRender(EventRender2D e) {
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
        FontRenderer font2 = this.mc.fontRendererObj;
        float health = this.curTarget.getHealth();
        double hpPercentage = health / this.curTarget.getMaxHealth();
        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
        double hpWidth = 92.0 * hpPercentage;
        int healthColor = ColorUtils.getHealthColor(this.curTarget.getHealth(), this.curTarget.getMaxHealth()).getRGB();
        int armorColor = ColorUtils.getArmorColor(this.curTarget.getTotalArmorValue(), this.curTarget.getMaxHealth()).getRGB();
        float right = this.x + 20;
        float height = this.y + 80;
        if (this.curTarget != null) {
            if (this.timer.hasReached(15.0)) {
                this.healthBarWidth = AnimationUtil.animate(hpWidth, this.healthBarWidth, 0.153f);
                this.timer.reset();
            }
            ScaledResolution scale = new ScaledResolution(this.mc);
            int factor = scale.getScaleFactor();
            RenderUtil.drawBorderedRect(right - 12.0f, height - 90.0f, right + 130.0f, height - 50.0f, 3.0f, new Color(10, 10, 10, 110).getRGB(), new Color(10, 10, 10, 50).getRGB());
            Scissor.start((right - 12.0f) * (float)factor, (height - 90.0f) * (float)factor, (right + 130.0f) * (float)factor, (height - 50.0f) * (float)factor);
            this.gblur.renderBlur(25.0f);
            Scissor.end();
            RenderUtil.drawFastRoundedRect(right - 12.0f, this.y - 10, right + 130.0f, this.y + 30, 0.0f, new Color(10, 10, 10, 20).getRGB());
            font2.drawString(this.curTarget.getName(), (int)right + 30, (int)height - 87, 0xFFFFFF);
            font2.drawString("HP:" + (int)this.curTarget.getHealth() + "/" + (int)this.curTarget.getMaxHealth() + " Hurt:" + (this.curTarget.hurtTime > 0), (int)right + 30, (int)height - 69, new Color(255, 255, 255).getRGB());
            font2.drawString("Dist: " + this.mc.thePlayer.getDistanceToEntity(this.curTarget), (int)right + 30, (int)height - 60, new Color(255, 255, 255).getRGB());
            if (this.curTarget instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)this.curTarget;
                ResourceLocation texture = ((AbstractClientPlayer)player).getLocationSkin();
                this.mc.getTextureManager().bindTexture(texture);
                Gui.drawScaledCustomSizeModalRect((int)(right - 11.0f), (int)(height - 89.0f), 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
            } else {
                GuiInventory.drawEntityOnScreen((int)(right + 9.0f), (int)(height - 54.0f), 15, 2.0f, 15.0f, this.curTarget);
            }
            RenderUtil.drawFastRoundedRect(right + 30.0f, height - 78.0f, right + 30.0f + (float)this.healthBarWidth, height - 71.0f, 0.0f, healthColor);
            Color c = new Color(armorColor);
            Color arm = new Color(c.getRed(), c.getGreen(), c.getBlue(), 130);
            RenderUtil.drawFastRoundedRect(right - 12.0f, height - 48.5f, right + 130.0f, height - 50.5f, 0.0f, arm.getRGB());
            RenderUtil.drawFastRoundedRect(right - 1.0f, height - 49.0f, right + 131.0f, height - 91.0f, 0.0f, new Color(255, 0, 0, 0).getRGB());
            GlStateManager.resetColor();
        }
    }

    @Override
    public void InScreenRender() {
        float right = this.x;
        float height = this.y + 70;
        RenderUtil.drawFastRoundedRect((int)right - 12, (int)height - 50, (int)right + 130, (int)height - 90, 0.0f, new Color(10, 10, 10, 130).getRGB());
        FontLoaders.NMSL18.drawString("TargetHUD", this.x + 2, this.y + 5, new Color(0, 0, 0).getRGB());
    }
}

