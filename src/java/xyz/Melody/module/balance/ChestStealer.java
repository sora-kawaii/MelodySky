/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import java.awt.Color;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ChestStealer
extends Module {
    private Numbers<Double> delay = new Numbers<Double>("Delay", 70.0, 0.0, 1000.0, 10.0);
    private Option<Boolean> menucheck = new Option<Boolean>("MenuCheck", true);
    private TimerUtil timer = new TimerUtil();

    public ChestStealer() {
        super("ChestStealer", new String[]{"cheststeal", "chests", "stealer"}, ModuleType.Balance);
        this.addValues(this.delay, this.menucheck);
        this.setColor(new Color(218, 97, 127).getRGB());
    }

    @EventHandler
    private void onUpdate(EventTick eventTick) {
        this.setSuffix(this.delay.getValue());
        if (this.mc.field_71439_g != null && this.mc.field_71439_g.field_71070_bA != null && this.mc.field_71439_g.field_71070_bA instanceof ContainerChest) {
            int n = 0;
            ContainerChest containerChest = (ContainerChest)this.mc.field_71439_g.field_71070_bA;
            if (StatCollector.func_74838_a((String)"container.chest").equalsIgnoreCase(containerChest.func_85151_d().func_145748_c_().func_150260_c()) || StatCollector.func_74838_a((String)"container.chestDouble").equalsIgnoreCase(containerChest.func_85151_d().func_145748_c_().func_150260_c()) && ((Boolean)this.menucheck.getValue()).booleanValue()) {
                for (n = 0; n < containerChest.func_85151_d().func_70302_i_(); ++n) {
                    if (containerChest.func_85151_d().func_70301_a(n) == null || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                    this.mc.field_71442_b.func_78753_a(containerChest.field_75152_c, n, 0, 1, this.mc.field_71439_g);
                    this.timer.reset();
                }
                if (this.isEmpty()) {
                    this.mc.field_71439_g.func_71053_j();
                }
            } else {
                return;
            }
        }
    }

    private boolean isEmpty() {
        if (this.mc.field_71439_g.field_71070_bA != null && this.mc.field_71439_g.field_71070_bA instanceof ContainerChest) {
            ContainerChest containerChest = (ContainerChest)this.mc.field_71439_g.field_71070_bA;
            for (int i = 0; i < containerChest.func_85151_d().func_70302_i_(); ++i) {
                ItemStack itemStack = containerChest.func_85151_d().func_70301_a(i);
                if (itemStack == null || itemStack.func_77973_b() == null) continue;
                return false;
            }
        }
        return true;
    }
}

