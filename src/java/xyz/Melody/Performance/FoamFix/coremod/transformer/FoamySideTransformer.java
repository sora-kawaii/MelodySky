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
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ClassReader classReader = new ClassReader(bytes);
        SideCapturingClassVisitor sideCapturingClassVisitor = new SideCapturingClassVisitor(327680);
        classReader.accept(sideCapturingClassVisitor, 7);
        if (sideCapturingClassVisitor.removableClasses.size() > 0) {
            throw new RuntimeException(String.format("Attempted to load class %s for invalid side %s", sideCapturingClassVisitor.className, SIDE));
        }
        if (sideCapturingClassVisitor.removableFields.size() > 0 || sideCapturingClassVisitor.removableMethods.size() > 0) {
            ClassWriter writer = new ClassWriter(0);
            classReader.accept(new SideRemovingClassVisitor(327680, writer, sideCapturingClassVisitor), 0);
            return writer.toByteArray();
        }
        return bytes;
    }

    public static class SideRemovingClassVisitor
    extends ClassVisitor {
        private final SideCapturingClassVisitor capturer;

        public SideRemovingClassVisitor(int api, ClassVisitor cv, SideCapturingClassVisitor capturer) {
            super(api, cv);
            this.capturer = capturer;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            String name1 = name + desc;
            if (this.capturer.removableFields.contains(name1)) {
                return null;
            }
            return super.visitField(access, name, desc, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            String name1 = name + desc;
            if (this.capturer.removableMethods.contains(name1)) {
                return null;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }

    public static class SideCapturingClassVisitor
    extends ClassVisitor {
        public List<String> removableClasses = new ArrayList<String>(1);
        public Set<String> removableFields = new HashSet<String>();
        public Set<String> removableMethods = new HashSet<String>();
        public String className;

        public SideCapturingClassVisitor(int api) {
            super(api);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.equals(SIDEONLY_DESCRIPTOR)) {
                return new SideCapturingAnnotationVisitor(this.api, this.removableClasses, this.className);
            }
            return null;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            final String name1 = name + desc;
            return new FieldVisitor(this.api, super.visitField(access, name, desc, signature, value)){

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    if (desc.equals(SIDEONLY_DESCRIPTOR)) {
                        return new SideCapturingAnnotationVisitor(this.api, removableFields, name1);
                    }
                    return null;
                }
            };
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final String name1 = name + desc;
            return new MethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions)){

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    if (desc.equals(SIDEONLY_DESCRIPTOR)) {
                        return new SideCapturingAnnotationVisitor(this.api, removableMethods, name1);
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

        public SideCapturingAnnotationVisitor(int api, Collection<String> targetSet, String targetName) {
            super(api);
            this.targetSet = targetSet;
            this.targetName = targetName;
        }

        @Override
        public void visitEnum(String name, String desc, String value) {
            if (name.equals("value") && !value.equals(SIDE)) {
                this.targetSet.add(this.targetName);
            }
        }
    }
}

