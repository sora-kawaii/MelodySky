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

    public FoamFixReplaceClassSimpleName(String ... stringArray) {
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
        public void visitMethodInsn(int n, String string, String string2, String string3, boolean bl) {
            if (n == 182 && "getSimpleName".equals(string2) && "java/lang/Class".equals(string)) {
                System.out.println("Replaced INVOKEVIRTUAL getSimpleName");
                super.visitMethodInsn(n, string, "getName", string3, bl);
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
            if (FoamFixReplaceClassSimpleName.this.methods.contains(string)) {
                return new FFMethodVisitor(this.api, this.cv.visitMethod(n, string, string2, string3, stringArray));
            }
            return this.cv.visitMethod(n, string, string2, string3, stringArray);
        }
    }
}

