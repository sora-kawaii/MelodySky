/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.game.InventoryUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ForagingMacro
extends Module {
    private Option<Boolean> useRod = new Option<Boolean>("Use Rod", false);
    private Numbers<Double> treeSlot = new Numbers<Double>("Sapling Slot", 0.0, 0.0, 8.0, 1.0);
    private Numbers<Double> bonemealSlot = new Numbers<Double>("Bonemeal Slot", 1.0, 0.0, 8.0, 1.0);
    private Numbers<Double> axeSlot = new Numbers<Double>("Axe Slot", 2.0, 0.0, 8.0, 1.0);
    private Numbers<Double> rodSlot = new Numbers<Double>("Rod Slot", 3.0, 0.0, 8.0, 1.0);
    private Numbers<Double> delay = new Numbers<Double>("PlaceDelay", 500.0, 0.0, 5000.0, 100.0);
    private Numbers<Double> timeBreak = new Numbers<Double>("TimeBeforeBreak", 500.0, 100.0, 1000.0, 10.0);
    private Numbers<Double> breakDelay = new Numbers<Double>("BreakDelay", 2000.0, 1000.0, 3000.0, 50.0);
    private ArrayList<BlockPos> dirtPos = new ArrayList();
    private TimerUtil yepTimer = new TimerUtil();
    private TimerUtil failSafeTimer = new TimerUtil();
    private TimerUtil shabTimer = new TimerUtil();
    private ForagingState foragingState;
    private int currentTree = 1;
    private int treeWait;

    public ForagingMacro() {
        super("ForagingMacro", new String[]{"am"}, ModuleType.Macros);
        this.addValues(this.useRod, this.treeSlot, this.bonemealSlot, this.axeSlot, this.rodSlot, this.delay, this.timeBreak, this.breakDelay);
        this.setModInfo("Auto Place -> Grow -> Break Trees.");
    }

    @Override
    public void onEnable() {
        Helper.sendMessage("[ForagingMacro] Aim a Block And Press ALT to Set Dirt Position.");
        this.foragingState = ForagingState.TREE;
        this.currentTree = 1;
        this.yepTimer.reset();
        this.failSafeTimer.reset();
        this.shabTimer.reset();
        this.treeWait = ((Double)this.delay.getValue()).intValue();
        this.dirtPos.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private void onKey(EventKey event) {
        if (Keyboard.getKeyName(event.getKey()).toLowerCase().contains("lmenu") && this.dirtPos.size() < 4) {
            this.dirtPos.add(this.mc.objectMouseOver.getBlockPos());
        }
    }

    @EventHandler
    private void drawBlocks(EventRender3D event) {
        for (BlockPos pos : this.dirtPos) {
            RenderUtil.drawFullBlockESP(pos, new Color(Colors.MAGENTA.c), event.getPartialTicks());
        }
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (this.dirtPos.size() < 4) {
            return;
        }
        int saplingCount = InventoryUtils.getAmountInHotbar("Jungle Sapling");
        int boneMealCount = InventoryUtils.getAmountInHotbar("Enchanted Bone Meal");
        if (saplingCount < 5 || boneMealCount < 2) {
            return;
        }
        switch (I.$SwitchMap$xyz$Melody$module$modules$macros$ForagingMacro$ForagingState[this.foragingState.ordinal()]) {
            case 1: {
                if (!this.failSafeTimer.hasReached((Double)this.breakDelay.getValue()) || !this.yepTimer.hasReached(this.treeWait)) break;
                this.swapSlot(((Double)this.treeSlot.getValue()).intValue());
                BlockPos cur = this.dirtPos.get(this.currentTree - 1);
                float[] rots = this.getRotations(cur, EnumFacing.DOWN);
                this.mc.thePlayer.rotationYaw = rots[0];
                this.mc.thePlayer.rotationPitch = rots[1];
                this.yepTimer.reset();
                this.foragingState = ForagingState.LOOKING;
                break;
            }
            case 2: {
                if (!this.yepTimer.hasReached(((Double)this.delay.getValue()).intValue())) break;
                this.swapSlot(((Double)this.bonemealSlot.getValue()).intValue());
                Client.rightClick();
                this.yepTimer.reset();
                this.shabTimer.reset();
                this.foragingState = ForagingState.RODSWAP;
                break;
            }
            case 3: {
                if (((Boolean)this.useRod.getValue()).booleanValue()) {
                    if (!this.yepTimer.hasReached(((Double)this.delay.getValue()).intValue())) break;
                    this.silentUse(((Double)this.axeSlot.getValue()).intValue(), ((Double)this.rodSlot.getValue()).intValue());
                    Client.rightClick();
                    this.yepTimer.reset();
                    this.failSafeTimer.reset();
                    this.shabTimer.reset();
                    this.foragingState = ForagingState.HARVEST;
                    this.swapSlot(((Double)this.axeSlot.getValue()).intValue());
                    break;
                }
                this.yepTimer.reset();
                this.failSafeTimer.reset();
                this.shabTimer.reset();
                this.foragingState = ForagingState.HARVEST;
                this.swapSlot(((Double)this.axeSlot.getValue()).intValue());
                break;
            }
            case 4: {
                if (this.failSafeTimer.hasReached((Double)this.breakDelay.getValue())) {
                    this.foragingState = ForagingState.TREE;
                    this.currentTree = 1;
                }
                if (!this.shabTimer.hasReached((Double)this.timeBreak.getValue()) || this.mc.objectMouseOver == null || this.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !this.yepTimer.hasReached(((Double)this.timeBreak.getValue()).intValue())) break;
                if (this.closestLog() != null) {
                    this.harvest();
                } else {
                    this.yepTimer.reset();
                    this.foragingState = ForagingState.TREE;
                    this.currentTree = 1;
                    this.treeWait = 500;
                }
                this.shabTimer.reset();
                break;
            }
        }
    }

    @EventHandler
    public void onTickWorld(EventTick event) {
        if (this.foragingState == ForagingState.LOOKING && this.yepTimer.hasReached((Double)this.delay.getValue())) {
            Client.rightClick();
            this.yepTimer.reset();
            if (this.currentTree < 4) {
                ++this.currentTree;
                this.foragingState = ForagingState.TREE;
                this.treeWait = ((Double)this.delay.getValue()).intValue();
            } else {
                this.foragingState = ForagingState.BONEMEAL;
            }
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void harvest() {
        MovingObjectPosition fake = this.mc.objectMouseOver;
        BlockPos bp = this.closestLog();
        if (bp == null) {
            return;
        }
        fake.hitVec = new Vec3(bp);
        EnumFacing enumFacing = fake.sideHit;
        if (enumFacing != null && this.mc.thePlayer != null) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bp, enumFacing));
        }
        this.mc.thePlayer.swingItem();
    }

    private BlockPos closestLog() {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return null;
        }
        float r = 2.0f;
        BlockPos playerPos = this.mc.thePlayer.getPosition();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> logs = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (blockState.getBlock() != Blocks.log && blockState.getBlock() != Blocks.vine && blockState.getBlock() != Blocks.sapling) continue;
                logs.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
        }
        logs.sort(Comparator.comparingDouble(this::lambda$closestLog$0));
        if (!logs.isEmpty()) {
            return new BlockPos(((Vec3)logs.get((int)0)).xCoord, ((Vec3)logs.get((int)0)).yCoord, ((Vec3)logs.get((int)0)).zCoord);
        }
        return null;
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - this.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double d1 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    private void swapSlot(int slot) {
        if (slot > 0 && slot <= 8) {
            this.mc.thePlayer.inventory.currentItem = slot - 1;
        }
    }

    public void silentUse(int mainSlot, int useSlot) {
        int oldSlot = this.mc.thePlayer.inventory.currentItem;
        if (useSlot > 0 && useSlot <= 8) {
            this.mc.thePlayer.inventory.currentItem = useSlot - 1;
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
        }
        if (mainSlot > 0 && mainSlot <= 8) {
            this.mc.thePlayer.inventory.currentItem = mainSlot - 1;
        } else if (mainSlot == 0) {
            this.mc.thePlayer.inventory.currentItem = oldSlot;
        }
    }

    private double lambda$closestLog$0(Vec3 vec) {
        return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    static enum ForagingState {
        TREE,
        BONEMEAL,
        RODSWAP,
        HARVEST,
        LOOKING;

    }

    static enum dir {
        NORTH,
        EAST,
        SOUTH,
        WEST;

    }
}

