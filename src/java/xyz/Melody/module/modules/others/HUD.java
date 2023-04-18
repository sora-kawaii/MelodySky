/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.Utils.animate.Translate;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.Fishing.SlugFishing;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;
import xyz.Melody.module.modules.QOL.Dungeons.AutoTerminals;

public final class HUD
extends Module {
    public Option<Boolean> blur = new Option<Boolean>("Gui Blur", true);
    public Option<Boolean> cgblur = new Option<Boolean>("ClickGui Blur", true);
    public Numbers<Double> bMax = new Numbers<Double>("Blur Value", 25.0, 10.0, 100.0, 5.0);
    public Numbers<Double> bSpeed = new Numbers<Double>("Blur Speed", 5.0, 1.0, 10.0, 1.0);
    public Option<Boolean> container = new Option<Boolean>("Gui Animations", true);
    public Option<Boolean> scoreBoard = new Option<Boolean>("ScoreBoard", true);
    private Option<Boolean> coords = new Option<Boolean>("Coords", true);
    private Option<Boolean> pots = new Option<Boolean>("Effects", true);
    private GaussianBlur gblur = new GaussianBlur();

    public HUD() {
        super("HUD", new String[]{"gui"}, ModuleType.Others);
        this.addValues(this.blur, this.cgblur, this.bMax, this.bSpeed, this.container, this.scoreBoard, this.coords, this.pots);
        this.setEnabled(true);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.setEnabled(true);
        super.onDisable();
    }

    @EventHandler
    private void remixHUD(EventRender2D eventRender2D) {
    }

    @EventHandler
    private void onRenderInfo(EventRender2D eventRender2D) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        float f = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0f : -2.0f;
        RenderUtil.drawFastRoundedRect(0.0f, 0.0f, 1.0f, 1.0f, 1.0f, new Color(10, 10, 10, 10).getRGB());
        if (((Boolean)this.coords.getValue()).booleanValue()) {
            this.mc.fontRendererObj.drawString("X: " + (int)this.mc.thePlayer.posX + "  Y: " + (int)this.mc.thePlayer.posY + "  Z: " + (int)this.mc.thePlayer.posZ, 3, (int)((float)(scaledResolution.getScaledHeight() - 10) + f), -1);
        }
        if (((Boolean)this.pots.getValue()).booleanValue()) {
            this.drawPotionStatus(scaledResolution);
        }
    }

    private void drawPotionStatus(ScaledResolution scaledResolution) {
        ArrayList<PotionEffect> arrayList = new ArrayList<PotionEffect>();
        for (PotionEffect object : this.mc.thePlayer.getActivePotionEffects()) {
            arrayList.add(object);
        }
        arrayList.sort(Comparator.comparingDouble(potionEffect -> -this.mc.fontRendererObj.getStringWidth(I18n.format(Potion.potionTypes[potionEffect.getPotionID()].getName(), new Object[0]))));
        float f = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0f : -2.0f;
        for (PotionEffect potionEffect2 : arrayList) {
            Potion potion = Potion.potionTypes[potionEffect2.getPotionID()];
            String string = I18n.format(potion.getName(), new Object[0]);
            String string2 = "";
            if (potionEffect2.getAmplifier() == 1) {
                string = string + " II";
            } else if (potionEffect2.getAmplifier() == 2) {
                string = string + " III";
            } else if (potionEffect2.getAmplifier() == 3) {
                string = string + " IV";
            }
            if (potionEffect2.getDuration() < 600 && potionEffect2.getDuration() > 300) {
                string2 = string2 + "\u00a76 " + Potion.getDurationString(potionEffect2);
            } else if (potionEffect2.getDuration() < 300) {
                string2 = string2 + "\u00a7c " + Potion.getDurationString(potionEffect2);
            } else if (potionEffect2.getDuration() > 600) {
                string2 = string2 + "\u00a77 " + Potion.getDurationString(potionEffect2);
            }
            this.mc.fontRendererObj.drawStringWithShadow(string, scaledResolution.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(string + string2), (float)(scaledResolution.getScaledHeight() - 9) + f, potion.getLiquidColor());
            this.mc.fontRendererObj.drawStringWithShadow(string2, scaledResolution.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(string2), (float)(scaledResolution.getScaledHeight() - 9) + f, -1);
            f -= 9.0f;
        }
    }

    public void handleContainer(Translate translate, Opacity opacity, float f, float f2) {
        if (((Boolean)this.blur.getValue()).booleanValue()) {
            this.gblur.renderBlur(opacity.getOpacity());
            opacity.interp(((Double)this.bMax.getValue()).floatValue(), ((Double)this.bSpeed.getValue()).intValue());
        }
        if (this.isEnabled() && ((Boolean)this.container.getValue()).booleanValue()) {
            AutoTerminals autoTerminals = (AutoTerminals)Client.instance.getModuleManager().getModuleByClass(AutoTerminals.class);
            AutoEnchantTable autoEnchantTable = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class);
            SlugFishing slugFishing = (SlugFishing)Client.instance.getModuleManager().getModuleByClass(SlugFishing.class);
            if (!(slugFishing.isEnabled() || autoTerminals.currentTerminal != null && autoTerminals.currentTerminal != AutoTerminals.TerminalType.NONE || autoEnchantTable.currentType != null && autoEnchantTable.currentType != AutoEnchantTable.expType.NONE)) {
                translate.interpolate(f, f2, 12.0);
                double d = f / 2.0f - translate.getX() / 2.0f;
                double d2 = f2 / 2.0f - translate.getY() / 2.0f;
                GlStateManager.translate(d, d2, 0.0);
                GlStateManager.scale(translate.getX() / f, translate.getY() / f2, 1.0f);
            }
        }
    }
}

