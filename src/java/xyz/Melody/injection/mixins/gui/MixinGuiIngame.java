package xyz.Melody.injection.mixins.gui;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRenderScoreboard;
import xyz.Melody.GUI.CustomUI.HUDManager;

@SideOnly(Side.CLIENT)
@Mixin({GuiIngame.class})
public abstract class MixinGuiIngame {
   @Shadow
   @Final
   protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
   @Shadow
   @Final
   protected Minecraft mc;
   @Shadow
   protected long healthUpdateCounter;
   @Shadow
   protected int updateCounter;
   @Shadow
   protected int playerHealth;
   @Shadow
   protected long lastSystemTime;
   @Shadow
   protected int lastPlayerHealth;
   @Shadow
   @Final
   protected Random rand;

   @Shadow
   protected abstract void renderHotbarItem(int var1, int var2, int var3, float var4, EntityPlayer var5);

   @Inject(
      method = "renderTooltip",
      at = {@At("RETURN")}
   )
   private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
      EventRender2D e = new EventRender2D(partialTicks);
      EventBus.getInstance().call(e);
   }

   @Inject(
      method = "renderScoreboard",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
      EventBus.getInstance().call(new EventRenderScoreboard(objective, scaledRes));
      if (HUDManager.getApiByName("ScoreBoard").isEnabled()) {
         ci.cancel();
      }

   }
}
