/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.injection.mixins.gui.MixinGuiScreen;

@Mixin(value={GuiChat.class})
public abstract class MixinGuiChat
extends MixinGuiScreen {
    @Inject(method="initGui", at={@At(value="HEAD")}, cancellable=true)
    private void initGui(CallbackInfo callbackInfo) {
        this.field_146292_n.add(new ClientButton(1145, this.field_146294_l / 2 - 50, this.field_146295_m - 50, 100, 20, "Current: " + (Client.clientChat ? "Melody" : "Vanilla"), new Color(20, 20, 20, 120)));
    }

    @Override
    protected void func_146284_a(GuiButton guiButton) throws IOException {
        switch (guiButton.field_146127_k) {
            case 1145: {
                Client.clientChat = !Client.clientChat;
                this.field_146297_k.func_147108_a(new GuiChat());
            }
        }
        super.func_146284_a(guiButton);
    }
}

