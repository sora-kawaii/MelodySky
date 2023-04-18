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
    private boolean etherWarped = false;
    private int curIndex = 0;
    private BlockPos curBP;
    private BlockPos nextBP;
    public boolean started = false;
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
        this.addValues(this.mode, this.hideCoords, this.mithril, this.admin, this.ugm, this.routeHelper, this.protect, this.killYogs, this.yogRange, this.weaponSlot, this.rcKill, this.aim, this.faceDown);
        this.setModInfo("Auto Mine Gemstone.");
    }

    @EventHandler
    private void onAdminDetected(EventTick eventTick) {
        if (((Boolean)this.admin.getValue()).booleanValue() && PlayerListUtils.tabContains("[ADMIN]")) {
            Helper.sendMessage("[AutoGemstone] Admin Detected, Warping to Main Lobby.");
            NotificationPublisher.queue("Admin Detected", "An Admin Joined Your Server.", NotificationType.WARN, 10000);
            WindowsNotification.show("MelodySky", "Admin Detected.");
            this.mc.thePlayer.sendChatMessage("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onAdminChat(ClientChatReceivedEvent clientChatReceivedEvent) {
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
            Helper.sendMessage("[AutoGemstone] Admin Detected, Quitting Server.");
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

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        if (this.started && string.contains("is empty! Refuel it by talking to a Drill Mechanic!")) {
            NotificationPublisher.queue("AutoRuby Drill Empty.", "Trying To Refill Your Drill.", NotificationType.WARN, 10000);
            this.started = false;
            this.restore = true;
            this.shouldSwitchToAbi = true;
        }
    }

    @EventHandler
    private void onKillYog(EventPreUpdate eventPreUpdate) {
        if (((Boolean)this.killYogs.getValue()).booleanValue()) {
            this.loadYogs();
        } else if (!this.yogs.isEmpty()) {
            this.yogs.clear();
        }
        if (!this.yogs.isEmpty()) {
            EntityMagmaCube entityMagmaCube = this.yogs.get(0);
            if (this.started) {
                NotificationPublisher.queue("AutoRuby", "Yog Detected, Trying to FUCK it.", NotificationType.WARN, 3000);
                this.started = false;
                this.killingYogs = true;
                this.attackTimer.reset();
            }
            if (entityMagmaCube != null && entityMagmaCube.isEntityAlive() && this.killingYogs) {
                this.mc.thePlayer.inventory.currentItem = ((Double)this.weaponSlot.getValue()).intValue() - 1;
                if (((Boolean)this.rcKill.getValue()).booleanValue()) {
                    if (((Boolean)this.faceDown.getValue()).booleanValue()) {
                        eventPreUpdate.setPitch(90.0f);
                        if (this.attackTimer.hasReached(180.0)) {
                            Client.rightClick();
                            this.attackTimer.reset();
                        }
                    } else {
                        if (((Boolean)this.aim.getValue()).booleanValue()) {
                            float[] fArray = RotationUtil.getPredictedRotations(entityMagmaCube);
                            eventPreUpdate.setYaw(fArray[0]);
                            eventPreUpdate.setPitch(fArray[1]);
                        }
                        if (this.attackTimer.hasReached(180.0)) {
                            Client.rightClick();
                            this.attackTimer.reset();
                        }
                    }
                } else {
                    float[] fArray = RotationUtil.getPredictedRotations(entityMagmaCube);
                    eventPreUpdate.setYaw(fArray[0]);
                    eventPreUpdate.setPitch(fArray[1]);
                    if (this.attackTimer.hasReached(180.0)) {
                        this.mc.thePlayer.swingItem();
                        this.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity((Entity)entityMagmaCube, C02PacketUseEntity.Action.ATTACK));
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
    public void onDrawGuiBackground(EventTick eventTick) {
        Object object;
        if (this.ticks < 20) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        if (this.shouldSwitchToAbi) {
            for (int i = 0; i < 9; ++i) {
                object = this.mc.thePlayer.inventory.mainInventory[i];
                if (object != null && ((ItemStack)object).getItem() != null && ItemUtils.getSkyBlockID((ItemStack)object).startsWith("ABIPHONE")) {
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
        GuiScreen guiScreen = this.mc.currentScreen;
        if (Client.inSkyblock && guiScreen instanceof GuiChest && (object = ((GuiChest)guiScreen).inventorySlots) instanceof ContainerChest) {
            String string = this.getGuiName(guiScreen);
            if (string.startsWith("Abiphone") && this.shouldCallSB) {
                List<Slot> list = ((Container)object).inventorySlots;
                for (Slot slot : list) {
                    ItemStack itemStack = slot.getStack();
                    if (itemStack == null || !itemStack.hasDisplayName() || !StringUtils.stripControlCodes(itemStack.getDisplayName()).equals("Jotraeline Greatforge")) continue;
                    this.clickSlot(slot.slotNumber, 0, 0, 0);
                    this.shouldCallSB = false;
                    this.shouldCombind = false;
                    break;
                }
            }
            if (string.equals("Drill Anvil")) {
                if (!this.shouldCombind) {
                    for (int i = 0; i < 9; ++i) {
                        ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
                        if (itemStack == null || itemStack.getItem() == null) continue;
                        if (ItemUtils.getSkyBlockID(itemStack).contains("DRILL") && !((Container)object).inventorySlots.get(29).getHasStack() && Block.getBlockFromItem(((Container)object).inventorySlots.get(13).getStack().getItem()) == Blocks.barrier) {
                            int n = i + 1;
                            this.clickSlot(80 + n, 0, 0, 1);
                            this.drillSlot = 80 + n;
                            return;
                        }
                        if (!ItemUtils.getSkyBlockID(itemStack).contains("OIL_BARREL") || ((Container)object).inventorySlots.get(33).getHasStack() || !((Container)object).inventorySlots.get(29).getHasStack()) continue;
                        int n = i + 1;
                        this.clickSlot(80 + n, 0, 0, 1);
                        this.fuelSlot = 80 + n;
                        return;
                    }
                }
                if (((Container)object).inventorySlots.get(29).getHasStack() && ((Container)object).inventorySlots.get(33).getHasStack()) {
                    this.clickSlot(22, 0, 0, 0);
                    this.shouldCombind = true;
                }
                if (this.shouldCombind && !((Container)object).inventorySlots.get(29).getHasStack()) {
                    if (!this.shouldClickDrill && Block.getBlockFromItem(((Container)object).inventorySlots.get(13).getStack().getItem()) != Blocks.barrier) {
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
    private void tickWayPoints(EventTick eventTick) {
        if (this.started) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
        if (this.started && ((Boolean)this.protect.getValue()).booleanValue()) {
            boolean bl = false;
            String string = null;
            for (EntityPlayer entityPlayer : this.mc.theWorld.playerEntities) {
                AntiBot antiBot = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
                if (!antiBot.isInTablist(entityPlayer)) continue;
                if (entityPlayer != this.mc.thePlayer && this.mc.thePlayer.canEntityBeSeen(entityPlayer)) {
                    bl = true;
                    string = entityPlayer.getName();
                    break;
                }
                bl = false;
            }
            if (bl) {
                this.started = false;
                WindowsNotification.show("AutoRuby Player Detected", "Player Name: " + string + ".");
                NotificationPublisher.queue("AutoRuby", "Player Detected, Auto Disabled.", NotificationType.WARN, 10000);
                Helper.sendMessage("Player Name: " + string + ".");
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
    private void idk(EventTick eventTick) {
        if (!((Boolean)this.mithril.getValue()).booleanValue()) {
            GemstoneNuker gemstoneNuker = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            if (this.started) {
                Object object;
                BlockPos blockPos2;
                BlockPos blockPos3;
                for (int i = 0; i < this.wps.size(); ++i) {
                    blockPos3 = this.wps.get(i);
                    blockPos2 = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                    object = new BlockPos(new Vec3i(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ()));
                    BlockPos blockPos4 = new BlockPos(new Vec3i(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ()));
                    if (blockPos4.getX() != ((Vec3i)object).getX() || blockPos4.getY() != ((Vec3i)object).getY() || blockPos4.getZ() != ((Vec3i)object).getZ()) continue;
                    this.curIndex = i;
                    this.curBP = blockPos4;
                }
                if (this.curBP == null) {
                    return;
                }
                BlockPos blockPos5 = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                blockPos3 = new BlockPos(new Vec3i(this.curBP.getX(), this.curBP.getY(), this.curBP.getZ()));
                blockPos2 = new BlockPos(new Vec3i(blockPos5.getX(), blockPos5.getY(), blockPos5.getZ()));
                if ((float)Math.abs(blockPos3.getX() - blockPos2.getX()) > 0.2f || (float)Math.abs(blockPos3.getY() - blockPos2.getY()) > 0.2f || (float)Math.abs(blockPos3.getZ() - blockPos2.getZ()) > 0.2f) {
                    this.nextBP = blockPos3;
                }
                if (this.curBP != new BlockPos(this.mc.thePlayer.getPositionVector()).down() && this.fallSafeTimer.hasReached(5000.0)) {
                    object = (ArrayList)this.wps.clone();
                    ((ArrayList)object).sort(Comparator.comparingDouble(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
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
                if (!gemstoneNuker.gemstones.isEmpty()) {
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
                        object = gemstoneNuker.getRotations(this.nextBP, gemstoneNuker.getClosestEnum(this.nextBP));
                        this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, (float)object[0], 120.0f);
                        this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, (float)object[1], 120.0f);
                        if (this.timer.hasReached(75.0)) {
                            if (this.timer.hasReached(150.0)) {
                                if (this.nextBP != null) {
                                    this.etherWarp(this.nextBP);
                                }
                                if (gemstoneNuker.isEnabled()) {
                                    gemstoneNuker.setEnabled(false);
                                }
                                if (this.timer.hasReached(650.0)) {
                                    ++this.curIndex;
                                    this.curBP = this.nextBP;
                                    gemstoneNuker.setEnabled(true);
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
            MithrilNuker mithrilNuker = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (this.started) {
                Object object;
                BlockPos blockPos6;
                BlockPos blockPos7;
                for (int i = 0; i < this.wps.size(); ++i) {
                    blockPos7 = this.wps.get(i);
                    blockPos6 = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                    object = new BlockPos(new Vec3i(blockPos7.getX(), blockPos7.getY(), blockPos7.getZ()));
                    BlockPos blockPos8 = new BlockPos(new Vec3i(blockPos6.getX(), blockPos6.getY(), blockPos6.getZ()));
                    if (blockPos8.getX() != ((Vec3i)object).getX() || blockPos8.getY() != ((Vec3i)object).getY() || blockPos8.getZ() != ((Vec3i)object).getZ()) continue;
                    this.curIndex = i;
                    this.curBP = blockPos8;
                }
                if (this.curBP == null) {
                    return;
                }
                BlockPos blockPos9 = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
                blockPos7 = new BlockPos(new Vec3i(this.curBP.getX(), this.curBP.getY(), this.curBP.getZ()));
                blockPos6 = new BlockPos(new Vec3i(blockPos9.getX(), blockPos9.getY(), blockPos9.getZ()));
                if (blockPos7.getX() != blockPos6.getX() || blockPos7.getY() != blockPos6.getY() || blockPos7.getZ() != blockPos6.getZ()) {
                    this.nextBP = blockPos7;
                }
                if (this.curBP != new BlockPos(this.mc.thePlayer.getPositionVector()).down() && this.fallSafeTimer.hasReached(5000.0)) {
                    object = (ArrayList)this.wps.clone();
                    ((ArrayList)object).sort(Comparator.comparingDouble(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
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
                if (!mithrilNuker.blockPoses.isEmpty()) {
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
                        object = mithrilNuker.getRotations(this.nextBP, mithrilNuker.getClosestEnum(this.nextBP));
                        this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, (float)object[0], 120.0f);
                        this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, (float)object[1], 120.0f);
                        if (this.timer.hasReached(75.0)) {
                            if (this.timer.hasReached(150.0)) {
                                if (this.nextBP != null) {
                                    this.etherWarp(this.nextBP);
                                }
                                if (mithrilNuker.isEnabled()) {
                                    mithrilNuker.setEnabled(false);
                                }
                                if (this.timer.hasReached(650.0)) {
                                    ++this.curIndex;
                                    this.curBP = this.nextBP;
                                    mithrilNuker.setEnabled(true);
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
    private void R3D(EventRender3D eventRender3D) {
        for (int i = 0; i < this.wps.size(); ++i) {
            BlockPos blockPos = this.wps.get(i);
            BlockPos blockPos2 = null;
            blockPos2 = i + 1 == this.wps.size() ? this.wps.get(0) : this.wps.get(i + 1);
            if (blockPos == this.nextBP) {
                RenderUtil.drawSolidBlockESP(blockPos, ColorUtils.addAlpha(FadeUtil.PURPLE.getColor(), 190).getRGB(), 2.0f, eventRender3D.getPartialTicks());
            } else if (blockPos == this.curBP) {
                RenderUtil.drawSolidBlockESP(blockPos, ColorUtils.addAlpha(FadeUtil.GREEN.getColor(), 190).getRGB(), 2.0f, eventRender3D.getPartialTicks());
            } else {
                RenderUtil.drawSolidBlockESP(blockPos, ColorUtils.addAlpha(FadeUtil.BLUE.getColor(), 190).getRGB(), 2.0f, eventRender3D.getPartialTicks());
            }
            String string = (Boolean)this.hideCoords.getValue() != false ? (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "#" + (i + 1) : (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "#" + (i + 1) + (Object)((Object)EnumChatFormatting.WHITE) + ": " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
            this.renderTag(blockPos, string);
            if (!((Boolean)this.routeHelper.getValue()).booleanValue()) continue;
            double d = (double)blockPos2.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double d2 = (double)blockPos2.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double d3 = (double)blockPos2.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            double d4 = (double)blockPos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double d5 = (double)blockPos.getY() - this.mc.getRenderManager().viewerPosY + 1.0 + (double)this.mc.thePlayer.getEyeHeight();
            double d6 = (double)blockPos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            double d7 = (double)blockPos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double d8 = (double)blockPos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double d9 = (double)blockPos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            RenderUtil.startDrawing();
            GL11.glEnable(2848);
            RenderUtil.setColor(Colors.MAGENTA.c);
            GL11.glLineWidth(3.0f);
            GL11.glBegin(1);
            GL11.glVertex3d(d, d2, d3);
            GL11.glVertex3d(d4, d5, d6);
            GL11.glEnd();
            RenderUtil.setColor(FadeUtil.BLUE.getColor().getRGB());
            GL11.glLineWidth(3.0f);
            GL11.glBegin(1);
            GL11.glVertex3d(d4, d5, d6);
            GL11.glVertex3d(d7, d8, d9);
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
            GemstoneNuker gemstoneNuker = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            gemstoneNuker.mode.setValue((Enum)this.mode.getValue());
            gemstoneNuker.protect.setValue(false);
            if (!gemstoneNuker.isEnabled()) {
                gemstoneNuker.setEnabled(true);
            }
        } else {
            MithrilNuker mithrilNuker = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (!mithrilNuker.isEnabled()) {
                mithrilNuker.setEnabled(true);
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
            GemstoneNuker gemstoneNuker = (GemstoneNuker)Client.instance.getModuleManager().getModuleByClass(GemstoneNuker.class);
            if (gemstoneNuker.isEnabled()) {
                gemstoneNuker.setEnabled(false);
            }
        } else {
            MithrilNuker mithrilNuker = (MithrilNuker)Client.instance.getModuleManager().getModuleByClass(MithrilNuker.class);
            if (mithrilNuker.isEnabled()) {
                mithrilNuker.setEnabled(false);
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
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private void etherWarp(BlockPos blockPos) {
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
        this.yogs.sort(Comparator.comparingDouble(entityMagmaCube -> this.mc.thePlayer.getDistanceToEntity((Entity)entityMagmaCube)));
    }

    private void renderTag(BlockPos blockPos, String string) {
        float f = (float)(this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) / 10.0);
        if (f < 1.1f) {
            f = 1.1f;
        }
        float f2 = f * 1.8f;
        f2 /= 100.0f;
        double d = (double)blockPos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
        double d2 = (double)blockPos.getY() - this.mc.getRenderManager().viewerPosY + 0.3;
        double d3 = (double)blockPos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
        GL11.glPushMatrix();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslated(d, d2, d3);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
        GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
        GL11.glScalef(-f2, -f2, f2);
        GLUtil.setGLCap(2929, false);
        GLUtil.setGLCap(3042, true);
        float f3 = (float)(-this.mc.fontRendererObj.getStringWidth(string) / 2) - 4.6f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.fontRendererObj.drawString(string, (int)f3 + 4, -13, -1);
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }

    private void clickSlot(int n, int n2, int n3, int n4) {
        this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId + n2, n, n3, n4, this.mc.thePlayer);
    }

    public String getGuiName(GuiScreen guiScreen) {
        if (guiScreen instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)guiScreen).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
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
}

