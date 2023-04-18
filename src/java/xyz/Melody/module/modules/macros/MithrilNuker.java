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
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (this.playerWithin20B() && ((Boolean)this.protect.getValue()).booleanValue()) {
            this.currentDamage = 0.0f;
            Helper.sendMessage("[Mithril Fucker] Player Detected in 20 Blocks, Auto Disabled.");
            this.setEnabled(false);
            return;
        }
        if (Client.pickaxeAbilityReady && this.mc.playerController != null && this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem) != null) {
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem));
            Client.pickaxeAbilityReady = false;
        }
        if (this.currentDamage > 100.0f) {
            this.currentDamage = 0.0f;
        }
        if (this.blockPos != null && this.mc.theWorld != null && ((iBlockState = this.mc.theWorld.getBlockState(this.blockPos)).getBlock() == Blocks.bedrock || iBlockState.getBlock() == Blocks.air)) {
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
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            }
            this.mc.thePlayer.swingItem();
            this.currentDamage += 1.0f;
        }
        if (((Boolean)this.rot.getValue()).booleanValue()) {
            float f = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float f2 = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, f, 70.0f);
            eventPreUpdate.setYaw(this.mc.thePlayer.rotationYaw);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, f2, 70.0f);
            eventPreUpdate.setPitch(this.mc.thePlayer.rotationPitch);
        }
    }

    @EventHandler
    private void tick(EventTick eventTick) {
        if (((Boolean)this.sneak.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    @Override
    public void onEnable() {
        this.yaw = this.mc.thePlayer.rotationYaw;
        this.pitch = this.mc.thePlayer.rotationPitch;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentDamage = 0.0f;
        this.shabs.clear();
        this.sneaked = false;
        this.tempDisable = false;
        if (((Boolean)this.sneak.getValue()).booleanValue()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        super.onDisable();
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
        double d = (double)blockPos.getX() + 0.5 - this.mc.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d2 = (double)blockPos.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d3 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)blockPos.getY() + 0.5);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    public EnumFacing getClosestEnum(BlockPos blockPos) {
        EnumFacing enumFacing = EnumFacing.UP;
        float f = MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[0]);
        if (f >= 45.0f && f <= 135.0f) {
            enumFacing = EnumFacing.EAST;
        } else if (f >= 135.0f && f <= 180.0f || f <= -135.0f && f >= -180.0f) {
            enumFacing = EnumFacing.SOUTH;
        } else if (f <= -45.0f && f >= -135.0f) {
            enumFacing = EnumFacing.WEST;
        } else if (f >= -45.0f && f <= 0.0f || f <= 45.0f && f >= 0.0f) {
            enumFacing = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[1]) < -75.0f) {
            enumFacing = EnumFacing.UP;
        }
        return enumFacing;
    }

    private BlockPos getBlock() {
        int n = ((Double)this.range.getValue()).intValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        blockPos = blockPos.add(0, 1, 0);
        Vec3 vec3 = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            for (BlockPos object : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                IBlockState iBlockState = this.mc.theWorld.getBlockState(object);
                if (((Boolean)this.facingOnly.getValue()).booleanValue() && (RotationUtil.isInFov(this.yaw, this.pitch, object.getX(), object.getY(), object.getZ()) > 230.0 || (double)object.getY() < this.mc.thePlayer.posY) || !this.isMithril(iBlockState)) continue;
                arrayList.add(new Vec3((double)object.getX() + 0.5, object.getY(), (double)object.getZ() + 0.5));
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
            double d2 = ((Vec3)arrayList.get(i)).distanceTo(vec3);
            if (!(d2 < d)) continue;
            d = d2;
            vec33 = (Vec3)arrayList.get(i);
        }
        if (vec33 != null && d < 5.0) {
            return new BlockPos(vec33.xCoord, vec33.yCoord, vec33.zCoord);
        }
        return null;
    }

    private boolean playerWithin20B() {
        for (EntityPlayer entityPlayer : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || !(this.mc.thePlayer.getDistanceToEntity(entityPlayer) < 20.0f) || entityPlayer == this.mc.thePlayer) continue;
            return true;
        }
        return false;
    }

    private boolean isMithril(IBlockState iBlockState) {
        if (!((Boolean)this.wool.getValue()).booleanValue()) {
            if (iBlockState.getBlock() == Blocks.prismarine) {
                return true;
            }
            if (iBlockState.getBlock() == Blocks.wool) {
                return true;
            }
            if (iBlockState.getBlock() == Blocks.stained_hardened_clay) {
                return true;
            }
            if (iBlockState.getBlock() == Blocks.stone && iBlockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
                return true;
            }
        } else if (iBlockState.getBlock() == Blocks.wool) {
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
}

