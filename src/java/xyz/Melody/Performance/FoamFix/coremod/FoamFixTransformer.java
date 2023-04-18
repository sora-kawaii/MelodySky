/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import xyz.Melody.Performance.FoamFix.coremod.BlockPosPatch;
import xyz.Melody.Performance.FoamFix.coremod.FoamFixConstructorReplacer;
import xyz.Melody.Performance.FoamFix.coremod.FoamFixReplaceClassSimpleName;
import xyz.Melody.Performance.FoamFix.coremod.TransformerFunction;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;

public final class FoamFixTransformer
implements IClassTransformer {
    private static final Multimap<String, TransformerFunction> transformFunctions = HashMultimap.create();

    public static byte[] replaceConstructor(byte[] byArray, String string, String string2, String string3, String ... stringArray) {
        ClassReader classReader = new ClassReader(byArray);
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(new FoamFixConstructorReplacer(string2, string3, stringArray).getClassVisitor(327680, classWriter), 0);
        return classWriter.toByteArray();
    }

    public static byte[] spliceClasses(byte[] byArray, String string, String string2, String ... stringArray) {
        try {
            byte[] byArray2 = ((LaunchClassLoader)FoamFixTransformer.class.getClassLoader()).getClassBytes(string);
            return FoamFixTransformer.spliceClasses(byArray, byArray2, string, string2, stringArray);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static byte[] spliceClasses(byte[] byArray, byte[] byArray2, String string, String string2, String ... stringArray) {
        int n;
        boolean bl;
        int n2;
        Object object2;
        if (byArray2 == null) {
            throw new RuntimeException("Class " + string + " not found! This is a FoamFix bug!");
        }
        HashSet<String> hashSet = Sets.newHashSet(stringArray);
        ArrayList<String> arrayList = Lists.newArrayList(stringArray);
        ClassReader classReader = new ClassReader(byArray);
        ClassReader classReader2 = new ClassReader(byArray2);
        ClassWriter classWriter = new ClassWriter(0);
        final String string3 = string.replace('.', '/');
        final String string4 = string2.replace('.', '/');
        Remapper remapper = new Remapper(){

            @Override
            public String map(String string) {
                return string3.equals(string) ? string4 : string;
            }
        };
        ClassNode classNode = new ClassNode();
        ClassNode classNode2 = new ClassNode();
        classReader.accept(classNode, 0);
        classReader2.accept(new RemappingClassAdapter(classNode2, remapper), 8);
        for (Object object2 : classNode2.interfaces) {
            if (!((String)object2).contains("IFoamFix")) continue;
            classNode.interfaces.add(object2);
            System.out.println("Added INTERFACE: " + (String)object2);
        }
        for (n2 = 0; n2 < classNode2.methods.size(); ++n2) {
            if (!hashSet.contains(((MethodNode)classNode2.methods.get((int)n2)).name)) continue;
            object2 = (MethodNode)classNode2.methods.get(n2);
            bl = false;
            for (n = 0; n < classNode.methods.size(); ++n) {
                if (!((MethodNode)classNode.methods.get((int)n)).name.equals(((MethodNode)object2).name) || !((MethodNode)classNode.methods.get((int)n)).desc.equals(((MethodNode)object2).desc)) continue;
                MethodNode methodNode = (MethodNode)classNode.methods.get(n);
                System.out.println("Spliced in METHOD: " + string2 + "." + ((MethodNode)object2).name);
                classNode.methods.set(n, object2);
                methodNode.name = (String)arrayList.get(arrayList.indexOf(methodNode.name) & 0xFFFFFFFE) + "_foamfix_old";
                classNode.methods.add(methodNode);
                bl = true;
                break;
            }
            if (bl) continue;
            System.out.println("Added METHOD: " + string2 + "." + ((MethodNode)object2).name);
            classNode.methods.add(object2);
            bl = true;
        }
        for (n2 = 0; n2 < classNode2.fields.size(); ++n2) {
            if (!hashSet.contains(((FieldNode)classNode2.fields.get((int)n2)).name)) continue;
            object2 = (FieldNode)classNode2.fields.get(n2);
            bl = false;
            for (n = 0; n < classNode.fields.size(); ++n) {
                if (!((FieldNode)classNode.fields.get((int)n)).name.equals(((FieldNode)object2).name) || !((FieldNode)classNode.fields.get((int)n)).desc.equals(((FieldNode)object2).desc)) continue;
                System.out.println("Spliced in FIELD: " + string2 + "." + ((FieldNode)object2).name);
                classNode.fields.set(n, object2);
                bl = true;
                break;
            }
            if (bl) continue;
            System.out.println("Added FIELD: " + string2 + "." + ((FieldNode)object2).name);
            classNode.fields.add(object2);
            bl = true;
        }
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static void init() {
        if (FoamFixShared.config.geSmallPropertyStorage) {
            transformFunctions.put("net.minecraft.block.state.BlockStateContainer", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] byArray, String string) {
                    return FoamFixTransformer.spliceClasses(byArray, "xyz.Melody.FoamFix.common.FoamyBlockStateContainer", string, "createState", "createState");
                }
            });
            transformFunctions.put("net.minecraftforge.common.property.ExtendedBlockState", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] byArray, String string) {
                    return FoamFixTransformer.spliceClasses(byArray, "xyz.Melody.FoamFix.common.FoamyExtendedBlockStateContainer", string, "createState", "createState");
                }
            });
        }
        if (FoamFixShared.config.clDynamicItemModels) {
            transformFunctions.put("net.minecraftforge.client.model.ItemLayerModel", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] byArray, String string) {
                    return FoamFixTransformer.spliceClasses(byArray, "xyz.Melody.FoamFix.client.FoamFixDynamicItemModels", string, "bake", "bake");
                }
            });
        }
        if (FoamFixShared.config.geReplaceSimpleName) {
            transformFunctions.put("net.minecraft.world.World", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] byArray, String string) {
                    ClassReader classReader = new ClassReader(byArray);
                    ClassWriter classWriter = new ClassWriter(0);
                    classReader.accept(new FoamFixReplaceClassSimpleName("updateEntities", "func_72939_s").getClassVisitor(327680, classWriter), 0);
                    return classWriter.toByteArray();
                }
            });
        }
    }

    @Override
    public byte[] transform(String string, String string2, byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[] byArray2 = byArray;
        if (FoamFixShared.config.geBlockPosPatch) {
            byArray2 = "net.minecraft.util.math.Vec3i".equals(string2) ? BlockPosPatch.patchVec3i(byArray2) : BlockPosPatch.patchOtherClass(byArray2, "net.minecraft.util.math.BlockPos$MutableBlockPos".equals(string2));
        }
        for (TransformerFunction transformerFunction : transformFunctions.get(string2)) {
            byArray2 = transformerFunction.transform(byArray2, string2);
        }
        return byArray2;
    }
}

