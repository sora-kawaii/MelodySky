/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class GlowingMushroomNuker
extends Module {
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);
    private ArrayList<BlockPos> mushrooms = new ArrayList();
    private BlockPos clothestMushroom;

    public GlowingMushroomNuker() {
        super("GlowingMushroomNuker", new String[]{"chest"}, ModuleType.Macros);
        this.addValues(this.range);
        this.setModInfo("Auto Mine Glowing Mushrooms.");
    }

    @EventHandler
    private void onRotation(EventTick eventTick) {
        this.updateClothest();
        if (this.clothestMushroom != null) {
            MovingObjectPosition movingObjectPosition = this.mc.objectMouseOver;
            movingObjectPosition.hitVec = new Vec3(this.clothestMushroom);
            EnumFacing enumFacing = movingObjectPosition.sideHit;
            if (enumFacing != null && this.mc.thePlayer != null) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.clothestMushroom, enumFacing));
            }
            this.mc.thePlayer.swingItem();
            this.mushrooms.remove(this.clothestMushroom);
            this.clothestMushroom = null;
        }
    }

    @EventHandler
    private void onBlockDestory(BlockChangeEvent blockChangeEvent) {
        if (this.mushrooms.contains(blockChangeEvent.getPosition())) {
            this.mushrooms.remove(blockChangeEvent.getPosition());
        }
    }

    @EventHandler
    public void receivePacket(EventPacketRecieve eventPacketRecieve) {
        Vec3 vec3;
        BlockPos blockPos;
        S2APacketParticles s2APacketParticles;
        if (eventPacketRecieve.getPacket() instanceof S2APacketParticles && (s2APacketParticles = (S2APacketParticles)eventPacketRecieve.getPacket()).getParticleType() == EnumParticleTypes.SPELL_MOB && (this.mc.theWorld.getBlockState(blockPos = new BlockPos(vec3 = new Vec3(s2APacketParticles.getXCoordinate(), s2APacketParticles.getYCoordinate(), s2APacketParticles.getZCoordinate()))).getBlock() == Blocks.red_mushroom || this.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.brown_mushroom) && !this.mushrooms.contains(blockPos)) {
            this.mushrooms.add(blockPos);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.mushrooms.clear();
        super.onDisable();
    }

    @EventHandler
    public void onR3D(EventRender3D eventRender3D) {
        for (BlockPos blockPos : this.mushrooms) {
            RenderUtil.drawSolidBlockESP(blockPos, Colors.MAGENTA.c, eventRender3D.getPartialTicks());
        }
        if (this.clothestMushroom != null) {
            RenderUtil.drawSolidBlockESP(this.clothestMushroom, Colors.GREEN.c, eventRender3D.getPartialTicks());
        }
    }

    private void updateClothest() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mushrooms.isEmpty()) {
            return;
        }
        this.mushrooms.sort(Comparator.comparingDouble(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        BlockPos blockPos2 = this.mushrooms.get(0);
        if (this.mc.thePlayer.getDistance(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ()) < (Double)this.range.getValue()) {
            this.clothestMushroom = blockPos2;
            return;
        }
        this.clothestMushroom = null;
    }
}

