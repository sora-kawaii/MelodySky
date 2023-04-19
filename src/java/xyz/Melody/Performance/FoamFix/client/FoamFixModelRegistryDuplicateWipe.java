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
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        Map modelStore;
        ItemModelMesher imm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        BlockModelShapes bms = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
        ModelManager mgr = bms.getModelManager();
        Field f = ReflectionHelper.findField(ModelManager.class, "modelRegistry", "field_174958_a");
        try {
            RegistrySimple registry = (RegistrySimple)f.get(mgr);
            Client.instance.logger.info("Clearing unnecessary model registry of size " + registry.getKeys().size() + ".");
            for (ModelResourceLocation modelResourceLocation : registry.getKeys()) {
                registry.putObject(modelResourceLocation, ProxyClient.DUMMY_MODEL);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        f = ReflectionHelper.findField(BlockModelShapes.class, "bakedModelStore", "field_178129_a");
        try {
            modelStore = (Map)f.get(bms);
            Client.instance.logger.info("Clearing unnecessary model store of size " + modelStore.size() + ".");
            modelStore.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (imm instanceof ItemModelMesherForge) {
            f = ReflectionHelper.findField(ItemModelMesherForge.class, "models");
            try {
                modelStore = (IdentityHashMap)f.get(imm);
                Client.instance.logger.info("Clearing unnecessary item shapes cache of size " + ((IdentityHashMap)modelStore).size() + ".");
                ((IdentityHashMap)modelStore).clear();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

