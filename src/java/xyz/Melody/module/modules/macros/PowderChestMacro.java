/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class PowderChestMacro
extends Module {
    public static Vec3 nextRotation = null;
    public static ArrayList<BlockPos> done = new ArrayList();
    public static Option<Boolean> autoClear = new Option<Boolean>("AutoClear", true);
    public Option<Boolean> silent = new Option<Boolean>("Silent", false);
    private Numbers<Double> velocity = new Numbers<Double>("Speed", 50.0, 30.0, 100.0, 1.0);
    public static Vec3 chest;
    public static BlockPos chestPos;
    private float silentYaw;
    private float silentPitch;

    public PowderChestMacro() {
        super("PowderChest", new String[]{"chest"}, ModuleType.Macros);
        this.addValues(autoClear, this.silent, this.velocity);
        this.setModInfo("Auto Unlock Powder Chests.");
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        chest = this.getChest();
        chestPos = this.getChestPos();
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventHandler
    private void onRotation(EventPreUpdate event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (chest != null) {
            if (nextRotation != null) {
                float yaw = this.vec3ToRotation((Vec3)PowderChestMacro.nextRotation).yaw;
                float pitch = this.vec3ToRotation((Vec3)PowderChestMacro.nextRotation).pitch;
                float speed = ((Double)this.velocity.getValue()).floatValue();
                if (((Boolean)this.silent.getValue()).booleanValue()) {
                    this.silentYaw = this.smoothRotation(this.silentYaw, yaw, speed);
                    event.setYaw(this.silentYaw);
                    this.silentPitch = this.smoothRotation(this.silentPitch, pitch, speed);
                    event.setPitch(this.silentPitch);
                    return;
                }
                this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, speed);
                this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, speed);
                return;
            }
        }
        this.silentYaw = this.mc.thePlayer.rotationYaw;
        this.silentPitch = this.mc.thePlayer.rotationPitch;
    }

    @EventHandler
    public void receivePacket(EventPacketRecieve event) {
        S2APacketParticles packet;
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (event.getPacket() instanceof S2APacketParticles && (packet = (S2APacketParticles)event.getPacket()).getParticleType().equals((Object)EnumParticleTypes.CRIT)) {
            double dist;
            Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
            if (chest != null && (dist = chest.distanceTo(particlePos)) < 1.0) {
                nextRotation = particlePos;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        done.clear();
        super.onDisable();
    }

    @EventHandler
    public void onR3D(EventRender3D event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (TileEntity entity : this.mc.theWorld.loadedTileEntityList) {
            if (!(entity instanceof TileEntityChest)) continue;
            TileEntityChest chouShaBi = (TileEntityChest)entity;
            RenderUtil.drawSolidBlockESP(chouShaBi.getPos(), Colors.BLUE.c, event.getPartialTicks());
        }
        if (this.getChestPos() != null) {
            RenderUtil.drawSolidBlockESP(this.getChestPos(), Colors.ORANGE.c, event.getPartialTicks());
        }
    }

    private Vec3 getChest() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        if (!chests.isEmpty()) {
            chests.clear();
        }
        for (TileEntity entity : this.mc.theWorld.loadedTileEntityList) {
            if (!(entity instanceof TileEntityChest)) continue;
            TileEntityChest chest = (TileEntityChest)entity;
            Vec3 chestVec = new Vec3((float)chest.getPos().getX() + 0.5f, chest.getPos().getY(), (float)chest.getPos().getZ() + 0.5f);
            BlockPos chestPos = chest.getPos();
            if (done.contains(chestPos) || !(this.mc.thePlayer.getDistance(chest.getPos().getX(), chest.getPos().getY(), chest.getPos().getZ()) < 4.0)) continue;
            chests.add(chestVec);
        }
        chests.sort(Comparator.comparingDouble(this::lambda$getChest$0));
        if (!chests.isEmpty()) {
            return (Vec3)chests.get(0);
        }
        return null;
    }

    private BlockPos getChestPos() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos chest = null;
        if (PowderChestMacro.chest != null) {
            chest = new BlockPos(PowderChestMacro.chest);
        }
        return chest;
    }

    private float smoothRotation(float current, float target, float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(target - current);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return current + deltaAngle / 2.0f;
    }

    public Rotation vec3ToRotation(Vec3 vec) {
        double diffX = vec.xCoord - this.mc.thePlayer.posX;
        double diffY = vec.yCoord - this.mc.thePlayer.posY - (double)this.mc.thePlayer.getEyeHeight();
        double diffZ = vec.zCoord - this.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float pitch = (float)(-Math.atan2(dist, diffY));
        float yaw = (float)Math.atan2(diffZ, diffX);
        pitch = (float)PowderChestMacro.wrapAngleTo180(((double)(pitch * 180.0f) / Math.PI + 90.0) * -1.0);
        yaw = (float)PowderChestMacro.wrapAngleTo180((double)(yaw * 180.0f) / Math.PI - 90.0);
        return new Rotation(pitch, yaw);
    }

    private static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360.0 + 0.5) * 360.0;
    }

    private double lambda$getChest$0(Vec3 vec) {
        return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    private static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }
}

