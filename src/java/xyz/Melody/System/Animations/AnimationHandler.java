package xyz.Melody.System.Animations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xyz.Melody.Client;
import xyz.Melody.injection.mixins.item.ItemFoodAccessor;
import xyz.Melody.module.modules.others.OldAnimations;

public class AnimationHandler {
   private static final AnimationHandler INSTANCE = new AnimationHandler();
   private final Minecraft mc = Minecraft.getMinecraft();
   public float prevSwingProgress;
   public float swingProgress;
   private int swingProgressInt;
   private boolean isSwingInProgress;

   public static AnimationHandler getInstance() {
      return INSTANCE;
   }

   public float getSwingProgress(float partialTickTime) {
      float currentProgress = this.swingProgress - this.prevSwingProgress;
      if (!this.isSwingInProgress) {
         return this.mc.thePlayer.getSwingProgress(partialTickTime);
      } else {
         if (currentProgress < 0.0F) {
            ++currentProgress;
         }

         return this.prevSwingProgress + currentProgress * partialTickTime;
      }
   }

   private int getArmSwingAnimationEnd(EntityPlayerSP player) {
      return player.isPotionActive(Potion.digSpeed) ? 5 - player.getActivePotionEffect(Potion.digSpeed).getAmplifier() : (player.isPotionActive(Potion.digSlowdown) ? 8 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() * 2 : 6);
   }

   private void updateSwingProgress() {
      EntityPlayerSP player = this.mc.thePlayer;
      if (player != null) {
         this.prevSwingProgress = this.swingProgress;
         int max = this.getArmSwingAnimationEnd(player);
         OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
         if ((Boolean)anim.punching.getValue() && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && (!this.isSwingInProgress || this.swingProgressInt >= max >> 1 || this.swingProgressInt < 0)) {
            this.isSwingInProgress = true;
            this.swingProgressInt = -1;
         }

         if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= max) {
               this.swingProgressInt = 0;
               this.isSwingInProgress = false;
            }
         } else {
            this.swingProgressInt = 0;
         }

