package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class RenewCHPass extends Module {
   private Option shab = new Option("Compatible Mode", false);

   public RenewCHPass() {
      super("AutoRenewCHPass", new String[]{"rchp"}, ModuleType.QOL);
      this.addValues(new Value[]{this.shab});
      this.setColor((new Color(158, 205, 125)).getRGB());
      this.setModInfo("Auto Renew Crystal Hollows Pass.");
   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onChat(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (message.equals("Your pass to the Crystal Hollows will expire in 1 minute")) {
         this.mc.thePlayer.sendChatMessage("/purchasecrystallhollowspass");
      }

      if ((Boolean)this.shab.getValue()) {
         if (message.contains("Your pass to the Crystal Hollows will expire in 1 minute")) {
            this.mc.thePlayer.sendChatMessage("/purchasecrystallhollowspass");
         }

         if (message.contains("remaining on your pass.")) {
            this.mc.thePlayer.sendChatMessage("/purchasecrystallhollowspass");
         }
      }

   }
}
