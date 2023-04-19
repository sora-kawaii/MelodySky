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

    public static byte[] replaceConstructor(byte[] data, String className, String from, String to, String ... methods) {
        ClassReader reader = new ClassReader(data);
        ClassWriter writer = new ClassWriter(0);
        reader.accept(new FoamFixConstructorReplacer(from, to, methods).getClassVisitor(327680, writer), 0);
        return writer.toByteArray();
    }

    public static byte[] spliceClasses(byte[] data, String className, String targetClassName, String ... methods) {
        try {
            byte[] dataSplice = ((LaunchClassLoader)FoamFixTransformer.class.getClassLoader()).getClassBytes(className);
            return FoamFixTransformer.spliceClasses(data, dataSplice, className, targetClassName, methods);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] spliceClasses(byte[] data, byte[] dataSplice, String className, String targetClassName, String ... methods) {
        int j;
        boolean added;
        Object mn;
        int i;
        if (dataSplice == null) {
            throw new RuntimeException("Class " + className + " not found! This is a FoamFix bug!");
        }
        HashSet<String> methodSet = Sets.newHashSet(methods);
        ArrayList<String> methodList = Lists.newArrayList(methods);
        ClassReader readerData = new ClassReader(data);
        ClassReader readerSplice = new ClassReader(dataSplice);
        ClassWriter writer = new ClassWriter(0);
        final String className2 = className.replace('.', '/');
        final String targetClassName2 = targetClassName.replace('.', '/');
        Remapper remapper = new Remapper(){

            @Override
            public String map(String name) {
                return className2.equals(name) ? targetClassName2 : name;
            }
        };
        ClassNode nodeData = new ClassNode();
        ClassNode nodeSplice = new ClassNode();
        readerData.accept(nodeData, 0);
        readerSplice.accept(new RemappingClassAdapter(nodeSplice, remapper), 8);
        for (String s : nodeSplice.interfaces) {
            if (!s.contains("IFoamFix")) continue;
            nodeData.interfaces.add(s);
            System.out.println("Added INTERFACE: " + s);
        }
        for (i = 0; i < nodeSplice.methods.size(); ++i) {
            if (!methodSet.contains(((MethodNode)nodeSplice.methods.get((int)i)).name)) continue;
            mn = (MethodNode)nodeSplice.methods.get(i);
            added = false;
            for (j = 0; j < nodeData.methods.size(); ++j) {
                if (!((MethodNode)nodeData.methods.get((int)j)).name.equals(((MethodNode)mn).name) || !((MethodNode)nodeData.methods.get((int)j)).desc.equals(((MethodNode)mn).desc)) continue;
                MethodNode oldMn = (MethodNode)nodeData.methods.get(j);
                System.out.println("Spliced in METHOD: " + targetClassName + "." + ((MethodNode)mn).name);
                nodeData.methods.set(j, mn);
                oldMn.name = (String)methodList.get(methodList.indexOf(oldMn.name) & 0xFFFFFFFE) + "_foamfix_old";
                nodeData.methods.add(oldMn);
                added = true;
                break;
            }
            if (added) continue;
            System.out.println("Added METHOD: " + targetClassName + "." + ((MethodNode)mn).name);
            nodeData.methods.add(mn);
            added = true;
        }
        for (i = 0; i < nodeSplice.fields.size(); ++i) {
            if (!methodSet.contains(((FieldNode)nodeSplice.fields.get((int)i)).name)) continue;
            mn = (FieldNode)nodeSplice.fields.get(i);
            added = false;
            for (j = 0; j < nodeData.fields.size(); ++j) {
                if (!((FieldNode)nodeData.fields.get((int)j)).name.equals(((FieldNode)mn).name) || !((FieldNode)nodeData.fields.get((int)j)).desc.equals(((FieldNode)mn).desc)) continue;
                System.out.println("Spliced in FIELD: " + targetClassName + "." + ((FieldNode)mn).name);
                nodeData.fields.set(j, mn);
                added = true;
                break;
            }
            if (added) continue;
            System.out.println("Added FIELD: " + targetClassName + "." + ((FieldNode)mn).name);
            nodeData.fields.add(mn);
            added = true;
        }
        nodeData.accept(writer);
        return writer.toByteArray();
    }

    public static void init() {
        if (FoamFixShared.config.geSmallPropertyStorage) {
            transformFunctions.put("net.minecraft.block.state.BlockStateContainer", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] data, String transformedName) {
                    return FoamFixTransformer.spliceClasses(data, "xyz.Melody.FoamFix.common.FoamyBlockStateContainer", transformedName, "createState", "createState");
                }
            });
            transformFunctions.put("net.minecraftforge.common.property.ExtendedBlockState", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] data, String transformedName) {
                    return FoamFixTransformer.spliceClasses(data, "xyz.Melody.FoamFix.common.FoamyExtendedBlockStateContainer", transformedName, "createState", "createState");
                }
            });
        }
        if (FoamFixShared.config.clDynamicItemModels) {
            transformFunctions.put("net.minecraftforge.client.model.ItemLayerModel", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] data, String transformedName) {
                    return FoamFixTransformer.spliceClasses(data, "xyz.Melody.FoamFix.client.FoamFixDynamicItemModels", transformedName, "bake", "bake");
                }
            });
        }
        if (FoamFixShared.config.geReplaceSimpleName) {
            transformFunctions.put("net.minecraft.world.World", new TransformerFunction(){

                @Override
                public byte[] transform(byte[] data, String transformedName) {
                    ClassReader reader = new ClassReader(data);
                    ClassWriter writer = new ClassWriter(0);
                    reader.accept(new FoamFixReplaceClassSimpleName("updateEntities", "func_72939_s").getClassVisitor(327680, writer), 0);
                    return writer.toByteArray();
                }
            });
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] dataOrig) {
        if (dataOrig == null) {
            return null;
        }
        byte[] data = dataOrig;
        if (FoamFixShared.config.geBlockPosPatch) {
            data = "net.minecraft.util.math.Vec3i".equals(transformedName) ? BlockPosPatch.patchVec3i(data) : BlockPosPatch.patchOtherClass(data, "net.minecraft.util.math.BlockPos$MutableBlockPos".equals(transformedName));
        }
        for (TransformerFunction function : transformFunctions.get(transformedName)) {
            data = function.transform(data, transformedName);
        }
        return data;
    }
}

