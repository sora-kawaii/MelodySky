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
            MovingObjectPosition movingObjectPosition = this.mc.objectMouseOver;
            movingObjectPosition.hitVec = new Vec3(this.closestIce);
            EnumFacing enumFacing = movingObjectPosition.sideHit;
            if (enumFacing != null && this.mc.thePlayer != null) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.closestIce, enumFacing));
            }
            this.mc.thePlayer.swingItem();
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
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return null;
        }
        if (((Boolean)this.fto.getValue()).booleanValue()) {
            ArrayList<BlockPos> arrayList = ((FrozenTreasureESP)Client.instance.getModuleManager().getModuleByClass(FrozenTreasureESP.class)).ices;
            arrayList.removeIf(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > (Double)this.range.getValue());
            arrayList.sort(Comparator.comparingDouble(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
            if (!arrayList.isEmpty()) {
                return arrayList.get(0);
            }
            return null;
        }
        float f = ((Double)this.range.getValue()).floatValue();
        BlockPos blockPos2 = this.mc.thePlayer.getPosition().add(0, 1, 0);
        Vec3i vec3i = new Vec3i(f, f, f);
        Vec3i vec3i2 = new Vec3i(f, (Double)this.depth.getValue(), f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos2 != null) {
            for (BlockPos blockPos3 : BlockPos.getAllInBox(blockPos2.add(vec3i), blockPos2.subtract(vec3i2))) {
                IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos3);
                if (iBlockState.getBlock() != Blocks.ice || this.broken.contains(blockPos3)) continue;
                arrayList.add(new Vec3((double)blockPos3.getX() + 0.5, blockPos3.getY(), (double)blockPos3.getZ() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!arrayList.isEmpty()) {
            return new BlockPos(((Vec3)arrayList.get((int)0)).xCoord, ((Vec3)arrayList.get((int)0)).yCoord, ((Vec3)arrayList.get((int)0)).zCoord);
        }
        return null;
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        KeyBinding.unPressAllKeys();
        this.setEnabled(false);
    }
}

