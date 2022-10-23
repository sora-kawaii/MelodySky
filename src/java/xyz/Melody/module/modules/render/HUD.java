package xyz.Melody.module.modules.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class HUD extends Module {
   private Option coords = new Option("Coords", true);
   private Option pots = new Option("Effects", true);

   public HUD() {
      super("HUD", new String[]{"gui"}, ModuleType.Others);
      this.addValues(new Value[]{this.coords, this.pots});
      this.setEnabled(true);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventHandler
   public void Render2d(EventRender2D e) {
      NotificationPublisher.publish(new ScaledResolution(this.mc));
   }

   @EventHandler
   private void onRenderInfo(EventRender2D event) {
      ScaledResolution mainWindow = new ScaledResolution(this.mc);
      float infoY = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0F : -2.0F;
      if (!this.mc.gameSettings.showDebugInfo) {
         if ((Boolean)this.coords.getValue()) {
            this.mc.fontRendererObj.drawString("X: " + (int)this.mc.thePlayer.posX + "  Y: " + (int)this.mc.thePlayer.posY + "  Z: " + (int)this.mc.thePlayer.posZ, 3, (int)((float)(mainWindow.getScaledHeight() - 10) + infoY), -1);
         }

         if ((Boolean)this.pots.getValue()) {
            this.drawPotionStatus(mainWindow);
         }

      }
   }

   private void drawPotionStatus(ScaledResolution sr) {
      List potions = new ArrayList();
      Iterator var3 = this.mc.thePlayer.getActivePotionEffects().iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         potions.add((PotionEffect)o);
      }

      potions.sort(Comparator.comparingDouble((effectx) -> {
         return (double)(-this.mc.fontRendererObj.getStringWidth(I18n.format(Potion.potionTypes[effectx.getPotionID()].getName(), new Object[0])));
      }));
      float pY = this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChat ? -15.0F : -2.0F;

      for(Iterator var10 = potions.iterator(); var10.hasNext(); pY -= 9.0F) {
         PotionEffect effect = (PotionEffect)var10.next();
         Potion potion = Potion.potionTypes[effect.getPotionID()];
         String name = I18n.format(potion.getName(), new Object[0]);
         String PType = "";
         if (effect.getAmplifier() == 1) {
            name = name + " II";
         } else if (effect.getAmplifier() == 2) {
            name = name + " III";
         } else if (effect.getAmplifier() == 3) {
            name = name + " IV";
         }

         if (effect.getDuration() < 600 && effect.getDuration() > 300) {
            PType = PType + "§6 " + Potion.getDurationString(effect);
         } else if (effect.getDuration() < 300) {
            PType = PType + "§c " + Potion.getDurationString(effect);
         } else if (effect.getDuration() > 600) {
            PType = PType + "§7 " + Potion.getDurationString(effect);
         }

         this.mc.fontRendererObj.drawStringWithShadow(name, (float)(sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(name + PType)), (float)(sr.getScaledHeight() - 9) + pY, potion.getLiquidColor());
         this.mc.fontRendererObj.drawStringWithShadow(PType, (float)(sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(PType)), (float)(sr.getScaledHeight() - 9) + pY, -1);
      }

   }
}
