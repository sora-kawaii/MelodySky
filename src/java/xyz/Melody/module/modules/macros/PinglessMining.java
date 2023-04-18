/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import net.minecraft.block.Block;
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
    public void tick(EventTick eventTick) {
        Block block;
        MovingObjectPosition movingObjectPosition;
        ++this.ticks;
        if (this.ticks % 40 == 0) {
            this.broken.clear();
        }
        if (!this.timer.hasReached(50.0)) {
            return;
        }
        if (this.left != null && this.left.isKeyDown() && this.blockPos != null && (movingObjectPosition = this.mc.objectMouseOver) != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((block = this.mc.theWorld.getBlockState(movingObjectPosition.getBlockPos()).getBlock()) == Blocks.stone || block == Blocks.emerald_ore || block == Blocks.lapis_ore || block == Blocks.redstone_ore || block == Blocks.iron_ore || block == Blocks.gold_ore || block == Blocks.coal_ore || block == Blocks.diamond_ore || block == Blocks.nether_wart || block == Blocks.reeds || block == Blocks.potatoes || block == Blocks.carrots)) {
            this.broken.add(this.blockPos);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
        }
        this.timer.reset();
    }

    @EventHandler
    public void onRender(EventRender3D eventRender3D) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            this.broken.clear();
            return;
        }
        this.blockPos = this.closestBlock(eventRender3D);
        if (this.blockPos != null) {
            RenderUtil.drawSolidBlockESP(this.blockPos, Colors.RED.c, eventRender3D.getPartialTicks());
        }
    }

    private BlockPos closestBlock(EventRender3D eventRender3D) {
        Object object;
        int n = 5;
        BlockPos blockPos = this.mc.thePlayer.getPosition().add(0, 1, 0);
        Vec3 vec3 = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
            object = this.mc.theWorld.getBlockState(blockPos2);
            if (!this.isLookingAtBlock(blockPos2, eventRender3D) || this.broken.contains(blockPos2) || object.getBlock() == Blocks.air) continue;
            arrayList.add(new Vec3((double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5));
        }
        double d = 9999.0;
        object = null;
        for (Vec3 vec32 : arrayList) {
            double d2 = vec32.distanceTo(vec3);
            if (!(d2 < d)) continue;
            d = d2;
            object = vec32;
        }
        if (object != null) {
            return new BlockPos(((Vec3)object).xCoord, ((Vec3)object).yCoord, ((Vec3)object).zCoord);
        }
        return null;
    }

    private boolean isLookingAtBlock(BlockPos blockPos, EventRender3D eventRender3D) {
        AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);
        Vec3 vec3 = new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);
        Vec3 vec32 = this.mc.thePlayer.getLook(eventRender3D.getPartialTicks());
        vec32 = PinglessMining.scaleVec(vec32, 0.2f);
        for (int i = 0; i < 40; ++i) {
            if (axisAlignedBB.minX <= vec3.xCoord && axisAlignedBB.maxX >= vec3.xCoord && axisAlignedBB.minY <= vec3.yCoord && axisAlignedBB.maxY >= vec3.yCoord && axisAlignedBB.minZ <= vec3.zCoord && axisAlignedBB.maxZ >= vec3.zCoord) {
                return true;
            }
            vec3 = vec3.add(vec32);
        }
        return false;
    }

    private static Vec3 scaleVec(Vec3 vec3, float f) {
        return new Vec3(vec3.xCoord * (double)f, vec3.yCoord * (double)f, vec3.zCoord * (double)f);
    }
}

