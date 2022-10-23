package xyz.Melody.Utils.math;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;

public class RayMarchUtils {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static boolean isFacingBlock(BlockPos block, float range) {
      float stepSize = 0.15F;
      if (mc.thePlayer != null && mc.theWorld != null) {
         Vector3f position = new Vector3f((float)mc.thePlayer.posX, (float)mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), (float)mc.thePlayer.posZ);
         Vec3 look = mc.thePlayer.getLook(0.0F);
         Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
         step.scale(stepSize / step.length());

         for(int i = 0; (double)i < Math.floor((double)(range / stepSize)) - 2.0; ++i) {
            BlockPos blockAtPos = new BlockPos((double)position.x, (double)position.y, (double)position.z);
            if (blockAtPos.equals(block)) {
               return true;
            }

            position.translate(step.x, step.y, step.z);
         }
      }

      return false;
   }

   public static List getFacedEntityOfType(Class _class, float range) {
      float stepSize = 0.5F;
      if (mc.thePlayer != null && mc.theWorld != null) {
         Vector3f position = new Vector3f((float)mc.thePlayer.posX, (float)mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), (float)mc.thePlayer.posZ);
         Vec3 look = mc.thePlayer.getLook(0.0F);
         Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
         step.scale(stepSize / step.length());

         for(int i = 0; (double)i < Math.floor((double)(range / stepSize)) - 2.0; ++i) {
            List entities = mc.theWorld.getEntitiesWithinAABB(_class, new AxisAlignedBB((double)position.x - 0.5, (double)position.y - 0.5, (double)position.z - 0.5, (double)position.x + 0.5, (double)position.y + 0.5, (double)position.z + 0.5));
            if (!entities.isEmpty()) {
               return entities;
            }

            position.translate(step.x, step.y, step.z);
         }
      }

      return null;
   }
}
