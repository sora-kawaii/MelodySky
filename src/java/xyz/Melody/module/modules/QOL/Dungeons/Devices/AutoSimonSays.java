/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoSimonSays
extends Module {
    private final List<BlockPos> simonSaysQueue = new ArrayList<BlockPos>();
    public final BlockPos simonSaysStart = new BlockPos(110, 121, 91);
    public boolean clickedSimonSays;
    private long lastInteractTime;
    private TimerUtil timer = new TimerUtil();
    private Numbers<Double> delay = new Numbers<Double>("Delay", 350.0, 200.0, 1000.0, 10.0);

    public AutoSimonSays() {
        super("AutoSimonSays", new String[]{"ss"}, ModuleType.Dungeons);
        this.addValues(this.delay);
        this.setModInfo("Auto Do Simon Says Device.");
    }

    @Override
    public void onDisable() {
        this.simonSaysQueue.clear();
        this.clickedSimonSays = false;
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (!Client.inDungeons) {
            return;
        }
        if (this.simonSaysQueue.size() != 0 && System.currentTimeMillis() - this.lastInteractTime >= ((Double)this.delay.getValue()).longValue() && this.mc.theWorld.getBlockState(new BlockPos(110, 121, 92)).getBlock() == Blocks.stone_button) {
            for (BlockPos pos : new ArrayList<BlockPos>(this.simonSaysQueue)) {
                MovingObjectPosition intercept = this.calculateInterceptLook(pos, 5.5f);
                if (intercept == null || !this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), pos, intercept.sideHit, intercept.hitVec)) continue;
                this.mc.thePlayer.swingItem();
                this.simonSaysQueue.remove(pos);
                this.lastInteractTime = System.currentTimeMillis();
                break;
            }
        }
    }

    @EventHandler
    public void onPacket(BlockChangeEvent event) {
        if (!Client.inDungeons) {
            return;
        }
        if (!(event.getPosition().getX() != 111 || event.getNewBlock().getBlock() != Blocks.sea_lantern || this.simonSaysQueue.size() != 0 && this.simonSaysQueue.get(this.simonSaysQueue.size() - 1).equals(event.getPosition()))) {
            this.simonSaysQueue.add(new BlockPos(110, event.getPosition().getY(), event.getPosition().getZ()));
            this.clickedSimonSays = true;
        }
    }

    @EventHandler
    private void onReset(BlockChangeEvent event) {
        if (!Client.inDungeons) {
            return;
        }
        if (event.getPosition() == this.simonSaysStart && this.timer.hasReached(750.0)) {
            this.simonSaysQueue.clear();
            this.clickedSimonSays = false;
            Helper.sendMessage("[AutoSS] AutoSS Reset.");
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        this.simonSaysQueue.clear();
        this.clickedSimonSays = false;
    }

    public MovingObjectPosition calculateInterceptLook(BlockPos pos, float range) {
        Vec3 look;
        AxisAlignedBB aabb = this.getBlockAABB(pos);
        Vec3 vec3 = this.getPositionEyes();
        if (vec3.squareDistanceTo(look = AutoSimonSays.getMiddleOfAABB(aabb)) > (double)(range * range)) {
            return null;
        }
        return aabb.calculateIntercept(vec3, look);
    }

    public Vec3 getPositionEyes() {
        return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.fastEyeHeight(), this.mc.thePlayer.posZ);
    }

    public AxisAlignedBB getBlockAABB(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState(this.mc.theWorld, pos);
        return block.getSelectedBoundingBox(this.mc.theWorld, pos);
    }

    public float fastEyeHeight() {
        return this.mc.thePlayer.isSneaking() ? 1.54f : 1.62f;
    }

    public static Vec3 getMiddleOfAABB(AxisAlignedBB aabb) {
        return new Vec3((aabb.maxX + aabb.minX) / 2.0, (aabb.maxY + aabb.minY) / 2.0, (aabb.maxZ + aabb.minZ) / 2.0);
    }
}

