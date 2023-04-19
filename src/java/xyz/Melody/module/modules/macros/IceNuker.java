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
    public void onTick(EventTick event) {
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
            MovingObjectPosition fake = this.mc.objectMouseOver;
            fake.hitVec = new Vec3(this.closestIce);
            EnumFacing enumFacing = fake.sideHit;
            if (enumFacing != null && this.mc.thePlayer != null) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.closestIce, enumFacing));
            }
            this.mc.thePlayer.swingItem();
            this.broken.add(this.closestIce);
        }
        this.timer.reset();
    }

    @EventHandler
    public void renderWorld(EventRender3D event) {
        if (this.closestIce != null) {
            RenderUtil.drawSolidBlockESP(this.closestIce, Colors.MAGENTA.c, 3.5f, event.getPartialTicks());
        }
    }

    private BlockPos closestIce() {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return null;
        }
        if (((Boolean)this.fto.getValue()).booleanValue()) {
            ArrayList<BlockPos> ices = ((FrozenTreasureESP)Client.instance.getModuleManager().getModuleByClass(FrozenTreasureESP.class)).ices;
            ices.removeIf(this::lambda$closestIce$0);
            ices.sort(Comparator.comparingDouble(this::lambda$closestIce$1));
            if (!ices.isEmpty()) {
                return ices.get(0);
            }
            return null;
        }
        float r = ((Double)this.range.getValue()).floatValue();
        BlockPos playerPos = this.mc.thePlayer.getPosition().add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r, r);
        Vec3i depth = new Vec3i(r, (Double)this.depth.getValue(), r);
        ArrayList<Vec3> stones = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(depth))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (blockState.getBlock() != Blocks.ice || this.broken.contains(blockPos)) continue;
                stones.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
        }
        stones.sort(Comparator.comparingDouble(this::lambda$closestIce$2));
        if (!stones.isEmpty()) {
            return new BlockPos(((Vec3)stones.get((int)0)).xCoord, ((Vec3)stones.get((int)0)).yCoord, ((Vec3)stones.get((int)0)).zCoord);
        }
        return null;
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        KeyBinding.unPressAllKeys();
        this.setEnabled(false);
    }

    private double lambda$closestIce$2(Vec3 vec) {
        return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    private double lambda$closestIce$1(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean lambda$closestIce$0(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) > (Double)this.range.getValue();
    }
}

