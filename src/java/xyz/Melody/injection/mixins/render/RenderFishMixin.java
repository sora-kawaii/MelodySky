/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.Melody.Client;
import xyz.Melody.System.Animations.AnimationHandler;
import xyz.Melody.module.modules.others.OldAnimations;

@Mixin(value={RenderFish.class})
public class RenderFishMixin {
    @Redirect(method="doRender", at=@At(value="NEW", target="net/minecraft/util/Vec3", ordinal=1))
    private Vec3 oldMelodyFishingLine(double d, double d2, double d3) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (oldAnimations.isEnabled()) {
            return (Boolean)oldAnimations.oldRod.getValue() == false ? new Vec3(d, d2, d3) : AnimationHandler.getInstance().getOffset();
        }
        return new Vec3(d, d2, d3);
    }
}

