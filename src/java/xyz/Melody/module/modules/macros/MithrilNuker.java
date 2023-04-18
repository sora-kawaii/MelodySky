/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public final class MithrilNuker
extends Module {
    private ArrayList<EntityPlayer> shabs = new ArrayList();
    public ArrayList<BlockPos> blockPoses = new ArrayList();
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private BlockPos blockPos;
    private BlockPos lastBlockPos = null;
    private Numbers<Double> protectRange = new Numbers<Double>("ProtectRange", 10.0, 5.0, 100.0, 0.5);
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);
    private Option<Boolean> admin = new Option<Boolean>("AntiAdmin", true);
    private Option<Boolean> wool = new Option<Boolean>("WoolOnly", false);
    private Option<Boolean> facingOnly = new Option<Boolean>("FacingOnly", false);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", true);
    private Option<Boolean> sneak = new Option<Boolean>("Sneak", true);
    private Option<Boolean> protect = new Option<Boolean>("MacroProtect", true);
    private float currentDamage = 0.0f;
    private int blockHitDelay = 0;
    private boolean tempDisable = false;
    private boolean sneaked = false;
    private IBlockState blockState;

    public MithrilNuker() {
        super("MithrilNuker", new String[]{"am"}, ModuleType.Macros);
        this.addValues(this.protectRange, this.range, this.wool, this.admin, this.facingOnly, this.sneak, this.protect, this.rot);
        this.setModInfo("Auto Mine Mithril Around You.");
    }

    @EventHandler
    private void destoryBlock(EventPreUpdate eventPreUpdate) {
        IBlockState iBlockState;
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        if (this.playerWithin20B() && ((Boolean)this.protect.getValue()).booleanValue()) {
            this.currentDamage = 0.0f;
            Helper.sendMessage("[Mithril Fucker] Player Detected in 20 Blocks, Auto Disabled.");
            this.setEnabled(false);
            return;
        }
        if (Client.pickaxeAbilityReady && this.mc.field_71442_b != null && this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c) != null) {
            this.mc.field_71442_b.func_78769_a(this.mc.field_71439_g, this.mc.field_71441_e, this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c));
            Client.pickaxeAbilityReady = false;
        }
        if (this.currentDamage > 100.0f) {
            this.currentDamage = 0.0f;
        }
        if (this.blockPos != null && this.mc.field_71441_e != null && ((iBlockState = this.mc.field_71441_e.func_180495_p(this.blockPos)).func_177230_c() == Blocks.field_150357_h || iBlockState.func_177230_c() == Blocks.field_150350_a)) {
            this.currentDamage = 0.0f;
        }
        if (this.currentDamage == 0.0f) {
            this.lastBlockPos = this.blockPos;
            this.blockPos = this.getBlock();
        }
        if (this.blockPos != null) {
            if (this.blockHitDelay > 0) {
                --this.blockHitDelay;
                return;
            }
            if (this.currentDamage == 0.0f) {
                this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            }
            this.mc.field_71439_g.func_71038_i();
            this.currentDamage += 1.0f;
        }
        if (((Boolean)this.rot.getValue()).booleanValue()) {
            float f = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float f2 = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, f, 70.0f);
            eventPreUpdate.setYaw(this.mc.field_71439_g.field_70177_z);
            this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, f2, 70.0f);
            eventPreUpdate.setPitch(this.mc.field_71439_g.field_70125_A);
        }
    }

    @EventHandler
    private void tick(EventTick eventTick) {
        if (((Boolean)this.sneak.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74311_E.func_151463_i(), (boolean)true);
        }
    }

    @Override
    public void onEnable() {
        this.yaw = this.mc.field_71439_g.field_70177_z;
        this.pitch = this.mc.field_71439_g.field_70125_A;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentDamage = 0.0f;
        this.shabs.clear();
        this.sneaked = false;
        this.tempDisable = false;
        if (((Boolean)this.sneak.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74311_E.func_151463_i(), (boolean)false);
        }
        super.onDisable();
    }

    @EventHandler
    private void onAdminDetected(EventTick eventTick) {
        if (((Boolean)this.admin.getValue()).booleanValue() && PlayerListUtils.tabContains("[ADMIN]")) {
            Helper.sendMessage("[AutoGemstone] Admin Detected, Warping to Main Lobby.");
            NotificationPublisher.queue("Admin Detected", "An Admin Joined Your Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            this.mc.field_71439_g.func_71165_d("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onAdminChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.func_76338_a((String)clientChatReceivedEvent.message.func_150260_c());
        if (!((Boolean)this.admin.getValue()).booleanValue()) {
            return;
        }
        for (String string2 : FriendManager.getFriends().keySet()) {
            if (!string.contains(string2)) continue;
            return;
        }
        if (string.startsWith("From [ADMIN]") || string.startsWith("From [GM]") || string.startsWith("From [YOUTUBE]")) {
            NotificationPublisher.queue("Admin Detected", "An Admin msged you, Quitting Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            Helper.sendMessage("[AutoFish] Admin Detected, Quitting Server.");
            boolean bl = this.mc.func_71387_A();
            this.mc.field_71441_e.func_72882_A();
            this.mc.func_71403_a(null);
            if (bl) {
                this.mc.func_147108_a(new GuiMainMenu());
            } else {
                this.mc.func_147108_a(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @EventHandler
    public void onTick(EventRender3D eventRender3D) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), eventRender3D.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.func_177958_n() + 0.5 - this.mc.field_71439_g.field_70165_t + (double)enumFacing.func_82601_c() / 2.0;
        double d2 = (double)blockPos.func_177952_p() + 0.5 - this.mc.field_71439_g.field_70161_v + (double)enumFacing.func_82599_e() / 2.0;
        double d3 = this.mc.field_71439_g.field_70163_u + (double)this.mc.field_71439_g.func_70047_e() - ((double)blockPos.func_177956_o() + 0.5);
        double d4 = MathHelper.func_76133_a((double)(d * d + d2 * d2));
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    public EnumFacing getClosestEnum(BlockPos blockPos) {
        EnumFacing enumFacing = EnumFacing.UP;
        float f = MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[0]);
        if (f >= 45.0f && f <= 135.0f) {
            enumFacing = EnumFacing.EAST;
        } else if (f >= 135.0f && f <= 180.0f || f <= -135.0f && f >= -180.0f) {
            enumFacing = EnumFacing.SOUTH;
        } else if (f <= -45.0f && f >= -135.0f) {
            enumFacing = EnumFacing.WEST;
        } else if (f >= -45.0f && f <= 0.0f || f <= 45.0f && f >= 0.0f) {
            enumFacing = EnumFacing.NORTH;
        }
        if (MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) > 75.0f || MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) < -75.0f) {
            enumFacing = EnumFacing.UP;
        }
        return enumFacing;
    }

    private BlockPos getBlock() {
        int n = ((Double)this.range.getValue()).intValue();
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3 vec3 = this.mc.field_71439_g.func_174791_d();
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            for (Object object : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                IBlockState object2 = this.mc.field_71441_e.func_180495_p((BlockPos)object);
                if (((Boolean)this.facingOnly.getValue()).booleanValue() && (RotationUtil.isInFov(this.yaw, this.pitch, object.func_177958_n(), object.func_177956_o(), object.func_177952_p()) > 230.0 || (double)object.func_177956_o() < this.mc.field_71439_g.field_70163_u) || !this.isMithril(object2)) continue;
                arrayList.add(new Vec3((double)object.func_177958_n() + 0.5, object.func_177956_o(), (double)object.func_177952_p() + 0.5));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Vec3 vec32 : arrayList) {
            arrayList2.add(new BlockPos(vec32));
        }
        this.blockPoses.clear();
        this.blockPoses.addAll(arrayList2);
        double d = 9999.0;
        Vec3 vec33 = null;
        for (int i = 0; i < arrayList.size(); ++i) {
            double d2 = ((Vec3)arrayList.get(i)).func_72438_d(vec3);
            if (!(d2 < d)) continue;
            d = d2;
            vec33 = (Vec3)arrayList.get(i);
        }
        if (vec33 != null && d < 5.0) {
            return new BlockPos(vec33.field_72450_a, vec33.field_72448_b, vec33.field_72449_c);
        }
        return null;
    }

    private boolean playerWithin20B() {
        for (EntityPlayer entityPlayer : this.mc.field_71441_e.field_73010_i) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || !(this.mc.field_71439_g.func_70032_d(entityPlayer) < 20.0f) || entityPlayer == this.mc.field_71439_g) continue;
            return true;
        }
        return false;
    }

    private boolean isMithril(IBlockState iBlockState) {
        if (!((Boolean)this.wool.getValue()).booleanValue()) {
            if (iBlockState.func_177230_c() == Blocks.field_180397_cI) {
                return true;
            }
            if (iBlockState.func_177230_c() == Blocks.field_150325_L) {
                return true;
            }
            if (iBlockState.func_177230_c() == Blocks.field_150406_ce) {
                return true;
            }
            if (iBlockState.func_177230_c() == Blocks.field_150348_b && iBlockState.func_177229_b(BlockStone.field_176247_a) == BlockStone.EnumType.DIORITE_SMOOTH) {
                return true;
            }
        } else if (iBlockState.func_177230_c() == Blocks.field_150325_L) {
            return true;
        }
        return false;
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
}

