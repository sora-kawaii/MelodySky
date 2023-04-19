/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.ColorUtils;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.gl.GLUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.modules.macros.GemstoneNuker;
import xyz.Melody.module.modules.macros.MithrilNuker;

public final class AutoRuby
extends Module {
    private TimerUtil timer = new TimerUtil();
    private TimerUtil ewTimer = new TimerUtil();
    public ArrayList<BlockPos> wps = new ArrayList();
    private Mode<Enum> mode = new Mode("Mode", (Enum[])GemstoneNuker.Gemstone.values(), (Enum)GemstoneNuker.Gemstone.RUBY);
    private Option<Boolean> hideCoords = new Option<Boolean>("HideCoords", false);
    private Option<Boolean> mithril = new Option<Boolean>("Mithril", false);
    private Option<Boolean> admin = new Option<Boolean>("AntiAdmin", false);
    private Option<Boolean> ugm = new Option<Boolean>("UnGrabMouse", false);
    private Option<Boolean> killYogs = new Option<Boolean>("KillYogs", true);
    private Option<Boolean> rcKill = new Option<Boolean>("RCKillYogs", false);
    private Option<Boolean> aim = new Option<Boolean>("RCAimYogs", false);
    private Option<Boolean> faceDown = new Option<Boolean>("RCFaceDown", false);
    private Numbers<Double> yogRange = new Numbers<Double>("YogRange", 3.5, 1.0, 4.5, 0.5);
    private Numbers<Double> weaponSlot = new Numbers<Double>("WeaponSlot", 3.0, 1.0, 8.0, 1.0);
    private Option<Boolean> routeHelper = new Option<Boolean>("RouteHelper", true);
    private Option<Boolean> protect = new Option<Boolean>("MacroProtect", true);
    private Option<Boolean> refill = new Option<Boolean>("AutoRefill", true);
    private Option<Boolean> restart = new Option<Boolean>("AutoRestart", true);
    private boolean etherWarped = false;
    private int curIndex = 0;
    private BlockPos curBP;
    private BlockPos nextBP;
    public boolean started = false;
    private TimerUtil restartTimer = new TimerUtil();
    private boolean playerDetected = false;
    private int lastSlot = 0;
    private boolean restore = false;
    private boolean shouldSwitchToAbi = false;
    private boolean shouldCallSB = false;
    private boolean shouldClickFuel = false;
    private boolean shouldClickDrill = false;
    private boolean shouldCombind = false;
    private int drillSlot = 0;
    private int fuelSlot = 0;
    private ArrayList<EntityMagmaCube> yogs = new ArrayList();
    private TimerUtil attackTimer = new TimerUtil();
    private boolean killingYogs = false;
    private int ticks = 0;
    private TimerUtil fallSafeTimer = new TimerUtil();

    public AutoRuby() {
        super("AutoGemstone", new String[]{""}, ModuleType.Macros);
        this.addValues(this.mode, this.hideCoords, this.mithril, this.admin, this.ugm, this.routeHelper, this.protect, this.refill, this.restart, this.killYogs, this.yogRange, this.weaponSlot, this.rcKill, this.aim, this.faceDown);
        this.setModInfo("Auto Mine Gemstone.");
    }

    @EventHandler
    private void onAdminDetected(EventTick event) {
        if (((Boolean)this.admin.getValue()).booleanValue() && PlayerListUtils.tabContains("[ADMIN]")) {
            Helper.sendMessage("[AutoGemstone] Admin Detected, Warping to Main Lobby.");
            NotificationPublisher.queue("Admin Detected", "An Admin Joined Your Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            this.mc.thePlayer.sendChatMessage("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onAdminChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (!((Boolean)this.admin.getValue()).booleanValue()) {
            return;
        }
        for (String name : FriendManager.getFriends().keySet()) {
            if (!message.contains(name)) continue;
            return;
        }
        if (message.startsWith("From [ADMIN]") || message.startsWith("From [GM]") || message.startsWith("From [YOUTUBE]")) {
            NotificationPublisher.queue("Admin Detected", "An Admin msged you, Quitting Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            Helper.sendMessage("[AutoGemstone] Admin Detected, Quitting Server.");
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

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent event) {
        if (!((Boolean)this.refill.getValue()).booleanValue()) {
            this.shouldSwitchToAbi = false;
            return;
        }
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (this.started && message.contains("is empty! Refuel it by talking to a Drill Mechanic!")) {
            NotificationPublisher.queue("AutoRuby Drill Empty.", "Trying To Refill Your Drill.", NotificationType.WARN, 10000);
            this.started = false;
            this.restore = true;
            this.shouldSwitchToAbi = true;
        }
    }

    @EventHandler
    private void onKillYog(EventPreUpdate event) {
        if (((Boolean)this.killYogs.getValue()).booleanValue()) {
            this.loadYogs();
        } else if (!this.yogs.isEmpty()) {
            this.yogs.clear();
        }
        if (!this.yogs.isEmpty()) {
            EntityMagmaCube mcube = this.yogs.get(0);
            if (this.started) {
                NotificationPublisher.queue("AutoRuby", "Yog Detected, Trying to FUCK it.", NotificationType.WARN, 3000);
                this.started = false;
                this.killingYogs = true;
                this.attackTimer.reset();
            }
            if (mcube != null && mcube.isEntityAlive() && this.killingYogs) {
                this.mc.thePlayer.inventory.currentItem = ((Double)this.weaponSlot.getValue()).intValue() - 1;
                if (((Boolean)this.rcKill.getValue()).booleanValue()) {
                    if (((Boolean)this.faceDown.getValue()).booleanValue()) {
                        event.setPitch(90.0f);
                        if (this.attackTimer.hasReached(180.0)) {
                            Client.rightClick();
                            this.attackTimer.reset();
                        }
                    } else {
                        if (((Boolean)this.aim.getValue()).booleanValue()) {
                            float[] r = RotationUtil.getPredictedRotations(mcube);
                            event.setYaw(r[0]);
                            event.setPitch(r[1]);
                        }
                        if (this.attackTimer.hasReached(180.0)) {
                            Client.rightClick();
                            this.attackTimer.reset();
                        }
                    }
                } else {
                    float[] r = RotationUtil.getPredictedRotations(mcube);
                    event.setYaw(r[0]);
                    event.setPitch(r[1]);
                    if (this.attackTimer.hasReached(180.0)) {
                        this.mc.thePlayer.swingItem();
                        this.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity((Entity)mcube, C02PacketUseEntity.Action.ATTACK));
                        this.attackTimer.reset();
                    }
                }
            }
        } else if (this.killingYogs) {
            NotificationPublisher.queue("AutoRuby", "OKAY, Continued Mining..", NotificationType.SUCCESS, 3000);
            this.started = true;
            this.killingYogs = false;
            this.attackTimer.reset();
        }
    }

    @EventHandler
    public void onDrawGuiBackground(EventTick event) {
        Container container;
        if (this.ticks < 20) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        if (this.shouldSwitchToAbi) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
                if (itemStack != null && itemStack.getItem() != null && ItemUtils.getSkyBlockID(itemStack).startsWith("ABIPHONE")) {
                    this.mc.thePlayer.inventory.currentItem = i;
                    break;
                }
                if (i != 8) continue;
                NotificationPublisher.queue("AutoRuby Auto Refill.", "No Abiphone Found in Hotbar.", NotificationType.ERROR, 10000);
            }
            Client.rightClick();
            this.shouldCallSB = true;
            this.shouldSwitchToAbi = false;
        }
        GuiScreen gui = this.mc.currentScreen;
        if (Client.inSkyblock && gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            String chestName = this.getGuiName(gui);
            if (chestName.startsWith("Abiphone") && this.shouldCallSB) {
                List<Slot> slots = container.inventorySlots;
                for (Slot slot : slots) {
                    ItemStack is = slot.getStack();
                    if (is == null || !is.hasDisplayName() || !StringUtils.stripControlCodes(is.getDisplayName()).equals("Jotraeline Greatforge")) continue;
                    this.clickSlot(slot.slotNumber, 0, 0, 0);
                    this.shouldCallSB = false;
                    this.shouldCombind = false;
                    break;
                }
            }
            if (chestName.equals("Drill Anvil")) {
                if (!this.shouldCombind) {
                    for (int i = 0; i < 9; ++i) {
                        ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
                        if (itemStack == null || itemStack.getItem() == null) continue;
                        if (ItemUtils.getSkyBlockID(itemStack).contains("DRILL") && !container.inventorySlots.get(29).getHasStack() && Block.getBlockFromItem(container.inventorySlots.get(13).getStack().getItem()) == Blocks.barrier) {
                            int slotNum = i + 1;
                            this.clickSlot(80 + slotNum, 0, 0, 1);
                            this.drillSlot = 80 + slotNum;
                            return;
                        }
                        if (!ItemUtils.getSkyBlockID(itemStack).contains("OIL_BARREL") || container.inventorySlots.get(33).getHasStack() || !container.inventorySlots.get(29).getHasStack()) continue;
                        int slotNum = i + 1;
                        this.clickSlot(80 + slotNum, 0, 0, 1);
                        this.fuelSlot = 80 + slotNum;
                        return;
                    }
                }
                if (container.inventorySlots.get(29).getHasStack() && container.inventorySlots.get(33).getHasStack()) {
                    this.clickSlot(22, 0, 0, 0);
                    this.shouldCombind = true;
                }
                if (this.shouldCombind && !container.inventorySlots.get(29).getHasStack()) {
                    if (!this.shouldClickDrill && Block.getBlockFromItem(container.inventorySlots.get(13).getStack().getItem()) != Blocks.barrier) {
                        this.clickSlot(13, 0, 0, 0);
                        this.shouldClickDrill = true;
                        return;
                    }
                    if (!this.shouldClickFuel) {
                        this.clickSlot(this.drillSlot, 0, 0, 0);
                        this.clickSlot(33, 0, 0, 0);
                        this.clickSlot(this.fuelSlot, 0, 0, 0);
                        this.shouldClickFuel = true;
                        return;
                    }
                    this.mc.thePlayer.closeScreen();
                    this.reset();
                    if (this.restore) {
                        this.started = true;
                        this.restore = false;
                    }
                }
            }
        }
    }

    private void reset() {
        this.shouldSwitchToAbi = false;
        this.shouldCallSB = false;
        this.shouldClickFuel = false;
        this.shouldClickDrill = false;
        this.shouldCombind = false;
    }

    @EventHandler
    private void tickWayPoints(EventTick e) {
        if (this.started) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
        if (((Boolean)this.restart.getValue()).booleanValue()) {
            if (this.playerDetected) {
                if (this.restartTimer.hasReached(10000.0)) {
                    this.started = true;
                    this.playerDetected = false;
                    this.restartTimer.reset();
                }
            } else {
                this.restartTimer.reset();
            }
        } else {
            this.playerDetected = false;
        }
        if (this.started && ((Boolean)this.protect.getValue()).booleanValue()) {
            boolean cancanneedmacro = false;
            String name = null;
            for (EntityPlayer ep : this.mc.theWorld.playerEntities) {
                AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
                if (!ab.isInTablist(ep)) continue;
                if (ep != this.mc.thePlayer && this.mc.thePlayer.canEntityBeSeen(ep)) {
                    cancanneedmacro = true;
                    name = ep.getName();
                    break;
                }
                cancanneedmacro = false;
            }
            if (cancanneedmacro) {
                this.started = false;
                this.playerDetected = true;
                WindowsNotification.show("AutoRuby Player Detected", "Player Name: " + name + ".");
                NotificationPublisher.queue("AutoRuby", "Player Detected, Auto Disabled.", NotificationType.WARN, 10000);
                Helper.sendMessage("Player Name: " + name + ".");
            }
        }
        if (Keyboard.isKeyDown(56) && this.mc.currentScreen == null && !this.started && this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null) {
            if (this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.air) {
                return;
            }
            if (!this.wps.contains(this.mc.objectMouseOver.getBlockPos())) {
                this.wps.add(this.mc.objectMouseOver.getBlockPos());
            }
        }
    }

    @EventHandler
    private void idk(EventTick event) {
        if (!((Boolean)this.mithril.getValue()).booleanValue()) {
            GemstoneNuker gsn = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            if (this.started) {
                for (int i = 0; i < this.wps.size(); ++i) {
                    BlockPos pos = this.wps.get(i);
                    BlockPos standing = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                    BlockPos wp = new BlockPos(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
                    BlockPos sp = new BlockPos(new Vec3i(standing.getX(), standing.getY(), standing.getZ()));
                    if (sp.getX() != wp.getX() || sp.getY() != wp.getY() || sp.getZ() != wp.getZ()) continue;
                    this.curIndex = i;
                    this.curBP = sp;
                }
                if (this.curBP == null) {
                    return;
                }
                BlockPos standing = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                BlockPos cp = new BlockPos(new Vec3i(this.curBP.getX(), this.curBP.getY(), this.curBP.getZ()));
                BlockPos sp = new BlockPos(new Vec3i(standing.getX(), standing.getY(), standing.getZ()));
                if ((float)Math.abs(cp.getX() - sp.getX()) > 0.2f || (float)Math.abs(cp.getY() - sp.getY()) > 0.2f || (float)Math.abs(cp.getZ() - sp.getZ()) > 0.2f) {
                    this.nextBP = cp;
                }
                if (this.curBP != new BlockPos(this.mc.thePlayer.getPositionVector()).down() && this.fallSafeTimer.hasReached(5000.0)) {
                    ArrayList waypoints = (ArrayList)this.wps.clone();
                    waypoints.sort(Comparator.comparingDouble(this::lambda$idk$0));
                }
                this.fallSafeTimer.reset();
                if (this.curBP == null) {
                    this.curBP = this.wps.get(this.curIndex);
                    if (new BlockPos(this.mc.thePlayer.getPositionVector()).down() != this.curBP) {
                        Helper.sendMessage("[AutoRuby] Something went wrong, Please enter '.ar start' again.");
                        this.started = false;
                        return;
                    }
                }
                if (!gsn.gemstones.isEmpty()) {
                    this.timer.reset();
                    this.ewTimer.reset();
                    this.etherWarped = false;
                    this.nextBP = null;
                    this.mc.thePlayer.inventory.currentItem = 0;
                } else {
                    if (this.nextBP == null) {
                        if (this.curIndex + 1 < this.wps.size()) {
                            this.nextBP = this.wps.get(this.curIndex + 1);
                        }
                        if (this.curIndex + 1 >= this.wps.size()) {
                            this.curIndex = -1;
                            this.nextBP = this.wps.get(this.curIndex + 1);
                        }
                        this.ewTimer.reset();
                        this.timer.reset();
                        this.etherWarped = false;
                    }
                    if (this.nextBP != null) {
                        float[] r = gsn.getRotations(this.nextBP, gsn.getClosestEnum(this.nextBP));
                        this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, r[0], 120.0f);
                        this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, r[1], 120.0f);
                        if (this.timer.hasReached(75.0)) {
                            if (this.timer.hasReached(150.0)) {
                                if (this.nextBP != null) {
                                    this.etherWarp(this.nextBP);
                                }
                                if (gsn.isEnabled()) {
                                    gsn.setEnabled(false);
                                }
                                if (this.timer.hasReached(650.0)) {
                                    ++this.curIndex;
                                    this.curBP = this.nextBP;
                                    gsn.setEnabled(true);
                                    this.nextBP = null;
                                    this.timer.reset();
                                }
                            } else {
                                this.ewTimer.reset();
                            }
                        }
                    }
                }
            }
        } else {
            MithrilNuker mn = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (this.started) {
                for (int i = 0; i < this.wps.size(); ++i) {
                    BlockPos pos = this.wps.get(i);
                    BlockPos standing = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                    BlockPos wp = new BlockPos(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
                    BlockPos sp = new BlockPos(new Vec3i(standing.getX(), standing.getY(), standing.getZ()));
                    if (sp.getX() != wp.getX() || sp.getY() != wp.getY() || sp.getZ() != wp.getZ()) continue;
                    this.curIndex = i;
                    this.curBP = sp;
                }
                if (this.curBP == null) {
                    return;
                }
                BlockPos standing = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                BlockPos cp = new BlockPos(new Vec3i(this.curBP.getX(), this.curBP.getY(), this.curBP.getZ()));
                BlockPos sp = new BlockPos(new Vec3i(standing.getX(), standing.getY(), standing.getZ()));
                if (cp.getX() != sp.getX() || cp.getY() != sp.getY() || cp.getZ() != sp.getZ()) {
                    this.nextBP = cp;
                }
                if (this.curBP != new BlockPos(this.mc.thePlayer.getPositionVector()).down() && this.fallSafeTimer.hasReached(5000.0)) {
                    ArrayList waypoints = (ArrayList)this.wps.clone();
                    waypoints.sort(Comparator.comparingDouble(this::lambda$idk$1));
                }
                this.fallSafeTimer.reset();
                if (this.curBP == null) {
                    this.curBP = this.wps.get(this.curIndex);
                    if (new BlockPos(this.mc.thePlayer.getPositionVector()).down() != this.curBP) {
                        Helper.sendMessage("[AutoRuby] Something went wrong, Please enter '.ar start' again.");
                        this.started = false;
                        return;
                    }
                }
                if (!mn.blockPoses.isEmpty()) {
                    this.timer.reset();
                    this.ewTimer.reset();
                    this.etherWarped = false;
                    this.nextBP = null;
                    this.mc.thePlayer.inventory.currentItem = 0;
                } else {
                    if (this.nextBP == null) {
                        if (this.curIndex + 1 < this.wps.size()) {
                            this.nextBP = this.wps.get(this.curIndex + 1);
                        }
                        if (this.curIndex + 1 >= this.wps.size()) {
                            this.curIndex = -1;
                            this.nextBP = this.wps.get(this.curIndex + 1);
                        }
                        this.ewTimer.reset();
                        this.timer.reset();
                        this.etherWarped = false;
                    }
                    if (this.nextBP != null) {
                        float[] r = mn.getRotations(this.nextBP, mn.getClosestEnum(this.nextBP));
                        this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, r[0], 120.0f);
                        this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, r[1], 120.0f);
                        if (this.timer.hasReached(75.0)) {
                            if (this.timer.hasReached(150.0)) {
                                if (this.nextBP != null) {
                                    this.etherWarp(this.nextBP);
                                }
                                if (mn.isEnabled()) {
                                    mn.setEnabled(false);
                                }
                                if (this.timer.hasReached(650.0)) {
                                    ++this.curIndex;
                                    this.curBP = this.nextBP;
                                    mn.setEnabled(true);
                                    this.nextBP = null;
                                    this.timer.reset();
                                }
                            } else {
                                this.ewTimer.reset();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void R3D(EventRender3D event) {
        for (int i = 0; i < this.wps.size(); ++i) {
            BlockPos pos = this.wps.get(i);
            BlockPos npos = null;
            npos = i + 1 == this.wps.size() ? this.wps.get(0) : this.wps.get(i + 1);
            if (pos == this.nextBP) {
                RenderUtil.drawSolidBlockESP(pos, ColorUtils.addAlpha(FadeUtil.PURPLE.getColor(), 190).getRGB(), 2.0f, event.getPartialTicks());
            } else if (pos == this.curBP) {
                RenderUtil.drawSolidBlockESP(pos, ColorUtils.addAlpha(FadeUtil.GREEN.getColor(), 190).getRGB(), 2.0f, event.getPartialTicks());
            } else {
                RenderUtil.drawSolidBlockESP(pos, ColorUtils.addAlpha(FadeUtil.BLUE.getColor(), 190).getRGB(), 2.0f, event.getPartialTicks());
            }
            String str = (Boolean)this.hideCoords.getValue() != false ? (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "#" + (i + 1) : (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "#" + (i + 1) + (Object)((Object)EnumChatFormatting.WHITE) + ": " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
            this.renderTag(pos, str);
            if (!((Boolean)this.routeHelper.getValue()).booleanValue()) continue;
            double nposX = (double)npos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double nposY = (double)npos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double nposZ = (double)npos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            double posX = (double)pos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double posY = (double)pos.getY() - this.mc.getRenderManager().viewerPosY + 1.0 + (double)this.mc.thePlayer.getEyeHeight();
            double posZ = (double)pos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            double dposX = (double)pos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double dposY = (double)pos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double dposZ = (double)pos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            RenderUtil.startDrawing();
            GL11.glEnable(2848);
            RenderUtil.setColor(Colors.MAGENTA.c);
            GL11.glLineWidth(3.0f);
            GL11.glBegin(1);
            GL11.glVertex3d(nposX, nposY, nposZ);
            GL11.glVertex3d(posX, posY, posZ);
            GL11.glEnd();
            RenderUtil.setColor(FadeUtil.BLUE.getColor().getRGB());
            GL11.glLineWidth(3.0f);
            GL11.glBegin(1);
            GL11.glVertex3d(posX, posY, posZ);
            GL11.glVertex3d(dposX, dposY, dposZ);
            GL11.glEnd();
            GL11.glDisable(2848);
            RenderUtil.stopDrawing();
        }
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.ugm.getValue()).booleanValue()) {
            Client.ungrabMouse();
        }
        if (!((Boolean)this.mithril.getValue()).booleanValue()) {
            GemstoneNuker gsn = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            gsn.mode.setValue((Enum)this.mode.getValue());
            gsn.protect.setValue(false);
            if (!gsn.isEnabled()) {
                gsn.setEnabled(true);
            }
        } else {
            MithrilNuker mn = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (!mn.isEnabled()) {
                mn.setEnabled(true);
            }
        }
        this.timer.reset();
        this.ewTimer.reset();
        this.etherWarped = false;
        Helper.sendMessage("[AutoRuby] Press ALT to add waypoints.");
        Helper.sendMessage("[AutoRuby] Use '.autoruby start' to start mining.");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.ugm.getValue()).booleanValue()) {
            Client.regrabMouse();
        }
        if (!((Boolean)this.mithril.getValue()).booleanValue()) {
            GemstoneNuker gsn = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            if (gsn.isEnabled()) {
                gsn.setEnabled(false);
            }
        } else {
            MithrilNuker mn = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (mn.isEnabled()) {
                mn.setEnabled(false);
            }
        }
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        Helper.sendMessage("Remember to save the waypoints~");
        this.timer.reset();
        this.ewTimer.reset();
        this.etherWarped = false;
        this.curBP = null;
        this.nextBP = null;
        this.curIndex = 0;
        super.onDisable();
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void etherWarp(BlockPos pos) {
        if (this.etherWarped) {
            return;
        }
        if (this.mc.thePlayer.getHeldItem() != null && !ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()).equals("ASPECT_OF_THE_VOID") || this.mc.thePlayer.getHeldItem() == null) {
            this.lastSlot = this.mc.thePlayer.inventory.currentItem;
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
                if (itemStack == null || itemStack.getItem() == null || !ItemUtils.getSkyBlockID(itemStack).equals("ASPECT_OF_THE_VOID")) continue;
                this.mc.thePlayer.inventory.currentItem = i;
                break;
            }
        }
        if (this.ewTimer.hasReached(250.0)) {
            Client.rightClick();
            this.ewTimer.reset();
            this.etherWarped = true;
            this.mc.thePlayer.inventory.currentItem = 0;
        }
    }

    private void loadYogs() {
        this.yogs.clear();
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (entity.isDead || !entity.isEntityAlive() || !(entity instanceof EntityMagmaCube) || !((double)this.mc.thePlayer.getDistanceToEntity(entity) < (Double)this.yogRange.getValue())) continue;
            this.yogs.add((EntityMagmaCube)entity);
        }
        this.yogs.sort(Comparator.comparingDouble(this::lambda$loadYogs$2));
    }

    private void renderTag(BlockPos bp, String str) {
        float size = (float)(this.mc.thePlayer.getDistance(bp.getX(), bp.getY(), bp.getZ()) / 10.0);
        if (size < 1.1f) {
            size = 1.1f;
        }
        float scale = size * 1.8f;
        scale /= 100.0f;
        double pX = (double)bp.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
        double pY = (double)bp.getY() - this.mc.getRenderManager().viewerPosY + 0.3;
        double pZ = (double)bp.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
        GL11.glPushMatrix();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslated(pX, pY, pZ);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
        GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GLUtil.setGLCap(2929, false);
        GLUtil.setGLCap(3042, true);
        float nw = (float)(-this.mc.fontRendererObj.getStringWidth(str) / 2) - 4.6f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.fontRendererObj.drawString(str, (int)nw + 4, -13, -1);
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }

    private void clickSlot(int slot, int incrementWindowId, int button, int mode) {
        this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId + incrementWindowId, slot, button, mode, this.mc.thePlayer);
    }

    public String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
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

    private double lambda$loadYogs$2(EntityMagmaCube sb) {
        return this.mc.thePlayer.getDistanceToEntity(sb);
    }

    private double lambda$idk$1(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }

    private double lambda$idk$0(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }
}

