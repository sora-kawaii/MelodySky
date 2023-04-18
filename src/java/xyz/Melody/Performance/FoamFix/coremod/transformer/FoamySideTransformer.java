/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class FoamySideTransformer
implements IClassTransformer {
    public static final String SIDEONLY_DESCRIPTOR = Type.getDescriptor(SideOnly.class);
    private static String SIDE = FMLLaunchHandler.side().name();
    private static final boolean DEBUG = false;

    @Override
    public byte[] transform(String string, String string2, byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        ClassReader classReader = new ClassReader(byArray);
        SideCapturingClassVisitor sideCapturingClassVisitor = new SideCapturingClassVisitor(327680);
        classReader.accept(sideCapturingClassVisitor, 7);
        if (sideCapturingClassVisitor.removableClasses.size() > 0) {
            throw new RuntimeException(String.format("Attempted to load class %s for invalid side %s", sideCapturingClassVisitor.className, SIDE));
        }
        if (sideCapturingClassVisitor.removableFields.size() > 0 || sideCapturingClassVisitor.removableMethods.size() > 0) {
            ClassWriter classWriter = new ClassWriter(0);
            classReader.accept(new SideRemovingClassVisitor(327680, classWriter, sideCapturingClassVisitor), 0);
            return classWriter.toByteArray();
        }
        return byArray;
    }

    public static class SideRemovingClassVisitor
    extends ClassVisitor {
        private final SideCapturingClassVisitor capturer;

        public SideRemovingClassVisitor(int n, ClassVisitor classVisitor, SideCapturingClassVisitor sideCapturingClassVisitor) {
            super(n, classVisitor);
            this.capturer = sideCapturingClassVisitor;
        }

        @Override
        public FieldVisitor visitField(int n, String string, String string2, String string3, Object object) {
            String string4 = string + string2;
            if (this.capturer.removableFields.contains(string4)) {
                return null;
            }
            return super.visitField(n, string, string2, string3, object);
        }

        @Override
        public MethodVisitor visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
            String string4 = string + string2;
            if (this.capturer.removableMethods.contains(string4)) {
                return null;
            }
            return super.visitMethod(n, string, string2, string3, stringArray);
        }
    }

    public static class SideCapturingClassVisitor
    extends ClassVisitor {
        public List<String> removableClasses = new ArrayList<String>(1);
        public Set<String> removableFields = new HashSet<String>();
        public Set<String> removableMethods = new HashSet<String>();
        public String className;

        public SideCapturingClassVisitor(int n) {
            super(n);
        }

        @Override
        public void visit(int n, int n2, String string, String string2, String string3, String[] stringArray) {
            this.className = string;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String string, boolean bl) {
            if (string.equals(SIDEONLY_DESCRIPTOR)) {
                return new SideCapturingAnnotationVisitor(this.api, this.removableClasses, this.className);
            }
            return null;
        }

        @Override
        public FieldVisitor visitField(int n, String string, String string2, String string3, Object object) {
            final String string4 = string + string2;
            return new FieldVisitor(this.api, super.visitField(n, string, string2, string3, object)){

                @Override
                public AnnotationVisitor visitAnnotation(String string, boolean bl) {
                    if (string.equals(SIDEONLY_DESCRIPTOR)) {
                        return new SideCapturingAnnotationVisitor(this.api, removableFields, string4);
                    }
                    return null;
                }
            };
        }

        @Override
        public MethodVisitor visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
            final String string4 = string + string2;
            return new MethodVisitor(this.api, super.visitMethod(n, string, string2, string3, stringArray)){

                @Override
                public AnnotationVisitor visitAnnotation(String string, boolean bl) {
                    if (string.equals(SIDEONLY_DESCRIPTOR)) {
                        return new SideCapturingAnnotationVisitor(this.api, removableMethods, string4);
                    }
                    return null;
                }
            };
        }
    }

    public static class SideCapturingAnnotationVisitor
    extends AnnotationVisitor {
        public final Collection<String> targetSet;
        public final String targetName;

        public SideCapturingAnnotationVisitor(int n, Collection<String> collection, String string) {
            super(n);
            this.targetSet = collection;
            this.targetName = string;
        }

        @Override
        public void visitEnum(String string, String string2, String string3) {
            if (string.equals("value") && !string3.equals(SIDE)) {
                this.targetSet.add(this.targetName);
            }
        }
    }
}

