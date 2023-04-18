/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CropNuker
extends Module {
    private BlockPos crop = null;
    private final ArrayList<BlockPos> broken = new ArrayList();
    private TimerUtil timer = new TimerUtil();
    private Mode<Enum> mode = new Mode("Mode", (Enum[])crops.values(), (Enum)crops.Cane);
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);

    public CropNuker() {
        super("CropNuker", new String[]{"gn"}, ModuleType.Macros);
        this.addValues(this.mode, this.range);
        this.setModInfo("Auto Break Crops Around You.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.crop = null;
        this.broken.clear();
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        if (this.mc.field_71439_g == null) {
            this.broken.clear();
            return;
        }
        if (this.timer.hasReached(30000.0)) {
            this.broken.clear();
            this.timer.reset();
        }
        this.crop = this.closestCrop();
        if (this.crop != null) {
            this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.crop, EnumFacing.DOWN));
            this.mc.field_71439_g.func_71038_i();
            this.broken.add(this.crop);
        }
    }

    @EventHandler
    public void onTick(EventRender3D eventRender3D) {
        if (this.crop != null) {
            RenderUtil.drawSolidBlockESP(this.crop, Colors.MAGENTA.c, eventRender3D.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private BlockPos closestCrop() {
        if (this.mc.field_71441_e == null) {
            return null;
        }
        double d = (Double)this.range.getValue();
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3 vec3 = this.mc.field_71439_g.func_174791_d();
        Vec3i vec3i = new Vec3i(d, 2.0, d);
        Vec3i vec3i2 = new Vec3i(d, 0.0, d);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            switch (((Enum)this.mode.getValue()).toString().toLowerCase()) {
                case "all": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState object3 = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (object3.func_177230_c() != Blocks.field_150388_bm && object3.func_177230_c() != Blocks.field_150469_bN && object3.func_177230_c() != Blocks.field_150464_aj && object3.func_177230_c() != Blocks.field_150459_bM && object3.func_177230_c() != Blocks.field_150423_aK && object3.func_177230_c() != Blocks.field_150440_ba && object3.func_177230_c() != Blocks.field_150338_P && object3.func_177230_c() != Blocks.field_150337_Q && object3.func_177230_c() != Blocks.field_150375_by && object3.func_177230_c() != Blocks.field_150434_aF && object3.func_177230_c() != Blocks.field_150436_aH || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "cane": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i2), (BlockPos)blockPos.func_177973_b(vec3i2))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150436_aH || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "cactus": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i2), (BlockPos)blockPos.func_177973_b(vec3i2))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150434_aF || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "netherwart": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150388_bm || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "wheat": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150464_aj || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "carrot": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150459_bM || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "potato": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150469_bN || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "pumpkin": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150423_aK || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "melon": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150440_ba || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "mushroom": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150338_P && iBlockState.func_177230_c() != Blocks.field_150337_Q || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
                case "cocoa": {
                    for (Object object2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                        IBlockState iBlockState = this.mc.field_71441_e.func_180495_p((BlockPos)object2);
                        if (iBlockState.func_177230_c() != Blocks.field_150375_by || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.func_177958_n() + 0.5, object2.func_177956_o(), (double)object2.func_177952_p() + 0.5));
                    }
                    break;
                }
            }
        }
        double d2 = 9999.0;
        Object object = null;
        for (Vec3 vec32 : arrayList) {
            double d3 = vec32.func_72438_d(vec3);
            if (!(d3 < d2)) continue;
            d2 = d3;
            object = vec32;
        }
        if (object != null && d2 < 5.0) {
            return new BlockPos(((Vec3)object).field_72450_a, ((Vec3)object).field_72448_b, ((Vec3)object).field_72449_c);
        }
        return null;
    }

    private static enum crops {
        ALL,
        Cane,
        Cactus,
        NetherWart,
        Wheat,
        Carrot,
        Potato,
        Pumpkin,
        Melon,
        Mushroom,
        Cocoa;

    }
}

