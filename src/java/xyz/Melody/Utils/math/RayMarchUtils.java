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
    private static Minecraft mc = Minecraft.func_71410_x();

    public static boolean isFacingBlock(BlockPos blockPos, float f) {
        float f2 = 0.15f;
        if (RayMarchUtils.mc.field_71439_g != null && RayMarchUtils.mc.field_71441_e != null) {
            Vector3f vector3f = new Vector3f((float)RayMarchUtils.mc.field_71439_g.field_70165_t, (float)RayMarchUtils.mc.field_71439_g.field_70163_u + RayMarchUtils.mc.field_71439_g.func_70047_e(), (float)RayMarchUtils.mc.field_71439_g.field_70161_v);
            Vec3 vec3 = RayMarchUtils.mc.field_71439_g.func_70676_i(0.0f);
            Vector3f vector3f2 = new Vector3f((float)vec3.field_72450_a, (float)vec3.field_72448_b, (float)vec3.field_72449_c);
            vector3f2.scale(f2 / vector3f2.length());
            int n = 0;
            while ((double)n < Math.floor(f / f2) - 2.0) {
                BlockPos blockPos2 = new BlockPos(vector3f.x, vector3f.y, vector3f.z);
                if (blockPos2.equals(blockPos)) {
                    return true;
                }
                vector3f.translate(vector3f2.x, vector3f2.y, vector3f2.z);
                ++n;
            }
        }
        return false;
    }

    public static <T extends Entity> List<T> getFacedEntityOfType(Class<T> clazz, float f) {
        float f2 = 0.5f;
        if (RayMarchUtils.mc.field_71439_g != null && RayMarchUtils.mc.field_71441_e != null) {
            Vector3f vector3f = new Vector3f((float)RayMarchUtils.mc.field_71439_g.field_70165_t, (float)RayMarchUtils.mc.field_71439_g.field_70163_u + RayMarchUtils.mc.field_71439_g.func_70047_e(), (float)RayMarchUtils.mc.field_71439_g.field_70161_v);
            Vec3 vec3 = RayMarchUtils.mc.field_71439_g.func_70676_i(0.0f);
            Vector3f vector3f2 = new Vector3f((float)vec3.field_72450_a, (float)vec3.field_72448_b, (float)vec3.field_72449_c);
            vector3f2.scale(f2 / vector3f2.length());
            int n = 0;
            while ((double)n < Math.floor(f / f2) - 2.0) {
                List list = RayMarchUtils.mc.field_71441_e.func_72872_a(clazz, new AxisAlignedBB((double)vector3f.x - 0.5, (double)vector3f.y - 0.5, (double)vector3f.z - 0.5, (double)vector3f.x + 0.5, (double)vector3f.y + 0.5, (double)vector3f.z + 0.5));
                if (!list.isEmpty()) {
                    return list;
                }
                vector3f.translate(vector3f2.x, vector3f2.y, vector3f2.z);
                ++n;
            }
        }
        return null;
    }
}

