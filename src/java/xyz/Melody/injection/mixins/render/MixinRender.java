package xyz.Melody.injection.mixins.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Render.class})
public abstract class MixinRender {
   @Shadow
   @Final
   protected RenderManager renderManager;

   @Shadow
   protected abstract boolean bindEntityTexture(Entity var1);

   @Shadow
   protected abstract void renderName(Entity var1, double var2, double var4, double var6);

   @Shadow
   protected boolean canRenderName(Entity entity) {
      return false;
   }

   @Shadow
   public abstract FontRenderer getFontRendererFromRenderManager();

   @Shadow
   protected abstract void renderOffsetLivingLabel(Entity var1, double var2, double var4, double var6, String var8, float var9, double var10);

   @Overwrite
   public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      this.renderName(entity, x, y, z);
   }
}
