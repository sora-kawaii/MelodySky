/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public final class FoamFixConstructorReplacer {
    public final String from;
    public final String to;
    public final Set<String> methods;

    public FoamFixConstructorReplacer(String from, String to, String ... methods) {
        this.from = from.replace('.', '/');
        this.to = to.replace('.', '/');
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
        public void visitTypeInsn(int opcode, String type) {
            if (opcode == 187 && FoamFixConstructorReplacer.this.from.equals(type)) {
                System.out.println("Replaced NEW for " + FoamFixConstructorReplacer.this.from + " to " + FoamFixConstructorReplacer.this.to);
                super.visitTypeInsn(opcode, FoamFixConstructorReplacer.this.to);
            } else {
                super.visitTypeInsn(opcode, type);
            }
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (opcode == 183 && "<init>".equals(name) && FoamFixConstructorReplacer.this.from.equals(owner)) {
                System.out.println("Replaced INVOKESPECIAL for " + FoamFixConstructorReplacer.this.from + " to " + FoamFixConstructorReplacer.this.to);
                super.visitMethodInsn(opcode, FoamFixConstructorReplacer.this.to, name, desc, itf);
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
            if (FoamFixConstructorReplacer.this.methods.contains(name)) {
                return new FFMethodVisitor(this.api, this.cv.visitMethod(access, name, desc, signature, exceptions));
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

