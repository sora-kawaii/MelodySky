/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoHead
extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff;
    private final TimerUtil timer = new TimerUtil();
    private final Option<Boolean> eatHeads = new Option<Boolean>("Eatheads", true);
    private final Option<Boolean> eatApples = new Option<Boolean>("Eatapples", true);
    private final Numbers<Double> health = new Numbers<Double>("HpPercentage", 50.0, 10.0, 100.0, 1.0);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 750.0, 10.0, 2000.0, 25.0);

    public AutoHead() {
        super("AutoHead", new String[]{"AutoHead", "EH", "eathead"}, ModuleType.Balance);
        this.addValues(this.health, this.delay, this.eatApples, this.eatHeads);
    }

    @Override
    public void onEnable() {
        doingStuff = false;
        this.eatingApple = false;
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        KeyBinding keyBindUseItem;
        if (this.mc.gameSettings != null && (keyBindUseItem = this.mc.gameSettings.keyBindUseItem) != null) {
            KeyBinding.setKeyBindState(keyBindUseItem.getKeyCode(), false);
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        InventoryPlayer inventory = this.mc.thePlayer.inventory;
        if (inventory == null) {
            return;
        }
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            KeyBinding useItem = this.mc.gameSettings.keyBindUseItem;
            if (!this.timer.hasReached((Double)this.delay.getValue())) {
                this.eatingApple = false;
                this.repairItemPress();
                this.repairItemSwitch();
                return;
            }
            if (this.mc.thePlayer.capabilities.isCreativeMode || this.mc.thePlayer.isPotionActive(Potion.regeneration) || (double)this.mc.thePlayer.getHealth() >= (double)this.mc.thePlayer.getMaxHealth() * ((Double)this.health.getValue() / 100.0)) {
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            for (int i = 0; i < 2; ++i) {
                int slot;
                boolean doEatHeads;
                boolean bl = doEatHeads = i != 0;
                if (doEatHeads) {
                    if (!((Boolean)this.eatHeads.getValue()).booleanValue()) {
                        continue;
                    }
                } else if (!((Boolean)this.eatApples.getValue()).booleanValue()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                if ((slot = doEatHeads ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322)) == -1) continue;
                int tempSlot = inventory.currentItem;
                doingStuff = true;
                if (doEatHeads) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    this.timer.reset();
                    continue;
                }
                inventory.currentItem = slot;
                KeyBinding.setKeyBindState(useItem.getKeyCode(), true);
                if (this.eatingApple) continue;
                this.eatingApple = true;
                this.switched = tempSlot;
            }
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP p = this.mc.thePlayer;
        if (p == null) {
            return;
        }
        InventoryPlayer inventory = p.inventory;
        if (inventory == null) {
            return;
        }
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.currentItem = switched;
        this.switched = switched = -1;
    }

    private int getItemFromHotbar(int id) {
        for (int i = 0; i < 9; ++i) {
            ItemStack is;
            Item item;
            if (this.mc.thePlayer.inventory.mainInventory[i] == null || Item.getIdFromItem(item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) != id) continue;
            return i;
        }
        return -1;
    }
}

