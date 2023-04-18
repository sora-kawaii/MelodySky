/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import net.minecraft.client.gui.ScaledResolution;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Browser;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class InternetSurfing
extends Module {
    private Browser bruhSir;
    private Option<Boolean> onTop = new Option<Boolean>("AlwaysOnTop", true);

    public InternetSurfing() {
        super("InternetSurfing", new String[]{"internet", "surfing", "browser"}, ModuleType.Others);
        this.addValues(this.onTop);
        this.setModInfo("A Simple Web Bruhsir.");
    }

    @Override
    public void onEnable() {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        if (this.bruhSir == null) {
            this.bruhSir = new Browser("https://www.baidu.com/", "MelodySky Internet Surfing", true, true, (boolean)((Boolean)this.onTop.getValue()), (int)((float)scaledResolution.getScaledWidth() / 0.6f), (int)((double)scaledResolution.getScaledHeight() / 0.5), JWebBrowser.useEdgeRuntime());
        }
        super.onEnable();
    }

    @EventHandler
    private void tickBruhsir(EventTick eventTick) {
        if (this.bruhSir != null && this.bruhSir.closed) {
            this.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        if (this.bruhSir != null && !this.bruhSir.closed) {
            this.bruhSir.close();
        }
        this.bruhSir = null;
        super.onDisable();
    }
}

