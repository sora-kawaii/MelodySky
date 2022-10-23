package xyz.Melody.module.modules.macros;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class ActionMacro extends Module {
   private BlockPos blockPos;
   private BlockPos lastBlockPos = null;
   private float currentDamage = 0.0F;
   private Option forward = new Option("W", false);
   private Option backward = new Option("S", false);
   private Option left = new Option("A", false);
   private Option right = new Option("D", false);
   private Option space = new Option("Jump", false);
   private Option lmb = new Option("Lmb", false);

   public ActionMacro() {
      super("ActionMacro", new String[]{""}, ModuleType.Macros);
      this.addValues(new Value[]{this.forward, this.backward, this.left, this.right, this.space, this.lmb});
      this.setModInfo("Press Keyboard or Mouse Buttons.");
   }

   @EventHandler
   private void idk(EventPreUpdate event) {
      if ((Boolean)this.forward.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindForward.getKeyCode(), true);
      }

      if ((Boolean)this.backward.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindBack.getKeyCode(), true);
      }

      if ((Boolean)this.left.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), true);
      }

      if ((Boolean)this.right.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), true);
      }

      if ((Boolean)this.space.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindJump.getKeyCode(), true);
      }

      if ((Boolean)this.lmb.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindAttack.getKeyCode(), true);
      }

   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      KeyBinding.unPressAllKeys();
      super.onDisable();
   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      Helper.sendMessage("[MacroProtection] Auto Disabled " + EnumChatFormatting.GREEN + this.getName() + EnumChatFormatting.GRAY + " due to World Change.");
      KeyBinding.unPressAllKeys();
      this.setEnabled(false);
   }
}
