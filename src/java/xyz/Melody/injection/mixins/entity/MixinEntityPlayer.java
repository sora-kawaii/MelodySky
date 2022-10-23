package xyz.Melody.injection.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {
   @Shadow
   public PlayerCapabilities capabilities = new PlayerCapabilities();
   @Shadow
   public float eyeHeight;
   @Shadow
   public Container openContainer;
   @Shadow
   protected int flyToggleTimer;

   @Shadow
   public abstract GameProfile getGameProfile();

   @Shadow
   protected abstract boolean canTriggerWalking();

   @Shadow
   protected abstract String getSwimSound();

   @Shadow
   public abstract boolean isUsingItem();

   @Shadow
   public abstract ItemStack getItemInUse();

   @Shadow
   public abstract int getItemInUseDuration();

   @Shadow
   public abstract boolean isPlayerSleeping();

   @Shadow
   public abstract String getName();

   @Shadow
   public abstract ItemStack getHeldItem();

   @Shadow
   public abstract FoodStats getFoodStats();

   @Overwrite
   public float getEyeHeight() {
      float f = this.eyeHeight;
      if (this.isPlayerSleeping()) {
         f = 0.2F;
      }

      if (this.isSneaking()) {
         f -= 0.08F;
      }

      return f;
   }
}
