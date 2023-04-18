/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.Fishing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.modules.others.PlayerList;

public final class AutoFish
extends Module {
    private int tickTimer = 0;
    private int tickTimer1 = 0;
    private int dickTimer = 0;
    private Vec3 soundVec = null;
    private Option<Boolean> plist = new Option<Boolean>("EnablePlayerList", true);
    private Option<Boolean> unGrab = new Option<Boolean>("UnGrabMouse", false);
    private Option<Boolean> lockRod = new Option<Boolean>("LockRod", false);
    private Option<Boolean> admin = new Option<Boolean>("AntiAdmin", false);
    private Option<Boolean> lockView = new Option<Boolean>("LockView", false);
    private Option<Boolean> dead = new Option<Boolean>("DeathCheck", true);
    private Option<Boolean> waterCheck = new Option<Boolean>("Water/Lava Check", true);
    private Option<Boolean> escape = new Option<Boolean>("Escape", false);
    private Numbers<Double> escapeRange = new Numbers<Double>("Escape Range", 5.0, 0.0, 20.0, 1.0);
    private Option<Boolean> kill = new Option<Boolean>("AutoKill", false);
    private Option<Boolean> rckill = new Option<Boolean>("RightClickKill", false);
    private Numbers<Double> killRange = new Numbers<Double>("KillRange", 3.0, 0.0, 4.2, 0.1);
    private Numbers<Double> rccd = new Numbers<Double>("RcDelay(ms)", 2500.0, 100.0, 5000.0, 100.0);
    private Numbers<Double> angleDiff = new Numbers<Double>("AngleDiff", 1.0E-4, 0.0, 0.1, 1.0E-4);
    private Numbers<Double> angleSize = new Numbers<Double>("AngleSize", 60.0, 10.0, 100.0, 5.0);
    private Numbers<Double> killSize = new Numbers<Double>("ScKillSize", 1.0, 1.0, 20.0, 1.0);
    private Option<Boolean> autoThrow = new Option<Boolean>("AutoThrow", false);
    private Option<Boolean> showDebug = new Option<Boolean>("Show Debug", false);
    private Option<Boolean> packetDebug = new Option<Boolean>("PacketDebug", false);
    private Mode<Enum> rotationMode = new Mode("RotationMode", (Enum[])rotations.values(), (Enum)rotations.Yaw);
    private Option<Boolean> rotation = new Option<Boolean>("NoRotationAFK", true);
    private Mode<Enum> moveMode = new Mode("MoveMode", (Enum[])moves.values(), (Enum)moves.AD);
    private Option<Boolean> move = new Option<Boolean>("NoMovingAFK", true);
    private Option<Boolean> holdShift = new Option<Boolean>("Sneaking", false);
    private Option<Boolean> randomDelay = new Option<Boolean>("RandomDelay", true);
    private Numbers<Double> angle = new Numbers<Double>("RotationAngle", 1.0, 1.0, 5.0, 1.0);
    private Numbers<Double> tickTimerVale = new Numbers<Double>("TickTimer", 80.0, 20.0, 200.0, 10.0);
    private Option<Boolean> soundBB = new Option<Boolean>("SoundBox", false);
    private Numbers<Double> soundRadius = new Numbers<Double>("SoundRadius", 0.5, 0.1, 5.0, 0.1);
    private Option<Boolean> squid = new Option<Boolean>("KillSquids", true);
    private Option<Boolean> guard = new Option<Boolean>("KillGuardians", true);
    private Option<Boolean> skeleton = new Option<Boolean>("KillSkeletons", true);
    private Option<Boolean> zombie = new Option<Boolean>("KillZombies", true);
    private Option<Boolean> witch = new Option<Boolean>("KillWitches", true);
    private Option<Boolean> cat = new Option<Boolean>("KillOcelots", true);
    private Option<Boolean> silverfish = new Option<Boolean>("KillSilverFishes", true);
    private Option<Boolean> golem = new Option<Boolean>("KillGolems", true);
    private Option<Boolean> rabbit = new Option<Boolean>("KillRabbits", true);
    private Option<Boolean> sheep = new Option<Boolean>("KillSheeps", true);
    private Option<Boolean> endermite = new Option<Boolean>("KillEnderMites", true);
    private Option<Boolean> blaze = new Option<Boolean>("KillBlazes", true);
    private Option<Boolean> pigman = new Option<Boolean>("KillPigmans", true);
    private Option<Boolean> horse = new Option<Boolean>("KillHorses", true);
    private Option<Boolean> player = new Option<Boolean>("KillOthers", true);
    private Enum<?> currentStage = stage.NONE;
    private boolean backRotaion = false;
    private boolean soundReady = false;
    private boolean soundCDReady = false;
    private boolean motionReady = false;
    private int extraDelay = 0;
    private boolean delaySet = false;
    private List<Entity> allSCNear = new ArrayList<Entity>();
    private EntityLivingBase currentSC = null;
    private TimerUtil attackTimer = new TimerUtil();
    private TimerUtil rightClickTimer = new TimerUtil();
    private boolean reachedSize = false;
    private int lcIndex = 0;
    private boolean yawRecorded = false;
    private boolean pitchRecorded = false;
    private float lastRotationYaw = 0.0f;
    private float lastRotationPitch = 0.0f;
    private boolean yawRestored = true;
    private boolean pitchRestored = true;
    private float yawDiff = 0.0f;
    private float pitchDiff = 0.0f;
    private boolean shouldSwitchToWeapon = false;
    private boolean shouldSwitchToRod = false;
    private boolean switchedToRod = false;
    private Vec3 lockedVec = new Vec3(0.0, 0.0, 0.0);
    private TimerUtil reThrowTimer = new TimerUtil();
    private TimerUtil moveTimer = new TimerUtil();
    private boolean moveDone = false;
    private boolean moved = false;
    private boolean moveBack = false;
    private boolean needToEscape = false;
    private TimerUtil escapeDelay = new TimerUtil();
    private EntityPlayer playerCaused;
    private boolean escaped = false;

    public AutoFish() {
        super("AutoFish", new String[]{"af", "fishing", "fish"}, ModuleType.Fishing);
        this.addValues(this.plist, this.unGrab, this.lockRod, this.lockView, this.dead, this.admin, this.waterCheck, this.escape, this.escapeRange, this.autoThrow, this.showDebug, this.packetDebug, this.rotationMode, this.rotation, this.moveMode, this.move, this.holdShift, this.randomDelay, this.angle, this.tickTimerVale, this.soundBB, this.soundRadius, this.angleDiff, this.angleSize, this.kill, this.rckill, this.rccd, this.killSize, this.killRange, this.squid, this.guard, this.skeleton, this.zombie, this.witch, this.cat, this.silverfish, this.golem, this.rabbit, this.sheep, this.endermite, this.blaze, this.pigman, this.horse, this.player);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Just Auto Fish.");
    }

    @Override
    public void onEnable() {
        PlayerList playerList;
        if (((Boolean)this.unGrab.getValue()).booleanValue()) {
            Client.ungrabMouse();
        }
        if (!(playerList = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class)).isEnabled() && ((Boolean)this.plist.getValue()).booleanValue()) {
            playerList.setEnabled(true);
        }
        if (this.mc.field_71476_x != null && this.mc.field_71476_x.field_72308_g == null) {
            this.lockedVec = this.mc.field_71476_x.field_72307_f;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        PlayerList playerList;
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74311_E.func_151463_i(), (boolean)false);
        }
        if (((Boolean)this.unGrab.getValue()).booleanValue()) {
            Client.regrabMouse();
        }
        if ((playerList = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class)).isEnabled() && ((Boolean)this.plist.getValue()).booleanValue()) {
            playerList.setEnabled(false);
        }
        this.playerCaused = null;
        this.needToEscape = false;
        this.lockedVec = new Vec3(0.0, 0.0, 0.0);
        this.reThrowTimer.reset();
        this.moveDone = false;
        this.escaped = false;
        this.tickTimer = 0;
        this.tickTimer1 = 0;
        this.dickTimer = 0;
        this.soundVec = null;
        this.currentStage = stage.NONE;
        this.backRotaion = false;
        this.soundReady = false;
        this.soundCDReady = false;
        this.motionReady = false;
        this.extraDelay = 0;
        this.delaySet = false;
        this.currentSC = null;
        this.yawRecorded = false;
        this.pitchRecorded = false;
        this.lastRotationYaw = 0.0f;
        this.lastRotationPitch = 0.0f;
        this.shouldSwitchToWeapon = false;
        this.shouldSwitchToRod = false;
        this.yawRestored = true;
        this.pitchRestored = true;
        this.moveBack = false;
        this.switchedToRod = false;
        this.reachedSize = false;
        this.attackTimer.reset();
        this.rightClickTimer.reset();
        this.escapeDelay.reset();
        this.escaped = false;
        super.onDisable();
    }

    private boolean shouldAttack() {
        return this.attackTimer.hasReached(1000.0 / (8.0 + MathUtil.randomDouble(-1.0, 1.0)));
    }

    @EventHandler
    private void onPlayerDetected(EventTick eventTick) {
        if (this.playerInRange() != null) {
            this.playerCaused = this.playerInRange();
            this.needToEscape = true;
        }
        if (((Boolean)this.escape.getValue()).booleanValue() && !this.escaped) {
            if (this.needToEscape && this.escapeDelay.hasReached(3000.0)) {
                WindowsNotification.show("MelodySky - AutoFish", "Escaped. Player Name: " + this.playerCaused.func_70005_c_() + ".");
                Helper.sendMessage("[AutoFish] Player Detected, Warping to Main Lobby.");
                Helper.sendMessage("[AutoFish] Player Name: " + this.playerCaused.func_70005_c_() + ".");
                this.mc.field_71439_g.func_71165_d("/l");
                this.escaped = true;
                this.setEnabled(false);
                this.escapeDelay.reset();
            }
            if (!this.needToEscape) {
                this.escapeDelay.reset();
            }
        }
        if (((Boolean)this.admin.getValue()).booleanValue() && PlayerListUtils.tabContains("[ADMIN]") && this.allSCNear.isEmpty()) {
            Helper.sendMessage("[AutoGemstone] Admin Detected, Warping to Main Lobby.");
            NotificationPublisher.queue("Admin Detected", "An Admin Joined Your Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            this.mc.field_71439_g.func_71165_d("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
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
    private void onDead(EventTick eventTick) {
        if (!((Boolean)this.dead.getValue()).booleanValue()) {
            return;
        }
        if (!this.mc.field_71439_g.func_70089_S() || this.mc.field_71439_g.field_70128_L) {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.isDead, Disabled AutoFish.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.field_71439_g.field_70173_aa <= 1) {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.tickExisted <= 1, Disabled AutoFish.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.field_71439_g.func_110143_aJ() == 0.0f) {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.getHealth() == 0, Disabled AutoFish.");
            this.setEnabled(false);
            return;
        }
    }

    @EventHandler
    private void onReThrow(EventTick eventTick) {
        if (!((Boolean)this.waterCheck.getValue()).booleanValue()) {
            return;
        }
        if (this.reachedSize || !this.pitchRestored || !this.yawRestored) {
            return;
        }
        if (this.mc.field_71439_g.func_70694_bm() != null && this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemFishingRod) {
            if (this.mc.field_71439_g.field_71104_cf != null) {
                if (!this.mc.field_71439_g.field_71104_cf.func_70090_H() && !this.mc.field_71439_g.field_71104_cf.func_180799_ab() && this.reThrowTimer.hasReached(10000.0)) {
                    Client.rightClick();
                    this.currentStage = stage.NONE;
                    this.reThrowTimer.reset();
                }
            } else {
                this.reThrowTimer.reset();
            }
        }
    }

    @EventHandler
    private void onLockRod(EventTick eventTick) {
        if (!((Boolean)this.lockRod.getValue()).booleanValue()) {
            return;
        }
        if (this.reachedSize || !this.pitchRestored || !this.yawRestored) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemFishingRod) || !this.yawRestored || !this.pitchRestored) continue;
            this.mc.field_71439_g.field_71071_by.field_70461_c = i;
            break;
        }
    }

    @EventHandler
    private void lockView(EventRender2D eventRender2D) {
        if (!((Boolean)this.lockView.getValue()).booleanValue()) {
            return;
        }
        if (this.reachedSize || !this.pitchRestored || !this.yawRestored) {
            return;
        }
        if (this.currentStage != stage.NONE) {
            Rotation rotation = this.vec3ToRotation(this.lockedVec);
            this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, rotation.yaw, 30.0f);
            this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, rotation.pitch, 30.0f);
        }
    }

    @EventHandler
    private void onKillSC(EventPreUpdate eventPreUpdate) {
        float f;
        float f2;
        if (!((Boolean)this.kill.getValue()).booleanValue()) {
            return;
        }
        this.loadSCs();
        if (!this.needToEscape) {
            if (((Double)this.killSize.getValue()).intValue() > 1) {
                if (this.allSCNear.size() >= ((Double)this.killSize.getValue()).intValue()) {
                    this.reachedSize = true;
                }
            } else {
                this.reachedSize = true;
            }
        } else {
            this.reachedSize = true;
        }
        if (this.currentSC == null && !this.allSCNear.isEmpty()) {
            this.currentSC = (EntityLivingBase)this.allSCNear.get(0);
        }
        if (this.currentSC != null && this.shouldSwitchToWeapon && this.reachedSize) {
            this.mc.field_71439_g.field_71071_by.field_70461_c = 0;
            this.mc.field_71439_g.field_71174_a.func_147297_a(new C09PacketHeldItemChange(0));
            this.rightClickTimer.reset();
            this.attackTimer.reset();
            this.shouldSwitchToWeapon = false;
        }
        float f3 = ((Double)this.angleSize.getValue()).floatValue() * 3.0f;
        if (this.currentSC != null && this.shouldAttack() && this.reachedSize) {
            if (this.mc.field_71439_g.field_71071_by.field_70461_c != 0) {
                this.shouldSwitchToWeapon = true;
            } else if (this.mc.field_71439_g.field_71071_by.field_70461_c == 0) {
                this.switchedToRod = false;
                if (!((Boolean)this.rckill.getValue()).booleanValue()) {
                    this.attack(this.currentSC);
                }
                if (((Boolean)this.rckill.getValue()).booleanValue()) {
                    if (this.rightClickTimer.hasReached(((Double)this.rccd.getValue()).longValue()) && this.mc.field_71439_g.field_71071_by.field_70461_c == 0) {
                        Client.rightClick();
                        this.rightClickTimer.reset();
                    }
                    if (!this.yawRecorded) {
                        this.lastRotationYaw = this.mc.field_71439_g.field_70177_z;
                        this.yawRestored = false;
                        this.yawRecorded = true;
                    }
                    if (!this.pitchRecorded) {
                        this.lastRotationPitch = this.mc.field_71439_g.field_70125_A;
                        this.pitchRestored = false;
                        this.pitchRecorded = true;
                    }
                    if (this.yawRecorded && this.pitchRecorded) {
                        f2 = RotationUtil.getRotationToEntity(this.currentSC)[0];
                        f = RotationUtil.getRotationToEntity(this.currentSC)[1];
                        this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, f2, f3);
                        this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, f, f3);
                    }
                }
            }
        }
        f2 = ((Double)this.angleDiff.getValue()).floatValue() * 10.0f;
        if (this.currentSC == null && this.allSCNear.isEmpty()) {
            if (this.yawRecorded) {
                this.yawDiff = Math.abs(this.mc.field_71439_g.field_70177_z - this.lastRotationYaw);
                if (Math.abs(this.mc.field_71439_g.field_70177_z - this.lastRotationYaw) > 360.0f - f2) {
                    this.mc.field_71439_g.field_70177_z = this.lastRotationYaw;
                    this.mc.field_71439_g.field_70125_A = this.lastRotationPitch;
                    this.yawRestored = true;
                    this.yawRecorded = false;
                }
                if (Math.abs(this.mc.field_71439_g.field_70177_z - this.lastRotationYaw) > f2) {
                    f = this.lastRotationYaw;
                    this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, f, f3);
                } else {
                    this.mc.field_71439_g.field_70177_z = this.lastRotationYaw;
                    this.yawRestored = true;
                    this.yawRecorded = false;
                }
            }
            if (this.pitchRecorded) {
                this.pitchDiff = Math.abs(this.mc.field_71439_g.field_70125_A - this.lastRotationPitch);
                if (Math.abs(this.mc.field_71439_g.field_70125_A - this.lastRotationPitch) > f2) {
                    f = this.lastRotationPitch;
                    this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, f, f3);
                } else {
                    this.mc.field_71439_g.field_70125_A = this.lastRotationPitch;
                    this.pitchRestored = true;
                    this.pitchRecorded = false;
                }
            }
            this.reachedSize = false;
        }
        if (this.currentSC != null && (double)this.mc.field_71439_g.func_70032_d(this.currentSC) > (Double)this.killRange.getValue()) {
            if (!this.switchedToRod) {
                this.shouldSwitchToRod = true;
            }
            this.currentSC = null;
        }
        if (this.allSCNear.isEmpty() && !this.switchedToRod) {
            this.shouldSwitchToRod = true;
        }
        if (this.currentSC != null && !this.currentSC.func_70089_S()) {
            if (!this.switchedToRod) {
                this.shouldSwitchToRod = true;
            }
            this.currentSC = null;
        }
        if (this.currentSC == null && this.shouldSwitchToRod && !this.reachedSize) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
                if (itemStack == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemFishingRod) || !this.yawRestored || !this.pitchRestored) continue;
                this.dickTimer = 40;
                this.lastRotationYaw = 0.0f;
                this.lastRotationPitch = 0.0f;
                this.mc.field_71439_g.field_71071_by.field_70461_c = i;
                this.shouldSwitchToRod = false;
                this.switchedToRod = true;
                break;
            }
        }
        if (this.currentSC != null && this.reachedSize) {
            this.reset();
            this.tickTimer = 0;
            this.tickTimer1 = 0;
            this.dickTimer = 0;
            this.soundVec = null;
        }
    }

    @EventHandler
    private void onESPSC(EventRender3D eventRender3D) {
        if (!((Boolean)this.soundBB.getValue()).booleanValue()) {
            return;
        }
        if (this.currentSC != null && this.reachedSize) {
            RenderUtil.entityOutlineAXIS(this.currentSC, Colors.AQUA.c, eventRender3D);
        }
    }

    @EventHandler
    private void onDebugDraw(EventRender2D eventRender2D) {
        if (!((Boolean)this.showDebug.getValue()).booleanValue()) {
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.mc.field_71466_p.func_78276_b("Current Stage: " + this.currentStage, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 6, -1);
        this.mc.field_71466_p.func_78276_b("TickTimer: " + this.tickTimer, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 18, -1);
        this.mc.field_71466_p.func_78276_b("SoundCDTimer: " + this.tickTimer1 + (this.tickTimer1 == 50 ? " (Ready)" : ""), scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 30, -1);
        this.mc.field_71466_p.func_78276_b("AutoThrowTimer: " + this.dickTimer, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 42, -1);
        this.mc.field_71466_p.func_78276_b("SoundMonitor: " + this.soundReady, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 54, -1);
        this.mc.field_71466_p.func_78276_b("SoundReady: " + this.soundCDReady, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 66, -1);
        this.mc.field_71466_p.func_78276_b("MotionReady: " + this.motionReady, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 78, -1);
        if (((Boolean)this.randomDelay.getValue()).booleanValue()) {
            this.mc.field_71466_p.func_78276_b("ExtraDelay: " + this.extraDelay, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 90, -1);
        }
        this.mc.field_71466_p.func_78276_b("YawReady: " + this.yawRestored, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 102, -1);
        this.mc.field_71466_p.func_78276_b("YawDiff: " + this.yawDiff, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 114, -1);
        this.mc.field_71466_p.func_78276_b("PitchReady: " + this.pitchRestored, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 126, -1);
        this.mc.field_71466_p.func_78276_b("PitchDiff: " + this.pitchDiff, scaledResolution.func_78326_a() / 2 + 6, scaledResolution.func_78328_b() / 2 + 138, -1);
    }

    @EventHandler
    private void onSneak(EventTick eventTick) {
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74311_E.func_151463_i(), (boolean)true);
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.field_71439_g.field_71104_cf != null && this.currentStage != stage.FINISH) {
            this.currentStage = stage.WAITING;
        }
        if (this.mc.field_71439_g.func_70694_bm() == null || !(this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemFishingRod)) {
            this.currentStage = stage.NONE;
        }
        if (this.currentStage == stage.NONE) {
            this.reset();
            this.tickTimer = 0;
            this.tickTimer1 = 0;
            this.soundVec = null;
        }
        if (this.currentStage == stage.WAITING && this.mc.field_71439_g.field_71104_cf == null) {
            this.currentStage = stage.NONE;
        }
        if (this.mc.field_71439_g.func_70694_bm() != null && ((Boolean)this.autoThrow.getValue()).booleanValue() && this.currentStage == stage.NONE && this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemFishingRod) {
            if (this.dickTimer < 20) {
                ++this.dickTimer;
                return;
            }
            if (((Boolean)this.rotation.getValue()).booleanValue()) {
                if (this.backRotaion) {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.field_71439_g.field_70177_z -= ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.field_71439_g.field_70125_A -= ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                } else {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.field_71439_g.field_70177_z += ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.field_71439_g.field_70125_A += ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                }
            }
            Client.rightClick();
        }
        this.dickTimer = 0;
    }

    @EventHandler
    public void onPacket(EventPacketRecieve eventPacketRecieve) {
        Packet<INetHandlerPlayClient> packet;
        if (this.currentStage != stage.WAITING) {
            return;
        }
        if (this.mc.field_71439_g.field_71104_cf == null) {
            return;
        }
        Packet<?> packet2 = eventPacketRecieve.getPacket();
        if (packet2 instanceof S29PacketSoundEffect) {
            packet = (S29PacketSoundEffect)packet2;
            if (((Boolean)this.packetDebug.getValue()).booleanValue()) {
                Helper.sendMessage("Current Sound: " + packet.func_149212_c());
            }
            if (packet.func_149212_c().contains("game.player.swim.splash") || packet.func_149212_c().contains("random.splash")) {
                float f = ((Double)this.soundRadius.getValue()).floatValue();
                if (Math.abs(packet.func_149207_d() - this.mc.field_71439_g.field_71104_cf.field_70165_t) <= (double)f && Math.abs(packet.func_149210_f() - this.mc.field_71439_g.field_71104_cf.field_70161_v) <= (double)f) {
                    this.soundReady = true;
                    this.soundVec = new Vec3(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
                }
            }
        }
        if (packet2 instanceof S12PacketEntityVelocity && (packet = (S12PacketEntityVelocity)packet2).func_149411_d() == 0 && packet.func_149410_e() != 0 && packet.func_149409_f() == 0) {
            this.motionReady = true;
        }
    }

    @EventHandler
    private void onMove(EventTick eventTick) {
        if (((Boolean)this.move.getValue()).booleanValue()) {
            int n;
            int n2 = this.moveMode.getValue() == moves.AD ? this.mc.field_71474_y.field_74370_x.func_151463_i() : this.mc.field_71474_y.field_74351_w.func_151463_i();
            int n3 = n = this.moveMode.getValue() == moves.AD ? this.mc.field_71474_y.field_74366_z.func_151463_i() : this.mc.field_71474_y.field_74368_y.func_151463_i();
            if (!this.moveDone) {
                if (this.currentStage == stage.FINISH && !this.moved) {
                    this.moveTimer.reset();
                    KeyBinding.func_74510_a((int)n2, (boolean)true);
                    this.moved = true;
                }
                if (this.moved && this.moveTimer.hasReached(50.0)) {
                    KeyBinding.func_74510_a((int)n2, (boolean)false);
                    if (this.moveTimer.hasReached(100.0)) {
                        KeyBinding.func_74510_a((int)n, (boolean)true);
                        if (this.moveTimer.hasReached(150.0)) {
                            KeyBinding.func_74510_a((int)n, (boolean)false);
                            this.moveDone = true;
                        }
                    }
                }
            } else if (this.currentStage != stage.FINISH) {
                this.moved = false;
                this.moveTimer.reset();
                this.moveDone = false;
                KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74370_x.func_151463_i(), (boolean)false);
                KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74366_z.func_151463_i(), (boolean)false);
            }
        }
    }

    @EventHandler
    private void onReady(EventTick eventTick) {
        if (this.soundCDReady && this.motionReady && this.currentStage != stage.FINISH) {
            this.currentStage = stage.FINISH;
            Client.rightClick();
            this.reset();
        }
    }

    @EventHandler
    private void onCDReady(EventTick eventTick) {
        if (this.currentStage == stage.WAITING) {
            if (this.tickTimer1 < 55) {
                this.soundCDReady = false;
                this.soundReady = false;
                ++this.tickTimer1;
            } else {
                this.soundCDReady = this.soundReady;
            }
        }
    }

    @EventHandler
    private void onThrow(EventTick eventTick) {
        if (this.currentStage == stage.FINISH) {
            if (!this.delaySet) {
                this.extraDelay = (Boolean)this.randomDelay.getValue() != false ? Math.abs((int)(Math.random() * 50.0)) : 0;
                this.delaySet = true;
            }
            if (this.tickTimer < ((Double)this.tickTimerVale.getValue()).intValue() + this.extraDelay) {
                ++this.tickTimer;
                return;
            }
            Client.rightClick();
            if (((Boolean)this.rotation.getValue()).booleanValue()) {
                if (this.backRotaion) {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.field_71439_g.field_70177_z -= ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.field_71439_g.field_70125_A -= ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                } else {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.field_71439_g.field_70177_z += ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.field_71439_g.field_70125_A += ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                }
            }
            this.currentStage = stage.WAITING;
            this.tickTimer1 = 0;
            this.tickTimer = 0;
            this.delaySet = false;
        }
    }

    @EventHandler
    private void onR3D(EventRender3D eventRender3D) {
        AxisAlignedBB axisAlignedBB;
        if (!((Boolean)this.soundBB.getValue()).booleanValue()) {
            return;
        }
        if (this.mc.field_71439_g.field_71104_cf != null) {
            axisAlignedBB = this.mc.field_71439_g.field_71104_cf.func_174813_aQ().func_72314_b((Double)this.soundRadius.getValue(), 0.0, (Double)this.soundRadius.getValue());
            RenderUtil.drawOutlinedBoundingBox(axisAlignedBB, Colors.AQUA.c, 2.0f, eventRender3D.getPartialTicks());
        }
        if (this.soundVec != null) {
            axisAlignedBB = new AxisAlignedBB(this.soundVec.field_72450_a + 0.05, this.soundVec.field_72448_b + 0.05, this.soundVec.field_72449_c + 0.05, this.soundVec.field_72450_a - 0.05, this.soundVec.field_72448_b - 0.05, this.soundVec.field_72449_c - 0.05);
            RenderUtil.drawOutlinedBoundingBox(axisAlignedBB, Colors.RED.c, 2.0f, eventRender3D.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void reset() {
        this.soundReady = false;
        this.soundCDReady = false;
        this.motionReady = false;
    }

    private void loadSCs() {
        this.allSCNear.clear();
        this.allSCNear = this.getTargets((Double)this.killRange.getValue());
        this.allSCNear.sort(Comparator.comparingDouble(entity -> this.mc.field_71439_g.func_70068_e((Entity)entity)));
    }

    public List<Entity> getTargets(Double d) {
        return this.mc.field_71441_e.field_72996_f.stream().filter(entity -> this.isSC((Entity)entity) && (double)this.mc.field_71439_g.func_70032_d((Entity)entity) < d).collect(Collectors.toList());
    }

    private void attack(EntityLivingBase entityLivingBase) {
        this.mc.field_71439_g.func_71038_i();
        this.mc.field_71439_g.field_71174_a.func_147297_a(new C02PacketUseEntity((Entity)entityLivingBase, C02PacketUseEntity.Action.ATTACK));
        this.attackTimer.reset();
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

    private EntityPlayer playerInRange() {
        if (!((Boolean)this.escape.getValue()).booleanValue()) {
            return null;
        }
        for (EntityPlayer entityPlayer : this.mc.field_71441_e.field_73010_i) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || FriendManager.isFriend(entityPlayer.func_70005_c_()) || !(this.mc.field_71439_g.func_70032_d(entityPlayer) < (float)((Double)this.escapeRange.getValue()).intValue()) || entityPlayer == this.mc.field_71439_g) continue;
            return entityPlayer;
        }
        return null;
    }

    public Rotation vec3ToRotation(Vec3 vec3) {
        double d = vec3.field_72450_a - this.mc.field_71439_g.field_70165_t;
        double d2 = vec3.field_72448_b - this.mc.field_71439_g.field_70163_u - (double)this.mc.field_71439_g.func_70047_e();
        double d3 = vec3.field_72449_c - this.mc.field_71439_g.field_70161_v;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(-Math.atan2(d4, d2));
        float f2 = (float)Math.atan2(d3, d);
        f = (float)AutoFish.wrapAngleTo180(((double)(f * 180.0f) / Math.PI + 90.0) * -1.0);
        f2 = (float)AutoFish.wrapAngleTo180((double)(f2 * 180.0f) / Math.PI - 90.0);
        return new Rotation(f, f2);
    }

    private static double wrapAngleTo180(double d) {
        return d - Math.floor(d / 360.0 + 0.5) * 360.0;
    }

    private boolean isSC(Entity entity) {
        if (entity == this.mc.field_71439_g) {
            return false;
        }
        if ((double)this.mc.field_71439_g.func_70032_d(entity) > (Double)this.killRange.getValue()) {
            return false;
        }
        if (entity.field_70128_L || !entity.func_70089_S()) {
            return false;
        }
        if (entity.func_145748_c_() != null && PlayerListUtils.tabContains(entity.func_70005_c_())) {
            return false;
        }
        if (entity instanceof EntitySquid && ((Boolean)this.squid.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityGuardian && ((Boolean)this.guard.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityZombie && ((Boolean)this.zombie.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntitySkeleton && ((Boolean)this.skeleton.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityWitch && ((Boolean)this.witch.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityOcelot && ((Boolean)this.cat.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntitySilverfish && ((Boolean)this.silverfish.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityGolem && ((Boolean)this.golem.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityRabbit && ((Boolean)this.rabbit.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntitySheep && ((Boolean)this.sheep.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityEndermite && ((Boolean)this.endermite.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityBlaze && ((Boolean)this.blaze.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityPigZombie && ((Boolean)this.pigman.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityPlayer && ((Boolean)this.player.getValue()).booleanValue()) {
            return true;
        }
        return entity instanceof EntityHorse && (Boolean)this.horse.getValue() != false;
    }

    private static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float f, float f2) {
            this.pitch = f;
            this.yaw = f2;
        }
    }

    static enum stage {
        NONE,
        WAITING,
        FINISH;

    }

    static enum moves {
        WS,
        AD;

    }

    static enum rotations {
        Yaw,
        Pitch;

    }
}

