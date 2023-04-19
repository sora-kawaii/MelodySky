/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class Cam
extends Module {
    public Option<Boolean> name = new Option<Boolean>("Show Self Name", true);
    public Option<Boolean> showRank = new Option<Boolean>("Show Custom Rank", true);
    public Option<Boolean> colorHurtCam = new Option<Boolean>("ColorHurtCam", true);
    public Option<Boolean> bht = new Option<Boolean>("NoHurtCam", true);
    public Option<Boolean> noFire = new Option<Boolean>("NoFireRender", false);
    public Option<Boolean> noBlindness = new Option<Boolean>("NoBlindness", true);
    public Option<Boolean> noClip = new Option<Boolean>("NoClip", false);

    public Cam() {
        super("Camera", ModuleType.Render);
        this.addValues(this.name, this.showRank, this.bht, this.colorHurtCam, this.noFire, this.noBlindness, this.noClip);
        this.setModInfo("Better Camera Render.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private void onTick() {
        if (!((Boolean)this.noBlindness.getValue()).booleanValue()) {
            return;
        }
        if (this.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
            this.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }
    }

    @EventHandler
    private void onHurt(EventRender2D event) {
        if (!((Boolean)this.bht.getValue()).booleanValue()) {
            return;
        }
        if (!((Boolean)this.colorHurtCam.getValue()).booleanValue()) {
            return;
        }
        ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
        if (this.mc.thePlayer.hurtTime > 0) {
            RenderUtil.drawBorderedRect(0.0f, 0.0f, sc.getScaledWidth(), sc.getScaledHeight(), 10.0f, new Color(25 * this.mc.thePlayer.hurtTime, 20, 20, 20 * this.mc.thePlayer.hurtTime).getRGB(), new Color(255, 255, 255, 1).getRGB());
        }
    }
}

