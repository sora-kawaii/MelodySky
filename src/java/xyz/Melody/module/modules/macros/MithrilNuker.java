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
    private void destoryBlock(EventPreUpdate event) {
        IBlockState blockState;
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
        if (this.blockPos != null && this.mc.theWorld != null && ((blockState = this.mc.theWorld.getBlockState(this.blockPos)).getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air)) {
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
            float yaw = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float pitch = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, 70.0f);
            event.setYaw(this.mc.thePlayer.rotationYaw);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, 70.0f);
            event.setPitch(this.mc.thePlayer.rotationPitch);
        }
    }

    @EventHandler
    private void tick(EventTick event) {
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
    public void onTick(EventRender3D event) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - this.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double d1 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    public EnumFacing getClosestEnum(BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        float rotations2 = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations2 >= 45.0f && rotations2 <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        } else if (rotations2 >= 135.0f && rotations2 <= 180.0f || rotations2 <= -135.0f && rotations2 >= -180.0f) {
            closestEnum = EnumFacing.SOUTH;
        } else if (rotations2 <= -45.0f && rotations2 >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        } else if (rotations2 >= -45.0f && rotations2 <= 0.0f || rotations2 <= 45.0f && rotations2 >= 0.0f) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }

    private BlockPos getBlock() {
        int r = ((Double)this.range.getValue()).intValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos playerPos = this.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (((Boolean)this.facingOnly.getValue()).booleanValue() && (RotationUtil.isInFov(this.yaw, this.pitch, blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 230.0 || (double)blockPos.getY() < this.mc.thePlayer.posY) || !this.isMithril(blockState)) continue;
                chests.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
        }
        ArrayList<BlockPos> poses = new ArrayList<BlockPos>();
        for (Vec3 v : chests) {
            poses.add(new BlockPos(v));
        }
        this.blockPoses.clear();
        this.blockPoses.addAll(poses);
        double d = 9999.0;
        Vec3 closest = null;
        for (int i = 0; i < chests.size(); ++i) {
            double dist = ((Vec3)chests.get(i)).distanceTo(playerVec);
            if (!(dist < d)) continue;
            d = dist;
            closest = (Vec3)chests.get(i);
        }
        if (closest != null && d < 5.0) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private boolean playerWithin20B() {
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(player) || !(this.mc.thePlayer.getDistanceToEntity(player) < 20.0f) || player == this.mc.thePlayer) continue;
            return true;
        }
        return false;
    }

    private boolean isMithril(IBlockState blockState) {
        if (!((Boolean)this.wool.getValue()).booleanValue()) {
            if (blockState.getBlock() == Blocks.prismarine) {
                return true;
            }
            if (blockState.getBlock() == Blocks.wool) {
                return true;
            }
            if (blockState.getBlock() == Blocks.stained_hardened_clay) {
                return true;
            }
            if (blockState.getBlock() == Blocks.stone && blockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
                return true;
            }
        } else if (blockState.getBlock() == Blocks.wool) {
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
}

