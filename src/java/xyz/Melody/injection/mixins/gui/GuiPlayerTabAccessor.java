/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GuiPlayerTabOverlay.class})
public interface GuiPlayerTabAccessor {
    @Accessor(value="footer")
    public IChatComponent getFooter();

    @Accessor(value="header")
    public IChatComponent getHeader();
}

