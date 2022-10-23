package xyz.Melody.injection.mixins.item;

import java.util.Random;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.Melody.injection.mixins.render.MixinRender;

@Mixin({RenderEntityItem.class})
public abstract class MixinRenderEntityItem extends MixinRender {
   @Shadow
   private Random field_177079_e;
   @Shadow
   @Final
   private RenderItem itemRenderer;

   @Shadow
   protected abstract ResourceLocation getEntityTexture(Entity var1);

   @Shadow
   public abstract boolean shouldBob();

   @Shadow
   protected abstract int func_177078_a(ItemStack var1);
}
