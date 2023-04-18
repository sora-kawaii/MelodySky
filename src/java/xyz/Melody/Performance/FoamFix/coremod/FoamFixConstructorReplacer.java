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

    public FoamFixConstructorReplacer(String string, String string2, String ... stringArray) {
        this.from = string.replace('.', '/');
        this.to = string2.replace('.', '/');
        this.methods = ImmutableSet.copyOf(stringArray);
    }

    public ClassVisitor getClassVisitor(int n, ClassVisitor classVisitor) {
        return new FFClassVisitor(n, classVisitor);
    }

    private class FFMethodVisitor
    extends MethodVisitor {
        public FFMethodVisitor(int n, MethodVisitor methodVisitor) {
            super(n, methodVisitor);
        }

        @Override
        public void visitTypeInsn(int n, String string) {
            if (n == 187 && FoamFixConstructorReplacer.this.from.equals(string)) {
                System.out.println("Replaced NEW for " + FoamFixConstructorReplacer.this.from + " to " + FoamFixConstructorReplacer.this.to);
                super.visitTypeInsn(n, FoamFixConstructorReplacer.this.to);
            } else {
                super.visitTypeInsn(n, string);
            }
        }

        @Override
        public void visitMethodInsn(int n, String string, String string2, String string3, boolean bl) {
            if (n == 183 && "<init>".equals(string2) && FoamFixConstructorReplacer.this.from.equals(string)) {
                System.out.println("Replaced INVOKESPECIAL for " + FoamFixConstructorReplacer.this.from + " to " + FoamFixConstructorReplacer.this.to);
                super.visitMethodInsn(n, FoamFixConstructorReplacer.this.to, string2, string3, bl);
            } else {
                super.visitMethodInsn(n, string, string2, string3, bl);
            }
        }
    }

    private class FFClassVisitor
    extends ClassVisitor {
        public FFClassVisitor(int n, ClassVisitor classVisitor) {
            super(n, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
            if (FoamFixConstructorReplacer.this.methods.contains(string)) {
                return new FFMethodVisitor(this.api, this.cv.visitMethod(n, string, string2, string3, stringArray));
            }
            return this.cv.visitMethod(n, string, string2, string3, stringArray);
        }
    }
}

