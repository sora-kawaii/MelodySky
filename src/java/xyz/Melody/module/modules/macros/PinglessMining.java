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
        this.left = this.mc.field_71474_y.field_74312_F;
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
        if (this.left != null && this.left.func_151470_d() && this.blockPos != null && (movingObjectPosition = this.mc.field_71476_x) != null && movingObjectPosition.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && ((block = this.mc.field_71441_e.func_180495_p(movingObjectPosition.func_178782_a()).func_177230_c()) == Blocks.field_150348_b || block == Blocks.field_150412_bA || block == Blocks.field_150369_x || block == Blocks.field_150450_ax || block == Blocks.field_150366_p || block == Blocks.field_150352_o || block == Blocks.field_150365_q || block == Blocks.field_150482_ag || block == Blocks.field_150388_bm || block == Blocks.field_150436_aH || block == Blocks.field_150469_bN || block == Blocks.field_150459_bM)) {
            this.broken.add(this.blockPos);
            this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            this.mc.field_71439_g.func_71038_i();
        }
        this.timer.reset();
    }

    @EventHandler
    public void onRender(EventRender3D eventRender3D) {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
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
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c().func_177982_a(0, 1, 0);
        Vec3 vec3 = this.mc.field_71439_g.func_174791_d();
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
            object = this.mc.field_71441_e.func_180495_p(blockPos2);
            if (!this.isLookingAtBlock(blockPos2, eventRender3D) || this.broken.contains(blockPos2) || object.func_177230_c() == Blocks.field_150350_a) continue;
            arrayList.add(new Vec3((double)blockPos2.func_177958_n() + 0.5, blockPos2.func_177956_o(), (double)blockPos2.func_177952_p() + 0.5));
        }
        double d = 9999.0;
        object = null;
        for (Vec3 vec32 : arrayList) {
            double d2 = vec32.func_72438_d(vec3);
            if (!(d2 < d)) continue;
            d = d2;
            object = vec32;
        }
        if (object != null) {
            return new BlockPos(((Vec3)object).field_72450_a, ((Vec3)object).field_72448_b, ((Vec3)object).field_72449_c);
        }
        return null;
    }

    private boolean isLookingAtBlock(BlockPos blockPos, EventRender3D eventRender3D) {
        AxisAlignedBB axisAlignedBB = AxisAlignedBB.func_178781_a((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p(), (double)(blockPos.func_177958_n() + 1), (double)(blockPos.func_177956_o() + 1), (double)(blockPos.func_177952_p() + 1));
        Vec3 vec3 = new Vec3(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u + (double)this.mc.field_71439_g.func_70047_e(), this.mc.field_71439_g.field_70161_v);
        Vec3 vec32 = this.mc.field_71439_g.func_70676_i(eventRender3D.getPartialTicks());
        vec32 = PinglessMining.scaleVec(vec32, 0.2f);
        for (int i = 0; i < 40; ++i) {
            if (axisAlignedBB.field_72340_a <= vec3.field_72450_a && axisAlignedBB.field_72336_d >= vec3.field_72450_a && axisAlignedBB.field_72338_b <= vec3.field_72448_b && axisAlignedBB.field_72337_e >= vec3.field_72448_b && axisAlignedBB.field_72339_c <= vec3.field_72449_c && axisAlignedBB.field_72334_f >= vec3.field_72449_c) {
                return true;
            }
            vec3 = vec3.func_178787_e(vec32);
        }
        return false;
    }

    private static Vec3 scaleVec(Vec3 vec3, float f) {
        return new Vec3(vec3.field_72450_a * (double)f, vec3.field_72448_b * (double)f, vec3.field_72449_c * (double)f);
    }
}

