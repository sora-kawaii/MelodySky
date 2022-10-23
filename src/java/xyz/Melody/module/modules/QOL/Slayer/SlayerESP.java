package xyz.Melody.module.modules.QOL.Slayer;

import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class SlayerESP extends Module {
   public SlayerESP() {
      super("MiniBossESP", new String[]{"mbe"}, ModuleType.Slayer);
      this.setColor((new Color(158, 205, 125)).getRGB());
      this.setModInfo("Show Slayer Miniboss ESP.");
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST,
      receiveCanceled = true
   )
   public void onRenderEntity(RenderLivingEvent.Pre event) {
      Entity entity = event.entity;
      if (entity.hasCustomName()) {
         String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
         double x = event.entity.posX;
         double y = event.entity.posY;
         double z = event.entity.posZ;
         if (entityName.startsWith("Voidcrazed Maniac")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.MAGENTA.c), 190.0F);
         } else if (entityName.startsWith("Voidling Radical")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Voidling Devotee")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Revenant ")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 2.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Atoned Champion")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 2.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Atoned Revenant")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 2.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Deformed Revenant")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 2.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Tarantula ")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.66, y - 1.0, z - 0.66, x + 0.66, y - 0.25, z + 0.66), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Mutant Tarantula")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.66, y - 1.0, z - 0.66, x + 0.66, y - 0.25, z + 0.66), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Pack Enforcer")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 1.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Sven Follower")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 1.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         } else if (entityName.startsWith("Sven Alpha")) {
            RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(x - 0.5, y - 1.0, z - 0.5, x + 0.5, y, z + 0.5), new Color(Colors.AQUA.c), 190.0F);
         }
      }
   }
}
