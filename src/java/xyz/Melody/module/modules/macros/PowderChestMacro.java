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
    private void onUpdate(EventPreUpdate eventPreUpdate) {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
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
    private void onRotation(EventPreUpdate eventPreUpdate) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (chest != null) {
            if (nextRotation != null) {
                float f = this.vec3ToRotation((Vec3)PowderChestMacro.nextRotation).yaw;
                float f2 = this.vec3ToRotation((Vec3)PowderChestMacro.nextRotation).pitch;
                float f3 = ((Double)this.velocity.getValue()).floatValue();
                if (((Boolean)this.silent.getValue()).booleanValue()) {
                    this.silentYaw = this.smoothRotation(this.silentYaw, f, f3);
                    eventPreUpdate.setYaw(this.silentYaw);
                    this.silentPitch = this.smoothRotation(this.silentPitch, f2, f3);
                    eventPreUpdate.setPitch(this.silentPitch);
                    return;
                }
                this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, f, f3);
                this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, f2, f3);
                return;
            }
        }
        this.silentYaw = this.mc.field_71439_g.field_70177_z;
        this.silentPitch = this.mc.field_71439_g.field_70125_A;
    }

    @EventHandler
    public void receivePacket(EventPacketRecieve eventPacketRecieve) {
        S2APacketParticles s2APacketParticles;
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (eventPacketRecieve.getPacket() instanceof S2APacketParticles && (s2APacketParticles = (S2APacketParticles)eventPacketRecieve.getPacket()).func_179749_a().equals((Object)EnumParticleTypes.CRIT)) {
            double d;
            Vec3 vec3 = new Vec3(s2APacketParticles.func_149220_d(), s2APacketParticles.func_149226_e(), s2APacketParticles.func_149225_f());
            if (chest != null && (d = chest.func_72438_d(vec3)) < 1.0) {
                nextRotation = vec3;
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
    public void onR3D(EventRender3D eventRender3D) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (TileEntity tileEntity : this.mc.field_71441_e.field_147482_g) {
            if (!(tileEntity instanceof TileEntityChest)) continue;
            TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            RenderUtil.drawSolidBlockESP(tileEntityChest.func_174877_v(), Colors.BLUE.c, eventRender3D.getPartialTicks());
        }
        if (this.getChestPos() != null) {
            RenderUtil.drawSolidBlockESP(this.getChestPos(), Colors.ORANGE.c, eventRender3D.getPartialTicks());
        }
    }

    private Vec3 getChest() {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }
        for (TileEntity tileEntity : this.mc.field_71441_e.field_147482_g) {
            if (!(tileEntity instanceof TileEntityChest)) continue;
            TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            Vec3 vec32 = new Vec3((float)tileEntityChest.func_174877_v().func_177958_n() + 0.5f, tileEntityChest.func_174877_v().func_177956_o(), (float)tileEntityChest.func_174877_v().func_177952_p() + 0.5f);
            BlockPos blockPos = tileEntityChest.func_174877_v();
            if (done.contains(blockPos) || !(this.mc.field_71439_g.func_70011_f(tileEntityChest.func_174877_v().func_177958_n(), tileEntityChest.func_174877_v().func_177956_o(), tileEntityChest.func_174877_v().func_177952_p()) < 4.0)) continue;
            arrayList.add(vec32);
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.field_71439_g.func_70011_f(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c)));
        if (!arrayList.isEmpty()) {
            return (Vec3)arrayList.get(0);
        }
        return null;
    }

    private BlockPos getChestPos() {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        BlockPos blockPos = null;
        if (chest != null) {
            blockPos = new BlockPos(chest);
        }
        return blockPos;
    }

    private float smoothRotation(float f, float f2, float f3) {
        float f4 = MathHelper.func_76142_g((float)(f2 - f));
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4 / 2.0f;
    }

    public Rotation vec3ToRotation(Vec3 vec3) {
        double d = vec3.field_72450_a - this.mc.field_71439_g.field_70165_t;
        double d2 = vec3.field_72448_b - this.mc.field_71439_g.field_70163_u - (double)this.mc.field_71439_g.func_70047_e();
        double d3 = vec3.field_72449_c - this.mc.field_71439_g.field_70161_v;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(-Math.atan2(d4, d2));
        float f2 = (float)Math.atan2(d3, d);
        f = (float)PowderChestMacro.wrapAngleTo180(((double)(f * 180.0f) / Math.PI + 90.0) * -1.0);
        f2 = (float)PowderChestMacro.wrapAngleTo180((double)(f2 * 180.0f) / Math.PI - 90.0);
        return new Rotation(f, f2);
    }

    private static double wrapAngleTo180(double d) {
        return d - Math.floor(d / 360.0 + 0.5) * 360.0;
    }

    private static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float f, float f2) {
            this.pitch = f;
            this.yaw = f2;
        }
    }
}

