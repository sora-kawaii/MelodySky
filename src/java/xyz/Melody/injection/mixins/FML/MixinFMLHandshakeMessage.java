/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.FML;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;

@Mixin(value={FMLHandshakeMessage.ModList.class}, remap=false)
public abstract class MixinFMLHandshakeMessage {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method="<init>(Ljava/util/List;)V", at={@At(value="RETURN")})
    private void removeMod(List<ModContainer> list, CallbackInfo callbackInfo) {
        if (!Minecraft.func_71410_x().func_71387_A()) {
            this.modTags.remove("melodysky");
            this.modTags = this.modTags.entrySet().stream().filter(entry -> Client.isWhitelisted((String)entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }
}

