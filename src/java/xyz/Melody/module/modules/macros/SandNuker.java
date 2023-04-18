/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class SandNuker
extends Module {
    private ArrayList<BlockPos> broken = new ArrayList();
    private BlockPos closestStone;
    private int ticks = 0;
    private TimerUtil timer = new TimerUtil();
    private Numbers<Double> range = new Numbers<Double>("Range", 3.8, 2.0, 6.0, 0.1);
    private Numbers<Double> depth = new Numbers<Double>("Depth", 2.0, 0.0, 3.0, 0.5);
    private Numbers<Double> bps = new Numbers<Double>("Block Per Sec.", 10.0, 1.0, 50.0, 1.0);

    public SandNuker() {
        super("SandNuker", new String[]{""}, ModuleType.Macros);
        this.addValues(this.bps, this.range, this.depth);
        this.setModInfo("Auto Fuck Sand Blocks.");
    }

    @Override
    public void onDisable() {
        this.broken.clear();
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        ++this.ticks;
        if (this.broken.size() > 10) {
            this.broken.clear();
        }
        if (this.ticks > 20) {
            this.broken.clear();
            this.ticks = 0;
        }
        if (!this.timer.hasReached(1000 / ((Double)this.bps.getValue()).intValue())) {
            return;
        }
        this.closestStone = this.closestStone();
        if (this.closestStone != null) {
            MovingObjectPosition movingObjectPosition = this.mc.field_71476_x;
            movingObjectPosition.field_72307_f = new Vec3(this.closestStone);
            EnumFacing enumFacing = movingObjectPosition.field_178784_b;
            if (enumFacing != null && this.mc.field_71439_g != null) {
                this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.closestStone, enumFacing));
            }
            this.mc.field_71439_g.func_71038_i();
            this.broken.add(this.closestStone);
        }
        this.timer.reset();
    }

    @EventHandler
    public void renderWorld(EventRender3D eventRender3D) {
        if (this.closestStone != null) {
            RenderUtil.drawSolidBlockESP(this.closestStone, Colors.AQUA.c, 3.5f, eventRender3D.getPartialTicks());
        }
    }

    private BlockPos closestStone() {
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            return null;
        }
        float f = ((Double)this.range.getValue()).floatValue();
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c().func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(f, f, f);
        Vec3i vec3i2 = new Vec3i(f, 1.0 + (Double)this.depth.getValue(), f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i2))) {
                IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos2);
                if (iBlockState.func_177230_c() != Blocks.field_150354_m || this.broken.contains(blockPos2)) continue;
                arrayList.add(new Vec3((double)blockPos2.func_177958_n() + 0.5, blockPos2.func_177956_o(), (double)blockPos2.func_177952_p() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.field_71439_g.func_70011_f(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c)));
        if (!arrayList.isEmpty()) {
            return new BlockPos(((Vec3)arrayList.get((int)0)).field_72450_a, ((Vec3)arrayList.get((int)0)).field_72448_b, ((Vec3)arrayList.get((int)0)).field_72449_c);
        }
        return null;
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        KeyBinding.func_74506_a();
        this.setEnabled(false);
    }
}

