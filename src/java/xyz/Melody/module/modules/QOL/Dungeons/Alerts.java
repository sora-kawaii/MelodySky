/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.FMLModules.AlertsListener;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class Alerts
extends Module {
    private TimerUtil bonzoTimer = new TimerUtil();
    private TimerUtil bonzo2Timer = new TimerUtil();
    private TimerUtil spiritTimer = new TimerUtil();
    public boolean spiritMaskPoped = false;
    public boolean bonzoMaskPoped = false;
    public boolean bonzoMask2Poped = false;
    public boolean spiritMaskReady = true;
    public boolean bonzoMaskReady = true;
    public boolean bonzoMask2Ready = true;
    public boolean shouldShowSpiritMaskReady = false;
    public boolean shouldShowBonzoMaskReady = false;
    public boolean shouldShowBonzoMask2Ready = false;
    public Option<Boolean> rases = new Option<Boolean>("Rases Mode", true);
    public Option<Boolean> watcher = new Option<Boolean>("WatcherReady", true);
    public Option<Boolean> bonzo = new Option<Boolean>("BonzoMask", true);
    public Option<Boolean> spirit = new Option<Boolean>("SpiritMask", true);

    public Alerts() {
        super("Alerts", ModuleType.Dungeons);
        this.addValues(this.rases, this.watcher, this.bonzo, this.spirit);
        this.setModInfo("Create Alert Titles.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.bonzoTimer.reset();
        this.bonzo2Timer.reset();
        this.spiritTimer.reset();
        this.spiritMaskPoped = false;
        this.bonzoMaskPoped = false;
        this.bonzoMask2Poped = false;
        this.spiritMaskReady = true;
        this.bonzoMaskReady = true;
        this.bonzoMask2Ready = true;
        this.shouldShowSpiritMaskReady = false;
        this.shouldShowBonzoMaskReady = false;
        this.shouldShowBonzoMask2Ready = false;
        super.onDisable();
    }

    @EventHandler
    private void onTickPop(EventTick event) {
        if (AlertsListener.shouldShowWatcherReady) {
            Client.drawTitle("Watcher Ready", EnumChatFormatting.RED);
            AlertsListener.shouldShowWatcherReady = false;
        }
        if (AlertsListener.shouldShowSpiritMaskPoped) {
            if (!this.spiritMaskPoped) {
                this.spiritMaskPoped = true;
            }
            Client.drawTitle("Spirit Mask Poped!", EnumChatFormatting.RED);
            this.spiritMaskReady = false;
            this.shouldShowSpiritMaskReady = false;
            this.spiritTimer.reset();
            AlertsListener.shouldShowSpiritMaskPoped = false;
        }
        if (AlertsListener.shouldShowBonzoMaskPoped) {
            if (!this.bonzoMaskPoped) {
                this.bonzoMaskPoped = true;
            }
            Client.drawTitle("Bonzo Mask Poped!", EnumChatFormatting.RED);
            this.bonzoMaskReady = false;
            this.shouldShowBonzoMaskReady = false;
            this.bonzoTimer.reset();
            AlertsListener.shouldShowBonzoMaskPoped = false;
        }
        if (AlertsListener.shouldShowBonzoMask2Poped) {
            if (!this.bonzoMask2Poped) {
                this.bonzoMask2Poped = true;
            }
            Client.drawTitle("\u269a Bonzo Mask Poped!", EnumChatFormatting.RED);
            this.bonzoMask2Ready = false;
            this.shouldShowBonzoMask2Ready = false;
            this.bonzo2Timer.reset();
            AlertsListener.shouldShowBonzoMask2Poped = false;
        }
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (this.spiritMaskPoped && this.spiritTimer.hasReached(33000.0)) {
            this.shouldShowSpiritMaskReady = true;
            this.spiritTimer.reset();
        }
        if (this.bonzoMaskPoped && this.bonzoTimer.hasReached(211000.0)) {
            this.shouldShowBonzoMaskReady = true;
            this.bonzoTimer.reset();
        }
        if (this.bonzoMask2Poped && this.bonzo2Timer.hasReached(211000.0)) {
            this.shouldShowBonzoMask2Ready = true;
            this.bonzo2Timer.reset();
        }
    }

    @EventHandler
    private void onTickReady(EventTick event) {
        if (this.shouldShowSpiritMaskReady) {
            if (!this.spiritMaskReady) {
                this.spiritMaskReady = true;
            }
            Client.drawTitle("Spirit Mask Ready!", EnumChatFormatting.GREEN);
            this.spiritMaskPoped = false;
            Helper.sendMessage("Spirit Mask Ready!");
            this.shouldShowSpiritMaskReady = false;
        }
        if (this.shouldShowBonzoMaskReady) {
            if (!this.bonzoMaskReady) {
                this.bonzoMaskReady = true;
            }
            Client.drawTitle("Bonzo Mask Ready!", EnumChatFormatting.GREEN);
            this.bonzoMaskPoped = false;
            Helper.sendMessage("Bonzo Mask Ready!");
            this.shouldShowBonzoMaskReady = false;
        }
        if (this.shouldShowBonzoMask2Ready) {
            if (!this.bonzoMask2Ready) {
                this.bonzoMask2Ready = true;
            }
            Client.drawTitle("\u269a Bonzo Mask Ready!", EnumChatFormatting.GREEN);
            this.bonzoMask2Poped = false;
            Helper.sendMessage("\u269a Bonzo Mask Ready!");
            this.shouldShowBonzoMask2Ready = false;
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        this.spiritMaskPoped = false;
        this.bonzoMaskPoped = false;
        this.bonzoMask2Poped = false;
        this.spiritMaskReady = true;
        this.bonzoMaskReady = true;
        this.bonzoMask2Ready = true;
        this.shouldShowSpiritMaskReady = false;
        this.shouldShowBonzoMaskReady = false;
        this.shouldShowBonzoMask2Ready = false;
        this.bonzoTimer.reset();
        this.bonzo2Timer.reset();
        this.spiritTimer.reset();
    }
}

