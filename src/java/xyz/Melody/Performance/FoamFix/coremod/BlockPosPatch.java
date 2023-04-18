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

    public static byte[] patchVec3i(byte[] byArray) {
        ClassReader classReader = new ClassReader(byArray);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        for (FieldNode fieldNode : classNode.fields) {
            if (!"I".equals(fieldNode.desc) || fieldNode.access != 18) continue;
            fieldNode.access = 4;
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static byte[] patchOtherClass(byte[] byArray, boolean bl) {
        ClassReader classReader = new ClassReader(byArray);
        BlockPosClassVisitor blockPosClassVisitor = new BlockPosClassVisitor(327680, null, bl);
        classReader.accept(blockPosClassVisitor, 6);
        if (blockPosClassVisitor.hasChanged) {
            ClassWriter classWriter = new ClassWriter(0);
            blockPosClassVisitor.setCV(classWriter);
            classReader.accept(blockPosClassVisitor, 0);
            return classWriter.toByteArray();
        }
        return byArray;
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

        public BlockPosMethodVisitor(int n, BlockPosClassVisitor blockPosClassVisitor, MethodVisitor methodVisitor) {
            super(n, methodVisitor);
            this.classVisitor = blockPosClassVisitor;
        }

        @Override
        public void visitFieldInsn(int n, String string, String string2, String string3) {
            if (mutableOwners.contains(string)) {
                String string4 = (String)mutableFieldSwaps.get(string2);
                if (string4 != null) {
                    if (this.mv != null) {
                        this.mv.visitFieldInsn(n, "net/minecraft/util/math/Vec3i", string4, string3);
                    }
                    this.classVisitor.hasChanged = true;
                } else if (this.mv != null) {
                    this.mv.visitFieldInsn(n, string, string2, string3);
                }
            } else if (this.mv != null) {
                this.mv.visitFieldInsn(n, string, string2, string3);
            }
        }
    }

    private static class BlockPosClassVisitor
    extends ClassVisitor {
        private final boolean isMutable;
        private boolean hasChanged = false;

        public BlockPosClassVisitor(int n, ClassVisitor classVisitor, boolean bl) {
            super(n, classVisitor);
            this.isMutable = bl;
        }

        public void setCV(ClassVisitor classVisitor) {
            this.cv = classVisitor;
        }

        @Override
        public void visit(int n, int n2, String string, String string2, String string3, String[] stringArray) {
            if (mutableOwners.contains(string3)) {
                mutableOwners.add(string);
            }
            if (this.cv != null) {
                this.cv.visit(n, n2, string, string2, string3, stringArray);
            }
        }

        @Override
        public FieldVisitor visitField(int n, String string, String string2, String string3, Object object) {
            if (this.cv == null) {
                return null;
            }
            if (!this.isMutable || !mutableFieldSwaps.containsKey(string)) {
                return this.cv.visitField(n, string, string2, string3, object);
            }
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
            if (!this.isMutable || !mutableDeletedMethods.contains(string)) {
                return new BlockPosMethodVisitor(this.api, this, this.cv != null ? this.cv.visitMethod(n, string, string2, string3, stringArray) : null);
            }
            return null;
        }
    }
}

