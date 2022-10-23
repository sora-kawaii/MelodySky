package xyz.Melody.injection.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.misc.EventClick;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.injection.mixins.entity.PlayerControllerAccessor;
import xyz.Melody.module.balance.NoHitDelay;
import xyz.Melody.module.modules.others.OldAnimations;

@SideOnly(Side.CLIENT)
@Mixin({Minecraft.class})
public class MixinMinecraft {
   @Shadow
   public GuiScreen currentScreen;
   @Shadow
   private static final Logger logger = LogManager.getLogger();
   @Shadow
   private int leftClickCounter;
   @Shadow
   public MovingObjectPosition objectMouseOver;
   @Shadow
   public PlayerControllerMP playerController;
   @Shadow
   private int rightClickDelayTimer;
   @Shadow
   public EntityRenderer entityRenderer;
   private long lastFrame = this.getTime();

   public long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   @Inject(
      method = "clickMouse",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onLeftClick(CallbackInfo ci) {
      EventClick ce = new EventClick();
      EventClick.LeftClick left = (EventClick.LeftClick)EventBus.getInstance().call(ce.new LeftClick());
      EventBus.getInstance().call(ce);
      if (left.isCancelled()) {
         ci.cancel();
      }

      if (Client.instance.getModuleManager().getModuleByClass(NoHitDelay.class).isEnabled()) {
         this.leftClickCounter = 0;
      }

   }

   @Inject(
      method = "rightClickMouse",
      at = {@At("HEAD")}
   )
   public void rightClickMouse(CallbackInfo ci) {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      if (anim.isEnabled() && (Boolean)anim.punching.getValue() && ((PlayerControllerAccessor)Minecraft.getMinecraft().playerController).isHittingBlock() && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && (Minecraft.getMinecraft().thePlayer.getHeldItem().getItemUseAction() != EnumAction.NONE || Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
         Minecraft.getMinecraft().playerController.resetBlockRemoving();
      }

   }

   @Inject(
      method = "rightClickMouse",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRightClick(CallbackInfo ci) {
      EventClick ce = new EventClick();
      EventClick.RightClick right = (EventClick.RightClick)EventBus.getInstance().call(ce.new RightClick());
      EventBus.getInstance().call(ce);
      if (right.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = "startGame",
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;",
   shift = Shift.AFTER
)}
   )
   private void startGame(CallbackInfo ci) {
      Client.instance.start();
   }

   @Inject(
      method = "runGameLoop",
      at = {@At("HEAD")}
   )
   private void runGameLoopDeltaTime(CallbackInfo ci) {
   }

   @Inject(
      method = "runTick",
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V",
   shift = Shift.AFTER
)}
   )
   private void onKey(CallbackInfo ci) {
      if (Keyboard.getEventKeyState() && this.currentScreen == null) {
         EventBus.getInstance().call(new EventKey(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
      }

   }

   @Inject(
      method = "shutdown",
      at = {@At("HEAD")}
   )
   private void onShutdown(CallbackInfo ci) {
      Client.instance.stop();
   }

   @Inject(
      method = "displayCrashReport",
      at = {@At("HEAD")}
   )
   public void displayCrashReport(CrashReport crashReportIn, CallbackInfo ci) {
      Client.instance.stop();
   }

   @Inject(
      method = "runTick",
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/Minecraft;joinPlayerCounter:I",
   shift = Shift.BEFORE
)}
   )
   private void onTick(CallbackInfo callbackInfo) {
      EventTick e = new EventTick();
      EventBus.getInstance().call(e);
   }
}
