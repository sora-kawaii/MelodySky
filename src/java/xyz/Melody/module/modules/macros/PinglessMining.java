/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class PinglessMining
extends Module {
    private BlockPos blockPos = null;
    private TimerUtil timer = new TimerUtil();
    private final ArrayList<BlockPos> broken = new ArrayList();
    private KeyBinding left;
    private int ticks;

    public PinglessMining() {
        super("PinglessMining", new String[0], ModuleType.Macros);
        this.left = this.mc.gameSettings.keyBindAttack;
        this.ticks = 0;
    }

    @EventHandler
    public void tick(EventTick event) {
        Block b;
        MovingObjectPosition movingObjectPosition;
        ++this.ticks;
        if (this.ticks % 40 == 0) {
            this.broken.clear();
        }
        if (!this.timer.hasReached(50.0)) {
            return;
        }
        if (this.left != null && this.left.isKeyDown() && this.blockPos != null && (movingObjectPosition = this.mc.objectMouseOver) != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((b = this.mc.theWorld.getBlockState(movingObjectPosition.getBlockPos()).getBlock()) == Blocks.stone || b == Blocks.emerald_ore || b == Blocks.lapis_ore || b == Blocks.redstone_ore || b == Blocks.iron_ore || b == Blocks.gold_ore || b == Blocks.coal_ore || b == Blocks.diamond_ore || b == Blocks.nether_wart || b == Blocks.reeds || b == Blocks.potatoes || b == Blocks.carrots)) {
            this.broken.add(this.blockPos);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
        }
        this.timer.reset();
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            this.broken.clear();
            return;
        }
        this.blockPos = this.closestBlock(event);
        if (this.blockPos != null) {
            RenderUtil.drawSolidBlockESP(this.blockPos, Colors.RED.c, event.getPartialTicks());
        }
    }

    private BlockPos closestBlock(EventRender3D event) {
        int r = 5;
        BlockPos playerPos = this.mc.thePlayer.getPosition().add(0, 1, 0);
        Vec3 playerVec = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> blocks = new ArrayList<Vec3>();
        for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!this.isLookingAtBlock(blockPos, event) || this.broken.contains(blockPos) || blockState.getBlock() == Blocks.air) continue;
            blocks.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
        }
        double smallest = 9999.0;
        Vec3 closest = null;
        for (Vec3 block : blocks) {
            double dist = block.distanceTo(playerVec);
            if (!(dist < smallest)) continue;
            smallest = dist;
            closest = block;
        }
        if (closest != null) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private boolean isLookingAtBlock(BlockPos blockPos, EventRender3D event) {
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);
        Vec3 position = new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);
        Vec3 look = this.mc.thePlayer.getLook(event.getPartialTicks());
        look = PinglessMining.scaleVec(look, 0.2f);
        for (int i = 0; i < 40; ++i) {
            if (aabb.minX <= position.xCoord && aabb.maxX >= position.xCoord && aabb.minY <= position.yCoord && aabb.maxY >= position.yCoord && aabb.minZ <= position.zCoord && aabb.maxZ >= position.zCoord) {
                return true;
            }
            position = position.add(look);
        }
        return false;
    }

    private static Vec3 scaleVec(Vec3 vec, float f) {
        return new Vec3(vec.xCoord * (double)f, vec.yCoord * (double)f, vec.zCoord * (double)f);
    }
}

