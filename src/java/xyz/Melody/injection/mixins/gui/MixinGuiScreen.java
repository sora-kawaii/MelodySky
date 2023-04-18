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
    public Minecraft field_146297_k;
    @Shadow
    public int field_146294_l;
    @Shadow
    public int field_146295_m;
    @Shadow
    public List<GuiButton> field_146292_n;

    @Shadow
    protected void func_146284_a(GuiButton guiButton) throws IOException {
    }

    @Shadow
    protected void func_146276_q_() {
    }

    @Shadow
    protected void func_146270_b(int n) {
    }

    @Shadow
    public void func_73863_a(int n, int n2, float f) {
    }

    @Shadow
    public void func_73866_w_() {
    }
}