         this.swingProgress = (float)this.swingProgressInt / (float)max;
      }
   }

   @SubscribeEvent
   public void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase == Phase.END) {
         this.updateSwingProgress();
      }

   }

   public boolean renderItemInFirstPerson(ItemRenderer renderer, ItemStack stack, float equipProgress, float partialTicks) {
      if (stack == null) {
         return false;
      } else {
         OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
         Item item = stack.getItem();
         if (item != Items.filled_map && !this.mc.getRenderItem().shouldRenderItemIn3D(stack)) {
            EnumAction action = stack.getItemUseAction();
            if (item == Items.fishing_rod && !(Boolean)anim.oldRod.getValue() || action == EnumAction.NONE && !(Boolean)anim.oldModel.getValue() || action == EnumAction.BLOCK && !(Boolean)anim.oldBlockhitting.getValue() || action == EnumAction.BOW && !(Boolean)anim.oldBow.getValue()) {
               return false;
            } else {
               EntityPlayerSP player = this.mc.thePlayer;
               float var4 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
               GlStateManager.pushMatrix();
               GlStateManager.rotate(var4, 1.0F, 0.0F, 0.0F);
               GlStateManager.rotate(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
               RenderHelper.enableStandardItemLighting();
               GlStateManager.popMatrix();
               float pitch = player.prevRenderArmPitch + (player.renderArmPitch - player.prevRenderArmPitch) * partialTicks;
               float yaw = player.prevRenderArmYaw + (player.renderArmYaw - player.prevRenderArmYaw) * partialTicks;
               GlStateManager.rotate((player.rotationPitch - pitch) * 0.1F, 1.0F, 0.0F, 0.0F);
               GlStateManager.rotate((player.rotationYaw - yaw) * 0.1F, 0.0F, 1.0F, 0.0F);
               GlStateManager.enableRescaleNormal();
               if (item instanceof ItemCloth) {
                  GlStateManager.enableBlend();
                  GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
               }

               int i = this.mc.theWorld.getCombinedLight(new BlockPos(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ), 0);
               float brightnessX = (float)(i & '\uffff');
               float brightnessY = (float)(i >> 16);
               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);
               int rgb = item.getColorFromItemStack(stack, 0);
               float red = (float)(rgb >> 16 & 255) / 255.0F;
               float green = (float)(rgb >> 8 & 255) / 255.0F;
               float blue = (float)(rgb & 255) / 255.0F;
               GlStateManager.color(red, green, blue, 1.0F);
               GlStateManager.pushMatrix();
               int useCount = player.getItemInUseCount();
               float swingProgress = this.getSwingProgress(partialTicks);
               boolean blockHitOverride = false;
               if ((Boolean)anim.punching.getValue() && useCount <= 0 && this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                  boolean block = action == EnumAction.BLOCK;
                  boolean consume = false;
                  if (item instanceof ItemFood) {
                     boolean alwaysEdible = ((ItemFoodAccessor)item).getAlwaysEdible();
                     if (player.canEat(alwaysEdible)) {
                        consume = action == EnumAction.EAT || action == EnumAction.DRINK;
                     }
                  }

                  if (block || consume) {
                     blockHitOverride = true;
                  }
               }

               if ((useCount > 0 || blockHitOverride) && action != EnumAction.NONE && this.mc.thePlayer.isUsingItem()) {
                  switch (action) {
                     case EAT:
                     case DRINK:
                        this.doConsumeAnimation(stack, useCount, partialTicks);
                        this.doEquipAndSwingTransform(equipProgress, (Boolean)anim.oldBlockhitting.getValue() ? swingProgress : 0.0F);
                        break;
                     case BLOCK:
                        this.doEquipAndSwingTransform(equipProgress, (Boolean)anim.oldBlockhitting.getValue() ? swingProgress : 0.0F);
                        this.doSwordBlockAnimation();
                        break;
                     case BOW:
                        this.doEquipAndSwingTransform(equipProgress, (Boolean)anim.oldBlockhitting.getValue() ? swingProgress : 0.0F);
                        this.doBowAnimation(stack, useCount, partialTicks);
                  }
               } else {
                  this.doSwingTranslation(swingProgress);
                  this.doEquipAndSwingTransform(equipProgress, swingProgress);
               }

               if (item.shouldRotateAroundWhenRendering()) {
                  GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               }

               if (this.doFirstPersonTransform(stack)) {
                  renderer.renderItem(player, stack, TransformType.FIRST_PERSON);
               } else {
                  renderer.renderItem(player, stack, TransformType.NONE);
               }

               GlStateManager.popMatrix();
               if (item instanceof ItemCloth) {
                  GlStateManager.disableBlend();
               }

               GlStateManager.disableRescaleNormal();
               RenderHelper.disableStandardItemLighting();
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void doSwordBlock3rdPersonTransform() {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      if ((Boolean)anim.oldBlockhitting.getValue()) {
         GlStateManager.translate(-0.15F, -0.2F, 0.0F);
         GlStateManager.rotate(70.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(0.119F, 0.2F, -0.024F);
      }

   }

   private boolean doFirstPersonTransform(ItemStack stack) {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      switch (stack.getItemUseAction()) {
         case EAT:
         case DRINK:
            if (!(Boolean)anim.oldEating.getValue()) {
               return true;
            }
            break;
         case BLOCK:
            if (!(Boolean)anim.oldBlockhitting.getValue()) {
               return true;
            }
            break;
         case BOW:
            if (!(Boolean)anim.oldBow.getValue()) {
               return true;
            }
            break;
         case NONE:
            if (!(Boolean)anim.oldModel.getValue()) {
               return true;
            }
      }

      GlStateManager.translate(0.58800083F, 0.36999986F, -0.77000016F);
      GlStateManager.translate(0.0F, -0.3F, 0.0F);
      GlStateManager.scale(1.5F, 1.5F, 1.5F);
      GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(-0.9375F, -0.0625F, 0.0F);
      GlStateManager.scale(-2.0F, 2.0F, -2.0F);
      if (this.mc.getRenderItem().shouldRenderItemIn3D(stack)) {
         GlStateManager.scale(0.58823526F, 0.58823526F, 0.58823526F);
         GlStateManager.rotate(-25.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.25F, -0.125F);
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         return true;
      } else {
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         return false;
      }
   }

   private void doConsumeAnimation(ItemStack stack, int useCount, float partialTicks) {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      float useAmount;
      float f1;
      float f2;
      float f3;
      if ((Boolean)anim.oldEating.getValue()) {
         useAmount = (float)useCount - partialTicks + 1.0F;
         f1 = 1.0F - useAmount / (float)stack.getMaxItemUseDuration();
         f2 = 1.0F - f1;
         f2 = f2 * f2 * f2;
         f2 = f2 * f2 * f2;
         f2 = f2 * f2 * f2;
         f3 = 1.0F - f2;
         GlStateManager.translate(0.0F, MathHelper.abs(MathHelper.cos(useAmount / 4.0F * 3.1415927F) * 0.1F) * (float)((double)f1 > 0.2 ? 1 : 0), 0.0F);
         GlStateManager.translate(f3 * 0.6F, -f3 * 0.5F, 0.0F);
         GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
      } else {
         useAmount = (float)useCount - partialTicks + 1.0F;
         f1 = useAmount / (float)stack.getMaxItemUseDuration();
         f2 = MathHelper.abs(MathHelper.cos(useAmount / 4.0F * 3.1415927F) * 0.1F);
         if (f1 >= 0.8F) {
            f2 = 0.0F;
         }

         GlStateManager.translate(0.0F, f2, 0.0F);
         f3 = 1.0F - (float)Math.pow((double)f1, 27.0);
         GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
         GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
      }

   }

   private void doSwingTranslation(float swingProgress) {
      float swingProgress2 = MathHelper.sin(swingProgress * 3.1415927F);
      float swingProgress3 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
      GlStateManager.translate(-swingProgress3 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F * 2.0F) * 0.2F, -swingProgress2 * 0.2F);
   }

   private void doEquipAndSwingTransform(float equipProgress, float swingProgress) {
      GlStateManager.translate(0.56F, -0.52F - (1.0F - equipProgress) * 0.6F, -0.72F);
      GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      float swingProgress2 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
      float swingProgress3 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
      GlStateManager.rotate(-swingProgress2 * 20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-swingProgress3 * 20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(-swingProgress3 * 80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
   }

   private void doSwordBlockAnimation() {
      GlStateManager.translate(-0.5F, 0.2F, 0.0F);
      GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
   }

   private void doBowAnimation(ItemStack stack, int useCount, float partialTicks) {
      GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-0.9F, 0.2F, 0.0F);
      float totalPullback = (float)stack.getMaxItemUseDuration() - ((float)useCount - partialTicks + 1.0F);
      float pullbackNorm = totalPullback / 20.0F;
      pullbackNorm = (pullbackNorm * pullbackNorm + pullbackNorm * 2.0F) / 3.0F;
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      if (pullbackNorm > 1.0F) {
         pullbackNorm = 1.0F;
      }

      if (pullbackNorm > 0.1F) {
         GlStateManager.translate(0.0F, MathHelper.sin((totalPullback - 0.1F) * 1.3F) * 0.01F * (pullbackNorm - 0.1F), 0.0F);
      }

      GlStateManager.translate(0.0F, 0.0F, pullbackNorm * 0.1F);
      if ((Boolean)anim.oldBow.getValue()) {
         GlStateManager.rotate(-335.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, 0.5F, 0.0F);
      }

      float zScale = 1.0F + pullbackNorm * 0.2F;
      GlStateManager.scale(1.0F, 1.0F, zScale);
      if ((Boolean)anim.oldBow.getValue()) {
         GlStateManager.translate(0.0F, -0.5F, 0.0F);
         GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
      }

   }

   public Vec3 getOffset() {
      double fov = (double)Minecraft.getMinecraft().gameSettings.fovSetting;
      double decimalFov = fov / 110.0;
      return new Vec3(-decimalFov + decimalFov / 2.5 - decimalFov / 8.0 + 0.16, 0.0, 0.4);
   }
}
