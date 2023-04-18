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
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit == null) {
            this.lockedVec = this.mc.objectMouseOver.hitVec;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        PlayerList playerList;
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
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
                WindowsNotification.show("MelodySky - AutoFish", "Escaped. Player Name: " + this.playerCaused.getName() + ".");
                Helper.sendMessage("[AutoFish] Player Detected, Warping to Main Lobby.");
                Helper.sendMessage("[AutoFish] Player Name: " + this.playerCaused.getName() + ".");
                this.mc.thePlayer.sendChatMessage("/l");
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
            this.mc.thePlayer.sendChatMessage("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
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
            boolean bl = this.mc.isIntegratedServerRunning();
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            if (bl) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @EventHandler
    private void onDead(EventTick eventTick) {
        if (!((Boolean)this.dead.getValue()).booleanValue()) {
            return;
        }
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.isDead, Disabled AutoFish.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.thePlayer.ticksExisted <= 1) {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.tickExisted <= 1, Disabled AutoFish.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.thePlayer.getHealth() == 0.0f) {
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
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
            if (this.mc.thePlayer.fishEntity != null) {
                if (!this.mc.thePlayer.fishEntity.isInWater() && !this.mc.thePlayer.fishEntity.isInLava() && this.reThrowTimer.hasReached(10000.0)) {
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
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemFishingRod) || !this.yawRestored || !this.pitchRestored) continue;
            this.mc.thePlayer.inventory.currentItem = i;
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
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, rotation.yaw, 30.0f);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, rotation.pitch, 30.0f);
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
            this.mc.thePlayer.inventory.currentItem = 0;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
            this.rightClickTimer.reset();
            this.attackTimer.reset();
            this.shouldSwitchToWeapon = false;
        }
        float f3 = ((Double)this.angleSize.getValue()).floatValue() * 3.0f;
        if (this.currentSC != null && this.shouldAttack() && this.reachedSize) {
            if (this.mc.thePlayer.inventory.currentItem != 0) {
                this.shouldSwitchToWeapon = true;
            } else if (this.mc.thePlayer.inventory.currentItem == 0) {
                this.switchedToRod = false;
                if (!((Boolean)this.rckill.getValue()).booleanValue()) {
                    this.attack(this.currentSC);
                }
                if (((Boolean)this.rckill.getValue()).booleanValue()) {
                    if (this.rightClickTimer.hasReached(((Double)this.rccd.getValue()).longValue()) && this.mc.thePlayer.inventory.currentItem == 0) {
                        Client.rightClick();
                        this.rightClickTimer.reset();
                    }
                    if (!this.yawRecorded) {
                        this.lastRotationYaw = this.mc.thePlayer.rotationYaw;
                        this.yawRestored = false;
                        this.yawRecorded = true;
                    }
                    if (!this.pitchRecorded) {
                        this.lastRotationPitch = this.mc.thePlayer.rotationPitch;
                        this.pitchRestored = false;
                        this.pitchRecorded = true;
                    }
                    if (this.yawRecorded && this.pitchRecorded) {
                        f2 = RotationUtil.getRotationToEntity(this.currentSC)[0];
                        f = RotationUtil.getRotationToEntity(this.currentSC)[1];
                        this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, f2, f3);
                        this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, f, f3);
                    }
                }
            }
        }
        f2 = ((Double)this.angleDiff.getValue()).floatValue() * 10.0f;
        if (this.currentSC == null && this.allSCNear.isEmpty()) {
            if (this.yawRecorded) {
                this.yawDiff = Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw);
                if (Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw) > 360.0f - f2) {
                    this.mc.thePlayer.rotationYaw = this.lastRotationYaw;
                    this.mc.thePlayer.rotationPitch = this.lastRotationPitch;
                    this.yawRestored = true;
                    this.yawRecorded = false;
                }
                if (Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw) > f2) {
                    f = this.lastRotationYaw;
                    this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, f, f3);
                } else {
                    this.mc.thePlayer.rotationYaw = this.lastRotationYaw;
                    this.yawRestored = true;
                    this.yawRecorded = false;
                }
            }
            if (this.pitchRecorded) {
                this.pitchDiff = Math.abs(this.mc.thePlayer.rotationPitch - this.lastRotationPitch);
                if (Math.abs(this.mc.thePlayer.rotationPitch - this.lastRotationPitch) > f2) {
                    f = this.lastRotationPitch;
                    this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, f, f3);
                } else {
                    this.mc.thePlayer.rotationPitch = this.lastRotationPitch;
                    this.pitchRestored = true;
                    this.pitchRecorded = false;
                }
            }
            this.reachedSize = false;
        }
        if (this.currentSC != null && (double)this.mc.thePlayer.getDistanceToEntity(this.currentSC) > (Double)this.killRange.getValue()) {
            if (!this.switchedToRod) {
                this.shouldSwitchToRod = true;
            }
            this.currentSC = null;
        }
        if (this.allSCNear.isEmpty() && !this.switchedToRod) {
            this.shouldSwitchToRod = true;
        }
        if (this.currentSC != null && !this.currentSC.isEntityAlive()) {
            if (!this.switchedToRod) {
                this.shouldSwitchToRod = true;
            }
            this.currentSC = null;
        }
        if (this.currentSC == null && this.shouldSwitchToRod && !this.reachedSize) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
                if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemFishingRod) || !this.yawRestored || !this.pitchRestored) continue;
                this.dickTimer = 40;
                this.lastRotationYaw = 0.0f;
                this.lastRotationPitch = 0.0f;
                this.mc.thePlayer.inventory.currentItem = i;
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
        this.mc.fontRendererObj.drawString("Current Stage: " + this.currentStage, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 6, -1);
        this.mc.fontRendererObj.drawString("TickTimer: " + this.tickTimer, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 18, -1);
        this.mc.fontRendererObj.drawString("SoundCDTimer: " + this.tickTimer1 + (this.tickTimer1 == 50 ? " (Ready)" : ""), scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 30, -1);
        this.mc.fontRendererObj.drawString("AutoThrowTimer: " + this.dickTimer, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 42, -1);
        this.mc.fontRendererObj.drawString("SoundMonitor: " + this.soundReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 54, -1);
        this.mc.fontRendererObj.drawString("SoundReady: " + this.soundCDReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 66, -1);
        this.mc.fontRendererObj.drawString("MotionReady: " + this.motionReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 78, -1);
        if (((Boolean)this.randomDelay.getValue()).booleanValue()) {
            this.mc.fontRendererObj.drawString("ExtraDelay: " + this.extraDelay, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 90, -1);
        }
        this.mc.fontRendererObj.drawString("YawReady: " + this.yawRestored, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 102, -1);
        this.mc.fontRendererObj.drawString("YawDiff: " + this.yawDiff, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 114, -1);
        this.mc.fontRendererObj.drawString("PitchReady: " + this.pitchRestored, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 126, -1);
        this.mc.fontRendererObj.drawString("PitchDiff: " + this.pitchDiff, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 138, -1);
    }

    @EventHandler
    private void onSneak(EventTick eventTick) {
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.thePlayer.fishEntity != null && this.currentStage != stage.FINISH) {
            this.currentStage = stage.WAITING;
        }
        if (this.mc.thePlayer.getHeldItem() == null || !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)) {
            this.currentStage = stage.NONE;
        }
        if (this.currentStage == stage.NONE) {
            this.reset();
            this.tickTimer = 0;
            this.tickTimer1 = 0;
            this.soundVec = null;
        }
        if (this.currentStage == stage.WAITING && this.mc.thePlayer.fishEntity == null) {
            this.currentStage = stage.NONE;
        }
        if (this.mc.thePlayer.getHeldItem() != null && ((Boolean)this.autoThrow.getValue()).booleanValue() && this.currentStage == stage.NONE && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
            if (this.dickTimer < 20) {
                ++this.dickTimer;
                return;
            }
            if (((Boolean)this.rotation.getValue()).booleanValue()) {
                if (this.backRotaion) {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.thePlayer.rotationYaw -= ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.thePlayer.rotationPitch -= ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                } else {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.thePlayer.rotationYaw += ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.thePlayer.rotationPitch += ((Double)this.angle.getValue()).floatValue();
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
        if (this.mc.thePlayer.fishEntity == null) {
            return;
        }
        Packet<?> packet2 = eventPacketRecieve.getPacket();
        if (packet2 instanceof S29PacketSoundEffect) {
            packet = (S29PacketSoundEffect)packet2;
            if (((Boolean)this.packetDebug.getValue()).booleanValue()) {
                Helper.sendMessage("Current Sound: " + ((S29PacketSoundEffect)packet).getSoundName());
            }
            if (((S29PacketSoundEffect)packet).getSoundName().contains("game.player.swim.splash") || ((S29PacketSoundEffect)packet).getSoundName().contains("random.splash")) {
                float f = ((Double)this.soundRadius.getValue()).floatValue();
                if (Math.abs(((S29PacketSoundEffect)packet).getX() - this.mc.thePlayer.fishEntity.posX) <= (double)f && Math.abs(((S29PacketSoundEffect)packet).getZ() - this.mc.thePlayer.fishEntity.posZ) <= (double)f) {
                    this.soundReady = true;
                    this.soundVec = new Vec3(((S29PacketSoundEffect)packet).getX(), ((S29PacketSoundEffect)packet).getY(), ((S29PacketSoundEffect)packet).getZ());
                }
            }
        }
        if (packet2 instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)(packet = (S12PacketEntityVelocity)packet2)).getMotionX() == 0 && ((S12PacketEntityVelocity)packet).getMotionY() != 0 && ((S12PacketEntityVelocity)packet).getMotionZ() == 0) {
            this.motionReady = true;
        }
    }

    @EventHandler
    private void onMove(EventTick eventTick) {
        if (((Boolean)this.move.getValue()).booleanValue()) {
            int n;
            int n2 = this.moveMode.getValue() == moves.AD ? this.mc.gameSettings.keyBindLeft.getKeyCode() : this.mc.gameSettings.keyBindForward.getKeyCode();
            int n3 = n = this.moveMode.getValue() == moves.AD ? this.mc.gameSettings.keyBindRight.getKeyCode() : this.mc.gameSettings.keyBindBack.getKeyCode();
            if (!this.moveDone) {
                if (this.currentStage == stage.FINISH && !this.moved) {
                    this.moveTimer.reset();
                    KeyBinding.setKeyBindState(n2, true);
                    this.moved = true;
                }
                if (this.moved && this.moveTimer.hasReached(50.0)) {
                    KeyBinding.setKeyBindState(n2, false);
                    if (this.moveTimer.hasReached(100.0)) {
                        KeyBinding.setKeyBindState(n, true);
                        if (this.moveTimer.hasReached(150.0)) {
                            KeyBinding.setKeyBindState(n, false);
                            this.moveDone = true;
                        }
                    }
                }
            } else if (this.currentStage != stage.FINISH) {
                this.moved = false;
                this.moveTimer.reset();
                this.moveDone = false;
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), false);
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
                        this.mc.thePlayer.rotationYaw -= ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.thePlayer.rotationPitch -= ((Double)this.angle.getValue()).floatValue();
                    }
                    this.backRotaion = !this.backRotaion;
                } else {
                    if (this.rotationMode.getValue() == rotations.Yaw) {
                        this.mc.thePlayer.rotationYaw += ((Double)this.angle.getValue()).floatValue();
                    } else {
                        this.mc.thePlayer.rotationPitch += ((Double)this.angle.getValue()).floatValue();
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
        if (this.mc.thePlayer.fishEntity != null) {
            axisAlignedBB = this.mc.thePlayer.fishEntity.getEntityBoundingBox().expand((Double)this.soundRadius.getValue(), 0.0, (Double)this.soundRadius.getValue());
            RenderUtil.drawOutlinedBoundingBox(axisAlignedBB, Colors.AQUA.c, 2.0f, eventRender3D.getPartialTicks());
        }
        if (this.soundVec != null) {
            axisAlignedBB = new AxisAlignedBB(this.soundVec.xCoord + 0.05, this.soundVec.yCoord + 0.05, this.soundVec.zCoord + 0.05, this.soundVec.xCoord - 0.05, this.soundVec.yCoord - 0.05, this.soundVec.zCoord - 0.05);
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
        this.allSCNear.sort(Comparator.comparingDouble(entity -> this.mc.thePlayer.getDistanceSqToEntity((Entity)entity)));
    }

    public List<Entity> getTargets(Double d) {
        return this.mc.theWorld.loadedEntityList.stream().filter(entity -> this.isSC((Entity)entity) && (double)this.mc.thePlayer.getDistanceToEntity((Entity)entity) < d).collect(Collectors.toList());
    }

    private void attack(EntityLivingBase entityLivingBase) {
        this.mc.thePlayer.swingItem();
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)entityLivingBase, C02PacketUseEntity.Action.ATTACK));
        this.attackTimer.reset();
    }

    private float smoothRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
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
        for (EntityPlayer entityPlayer : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || FriendManager.isFriend(entityPlayer.getName()) || !(this.mc.thePlayer.getDistanceToEntity(entityPlayer) < (float)((Double)this.escapeRange.getValue()).intValue()) || entityPlayer == this.mc.thePlayer) continue;
            return entityPlayer;
        }
        return null;
    }

    public Rotation vec3ToRotation(Vec3 vec3) {
        double d = vec3.xCoord - this.mc.thePlayer.posX;
        double d2 = vec3.yCoord - this.mc.thePlayer.posY - (double)this.mc.thePlayer.getEyeHeight();
        double d3 = vec3.zCoord - this.mc.thePlayer.posZ;
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
        if (entity == this.mc.thePlayer) {
            return false;
        }
        if ((double)this.mc.thePlayer.getDistanceToEntity(entity) > (Double)this.killRange.getValue()) {
            return false;
        }
        if (entity.isDead || !entity.isEntityAlive()) {
            return false;
        }
        if (entity.getDisplayName() != null && PlayerListUtils.tabContains(entity.getName())) {
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

