/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.entity;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.misc.EventClickSlot;

@Mixin(value={PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method="windowClick", at={@At(value="HEAD")})
    private void windowClick(int windowid, int slot, int button, int mode, EntityPlayer entityPlayer, CallbackInfoReturnable<ItemStack> ci) {
        EventBus.getInstance().call(new EventClickSlot(windowid, slot, button, mode, entityPlayer));
    }
}

