/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Notification;

import net.minecraft.util.ResourceLocation;
import xyz.Melody.Utils.Colors;

public enum NotificationType {
    SUCCESS(Colors.GREEN.c, new ResourceLocation("Melody/noti/success.png")),
    INFO(Colors.GRAY.c, new ResourceLocation("Melody/noti/info.png")),
    WARN(Colors.YELLOW.c, new ResourceLocation("Melody/noti/warning.png")),
    ERROR(Colors.RED.c, new ResourceLocation("Melody/noti/error.png"));

    private final int color;
    private final ResourceLocation icon;

    public ResourceLocation getIcon() {
        return this.icon;
    }

    private NotificationType(int n2, ResourceLocation resourceLocation) {
        this.color = n2;
        this.icon = resourceLocation;
    }

    public final int getColor() {
        return this.color;
    }
}

