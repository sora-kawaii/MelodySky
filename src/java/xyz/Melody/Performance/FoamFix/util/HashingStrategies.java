/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.util;

import com.google.common.collect.ImmutableSet;
import gnu.trove.strategy.HashingStrategy;
import gnu.trove.strategy.IdentityHashingStrategy;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import org.lwjgl.util.vector.Vector3f;
import xyz.Melody.Performance.FoamFix.util.I;

public final class HashingStrategies {
    public static final HashingStrategy<byte[]> BYTE_ARRAY = new ByteArray(null);
    public static final HashingStrategy<float[]> FLOAT_ARRAY = new FloatArray(null);
    public static final HashingStrategy<float[][]> FLOAT_ARRAY_ARRAY = new FloatArrayArray(null);
    public static final HashingStrategy<int[]> INT_ARRAY = new IntArray(null);
    public static final HashingStrategy GENERIC = new ObjectStrategy(null);
    public static final HashingStrategy IDENTITY = new IdentityHashingStrategy();
    public static final HashingStrategy<ItemCameraTransforms> ITEM_CAMERA_TRANSFORMS = new ItemCameraTransformsStrategy(null);
    public static final HashingStrategy<ItemTransformVec3f> ITEM_TRANSFORM_VEC3F = new ItemTransformVecStrategy(null);

    private static final class FloatArrayArray
    implements HashingStrategy<float[][]> {
        private FloatArrayArray() {
        }

        @Override
        public int computeHashCode(float[][] fArray) {
            int n = 1;
            for (float[] fArray2 : fArray) {
                n = n * 31 + Arrays.hashCode(fArray2);
            }
            return n;
        }

        @Override
        public boolean equals(float[][] fArray, float[][] fArray2) {
            if (fArray == null) {
                return fArray2 == null;
            }
            if (fArray.length != fArray2.length) {
                return false;
            }
            for (int i = 0; i < fArray.length; ++i) {
                if (Arrays.equals(fArray[i], fArray2[i])) continue;
                return false;
            }
            return true;
        }

        FloatArrayArray(I i) {
            this();
        }
    }

    private static final class FloatArray
    implements HashingStrategy<float[]> {
        private FloatArray() {
        }

        @Override
        public int computeHashCode(float[] fArray) {
            return Arrays.hashCode(fArray);
        }

        @Override
        public boolean equals(float[] fArray, float[] fArray2) {
            return Arrays.equals(fArray, fArray2);
        }

        FloatArray(I i) {
            this();
        }
    }

    private static final class IntArray
    implements HashingStrategy<int[]> {
        private IntArray() {
        }

        @Override
        public int computeHashCode(int[] nArray) {
            return Arrays.hashCode(nArray);
        }

        @Override
        public boolean equals(int[] nArray, int[] nArray2) {
            return Arrays.equals(nArray, nArray2);
        }

        IntArray(I i) {
            this();
        }
    }

    private static final class ByteArray
    implements HashingStrategy<byte[]> {
        private ByteArray() {
        }

        @Override
        public int computeHashCode(byte[] byArray) {
            return Arrays.hashCode(byArray);
        }

        @Override
        public boolean equals(byte[] byArray, byte[] byArray2) {
            return Arrays.equals(byArray, byArray2);
        }

        ByteArray(I i) {
            this();
        }
    }

    private static final class ObjectStrategy
    implements HashingStrategy {
        private ObjectStrategy() {
        }

        public int computeHashCode(Object object) {
            return Objects.hashCode(object);
        }

        public boolean equals(Object object, Object object2) {
            return Objects.equals(object, object2);
        }

        ObjectStrategy(I i) {
            this();
        }
    }

    private static final class ItemTransformVecStrategy
    implements HashingStrategy<ItemTransformVec3f> {
        private ItemTransformVecStrategy() {
        }

        @Override
        public int computeHashCode(ItemTransformVec3f itemTransformVec3f) {
            int n = 1;
            for (Vector3f vector3f : ImmutableSet.of(itemTransformVec3f.field_178364_b, itemTransformVec3f.field_178363_d, itemTransformVec3f.field_178365_c)) {
                n = ((n * 31 + Float.floatToIntBits(vector3f.getX())) * 31 + Float.floatToIntBits(vector3f.getY())) * 31 + Float.floatToIntBits(vector3f.getZ());
            }
            return n;
        }

        @Override
        public boolean equals(ItemTransformVec3f itemTransformVec3f, ItemTransformVec3f itemTransformVec3f2) {
            return Objects.equals(itemTransformVec3f, itemTransformVec3f2);
        }

        ItemTransformVecStrategy(I i) {
            this();
        }
    }

    private static final class ItemCameraTransformsStrategy
    implements HashingStrategy<ItemCameraTransforms> {
        private ItemCameraTransformsStrategy() {
        }

        @Override
        public int computeHashCode(ItemCameraTransforms itemCameraTransforms) {
            int n = 1;
            for (ItemTransformVec3f itemTransformVec3f : ImmutableSet.of(itemCameraTransforms.field_178356_c, itemCameraTransforms.field_181700_p, itemCameraTransforms.field_181699_o, itemCameraTransforms.field_178354_e, itemCameraTransforms.field_178353_d, itemCameraTransforms.field_178355_b, new ItemTransformVec3f[0])) {
                for (Vector3f vector3f : ImmutableSet.of(itemTransformVec3f.field_178364_b, itemTransformVec3f.field_178363_d, itemTransformVec3f.field_178365_c)) {
                    n = ((n * 31 + Float.floatToIntBits(vector3f.getX())) * 31 + Float.floatToIntBits(vector3f.getY())) * 31 + Float.floatToIntBits(vector3f.getZ());
                }
            }
            return n;
        }

        @Override
        public boolean equals(ItemCameraTransforms itemCameraTransforms, ItemCameraTransforms itemCameraTransforms2) {
            if (itemCameraTransforms == null) {
                return itemCameraTransforms2 == null;
            }
            return Objects.equals(itemCameraTransforms.field_178356_c, itemCameraTransforms2.field_178356_c) && Objects.equals(itemCameraTransforms.field_181700_p, itemCameraTransforms2.field_181700_p) && Objects.equals(itemCameraTransforms.field_181699_o, itemCameraTransforms2.field_181699_o) && Objects.equals(itemCameraTransforms.field_178354_e, itemCameraTransforms2.field_178354_e) && Objects.equals(itemCameraTransforms.field_178353_d, itemCameraTransforms2.field_178353_d) && Objects.equals(itemCameraTransforms.field_178355_b, itemCameraTransforms2.field_178355_b);
        }

        ItemCameraTransformsStrategy(I i) {
            this();
        }
    }
}

