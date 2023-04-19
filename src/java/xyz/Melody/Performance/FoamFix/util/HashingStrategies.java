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
        public int computeHashCode(float[][] object) {
            int hash = 1;
            for (float[] anObject : object) {
                hash = hash * 31 + Arrays.hashCode(anObject);
            }
            return hash;
        }

        @Override
        public boolean equals(float[][] o1, float[][] o2) {
            if (o1 == null) {
                return o2 == null;
            }
            if (o1.length != o2.length) {
                return false;
            }
            for (int i = 0; i < o1.length; ++i) {
                if (Arrays.equals(o1[i], o2[i])) continue;
                return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((float[][])object, (float[][])object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((float[][])object);
        }

        FloatArrayArray(I x0) {
            this();
        }
    }

    private static final class FloatArray
    implements HashingStrategy<float[]> {
        private FloatArray() {
        }

        @Override
        public int computeHashCode(float[] object) {
            return Arrays.hashCode(object);
        }

        @Override
        public boolean equals(float[] o1, float[] o2) {
            return Arrays.equals(o1, o2);
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((float[])object, (float[])object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((float[])object);
        }

        FloatArray(I x0) {
            this();
        }
    }

    private static final class IntArray
    implements HashingStrategy<int[]> {
        private IntArray() {
        }

        @Override
        public int computeHashCode(int[] object) {
            return Arrays.hashCode(object);
        }

        @Override
        public boolean equals(int[] o1, int[] o2) {
            return Arrays.equals(o1, o2);
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((int[])object, (int[])object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((int[])object);
        }

        IntArray(I x0) {
            this();
        }
    }

    private static final class ByteArray
    implements HashingStrategy<byte[]> {
        private ByteArray() {
        }

        @Override
        public int computeHashCode(byte[] object) {
            return Arrays.hashCode(object);
        }

        @Override
        public boolean equals(byte[] o1, byte[] o2) {
            return Arrays.equals(o1, o2);
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((byte[])object, (byte[])object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((byte[])object);
        }

        ByteArray(I x0) {
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

        public boolean equals(Object o1, Object o2) {
            return Objects.equals(o1, o2);
        }

        ObjectStrategy(I x0) {
            this();
        }
    }

    private static final class ItemTransformVecStrategy
    implements HashingStrategy<ItemTransformVec3f> {
        private ItemTransformVecStrategy() {
        }

        @Override
        public int computeHashCode(ItemTransformVec3f transform) {
            int hash = 1;
            for (Vector3f vector : ImmutableSet.of(transform.rotation, transform.scale, transform.translation)) {
                hash = ((hash * 31 + Float.floatToIntBits(vector.getX())) * 31 + Float.floatToIntBits(vector.getY())) * 31 + Float.floatToIntBits(vector.getZ());
            }
            return hash;
        }

        @Override
        public boolean equals(ItemTransformVec3f o1, ItemTransformVec3f o2) {
            return Objects.equals(o1, o2);
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((ItemTransformVec3f)object, (ItemTransformVec3f)object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((ItemTransformVec3f)object);
        }

        ItemTransformVecStrategy(I x0) {
            this();
        }
    }

    private static final class ItemCameraTransformsStrategy
    implements HashingStrategy<ItemCameraTransforms> {
        private ItemCameraTransformsStrategy() {
        }

        @Override
        public int computeHashCode(ItemCameraTransforms object) {
            int hash = 1;
            for (ItemTransformVec3f transform : ImmutableSet.of(object.firstPerson, object.fixed, object.ground, object.gui, object.head, object.thirdPerson, new ItemTransformVec3f[0])) {
                for (Vector3f vector : ImmutableSet.of(transform.rotation, transform.scale, transform.translation)) {
                    hash = ((hash * 31 + Float.floatToIntBits(vector.getX())) * 31 + Float.floatToIntBits(vector.getY())) * 31 + Float.floatToIntBits(vector.getZ());
                }
            }
            return hash;
        }

        @Override
        public boolean equals(ItemCameraTransforms o1, ItemCameraTransforms o2) {
            if (o1 == null) {
                return o2 == null;
            }
            return Objects.equals(o1.firstPerson, o2.firstPerson) && Objects.equals(o1.fixed, o2.fixed) && Objects.equals(o1.ground, o2.ground) && Objects.equals(o1.gui, o2.gui) && Objects.equals(o1.head, o2.head) && Objects.equals(o1.thirdPerson, o2.thirdPerson);
        }

        @Override
        public boolean equals(Object object, Object object2) {
            return this.equals((ItemCameraTransforms)object, (ItemCameraTransforms)object2);
        }

        @Override
        public int computeHashCode(Object object) {
            return this.computeHashCode((ItemCameraTransforms)object);
        }

        ItemCameraTransformsStrategy(I x0) {
            this();
        }
    }
}

