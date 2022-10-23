package xyz.Melody.injection.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({EntityRenderer.class})
public interface EntityRendererAccessor {
}
