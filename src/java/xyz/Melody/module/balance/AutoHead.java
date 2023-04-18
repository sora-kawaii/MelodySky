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
        if (this.mc.field_71474_y != null && (keyBinding = this.mc.field_71474_y.field_74313_G) != null) {
            KeyBinding.func_74510_a((int)keyBinding.func_151463_i(), (boolean)false);
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate eventPreUpdate) {
        if (this.mc.field_71439_g == null) {
            return;
        }
        InventoryPlayer inventoryPlayer = this.mc.field_71439_g.field_71071_by;
        if (inventoryPlayer == null) {
            return;
        }
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            KeyBinding keyBinding = this.mc.field_71474_y.field_74313_G;
            if (!this.timer.hasReached((Double)this.delay.getValue())) {
                this.eatingApple = false;
                this.repairItemPress();
                this.repairItemSwitch();
                return;
            }
            if (this.mc.field_71439_g.field_71075_bZ.field_75098_d || this.mc.field_71439_g.func_70644_a(Potion.field_76428_l) || (double)this.mc.field_71439_g.func_110143_aJ() >= (double)this.mc.field_71439_g.func_110138_aP() * ((Double)this.health.getValue() / 100.0)) {
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
                int n2 = inventoryPlayer.field_70461_c;
                doingStuff = true;
                if (bl) {
                    this.mc.field_71439_g.field_71174_a.func_147297_a(new C09PacketHeldItemChange(n));
                    this.mc.field_71439_g.field_71174_a.func_147297_a(new C08PacketPlayerBlockPlacement(inventoryPlayer.func_70448_g()));
                    this.mc.field_71439_g.field_71174_a.func_147297_a(new C09PacketHeldItemChange(n2));
                    this.timer.reset();
                    continue;
                }
                inventoryPlayer.field_70461_c = n;
                KeyBinding.func_74510_a((int)keyBinding.func_151463_i(), (boolean)true);
                if (this.eatingApple) continue;
                this.eatingApple = true;
                this.switched = n2;
            }
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        InventoryPlayer inventoryPlayer = entityPlayerSP.field_71071_by;
        if (inventoryPlayer == null) {
            return;
        }
        int n = this.switched;
        if (n == -1) {
            return;
        }
        inventoryPlayer.field_70461_c = n;
        this.switched = n = -1;
    }

    private int getItemFromHotbar(int n) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack;
            Item item;
            if (this.mc.field_71439_g.field_71071_by.field_70462_a[i] == null || Item.func_150891_b((Item)(item = (itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i]).func_77973_b())) != n) continue;
            return i;
        }
        return -1;
    }
}

