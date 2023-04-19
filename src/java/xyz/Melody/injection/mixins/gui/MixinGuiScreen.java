/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiScreen.class})
public class MixinGuiScreen {
    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public List<GuiButton> buttonList;

    @Shadow
    protected void actionPerformed(GuiButton button) throws IOException {
    }

    @Shadow
    protected void drawDefaultBackground() {
    }

    @Shadow
    protected void drawWorldBackground(int tint) {
    }

    @Shadow
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Shadow
    public void initGui() {
    }
}

