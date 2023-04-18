/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xyz.Melody.Client;
import xyz.Melody.Performance.FoamFix.ProxyClient;

public final class FoamFixModelRegistryDuplicateWipe {
    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post post) {
        Object object;
        ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        BlockModelShapes blockModelShapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
        ModelManager modelManager = blockModelShapes.getModelManager();
        Field field = ReflectionHelper.findField(ModelManager.class, "modelRegistry", "field_174958_a");
        try {
            object = (RegistrySimple)field.get(modelManager);
            Client.instance.logger.info("Clearing unnecessary model registry of size " + ((RegistrySimple)object).getKeys().size() + ".");
            for (ModelResourceLocation modelResourceLocation : ((RegistrySimple)object).getKeys()) {
                ((RegistrySimple)object).putObject(modelResourceLocation, ProxyClient.DUMMY_MODEL);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        field = ReflectionHelper.findField(BlockModelShapes.class, "bakedModelStore", "field_178129_a");
        try {
            object = (Map)field.get(blockModelShapes);
            Client.instance.logger.info("Clearing unnecessary model store of size " + object.size() + ".");
            object.clear();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (itemModelMesher instanceof ItemModelMesherForge) {
            field = ReflectionHelper.findField(ItemModelMesherForge.class, "models");
            try {
                object = (IdentityHashMap)field.get(itemModelMesher);
                Client.instance.logger.info("Clearing unnecessary item shapes cache of size " + ((IdentityHashMap)object).size() + ".");
                ((IdentityHashMap)object).clear();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

