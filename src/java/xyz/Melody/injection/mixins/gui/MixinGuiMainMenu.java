/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import java.awt.Color;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.Menu.GuiHideMods;
import xyz.Melody.GUI.Menu.MainMenu;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu
extends GuiScreen {
    @Inject(method="initGui", at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        this.buttonList.add(new ClientButton(114, this.width - 100, 10, 60, 24, "MelodySky", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(19198, this.width - 165, 10, 60, 24, "Hide Mods", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(514, this.width - 10 - 24, 10, 25, 24, "", new ResourceLocation("Melody/icon/exit.png"), new Color(20, 20, 20, 60)));
        if (!Client.vanillaMenu) {
            this.mc.displayGuiScreen(new MainMenu());
        }
    }

    @Inject(method="actionPerformed", at={@At(value="HEAD")})
    private void actionPerformed(GuiButton guiButton, CallbackInfo callbackInfo) {
        switch (guiButton.id) {
            case 114: {
                Client.vanillaMenu = false;
                this.mc.displayGuiScreen(new MainMenu());
                break;
            }
            case 514: {
                this.mc.shutdown();
                break;
            }
            case 19198: {
                this.mc.displayGuiScreen(new GuiHideMods(this));
            }
        }
    }
}

