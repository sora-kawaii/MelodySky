/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;

public final class RayMarchUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isFacingBlock(BlockPos block, float range) {
        float stepSize = 0.15f;
        if (RayMarchUtils.mc.thePlayer != null && RayMarchUtils.mc.theWorld != null) {
            Vector3f position = new Vector3f((float)RayMarchUtils.mc.thePlayer.posX, (float)RayMarchUtils.mc.thePlayer.posY + RayMarchUtils.mc.thePlayer.getEyeHeight(), (float)RayMarchUtils.mc.thePlayer.posZ);
            Vec3 look = RayMarchUtils.mc.thePlayer.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                BlockPos blockAtPos = new BlockPos(position.x, position.y, position.z);
                if (blockAtPos.equals(block)) {
                    return true;
                }
                position.translate(step.x, step.y, step.z);
                ++i;
            }
        }
        return false;
    }

    public static <T extends Entity> List<T> getFacedEntityOfType(Class<T> _class, float range) {
        float stepSize = 0.5f;
        if (RayMarchUtils.mc.thePlayer != null && RayMarchUtils.mc.theWorld != null) {
            Vector3f position = new Vector3f((float)RayMarchUtils.mc.thePlayer.posX, (float)RayMarchUtils.mc.thePlayer.posY + RayMarchUtils.mc.thePlayer.getEyeHeight(), (float)RayMarchUtils.mc.thePlayer.posZ);
            Vec3 look = RayMarchUtils.mc.thePlayer.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                List<T> entities = RayMarchUtils.mc.theWorld.getEntitiesWithinAABB(_class, new AxisAlignedBB((double)position.x - 0.5, (double)position.y - 0.5, (double)position.z - 0.5, (double)position.x + 0.5, (double)position.y + 0.5, (double)position.z + 0.5));
                if (!entities.isEmpty()) {
                    return entities;
                }
                position.translate(step.x, step.y, step.z);
                ++i;
            }
        }
        return null;
    }
}

