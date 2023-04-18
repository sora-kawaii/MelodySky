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
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.QOL.FrozenTreasureESP;

public final class IceNuker
extends Module {
    private ArrayList<BlockPos> broken = new ArrayList();
    private BlockPos closestIce;
    private int ticks = 0;
    private TimerUtil timer = new TimerUtil();
    private Numbers<Double> range = new Numbers<Double>("Range", 3.8, 2.0, 6.0, 0.1);
    private Numbers<Double> depth = new Numbers<Double>("Depth", 1.0, 0.0, 4.0, 0.5);
    private Numbers<Double> bps = new Numbers<Double>("Block Per Sec.", 10.0, 1.0, 50.0, 1.0);
    private Option<Boolean> fto = new Option<Boolean>("Treasure Only", false);

    public IceNuker() {
        super("IceNuker", new String[]{""}, ModuleType.Macros);
        this.addValues(this.bps, this.depth, this.range, this.fto);
        this.setModInfo("Auto Fuck Ice");
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
        this.closestIce = this.closestIce();
        if (this.closestIce != null) {
            MovingObjectPosition movingObjectPosition = this.mc.field_71476_x;
            movingObjectPosition.field_72307_f = new Vec3(this.closestIce);
            EnumFacing enumFacing = movingObjectPosition.field_178784_b;
            if (enumFacing != null && this.mc.field_71439_g != null) {
                this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.closestIce, enumFacing));
            }
            this.mc.field_71439_g.func_71038_i();
            this.broken.add(this.closestIce);
        }
        this.timer.reset();
    }

    @EventHandler
    public void renderWorld(EventRender3D eventRender3D) {
        if (this.closestIce != null) {
            RenderUtil.drawSolidBlockESP(this.closestIce, Colors.MAGENTA.c, 3.5f, eventRender3D.getPartialTicks());
        }
    }

    private BlockPos closestIce() {
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            return null;
        }
        if (((Boolean)this.fto.getValue()).booleanValue()) {
            ArrayList<BlockPos> arrayList = ((FrozenTreasureESP)Client.instance.getModuleManager().getModuleByClass(FrozenTreasureESP.class)).ices;
            arrayList.removeIf(blockPos -> this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()) > (Double)this.range.getValue());
            arrayList.sort(Comparator.comparingDouble(blockPos -> this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p())));
            if (!arrayList.isEmpty()) {
                return arrayList.get(0);
            }
            return null;
        }
        float f = ((Double)this.range.getValue()).floatValue();
        BlockPos blockPos2 = this.mc.field_71439_g.func_180425_c().func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(f, f, f);
        Vec3i vec3i2 = new Vec3i(f, (Double)this.depth.getValue(), f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos2 != null) {
            for (BlockPos blockPos3 : BlockPos.func_177980_a((BlockPos)blockPos2.func_177971_a(vec3i), (BlockPos)blockPos2.func_177973_b(vec3i2))) {
                IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos3);
                if (iBlockState.func_177230_c() != Blocks.field_150432_aD || this.broken.contains(blockPos3)) continue;
                arrayList.add(new Vec3((double)blockPos3.func_177958_n() + 0.5, blockPos3.func_177956_o(), (double)blockPos3.func_177952_p() + 0.5));
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

