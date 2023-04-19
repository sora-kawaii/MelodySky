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
    private void onTick(EventTick e) {
        if (this.mc.objectMouseOver == null) {
            return;
        }
        if (this.mc.objectMouseOver.entityHit != null) {
            return;
        }
        if (Client.inDungeons && (this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockChest || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockLever)) {
            return;
        }
        if (!this.shouldSet) {
            return;
        }
        if (!this.timer.hasReached(60.0)) {
            return;
        }
        if (this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.air.getDefaultState().getBlock()) {
            BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            this.mc.theWorld.setBlockToAir(pos);
            blockposs.add(pos);
            this.timer.reset();
        }
        this.shouldSet = false;
    }

    @EventHandler
    private void tickBlock(EventTick e) {
        if (this.mc.objectMouseOver == null) {
            return;
        }
        if (this.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown(1)) {
            BlockPos p = this.mc.objectMouseOver.getBlockPos();
            if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe) {
                return;
            }
            for (BlockPos pos : blockposs) {
                if (Math.abs(p.getX() - pos.getX()) > 1 || Math.abs(p.getY() - pos.getY()) > 1 || Math.abs(p.getZ() - pos.getZ()) > 1) continue;
                blockposs.remove(pos);
                break;
            }
        }
        int i = 0;
        while (true) {
            if (i >= blockposs.size()) break;
            this.mc.theWorld.setBlockToAir(blockposs.get(i));
            ++i;
        }
    }

    @EventHandler
    private void tickUpdate(EventTick e) {
        if (this.mc.objectMouseOver == null) {
            return;
        }
        if (this.mc.objectMouseOver.entityHit != null) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        if (Client.inDungeons && this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockChest) {
            return;
        }
        if (!this.timer.hasReached(50.0)) {
            return;
        }
        if (((Boolean)this.pickaxe.getValue()).booleanValue() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe && Mouse.isButtonDown(1)) {
            this.mc.theWorld.setBlockToAir(this.mc.objectMouseOver.getBlockPos());
            blockposs.add(this.mc.objectMouseOver.getBlockPos());
            this.timer.reset();
        }
    }

    @EventHandler
    private void onKey(EventTick event) {
        if (this.mc.currentScreen != null) {
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

