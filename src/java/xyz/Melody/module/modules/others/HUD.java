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
    private void remixHUD(EventRender2D event) {
    }

    @EventHandler
    private void onRenderInfo(EventRender2D event) {
        ScaledResolution mainWindow = new ScaledResolution(this.mc);
        float infoY = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0f : -2.0f;
        RenderUtil.drawFastRoundedRect(0.0f, 0.0f, 1.0f, 1.0f, 1.0f, new Color(10, 10, 10, 10).getRGB());
        if (((Boolean)this.coords.getValue()).booleanValue()) {
            this.mc.fontRendererObj.drawString("X: " + (int)this.mc.thePlayer.posX + "  Y: " + (int)this.mc.thePlayer.posY + "  Z: " + (int)this.mc.thePlayer.posZ, 3, (int)((float)(mainWindow.getScaledHeight() - 10) + infoY), -1);
        }
        if (((Boolean)this.pots.getValue()).booleanValue()) {
            this.drawPotionStatus(mainWindow);
        }
    }

    private void drawPotionStatus(ScaledResolution sr) {
        ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
        for (PotionEffect o : this.mc.thePlayer.getActivePotionEffects()) {
            potions.add(o);
        }
        potions.sort(Comparator.comparingDouble(this::lambda$drawPotionStatus$0));
        float pY = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0f : -2.0f;
        for (PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName(), new Object[0]);
            String PType = "";
            if (effect.getAmplifier() == 1) {
                name = name + " II";
            } else if (effect.getAmplifier() == 2) {
                name = name + " III";
            } else if (effect.getAmplifier() == 3) {
                name = name + " IV";
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = PType + "\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + "\u00a77 " + Potion.getDurationString(effect);
            }
            this.mc.fontRendererObj.drawStringWithShadow(name, sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(name + PType), (float)(sr.getScaledHeight() - 9) + pY, potion.getLiquidColor());
            this.mc.fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(PType), (float)(sr.getScaledHeight() - 9) + pY, -1);
            pY -= 9.0f;
        }
    }

    public void handleContainer(Translate translate, Opacity opacity, float width, float height) {
        if (((Boolean)this.blur.getValue()).booleanValue()) {
            this.gblur.renderBlur(opacity.getOpacity());
            opacity.interp(((Double)this.bMax.getValue()).floatValue(), ((Double)this.bSpeed.getValue()).intValue());
        }
        if (this.isEnabled() && ((Boolean)this.container.getValue()).booleanValue()) {
            AutoTerminals at = (AutoTerminals)Client.instance.getModuleManager().getModuleByClass(AutoTerminals.class);
            AutoEnchantTable aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class);
            SlugFishing slug = (SlugFishing)Client.instance.getModuleManager().getModuleByClass(SlugFishing.class);
            if (!(slug.isEnabled() || at.currentTerminal != null && at.currentTerminal != AutoTerminals.TerminalType.NONE || aet.currentType != null && aet.currentType != AutoEnchantTable.expType.NONE)) {
                translate.interpolate(width, height, 12.0);
                double xmod = width / 2.0f - translate.getX() / 2.0f;
                double ymod = height / 2.0f - translate.getY() / 2.0f;
                GlStateManager.translate(xmod, ymod, 0.0);
                GlStateManager.scale(translate.getX() / width, translate.getY() / height, 1.0f);
            }
        }
    }

    private double lambda$drawPotionStatus$0(PotionEffect effect) {
        return -this.mc.fontRendererObj.getStringWidth(I18n.format(Potion.potionTypes[effect.getPotionID()].getName(), new Object[0]));
    }
}

