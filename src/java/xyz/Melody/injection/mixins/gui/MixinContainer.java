/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;

@Mixin(value={Container.class})
public class MixinContainer {
    @Inject(method="putStacksInSlots", at={@At(value="RETURN")})
    public void putStacksInSlots(ItemStack[] stacks, CallbackInfo ci) {
        AutoEnchantTable aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class);
        aet.processInventoryContents();
    }
}

