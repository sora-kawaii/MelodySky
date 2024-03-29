/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import java.util.Random;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRenderScoreboard;
import xyz.Melody.module.modules.others.HUD;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiIngame.class})
public abstract class MixinGuiIngame {
    @Shadow
    @Final
    protected Random rand;

    @Inject(method="renderTooltip", at={@At(value="RETURN")})
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        EventRender2D e = new EventRender2D(partialTicks);
        EventBus.getInstance().call(e);
    }

    @Inject(method="renderScoreboard", at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        EventBus.getInstance().call(new EventRenderScoreboard(objective, scaledRes));
        if (((Boolean)hud.scoreBoard.getValue()).booleanValue()) {
            ci.cancel();
        }
    }
}

