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
    private void onKey(EventKey eventKey) {
        if (Keyboard.getKeyName(eventKey.getKey()).toLowerCase().contains("lmenu") && this.dirtPos.size() < 4) {
            this.dirtPos.add(this.mc.objectMouseOver.getBlockPos());
        }
    }

    @EventHandler
    private void drawBlocks(EventRender3D eventRender3D) {
        for (BlockPos blockPos : this.dirtPos) {
            RenderUtil.drawFullBlockESP(blockPos, new Color(Colors.MAGENTA.c), eventRender3D.getPartialTicks());
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.dirtPos.size() < 4) {
            return;
        }
        int n = InventoryUtils.getAmountInHotbar("Jungle Sapling");
        int n2 = InventoryUtils.getAmountInHotbar("Enchanted Bone Meal");
        if (n < 5 || n2 < 2) {
            return;
        }
        switch (this.foragingState) {
            case TREE: {
                if (!this.failSafeTimer.hasReached((Double)this.breakDelay.getValue()) || !this.yepTimer.hasReached(this.treeWait)) break;
                this.swapSlot(((Double)this.treeSlot.getValue()).intValue());
                BlockPos blockPos = this.dirtPos.get(this.currentTree - 1);
                float[] fArray = this.getRotations(blockPos, EnumFacing.DOWN);
                this.mc.thePlayer.rotationYaw = fArray[0];
                this.mc.thePlayer.rotationPitch = fArray[1];
                this.yepTimer.reset();
                this.foragingState = ForagingState.LOOKING;
                break;
            }
            case BONEMEAL: {
                if (!this.yepTimer.hasReached(((Double)this.delay.getValue()).intValue())) break;
                this.swapSlot(((Double)this.bonemealSlot.getValue()).intValue());
                Client.rightClick();
                this.yepTimer.reset();
                this.shabTimer.reset();
                this.foragingState = ForagingState.RODSWAP;
                break;
            }
            case RODSWAP: {
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
            case HARVEST: {
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
    public void onTickWorld(EventTick eventTick) {
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
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void harvest() {
        MovingObjectPosition movingObjectPosition = this.mc.objectMouseOver;
        BlockPos blockPos = this.closestLog();
        if (blockPos == null) {
            return;
        }
        movingObjectPosition.hitVec = new Vec3(blockPos);
        EnumFacing enumFacing = movingObjectPosition.sideHit;
        if (enumFacing != null && this.mc.thePlayer != null) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
        }
        this.mc.thePlayer.swingItem();
    }

    private BlockPos closestLog() {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return null;
        }
        float f = 2.0f;
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        Vec3i vec3i = new Vec3i(f, f, f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos2);
                if (iBlockState.getBlock() != Blocks.log && iBlockState.getBlock() != Blocks.vine && iBlockState.getBlock() != Blocks.sapling) continue;
                arrayList.add(new Vec3((double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!arrayList.isEmpty()) {
            return new BlockPos(((Vec3)arrayList.get((int)0)).xCoord, ((Vec3)arrayList.get((int)0)).yCoord, ((Vec3)arrayList.get((int)0)).zCoord);
        }
        return null;
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() + 0.5 - this.mc.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d2 = (double)blockPos.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d3 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)blockPos.getY() + 0.5);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    private void swapSlot(int n) {
        if (n > 0 && n <= 8) {
            this.mc.thePlayer.inventory.currentItem = n - 1;
        }
    }

    public void silentUse(int n, int n2) {
        int n3 = this.mc.thePlayer.inventory.currentItem;
        if (n2 > 0 && n2 <= 8) {
            this.mc.thePlayer.inventory.currentItem = n2 - 1;
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
        }
        if (n > 0 && n <= 8) {
            this.mc.thePlayer.inventory.currentItem = n - 1;
        } else if (n == 0) {
            this.mc.thePlayer.inventory.currentItem = n3;
        }
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

