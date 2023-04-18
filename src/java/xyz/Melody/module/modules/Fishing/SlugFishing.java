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
import net.minecraft.network.play.INetHandlerPlayClient;
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
        PlayerList playerList;
        if (((Boolean)this.unGrab.getValue()).booleanValue()) {
            Client.ungrabMouse();
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit == null) {
            this.lockedVec = this.mc.objectMouseOver.hitVec;
        }
        if (!(playerList = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class)).isEnabled() && ((Boolean)this.plist.getValue()).booleanValue()) {
            playerList.setEnabled(true);
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
    private void onPlayerDetected(EventTick eventTick) {
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
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        if (string.startsWith("From [ADMIN]") || string.startsWith("From [GM]") || string.startsWith("From [YOUTUBE]")) {
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
    private void onLockRod(EventTick eventTick) {
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
    private void lockView(EventRender2D eventRender2D) {
        if (!((Boolean)this.lockView.getValue()).booleanValue()) {
            return;
        }
        if (this.currentStage != stage.NONE) {
            Rotation rotation = this.vec3ToRotation(this.lockedVec);
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, rotation.yaw, 30.0f);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, rotation.pitch, 30.0f);
        }
    }

    @EventHandler
    private void onDebugDraw(EventRender2D eventRender2D) {
        if (!((Boolean)this.showDebug.getValue()).booleanValue()) {
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.mc.fontRendererObj.drawString("Current Stage: " + this.currentStage, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 6, -1);
        this.mc.fontRendererObj.drawString("Time: " + (this.secondTimer.getCurrentMS() - this.secondTimer.getLastMS()) / 1000L, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 18, -1);
        this.mc.fontRendererObj.drawString("SoundCDTimer: " + this.tickTimer1 + (this.tickTimer1 == 50 ? " (Ready)" : ""), scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 30, -1);
        this.mc.fontRendererObj.drawString("SoundMonitor: " + this.soundReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 54, -1);
        this.mc.fontRendererObj.drawString("SoundReady: " + this.soundCDReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 66, -1);
        this.mc.fontRendererObj.drawString("MotionReady: " + this.motionReady, scaledResolution.getScaledWidth() / 2 + 6, scaledResolution.getScaledHeight() / 2 + 78, -1);
    }

    @EventHandler
    private void onSneak(EventTick eventTick) {
        if (((Boolean)this.holdShift.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    private void swapEmber(int n) {
        Helper.sendMessage("Swap to Ember Armor.");
        this.openWD(1, n);
        this.swapedToEmberArmor = true;
    }

    private void swapTrophy(int n) {
        Helper.sendMessage("Swap to Trophy Hunter Armor.");
        this.openWD(1, n);
        this.swapedToTrophyArmor = true;
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
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
    public void onPacket(EventPacketRecieve eventPacketRecieve) {
        Packet<INetHandlerPlayClient> packet;
        if (this.currentStage != stage.WAITING) {
            return;
        }
        Packet<?> packet2 = eventPacketRecieve.getPacket();
        if (packet2 instanceof S29PacketSoundEffect && (((S29PacketSoundEffect)(packet = (S29PacketSoundEffect)packet2)).getSoundName().contains("game.player.swim.splash") || ((S29PacketSoundEffect)packet).getSoundName().contains("random.splash"))) {
            float f = ((Double)this.soundRadius.getValue()).floatValue();
            if (Math.abs(((S29PacketSoundEffect)packet).getX() - this.mc.thePlayer.fishEntity.posX) <= (double)f && Math.abs(((S29PacketSoundEffect)packet).getZ() - this.mc.thePlayer.fishEntity.posZ) <= (double)f) {
                this.soundReady = true;
                this.soundVec = new Vec3(((S29PacketSoundEffect)packet).getX(), ((S29PacketSoundEffect)packet).getY(), ((S29PacketSoundEffect)packet).getZ());
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
    }

    @EventHandler
    private void onCDReady(EventTick eventTick) {
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

    public void openWD(int n, int n2) {
        this.page = n;
        this.slot = n2;
        this.shouldOpenWardrobe = true;
        this.mc.thePlayer.sendChatMessage("/pets");
        this.wdFaileTimer.reset();
    }

    @EventHandler
    public void onDrawGuiBackground(EventTick eventTick) {
        String string;
        Container container;
        GuiScreen guiScreen = this.mc.currentScreen;
        if (Client.inSkyblock && this.shouldOpenWardrobe && guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).inventorySlots) instanceof ContainerChest && (string = this.getGuiName(guiScreen)).endsWith("Pets")) {
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

    public String getGuiName(GuiScreen guiScreen) {
        if (guiScreen instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)guiScreen).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    private void clickSlot(int n, int n2) {
        this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId + n2, n, 0, 0, this.mc.thePlayer);
    }

    private boolean playerInRange() {
        if (!((Boolean)this.escape.getValue()).booleanValue()) {
            return false;
        }
        for (EntityPlayer entityPlayer : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || FriendManager.isFriend(entityPlayer.getName()) || !(this.mc.thePlayer.getDistanceToEntity(entityPlayer) < (float)((Double)this.escapeRange.getValue()).intValue()) || entityPlayer == this.mc.thePlayer) continue;
            return true;
        }
        return false;
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

    public Rotation vec3ToRotation(Vec3 vec3) {
        double d = vec3.xCoord - this.mc.thePlayer.posX;
        double d2 = vec3.yCoord - this.mc.thePlayer.posY - (double)this.mc.thePlayer.getEyeHeight();
        double d3 = vec3.zCoord - this.mc.thePlayer.posZ;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(-Math.atan2(d4, d2));
        float f2 = (float)Math.atan2(d3, d);
        f = (float)SlugFishing.wrapAngleTo180(((double)(f * 180.0f) / Math.PI + 90.0) * -1.0);
        f2 = (float)SlugFishing.wrapAngleTo180((double)(f2 * 180.0f) / Math.PI - 90.0);
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

