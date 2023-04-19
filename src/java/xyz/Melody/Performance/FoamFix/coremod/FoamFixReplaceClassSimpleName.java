/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public final class FoamFixReplaceClassSimpleName {
    public final Set<String> methods;

    public FoamFixReplaceClassSimpleName(String ... methods) {
        this.methods = ImmutableSet.copyOf(methods);
    }

    public ClassVisitor getClassVisitor(int api, ClassVisitor next) {
        return new FFClassVisitor(api, next);
    }

    private class FFMethodVisitor
    extends MethodVisitor {
        public FFMethodVisitor(int api, MethodVisitor next) {
            super(api, next);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (opcode == 182 && "getSimpleName".equals(name) && "java/lang/Class".equals(owner)) {
                System.out.println("Replaced INVOKEVIRTUAL getSimpleName");
                super.visitMethodInsn(opcode, owner, "getName", desc, itf);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        }
    }

    private class FFClassVisitor
    extends ClassVisitor {
        public FFClassVisitor(int api, ClassVisitor next) {
            super(api, next);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (FoamFixReplaceClassSimpleName.this.methods.contains(name)) {
                return new FFMethodVisitor(this.api, this.cv.visitMethod(access, name, desc, signature, exceptions));
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

