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

    public HUDApi(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
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

    public void setEnabled(boolean b) {
        if (b) {
            EventBus.getInstance().register(this);
            this.regFML(this);
        } else {
            EventBus.getInstance().unregister(this);
            this.unregFML(this);
        }
        this.enabled = b;
    }

    private void regFML(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    private void unregFML(Object obj) {
        MinecraftForge.EVENT_BUS.unregister(obj);
    }

    public void setXY(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}

