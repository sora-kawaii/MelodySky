/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPostUpdate;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.injection.mixins.entity.EPSPAccessor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.balance.AutoBlock;

public final class Aura
extends Module {
    public EntityLivingBase curTarget;
    private List<Entity> targets = new ArrayList<Entity>();
    private int index;
    private Mode<Enum> mode = new Mode("Mode", (Enum[])AuraMode.values(), (Enum)AuraMode.Single);
    private Numbers<Double> cps = new Numbers<Double>("CPS", 10.0, 1.0, 20.0, 0.5);
    private Numbers<Double> range = new Numbers<Double>("Range", 4.5, 1.0, 6.0, 0.1);
    private Numbers<Double> fov = new Numbers<Double>("Attack FOV", 360.0, 1.0, 360.0, 10.0);
    private Numbers<Double> sinC = new Numbers<Double>("Single Switch", 200.0, 1.0, 1000.0, 1.0);
    public Option<Boolean> targetHud = new Option<Boolean>("TargetHUD", true);
    private Option<Boolean> noSwing = new Option<Boolean>("No Swing", false);
    private Option<Boolean> mouseDown = new Option<Boolean>("Mouse Down", false);
    private Option<Boolean> ksprint = new Option<Boolean>("Keep Sprint", true);
    private Option<Boolean> pre = new Option<Boolean>("PreAttack", false);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", true);
    private Option<Boolean> esp = new Option<Boolean>("ESP", false);
    private Option<Boolean> players = new Option<Boolean>("Players", true);
    private Option<Boolean> friend = new Option<Boolean>("FriendFilter", true);
    private Option<Boolean> team = new Option<Boolean>("TeammateFilter", true);
    private Option<Boolean> animals = new Option<Boolean>("Animals", true);
    private Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private Option<Boolean> invis = new Option<Boolean>("Invisibles", false);
    private Option<Boolean> death = new Option<Boolean>("DeathCheck", true);
    public boolean isBlocking;
    private Comparator<Entity> angleComparator = Comparator.comparingDouble(this::lambda$new$0);
    private TimerUtil attackTimer = new TimerUtil();
    private TimerUtil switchTimer = new TimerUtil();
    private TimerUtil singleTimer = new TimerUtil();
    private TimerUtil sinCTimer = new TimerUtil();
    private boolean cpsReady = false;

    public Aura() {
        super("KillAura", new String[]{"ka", "aura", "killa"}, ModuleType.Balance);
        this.addValues(this.mode, this.cps, this.range, this.fov, this.sinC, this.targetHud, this.noSwing, this.mouseDown, this.ksprint, this.pre, this.rot, this.esp, this.players, this.friend, this.team, this.animals, this.mobs, this.invis, this.death);
    }

    @Override
    public void onDisable() {
        this.curTarget = null;
        this.targets.clear();
        if (Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() && this.mc.thePlayer.getItemInUseCount() > 0) {
            this.unBlock();
        }
    }

    @Override
    public void onEnable() {
        this.curTarget = null;
        this.index = 0;
    }

    public static double random(double min, double max) {
        Random random = new Random();
        return min + (double)((int)(random.nextDouble() * (max - min)));
    }

    private boolean cpsReady() {
        return this.attackTimer.hasReached(1000.0 / ((Double)this.cps.getValue() + 5.0 + MathUtil.randomDouble(-3.0, 3.0)));
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (this.curTarget == null) {
            return;
        }
        if (((Boolean)this.mouseDown.getValue()).booleanValue() && !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        if (((Boolean)this.esp.getValue()).booleanValue()) {
            if (this.mode.getValue() == AuraMode.Multi) {
                for (Entity ent : this.targets) {
                    Color col = new Color(60, 127, 130, 120);
                    RenderUtil.drawFilledESP(ent, col, event, 3.0f);
                }
                Color col = new Color(200, 30, 30, 190);
                RenderUtil.drawFilledESP(this.curTarget, col, event, 6.0f);
                return;
            }
            float partialTicks = event.getPartialTicks();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            RenderUtil.startDrawing();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(4.0f);
            GL11.glBegin(3);
            double x = this.curTarget.lastTickPosX + (this.curTarget.posX - this.curTarget.lastTickPosX) * (double)partialTicks - this.mc.getRenderManager().viewerPosX;
            double y = this.curTarget.lastTickPosY + (this.curTarget.posY - this.curTarget.lastTickPosY) * (double)partialTicks - this.mc.getRenderManager().viewerPosY;
            double z = this.curTarget.lastTickPosZ + (this.curTarget.posZ - this.curTarget.lastTickPosZ) * (double)partialTicks - this.mc.getRenderManager().viewerPosZ;
            for (int i = 0; i <= 10; ++i) {
                RenderUtil.glColor(FadeUtil.fade(FadeUtil.BLUE.getColor()).getRGB());
                GL11.glVertex3d(x + 1.1 * Math.cos((double)i * (Math.PI * 2) / 9.0), y, z + 1.1 * Math.sin((double)i * (Math.PI * 2) / 9.0));
            }
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            RenderUtil.stopDrawing();
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }

    private boolean hasSword() {
        if (this.mc.thePlayer.getCurrentEquippedItem() != null) {
            return this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
        }
        return false;
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (!((Boolean)this.ksprint.getValue()).booleanValue()) {
            this.mc.thePlayer.setSprinting(false);
        }
        if (((Boolean)this.death.getValue()).booleanValue() && this.mc.thePlayer != null) {
            if (!this.mc.thePlayer.isEntityAlive() || this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiGameOver) {
                this.setEnabled(false);
                return;
            }
            if (this.mc.thePlayer.ticksExisted <= 1) {
                this.setEnabled(false);
                return;
            }
        }
    }

    private void block() {
        if (this.hasSword()) {
            ((EPSPAccessor)((Object)this.mc.thePlayer)).setItemInUseCount(this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
            this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
            this.isBlocking = true;
        }
    }

    private void unBlock() {
        if (this.hasSword() && this.isBlocking) {
            if (!this.mc.thePlayer.isBlocking() && this.mc.thePlayer.getItemInUseCount() > 0) {
                ((EPSPAccessor)((Object)this.mc.thePlayer)).setItemInUseCount(0);
            }
            this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN, EnumFacing.DOWN));
            this.isBlocking = false;
        }
    }

    @EventHandler
    private void onAuraLoad(EventRender2D event) {
        this.cpsReady = this.cpsReady();
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Mouse.isButtonDown(1) && this.curTarget != null) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        }
        if (this.curTarget != null && (!this.curTarget.isEntityAlive() || this.curTarget.isEntityUndead())) {
            this.curTarget = this.getTargets((Double)this.range.getValue()).isEmpty() ? null : (EntityLivingBase)this.getTargets((Double)this.range.getValue()).get(0);
        }
        this.targets.removeIf(this::lambda$onAuraLoad$1);
        if (this.targets.size() > 0 && this.mode.getValue() == AuraMode.Multi) {
            if (this.curTarget == null) {
                this.curTarget = (EntityLivingBase)this.targets.get(0);
            }
            if (this.curTarget.hurtTime > 0 || this.switchTimer.hasReached((Double)this.sinC.getValue())) {
                this.curTarget.hurtTime = 0;
                ++this.index;
                if (this.index + 1 > this.targets.size()) {
                    this.index = 0;
                }
                this.curTarget = (EntityLivingBase)this.targets.get(this.index);
                this.switchTimer.reset();
            }
        }
    }

    @EventHandler
    private void tickAura(EventTick event) {
        if (((Boolean)this.mouseDown.getValue()).booleanValue() && !this.mc.gameSettings.keyBindAttack.isKeyDown() && this.sinCTimer.hasReached(200.0)) {
            this.curTarget = null;
            if (this.isBlocking) {
                this.unBlock();
            }
            this.sinCTimer.reset();
            return;
        }
        if (this.mode.getValue() == AuraMode.Single && (this.curTarget == null || this.curTarget.isDead || !this.curTarget.isEntityAlive() || (double)this.mc.thePlayer.getDistanceToEntity(this.curTarget) > (Double)this.range.getValue() || RotationUtil.isInFov(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, this.curTarget.posX, this.curTarget.posY, this.curTarget.posZ) > (Double)this.fov.getValue())) {
            this.curTarget = null;
            this.targets = this.getTargets((Double)this.range.getValue());
            this.targets.sort(this.angleComparator);
        }
        if (this.curTarget != null && (this.curTarget.getHealth() == 0.0f || !this.curTarget.isEntityAlive() || (double)this.mc.thePlayer.getDistanceToEntity(this.curTarget) > (Double)this.range.getValue())) {
            this.curTarget = null;
            ++this.index;
        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (!((Boolean)this.mouseDown.getValue()).booleanValue() || ((Boolean)this.mouseDown.getValue()).booleanValue() && this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            boolean autoblock;
            if (this.mode.getValue() == AuraMode.Single && this.curTarget == null && !this.targets.isEmpty()) {
                this.curTarget = (EntityLivingBase)this.targets.get(0);
            }
            boolean bl = autoblock = Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() || Mouse.isButtonDown(1) && this.mc.currentScreen == null && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
            if (this.curTarget == null && autoblock && this.isBlocking && this.hasSword()) {
                this.unBlock();
            }
            if (this.hasSword() && this.curTarget != null && autoblock && !this.isBlocking) {
                this.block();
            }
            if (this.mode.getValue() == AuraMode.Multi) {
                if (this.targets.isEmpty() || this.curTarget == null || this.singleTimer.hasReached((Double)this.sinC.getValue())) {
                    this.targets = this.getTargets((Double)this.range.getValue());
                }
                for (Entity e : this.targets) {
                    if (e != null && e.isEntityAlive() && !e.isDead && !((double)this.mc.thePlayer.getDistanceToEntity(e) > (Double)this.range.getValue())) continue;
                    this.targets = this.getTargets((Double)this.range.getValue());
                }
                this.sinCTimer.reset();
            }
            if (this.targets.size() > 1 && this.mode.getValue() == AuraMode.Single && this.curTarget != null) {
                if ((double)this.curTarget.getDistanceToEntity(this.mc.thePlayer) > (Double)this.range.getValue()) {
                    this.curTarget = null;
                } else if (this.curTarget.isDead) {
                    this.curTarget = null;
                }
                this.singleTimer.reset();
            }
            if (((Boolean)this.pre.getValue()).booleanValue() && this.curTarget != null) {
                if (((Boolean)this.pre.getValue()).booleanValue() && this.cpsReady) {
                    if (this.hasSword() && this.mc.thePlayer.isBlocking() && this.isValidEntity(this.curTarget)) {
                        this.unBlock();
                    }
                    this.attack();
                    if (!this.mc.thePlayer.isBlocking() && this.hasSword() && autoblock) {
                        this.block();
                    }
                }
                this.curTarget = null;
            }
        }
        if (!this.targets.isEmpty() && this.curTarget != null && this.mode.getValue() != AuraMode.Multi && ((Boolean)this.rot.getValue()).booleanValue()) {
            float yaw = this.getRotationFormEntity(this.curTarget)[0];
            float pitch = this.getRotationFormEntity(this.curTarget)[1];
            float smoothYaw = (float)MathUtil.randomDouble((double)yaw - (double)new Random().nextFloat() * 0.1, (double)yaw + (double)new Random().nextFloat() * 0.1);
            float smoothPitch = (float)MathUtil.randomDouble((double)pitch - (double)new Random().nextFloat() * 0.1, (double)pitch + (double)new Random().nextFloat() * 0.1);
            this.mc.thePlayer.rotationYawHead = smoothYaw;
            Client.instance.rotationPitchHead = smoothPitch;
            this.mc.thePlayer.renderYawOffset = smoothYaw;
            this.mc.thePlayer.prevRenderYawOffset = smoothYaw;
            event.setYaw(smoothYaw);
            event.setPitch(smoothPitch);
        }
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

    public float[] getRotationFormEntity(EntityLivingBase target) {
        return RotationUtil.getPredictedRotations(target);
    }

    @EventHandler
    private void onUpdatePost(EventPostUpdate e) {
        if (((Boolean)this.mouseDown.getValue()).booleanValue() && !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        if (((Boolean)this.pre.getValue()).booleanValue()) {
            return;
        }
        if (this.curTarget != null) {
            boolean autoblock;
            if (this.cpsReady) {
                if (this.hasSword() && this.mc.thePlayer.isBlocking() && this.isValidEntity(this.curTarget)) {
                    this.unBlock();
                }
                this.attack();
            }
            boolean bl = autoblock = Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() || Mouse.isButtonDown(1) && this.mc.currentScreen == null && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
            if (!this.mc.thePlayer.isBlocking() && this.hasSword() && autoblock) {
                this.block();
            }
        }
    }

    private void attack() {
        boolean autoblock;
        boolean bl = autoblock = Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() || Mouse.isButtonDown(1) && this.mc.currentScreen == null && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
        if (((Boolean)this.noSwing.getValue()).booleanValue() && autoblock) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        } else {
            this.mc.thePlayer.swingItem();
        }
        this.mc.thePlayer.onEnchantmentCritical(this.curTarget);
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)this.curTarget, C02PacketUseEntity.Action.ATTACK));
        if (this.cpsReady) {
            this.attackTimer.reset();
        }
        if (this.isBlocking && !autoblock) {
            this.unBlock();
        }
    }

    public List<Entity> getTargets(Double value) {
        if (this.mode.getValue() != AuraMode.Multi) {
            this.mc.theWorld.loadedEntityList.sort(Comparator.comparingDouble(this::lambda$getTargets$2));
            return this.mc.theWorld.loadedEntityList.subList(0, this.mc.theWorld.loadedEntityList.size() > 4 ? 4 : this.mc.theWorld.loadedEntityList.size()).stream().filter(this::lambda$getTargets$3).collect(Collectors.toList());
        }
        return this.mc.theWorld.loadedEntityList.stream().filter(arg_0 -> this.lambda$getTargets$4(value, arg_0)).collect(Collectors.toList());
    }

    private boolean isValidEntity(Entity ent) {
        if (ent == this.mc.thePlayer) {
            return false;
        }
        if ((double)this.mc.thePlayer.getDistanceToEntity(ent) > (Double)this.range.getValue()) {
            return false;
        }
        if (!ent.isEntityAlive()) {
            return false;
        }
        if (RotationUtil.isInFov(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, ent.posX, ent.posY, ent.posZ) > (Double)this.fov.getValue()) {
            return false;
        }
        AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (ab.isEntityBot(ent)) {
            return false;
        }
        if (ent instanceof EntityPlayer && ((Boolean)this.players.getValue()).booleanValue() && !this.isOnSameTeam(ent)) {
            return true;
        }
        if (ent.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue()) {
            return false;
        }
        if (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getName()) && ((Boolean)this.friend.getValue()).booleanValue()) {
            return false;
        }
        if ((ent instanceof EntityMob || ent instanceof EntityGhast || ent instanceof EntityGolem || ent instanceof EntityDragon || ent instanceof EntitySlime) && ((Boolean)this.mobs.getValue()).booleanValue()) {
            return true;
        }
        if ((ent instanceof EntitySquid || ent instanceof EntityBat || ent instanceof EntityVillager) && ((Boolean)this.animals.getValue()).booleanValue()) {
            return true;
        }
        return ent instanceof EntityAnimal && (Boolean)this.animals.getValue() != false;
    }

    public boolean isOnSameTeam(Entity entity) {
        if (!((Boolean)this.team.getValue()).booleanValue()) {
            return false;
        }
        if (this.mc.thePlayer.getDisplayName().getUnformattedText().startsWith("\u00a7")) {
            if (this.mc.thePlayer.getDisplayName().getUnformattedText().length() <= 2 || entity.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }
            if (this.mc.thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
                return true;
            }
        }
        return false;
    }

    private boolean lambda$getTargets$4(Double value, Entity e) {
        return (double)this.mc.thePlayer.getDistanceToEntity(e) <= value && this.isValidEntity(e);
    }

    private boolean lambda$getTargets$3(Entity e) {
        return this.isValidEntity(e);
    }

    private double lambda$getTargets$2(Entity e) {
        return this.mc.thePlayer.getDistanceToEntity(e);
    }

    private boolean lambda$onAuraLoad$1(Entity e) {
        return (double)this.mc.thePlayer.getDistanceToEntity(e) > (Double)this.range.getValue() || !e.isEntityAlive() || e.isDead;
    }

    private double lambda$new$0(Entity e2) {
        return e2.getDistanceToEntity(this.mc.thePlayer);
    }

    static enum rotationMode {
        Tick,
        Packet;

    }

    static enum AuraMode {
        Multi,
        Single;

    }
}

