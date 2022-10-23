package xyz.Melody.module.modules.others;

import net.minecraft.block.BlockAir;
import net.minecraft.potion.Potion;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class OldAnimations extends Module {
   public Option oldRod = new Option("Rod", true);
   public Option oldBow = new Option("Bow", true);
   public Option oldEating = new Option("Eating", true);
   public Option oldModel = new Option("Model", true);
   public Option punching = new Option("Punching", true);
   public Option oldBlockhitting = new Option("BlockHit", true);

   public OldAnimations() {
      super("OldAnimations", new String[]{"alc", "asc", "lobby"}, ModuleType.Others);
      this.addValues(new Value[]{this.oldBlockhitting, this.punching, this.oldModel, this.oldEating, this.oldBow, this.oldRod});
      this.setModInfo("1.7 Animations");
      this.setEnabled(true);
   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.getBlockPos() != null) {
            if ((this.mc.thePlayer.isBlocking() || this.mc.thePlayer.isUsingItem()) && !(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir)) {
               int idk = this.mc.thePlayer.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (this.mc.thePlayer.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
               if (this.mc.gameSettings.keyBindAttack.isKeyDown() && (!this.mc.thePlayer.isSwingInProgress || this.mc.thePlayer.swingProgressInt >= idk / 2 || this.mc.thePlayer.swingProgressInt < 0)) {
                  this.mc.thePlayer.swingProgressInt = -1;
                  this.mc.thePlayer.isSwingInProgress = true;
               }
            }

         }
      }
   }
}
