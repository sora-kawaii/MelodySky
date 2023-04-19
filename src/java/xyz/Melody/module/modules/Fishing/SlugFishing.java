/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.Fishing;

import java.awt.Color;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
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
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.modules.others.PlayerList;

public final class SlugFishing
extends Module {
    private int tickTimer1 = 0;
    private Vec3 soundVec = null;
    private Numbers<Double> emberSlot = new Numbers<Double>("Ember Slot", 5.0, 0.0, 20.0, 1.0);
    private Numbers<Double> trophySlot = new Numbers<Double>("Trophy Slot", 5.0, 0.0, 20.0, 1.0);
    private Numbers<Double> emberDelay = new Numbers<Double>("EmberSwapDelay", 750.0, 250.0, 1500.0, 10.0);
    private Option<Boolean> plist = new Option<Boolean>("EnablePlayerList", true);
    private Option<Boolean> unGrab = new Option<Boolean>("UnGrabMouse", false);
    private Option<Boolean> lockRod = new Option<Boolean>("LockRod", false);
    private Option<Boolean> admin = new Option<Boolean>("AntiAdmin", false);
    private Option<Boolean> lockView = new Option<Boolean>("LockView", false);
    private Option<Boolean> dead = new Option<Boolean>("DeathCheck", true);
    private Option<Boolean> escape = new Option<Boolean>("Escape", false);
    private Numbers<Double> escapeRange = new Numbers<Double>("Escape Range", 5.0, 0.0, 20.0, 1.0);
    private Option<Boolean> showDebug = new Option<Boolean>("Show Debug", false);
    private Mode<Enum> rotationMode = new Mode("RotationMode", (Enum[])rotations.values(), (Enum)rotations.Yaw);
    private Option<Boolean> rotation = new Option<Boolean>("NoRotationAFK", true);
    private Mode<Enum> moveMode = new Mode("MoveMode", (Enum[])moves.values(), (Enum)moves.AD);
    private Option<Boolean> move = new Option<Boolean>("NoMovingAFK", true);
    private Option<Boolean> holdShift = new Option<Boolean>("Sneaking", false);
    private Numbers<Double> angle = new Numbers<Double>("RotationAngle", 1.0, 1.0, 5.0, 1.0);
    private Option<Boolean> soundBB = new Option<Boolean>("SoundBox", false);
    private Numbers<Double> soundRadius = new Numbers<Double>("SoundRadius", 0.5, 0.1, 5.0, 0.1);
    private Enum<?> currentStage = stage.NONE;
    private boolean backRotaion = false;
    private boolean soundReady = false;
    private boolean soundCDReady = false;
    private boolean motionReady = false;
    private Vec3 lockedVec = new Vec3(0.0, 0.0, 0.0);
    private TimerUtil moveTimer = new TimerUtil();
    private boolean moveDone = false;
    private boolean moved = false;
    private boolean needToEscape = false;
    private TimerUtil escapeDelay = new TimerUtil();
    private boolean swapedToEmberArmor = false;
    private boolean swapedToTrophyArmor = false;
    private TimerUtil secondTimer = new TimerUtil();
    private TimerUtil finishedTimer = new TimerUtil();
    private TimerUtil throwFaileTimer = new TimerUtil();
    private TimerUtil wdFaileTimer = new TimerUtil();
    private TimerUtil emberTimer = new TimerUtil();
    private TimerUtil timerTimer = new TimerUtil();
    private int page = 0;
    private int slot = 0;
    private boolean shouldOpenWardrobe = false;
    private boolean escaped = false;

    public SlugFishing() {
        super("SlugFishing", new String[]{"slug"}, ModuleType.Fishing);
        this.addValues(this.emberSlot, this.trophySlot, this.emberDelay, this.plist, this.unGrab, this.lockRod, this.lockView, this.dead, this.admin, this.escape, this.escapeRange, this.showDebug, this.rotationMode, this.rotation, this.moveMode, this.move, this.holdShift, this.angle, this.soundBB, this.soundRadius);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Auto Swap Ember/Trophy Armor and Fishing.");
    }

    @Override
    public void onEnable() {
        PlayerList pl;
        if (((Boolean)this.unGrab.getValue()).booleanValue()) {
            Client.ungrabMouse();
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit == null) {
            this.lockedVec = this.mc.objectMouseOver.hitVec;
        }
        if (!(pl = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class)).isEnabled() && ((Boolean)this.plist.getValue()).booleanValue()) {
            pl.setEnabled(true);
        }
        this.emberTimer.reset();
        this.timerTimer.reset();
        this.throwFaileTimer.reset();
        this.finishedTimer.reset();
        this.secondTimer.reset();
        this.wdFaileTimer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        PlayerList pl;
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        if (((Boolean)this.unGrab.getValue()).booleanValue()) {
            Client.regrabMouse();
        }
        if ((pl = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class)).isEnabled() && ((Boolean)this.plist.getValue()).booleanValue()) {
            pl.setEnabled(false);
        }
        this.page = 0;
        this.slot = 0;
        this.shouldOpenWardrobe = false;
        this.emberTimer.reset();
        this.timerTimer.reset();
        this.throwFaileTimer.reset();
        this.finishedTimer.reset();
        this.secondTimer.reset();
        this.wdFaileTimer.reset();
        this.needToEscape = false;
        this.lockedVec = new Vec3(0.0, 0.0, 0.0);
        this.moveDone = false;
        this.escaped = false;
        this.tickTimer1 = 0;
        this.soundVec = null;
        this.currentStage = stage.NONE;
        this.backRotaion = false;
        this.soundReady = false;
        this.soundCDReady = false;
        this.motionReady = false;
        this.escapeDelay.reset();
        this.escaped = false;
        super.onDisable();
    }

    @EventHandler
    private void onPlayerDetected(EventTick event) {
        if (this.playerInRange()) {
            this.needToEscape = true;
        }
        if (((Boolean)this.escape.getValue()).booleanValue() && !this.escaped) {
            if (this.needToEscape && this.escapeDelay.hasReached(5000.0)) {
                Helper.sendMessage("[AutoFish] Player Detected, Warping to Private Island.");
                this.mc.thePlayer.sendChatMessage("/l");
                this.escaped = true;
                this.setEnabled(false);
                this.escapeDelay.reset();
            }
            if (!this.needToEscape) {
                this.escapeDelay.reset();
            }
        }
        if (((Boolean)this.admin.getValue()).booleanValue() && PlayerListUtils.tabContains("[ADMIN]")) {
            Helper.sendMessage("[AutoFish] Admin Detected, Warping to Private Island.");
            this.mc.thePlayer.sendChatMessage("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.startsWith("From [ADMIN]") || message.startsWith("From [GM]") || message.startsWith("From [YOUTUBE]")) {
            Helper.sendMessage("[AutoFish] Admin Detected, Quitting Server.");
            boolean flag = this.mc.isIntegratedServerRunning();
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            if (flag) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @EventHandler
    private void onDead(EventTick event) {
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
    private void onLockRod(EventTick event) {
        if (!((Boolean)this.lockRod.getValue()).booleanValue()) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemFishingRod)) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            break;
        }
    }

    @EventHandler
    private void lockView(EventRender2D event) {
        if (!((Boolean)this.lockView.getValue()).booleanValue()) {
            return;
        }
        if (this.currentStage != stage.NONE) {
            Rotation r = this.vec3ToRotation(this.lockedVec);
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, r.yaw, 30.0f);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, r.pitch, 30.0f);
        }
    }

    @EventHandler
    private void onDebugDraw(EventRender2D event) {
        if (!((Boolean)this.showDebug.getValue()).booleanValue()) {
            return;
        }
        ScaledResolution scale = new ScaledResolution(this.mc);
        this.mc.fontRendererObj.drawString("Current Stage: " + this.currentStage, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 6, -1);
        this.mc.fontRendererObj.drawString("Time: " + (this.secondTimer.getCurrentMS() - this.secondTimer.getLastMS()) / 1000L, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 18, -1);
        this.mc.fontRendererObj.drawString("SoundCDTimer: " + this.tickTimer1 + (this.tickTimer1 == 50 ? " (Ready)" : ""), scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 30, -1);
        this.mc.fontRendererObj.drawString("SoundMonitor: " + this.soundReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 54, -1);
        this.mc.fontRendererObj.drawString("SoundReady: " + this.soundCDReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 66, -1);
        this.mc.fontRendererObj.drawString("MotionReady: " + this.motionReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 78, -1);
    }

    @EventHandler
    private void onSneak(EventTick event) {
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    private void swapEmber(int slot) {
        Helper.sendMessage("Swap to Ember Armor.");
        this.openWD(1, slot);
        this.swapedToEmberArmor = true;
    }

    private void swapTrophy(int slot) {
        Helper.sendMessage("Swap to Trophy Hunter Armor.");
        this.openWD(1, slot);
        this.swapedToTrophyArmor = true;
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (this.currentStage == stage.NONE) {
            if (this.mc.thePlayer.getHeldItem() == null || !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)) {
                Helper.sendMessage("[Slug Fishing] Please Hold a Fishing rod to use This Feature.");
                this.setEnabled(false);
                return;
            }
            this.currentStage = stage.EMBER;
        }
        if (this.currentStage == stage.FINISH) {
            this.soundReady = false;
            this.soundCDReady = false;
            this.motionReady = false;
            this.tickTimer1 = 0;
            if (this.finishedTimer.hasReached(200.0)) {
                this.currentStage = stage.EMBER;
                this.finishedTimer.reset();
                this.emberTimer.reset();
            }
        }
        if (this.currentStage == stage.EMBER && this.emberTimer.hasReached(200.0)) {
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
            this.swapEmber(((Double)this.emberSlot.getValue()).intValue());
            if (this.swapedToEmberArmor) {
                this.currentStage = stage.TIMER;
            }
            this.emberTimer.reset();
            this.timerTimer.reset();
        }
        if (this.currentStage == stage.TIMER && this.timerTimer.hasReached(((Double)this.emberDelay.getValue()).longValue())) {
            if (this.swapedToEmberArmor) {
                if (this.mc.thePlayer.fishEntity == null) {
                    Client.rightClick();
                }
                this.swapedToEmberArmor = false;
                this.secondTimer.reset();
            }
            if (this.secondTimer.hasReached(25000.0)) {
                this.currentStage = stage.TROPHY;
                this.secondTimer.reset();
            }
            this.timerTimer.reset();
        }
        if (this.currentStage == stage.TROPHY) {
            this.swapTrophy(((Double)this.trophySlot.getValue()).intValue());
            if (this.swapedToTrophyArmor) {
                this.finishedTimer.reset();
                this.throwFaileTimer.reset();
                this.currentStage = stage.WAITING;
            }
        }
        if (this.currentStage == stage.WAITING) {
            this.swapedToTrophyArmor = false;
            if (this.soundCDReady && this.motionReady) {
                if (this.mc.thePlayer.fishEntity != null) {
                    Client.rightClick();
                }
                this.finishedTimer.reset();
                this.throwFaileTimer.reset();
                this.currentStage = stage.FINISH;
            }
        }
        if (this.mc.thePlayer.getHeldItem() == null || !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)) {
            this.currentStage = stage.NONE;
        }
        if (this.currentStage == stage.NONE) {
            this.reset();
            this.tickTimer1 = 0;
            this.soundVec = null;
        }
        if (this.currentStage == stage.WAITING && this.mc.thePlayer.fishEntity == null && this.throwFaileTimer.hasReached(500.0)) {
            this.currentStage = stage.NONE;
            this.throwFaileTimer.reset();
        }
    }

    @EventHandler
    public void onPacket(EventPacketRecieve e) {
        S12PacketEntityVelocity velocity;
        S29PacketSoundEffect sound;
        if (this.currentStage != stage.WAITING) {
            return;
        }
        Packet<?> packet = e.getPacket();
        if (packet instanceof S29PacketSoundEffect && ((sound = (S29PacketSoundEffect)packet).getSoundName().contains("game.player.swim.splash") || sound.getSoundName().contains("random.splash"))) {
            float radius = ((Double)this.soundRadius.getValue()).floatValue();
            if (Math.abs(sound.getX() - this.mc.thePlayer.fishEntity.posX) <= (double)radius && Math.abs(sound.getZ() - this.mc.thePlayer.fishEntity.posZ) <= (double)radius) {
                this.soundReady = true;
                this.soundVec = new Vec3(sound.getX(), sound.getY(), sound.getZ());
            }
        }
        if (packet instanceof S12PacketEntityVelocity && (velocity = (S12PacketEntityVelocity)packet).getMotionX() == 0 && velocity.getMotionY() != 0 && velocity.getMotionZ() == 0) {
            this.motionReady = true;
        }
    }

    @EventHandler
    private void onMove(EventTick event) {
        if (((Boolean)this.move.getValue()).booleanValue()) {
            int secondStep;
            int firstStep = this.moveMode.getValue() == moves.AD ? this.mc.gameSettings.keyBindLeft.getKeyCode() : this.mc.gameSettings.keyBindForward.getKeyCode();
            int n = secondStep = this.moveMode.getValue() == moves.AD ? this.mc.gameSettings.keyBindRight.getKeyCode() : this.mc.gameSettings.keyBindBack.getKeyCode();
            if (!this.moveDone) {
                if (this.currentStage == stage.FINISH && !this.moved) {
                    this.moveTimer.reset();
                    KeyBinding.setKeyBindState(firstStep, true);
                    this.moved = true;
                }
                if (this.moved && this.moveTimer.hasReached(50.0)) {
                    KeyBinding.setKeyBindState(firstStep, false);
                    if (this.moveTimer.hasReached(100.0)) {
                        KeyBinding.setKeyBindState(secondStep, true);
                        if (this.moveTimer.hasReached(150.0)) {
                            KeyBinding.setKeyBindState(secondStep, false);
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
    private void onReady(EventTick event) {
    }

    @EventHandler
    private void onCDReady(EventTick event) {
        if (this.currentStage == stage.WAITING) {
            if (this.tickTimer1 < 50) {
                this.soundCDReady = false;
                this.soundReady = false;
                ++this.tickTimer1;
            } else {
                this.soundCDReady = this.soundReady;
            }
        }
    }

    @EventHandler
    private void onR3D(EventRender3D event) {
        if (!((Boolean)this.soundBB.getValue()).booleanValue()) {
            return;
        }
        if (this.mc.thePlayer.fishEntity != null) {
            AxisAlignedBB fishingEntityBB = this.mc.thePlayer.fishEntity.getEntityBoundingBox().expand((Double)this.soundRadius.getValue(), 0.0, (Double)this.soundRadius.getValue());
            RenderUtil.drawOutlinedBoundingBox(fishingEntityBB, Colors.AQUA.c, 2.0f, event.getPartialTicks());
        }
        if (this.soundVec != null) {
            AxisAlignedBB soundVecBB = new AxisAlignedBB(this.soundVec.xCoord + 0.05, this.soundVec.yCoord + 0.05, this.soundVec.zCoord + 0.05, this.soundVec.xCoord - 0.05, this.soundVec.yCoord - 0.05, this.soundVec.zCoord - 0.05);
            RenderUtil.drawOutlinedBoundingBox(soundVecBB, Colors.RED.c, 2.0f, event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void reset() {
        this.soundReady = false;
        this.soundCDReady = false;
        this.motionReady = false;
    }

    public void openWD(int page, int slot) {
        this.page = page;
        this.slot = slot;
        this.shouldOpenWardrobe = true;
        this.mc.thePlayer.sendChatMessage("/pets");
        this.wdFaileTimer.reset();
    }

    @EventHandler
    public void onDrawGuiBackground(EventTick event) {
        String chestName;
        Container container;
        GuiScreen gui = this.mc.currentScreen;
        if (Client.inSkyblock && this.shouldOpenWardrobe && gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest && (chestName = this.getGuiName(gui)).endsWith("Pets")) {
            this.clickSlot(48, 0);
            this.clickSlot(32, 1);
            if (this.page == 1) {
                if (this.slot > 0 && this.slot < 10) {
                    this.clickSlot(this.slot + 35, 2);
                    this.mc.thePlayer.closeScreen();
                }
            } else if (this.page == 2) {
                this.clickSlot(53, 2);
                if (this.slot > 0 && this.slot < 10) {
                    this.clickSlot(this.slot + 35, 3);
                    this.mc.thePlayer.closeScreen();
                }
            }
            this.shouldOpenWardrobe = false;
        }
    }

    public String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    private void clickSlot(int slot, int incrementWindowId) {
        this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId + incrementWindowId, slot, 0, 0, this.mc.thePlayer);
    }

    private boolean playerInRange() {
        if (!((Boolean)this.escape.getValue()).booleanValue()) {
            return false;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(player) || FriendManager.isFriend(player.getName()) || !(this.mc.thePlayer.getDistanceToEntity(player) < (float)((Double)this.escapeRange.getValue()).intValue()) || player == this.mc.thePlayer) continue;
            return true;
        }
        return false;
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
        pitch = (float)SlugFishing.wrapAngleTo180(((double)(pitch * 180.0f) / Math.PI + 90.0) * -1.0);
        yaw = (float)SlugFishing.wrapAngleTo180((double)(yaw * 180.0f) / Math.PI - 90.0);
        return new Rotation(pitch, yaw);
    }

    private static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360.0 + 0.5) * 360.0;
    }

    private static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }

    static enum stage {
        NONE,
        EMBER,
        TIMER,
        TROPHY,
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

