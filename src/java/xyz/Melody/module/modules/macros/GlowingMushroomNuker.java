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
    private void onRotation(EventTick event) {
        this.updateClothest();
        if (this.clothestMushroom != null) {
            MovingObjectPosition fake = this.mc.objectMouseOver;
            fake.hitVec = new Vec3(this.clothestMushroom);
            EnumFacing enumFacing = fake.sideHit;
            if (enumFacing != null && this.mc.thePlayer != null) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.clothestMushroom, enumFacing));
            }
            this.mc.thePlayer.swingItem();
            this.mushrooms.remove(this.clothestMushroom);
            this.clothestMushroom = null;
        }
    }

    @EventHandler
    private void onBlockDestory(BlockChangeEvent event) {
        if (this.mushrooms.contains(event.getPosition())) {
            this.mushrooms.remove(event.getPosition());
        }
    }

    @EventHandler
    public void receivePacket(EventPacketRecieve event) {
        Vec3 particlePos;
        BlockPos b;
        S2APacketParticles packet;
        if (event.getPacket() instanceof S2APacketParticles && (packet = (S2APacketParticles)event.getPacket()).getParticleType() == EnumParticleTypes.SPELL_MOB && (this.mc.theWorld.getBlockState(b = new BlockPos(particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate()))).getBlock() == Blocks.red_mushroom || this.mc.theWorld.getBlockState(b).getBlock() == Blocks.brown_mushroom) && !this.mushrooms.contains(b)) {
            this.mushrooms.add(b);
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
    public void onR3D(EventRender3D event) {
        for (BlockPos pos : this.mushrooms) {
            RenderUtil.drawSolidBlockESP(pos, Colors.MAGENTA.c, event.getPartialTicks());
        }
        if (this.clothestMushroom != null) {
            RenderUtil.drawSolidBlockESP(this.clothestMushroom, Colors.GREEN.c, event.getPartialTicks());
        }
    }

    private void updateClothest() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mushrooms.isEmpty()) {
            return;
        }
        this.mushrooms.sort(Comparator.comparingDouble(this::lambda$updateClothest$0));
        BlockPos pos = this.mushrooms.get(0);
        if (this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) < (Double)this.range.getValue()) {
            this.clothestMushroom = pos;
            return;
        }
        this.clothestMushroom = null;
    }

    private double lambda$updateClothest$0(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }
}

