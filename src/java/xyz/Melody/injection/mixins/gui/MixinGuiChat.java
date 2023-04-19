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
    private void initGui(CallbackInfo ci) {
        this.buttonList.add(new ClientButton(1145, this.width / 2 - 50, this.height - 50, 100, 20, "Current: " + (Client.clientChat ? "Melody" : "Vanilla"), new Color(20, 20, 20, 120)));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1145: {
                Client.clientChat = !Client.clientChat;
                this.mc.displayGuiScreen(new GuiChat());
            }
        }
        super.actionPerformed(button);
    }
}

