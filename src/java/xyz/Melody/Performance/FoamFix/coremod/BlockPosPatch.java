/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import java.util.HashMap;
import java.util.HashSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class BlockPosPatch {
    private static final HashMap<String, String> mutableFieldSwaps = new HashMap();
    private static final HashSet<String> mutableDeletedMethods = new HashSet();
    private static final HashSet<String> mutableOwners = new HashSet();

    public static byte[] patchVec3i(byte[] data) {
        ClassReader reader = new ClassReader(data);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        for (FieldNode fn : node.fields) {
            if (!"I".equals(fn.desc) || fn.access != 18) continue;
            fn.access = 4;
        }
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] patchOtherClass(byte[] data, boolean isMutable) {
        ClassReader reader = new ClassReader(data);
        BlockPosClassVisitor cv = new BlockPosClassVisitor(327680, null, isMutable);
        reader.accept(cv, 6);
        if (cv.hasChanged) {
            ClassWriter writer = new ClassWriter(0);
            cv.setCV(writer);
            reader.accept(cv, 0);
            return writer.toByteArray();
        }
        return data;
    }

    static {
        mutableFieldSwaps.put("x", "x");
        mutableFieldSwaps.put("y", "y");
        mutableFieldSwaps.put("z", "z");
        mutableFieldSwaps.put("field_177997_b", "field_177962_a");
        mutableFieldSwaps.put("field_177998_c", "field_177960_b");
        mutableFieldSwaps.put("field_177996_d", "field_177961_c");
        mutableDeletedMethods.add("getX");
        mutableDeletedMethods.add("getY");
        mutableDeletedMethods.add("getZ");
        mutableDeletedMethods.add("func_177958_n");
        mutableDeletedMethods.add("func_177956_o");
        mutableDeletedMethods.add("func_177952_p");
        mutableOwners.add("net/minecraft/util/math/BlockPos$MutableBlockPos");
    }

    private static class BlockPosMethodVisitor
    extends MethodVisitor {
        private final BlockPosClassVisitor classVisitor;

        public BlockPosMethodVisitor(int api, BlockPosClassVisitor cv, MethodVisitor mv) {
            super(api, mv);
            this.classVisitor = cv;
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (mutableOwners.contains(owner)) {
                String dst = (String)mutableFieldSwaps.get(name);
                if (dst != null) {
                    if (this.mv != null) {
                        this.mv.visitFieldInsn(opcode, "net/minecraft/util/math/Vec3i", dst, desc);
                    }
                    this.classVisitor.hasChanged = true;
                } else if (this.mv != null) {
                    this.mv.visitFieldInsn(opcode, owner, name, desc);
                }
            } else if (this.mv != null) {
                this.mv.visitFieldInsn(opcode, owner, name, desc);
            }
        }
    }

    private static class BlockPosClassVisitor
    extends ClassVisitor {
        private final boolean isMutable;
        private boolean hasChanged = false;

        public BlockPosClassVisitor(int api, ClassVisitor next, boolean isMutable) {
            super(api, next);
            this.isMutable = isMutable;
        }

        public void setCV(ClassVisitor visitor) {
            this.cv = visitor;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            if (mutableOwners.contains(superName)) {
                mutableOwners.add(name);
            }
            if (this.cv != null) {
                this.cv.visit(version, access, name, signature, superName, interfaces);
            }
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if (this.cv == null) {
                return null;
            }
            if (!this.isMutable || !mutableFieldSwaps.containsKey(name)) {
                return this.cv.visitField(access, name, desc, signature, value);
            }
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (!this.isMutable || !mutableDeletedMethods.contains(name)) {
                return new BlockPosMethodVisitor(this.api, this, this.cv != null ? this.cv.visitMethod(access, name, desc, signature, exceptions) : null);
            }
            return null;
        }
    }
}

