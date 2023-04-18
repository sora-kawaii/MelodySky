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
        KeyBinding keyBinding;
        if (this.mc.gameSettings != null && (keyBinding = this.mc.gameSettings.keyBindUseItem) != null) {
            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate eventPreUpdate) {
        if (this.mc.thePlayer == null) {
            return;
        }
        InventoryPlayer inventoryPlayer = this.mc.thePlayer.inventory;
        if (inventoryPlayer == null) {
            return;
        }
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            KeyBinding keyBinding = this.mc.gameSettings.keyBindUseItem;
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
                int n;
                boolean bl;
                boolean bl2 = bl = i != 0;
                if (bl) {
                    if (!((Boolean)this.eatHeads.getValue()).booleanValue()) {
                        continue;
                    }
                } else if (!((Boolean)this.eatApples.getValue()).booleanValue()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                if ((n = bl ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322)) == -1) continue;
                int n2 = inventoryPlayer.currentItem;
                doingStuff = true;
                if (bl) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventoryPlayer.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                    this.timer.reset();
                    continue;
                }
                inventoryPlayer.currentItem = n;
                KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
                if (this.eatingApple) continue;
                this.eatingApple = true;
                this.switched = n2;
            }
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        if (entityPlayerSP == null) {
            return;
        }
        InventoryPlayer inventoryPlayer = entityPlayerSP.inventory;
        if (inventoryPlayer == null) {
            return;
        }
        int n = this.switched;
        if (n == -1) {
            return;
        }
        inventoryPlayer.currentItem = n;
        this.switched = n = -1;
    }

    private int getItemFromHotbar(int n) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack;
            Item item;
            if (this.mc.thePlayer.inventory.mainInventory[i] == null || Item.getIdFromItem(item = (itemStack = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) != n) continue;
            return i;
        }
        return -1;
    }
}

