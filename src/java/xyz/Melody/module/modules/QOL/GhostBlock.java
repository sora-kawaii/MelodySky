/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class GhostBlock
extends Module {
    private boolean shouldSet = false;
    public static ArrayList<BlockPos> blockposs = new ArrayList();
    private TimerUtil timer = new TimerUtil();
    private Option<Boolean> pickaxe = new Option<Boolean>("rcPickaxe", true);

    public GhostBlock() {
        super("GhostBlock", new String[]{"gb"}, ModuleType.QOL);
        this.addValues(this.pickaxe);
        this.setColor(new Color(244, 255, 149).getRGB());
        this.setModInfo("Create Ghost Block Where You are Looking.");
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.field_71476_x == null) {
            return;
        }
        if (this.mc.field_71476_x.field_72308_g != null) {
            return;
        }
        if (Client.inDungeons && (this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c() instanceof BlockChest || this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c() instanceof BlockLever)) {
            return;
        }
        if (!this.shouldSet) {
            return;
        }
        if (!this.timer.hasReached(60.0)) {
            return;
        }
        if (this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c() != Blocks.field_150350_a.func_176223_P().func_177230_c()) {
            BlockPos blockPos = this.mc.field_71476_x.func_178782_a();
            this.mc.field_71441_e.func_175698_g(blockPos);
            blockposs.add(blockPos);
            this.timer.reset();
        }
        this.shouldSet = false;
    }

    @EventHandler
    private void tickBlock(EventTick eventTick) {
        if (this.mc.field_71476_x == null) {
            return;
        }
        if (this.mc.field_71476_x.field_72308_g == null && Mouse.isButtonDown(1)) {
            BlockPos blockPos = this.mc.field_71476_x.func_178782_a();
            if (this.mc.field_71439_g.func_70694_bm() != null && this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemPickaxe) {
                return;
            }
            for (BlockPos blockPos2 : blockposs) {
                if (Math.abs(blockPos.func_177958_n() - blockPos2.func_177958_n()) > 1 || Math.abs(blockPos.func_177956_o() - blockPos2.func_177956_o()) > 1 || Math.abs(blockPos.func_177952_p() - blockPos2.func_177952_p()) > 1) continue;
                blockposs.remove(blockPos2);
                break;
            }
        }
        int n = 0;
        while (true) {
            if (n >= blockposs.size()) break;
            this.mc.field_71441_e.func_175698_g(blockposs.get(n));
            ++n;
        }
    }

    @EventHandler
    private void tickUpdate(EventTick eventTick) {
        if (this.mc.field_71476_x == null) {
            return;
        }
        if (this.mc.field_71476_x.field_72308_g != null) {
            return;
        }
        if (this.mc.field_71462_r != null) {
            return;
        }
        if (Client.inDungeons && this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c() instanceof BlockChest) {
            return;
        }
        if (!this.timer.hasReached(50.0)) {
            return;
        }
        if (((Boolean)this.pickaxe.getValue()).booleanValue() && this.mc.field_71439_g.func_70694_bm() != null && this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemPickaxe && Mouse.isButtonDown(1)) {
            this.mc.field_71441_e.func_175698_g(this.mc.field_71476_x.func_178782_a());
            blockposs.add(this.mc.field_71476_x.func_178782_a());
            this.timer.reset();
        }
    }

    @EventHandler
    private void onKey(EventTick eventTick) {
        if (this.mc.field_71462_r != null) {
            return;
        }
        if (this.getKey() == 0) {
            return;
        }
        if (!this.timer.hasReached(50.0)) {
            return;
        }
        if (Keyboard.isKeyDown(this.getKey())) {
            this.shouldSet = true;
        }
    }

    @Override
    public void onDisable() {
        this.setEnabled(true);
        super.onDisable();
    }
}

