/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import xyz.Melody.Event.EventBus;

public class HUDApi {
    public String name;
    public int x;
    public int y;
    private boolean enabled;
    public static boolean useISR;
    public Minecraft mc = Minecraft.getMinecraft();

    public HUDApi(String string, int n, int n2) {
        this.name = string;
        this.x = n;
        this.y = n2;
    }

    public void InScreenRender() {
        useISR = true;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bl) {
        if (bl) {
            EventBus.getInstance().register(this);
            this.regFML(this);
        } else {
            EventBus.getInstance().unregister(this);
            this.unregFML(this);
        }
        this.enabled = bl;
    }

    private void regFML(Object object) {
        MinecraftForge.EVENT_BUS.register(object);
    }

    private void unregFML(Object object) {
        MinecraftForge.EVENT_BUS.unregister(object);
    }

    public void setXY(int n, int n2) {
        this.x = n;
        this.y = n2;
    }
}

