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
        float f = this.mc.field_71462_r != null && this.mc.field_71462_r instanceof GuiChat ? -15.0f : -2.0f;
        RenderUtil.drawFastRoundedRect(0.0f, 0.0f, 1.0f, 1.0f, 1.0f, new Color(10, 10, 10, 10).getRGB());
        if (((Boolean)this.coords.getValue()).booleanValue()) {
            this.mc.field_71466_p.func_78276_b("X: " + (int)this.mc.field_71439_g.field_70165_t + "  Y: " + (int)this.mc.field_71439_g.field_70163_u + "  Z: " + (int)this.mc.field_71439_g.field_70161_v, 3, (int)((float)(scaledResolution.func_78328_b() - 10) + f), -1);
        }
        if (((Boolean)this.pots.getValue()).booleanValue()) {
            this.drawPotionStatus(scaledResolution);
        }
    }

    private void drawPotionStatus(ScaledResolution scaledResolution) {
        ArrayList<PotionEffect> arrayList = new ArrayList<PotionEffect>();
        for (Object object : this.mc.field_71439_g.func_70651_bq()) {
            arrayList.add((PotionEffect)object);
        }
        arrayList.sort(Comparator.comparingDouble(potionEffect -> -this.mc.field_71466_p.func_78256_a(I18n.func_135052_a((String)Potion.field_76425_a[potionEffect.func_76456_a()].func_76393_a(), (Object[])new Object[0]))));
        float f = this.mc.field_71462_r != null && this.mc.field_71462_r instanceof GuiChat ? -15.0f : -2.0f;
        for (PotionEffect potionEffect2 : arrayList) {
            Potion potion = Potion.field_76425_a[potionEffect2.func_76456_a()];
            String string = I18n.func_135052_a((String)potion.func_76393_a(), (Object[])new Object[0]);
            String string2 = "";
            if (potionEffect2.func_76458_c() == 1) {
                string = string + " II";
            } else if (potionEffect2.func_76458_c() == 2) {
                string = string + " III";
            } else if (potionEffect2.func_76458_c() == 3) {
                string = string + " IV";
            }
            if (potionEffect2.func_76459_b() < 600 && potionEffect2.func_76459_b() > 300) {
                string2 = string2 + "\u00a76 " + Potion.func_76389_a((PotionEffect)potionEffect2);
            } else if (potionEffect2.func_76459_b() < 300) {
                string2 = string2 + "\u00a7c " + Potion.func_76389_a((PotionEffect)potionEffect2);
            } else if (potionEffect2.func_76459_b() > 600) {
                string2 = string2 + "\u00a77 " + Potion.func_76389_a((PotionEffect)potionEffect2);
            }
            this.mc.field_71466_p.func_175063_a(string, scaledResolution.func_78326_a() - this.mc.field_71466_p.func_78256_a(string + string2), (float)(scaledResolution.func_78328_b() - 9) + f, potion.func_76401_j());
            this.mc.field_71466_p.func_175063_a(string2, scaledResolution.func_78326_a() - this.mc.field_71466_p.func_78256_a(string2), (float)(scaledResolution.func_78328_b() - 9) + f, -1);
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
                GlStateManager.func_179137_b((double)d, (double)d2, (double)0.0);
                GlStateManager.func_179152_a((float)(translate.getX() / f), (float)(translate.getY() / f2), (float)1.0f);
            }
        }
    }
}

