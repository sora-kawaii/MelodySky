/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.client;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventDrawText;

@Mixin(value={FontRenderer.class})
public class MixinFontRenderer {
    @ModifyVariable(method="renderString", at=@At(value="HEAD"), ordinal=0)
    private String renderString(String string) {
        if (string == null) {
            return string;
        }
        EventDrawText eventDrawText = new EventDrawText(string);
        EventBus.getInstance().call(eventDrawText);
        return eventDrawText.getText();
    }
}

