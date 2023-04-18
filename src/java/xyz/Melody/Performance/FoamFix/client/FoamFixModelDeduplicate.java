/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Performance.FoamFix.ProxyClient;
import xyz.Melody.Performance.FoamFix.client.Deduplicator;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;

public final class FoamFixModelDeduplicate {
    @SubscribeEvent(priority=EventPriority.LOW)
    public void onModelBake(ModelBakeEvent modelBakeEvent) {
        if (FoamFixShared.config.clDeduplicate) {
            ProgressManager.ProgressBar progressBar = ProgressManager.push("FoamFix: deduplicating", ((RegistrySimple)modelBakeEvent.modelRegistry).getKeys().size());
            if (ProxyClient.deduplicator == null) {
                ProxyClient.deduplicator = new Deduplicator();
            }
            Client.instance.logger.info("Deduplicating models...");
            ProxyClient.deduplicator.maxRecursion = FoamFixShared.config.clDeduplicateRecursionLevel;
            ProxyClient.deduplicator.addObjects(Block.blockRegistry.getKeys());
            ProxyClient.deduplicator.addObjects(Item.itemRegistry.getKeys());
            for (ModelResourceLocation modelResourceLocation : ((RegistrySimple)modelBakeEvent.modelRegistry).getKeys()) {
                IBakedModel iBakedModel = modelBakeEvent.modelRegistry.getObject(modelResourceLocation);
                String string = modelResourceLocation.toString();
                progressBar.step(String.format("[%s]", string));
                try {
                    ProxyClient.deduplicator.addObject(modelResourceLocation);
                    ProxyClient.deduplicator.deduplicateObject(iBakedModel, 0);
                }
                catch (Exception exception) {}
            }
            ProgressManager.pop(progressBar);
            Client.instance.logger.info("Deduplicated " + ProxyClient.deduplicator.successfuls + " objects.");
        }
        ProxyClient.deduplicator = null;
    }
}

