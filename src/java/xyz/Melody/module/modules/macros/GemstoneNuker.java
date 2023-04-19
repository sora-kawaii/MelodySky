/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemPickaxe;
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
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.modules.macros.AutoRuby;

public final class GemstoneNuker
extends Module {
    public ArrayList<BlockPos> gemstones = new ArrayList();
    private BlockPos blockPos;
    private BlockPos lastBlockPos = null;
    public Mode<Enum> mode = new Mode("Mode", (Enum[])Gemstone.values(), (Enum)Gemstone.JADE);
    public Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);
    public Option<Boolean> admin = new Option<Boolean>("AntiAdmin", false);
    public Option<Boolean> blueEgg = new Option<Boolean>("BlueEggDrill", false);
    public Numbers<Double> blueSlot = new Numbers<Double>("EggDrill Slot", 5.0, 1.0, 8.0, 1.0);
    public Option<Boolean> sort = new Option<Boolean>("Sort", false);
    public Option<Boolean> under = new Option<Boolean>("Under", false);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", true);
    private Option<Boolean> pickaxeCheck = new Option<Boolean>("Pickaxe", true);
    private Option<Boolean> pane = new Option<Boolean>("Pane", false);
    public Option<Boolean> protect = new Option<Boolean>("MacroProtect(10)", true);
    private float currentDamage = 0.0f;
    private int blockHitDelay = 0;
    private boolean tempDisable = false;
    private boolean holdingBlueEgg = false;
    private TimerUtil timer = new TimerUtil();
    private int lastSlot = 0;

    public GemstoneNuker() {
        super("GemstoneNuker", new String[]{"gm"}, ModuleType.Macros);
        this.addValues(this.mode, this.range, this.admin, this.blueEgg, this.blueSlot, this.sort, this.under, this.pickaxeCheck, this.protect, this.pane, this.rot);
        this.setModInfo("Auto Mine Gemstones Around You.");
    }

    @EventHandler
    private void destoryBlock(EventPreUpdate event) {
        IBlockState blockState;
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (this.tempDisable) {
            return;
        }
        AutoRuby ar = (AutoRuby)Client.instance.getModuleManager().getModuleByClass(AutoRuby.class);
        if (!ar.started && ar.isEnabled()) {
            return;
        }
        if (this.blockPos == null) {
            this.blockPos = this.getBlock();
            return;
        }
        if (this.mc.thePlayer.getDistance(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()) > 6.0) {
            this.gemstones.remove(this.blockPos);
            this.blockPos = null;
            return;
        }
        if (((Boolean)this.pickaxeCheck.getValue()).booleanValue()) {
            if (this.mc.thePlayer.getHeldItem() != null) {
                String id = ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem());
                if (this.mc.thePlayer.getHeldItem().getItem() != Items.prismarine_shard && !id.contains("GEMSTONE_GAUNTLET") && !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe)) {
                    return;
                }
            } else {
                return;
            }
        }
        if (!Client.pickaxeAbilityReady || this.mc.playerController == null) {
            this.timer.reset();
        }
        if (Client.pickaxeAbilityReady && this.mc.playerController != null) {
            if (((Boolean)this.blueEgg.getValue()).booleanValue()) {
                if (!this.holdingBlueEgg) {
                    this.lastSlot = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.inventory.currentItem = ((Double)this.blueSlot.getValue()).intValue() - 1;
                    System.out.println(this.mc.thePlayer.inventory.currentItem);
                }
                if (this.timer.hasReached(250.0) && this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem) != null && this.mc.thePlayer.inventory.currentItem == ((Double)this.blueSlot.getValue()).intValue() - 1) {
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem));
                    Client.pickaxeAbilityReady = false;
                    if (this.timer.hasReached(450.0)) {
                        this.mc.thePlayer.inventory.currentItem = this.lastSlot;
                        this.holdingBlueEgg = false;
                        this.timer.reset();
                    }
                }
            } else if (this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem) != null) {
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem));
                Client.pickaxeAbilityReady = false;
            }
        }
        if (this.currentDamage > 40.0f) {
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
            float[] r = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos));
            if (r == null) {
                return;
            }
            float yaw = r[0];
            float pitch = r[1];
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, 70.0f);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, 70.0f);
        }
    }

    @EventHandler
    private void onAdminDetected(EventTick event) {
        if (((Boolean)this.admin.getValue()).booleanValue() && (PlayerListUtils.tabContains("[ADMIN]") || PlayerListUtils.tabContains("[GM]"))) {
            Helper.sendMessage("[GemstoneNuker] Admin Detected, Warping to Private Island.");
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
            Helper.sendMessage("[GemstoneNuker] Admin Detected, Quitting Server.");
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
    private void onTick(EventTick event) {
        if (!((Boolean)this.protect.getValue()).booleanValue()) {
            return;
        }
        if (this.playerWithin10B() && !this.tempDisable) {
            this.tempDisable = true;
        }
        if (!this.playerWithin10B() && this.tempDisable) {
            this.tempDisable = false;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentDamage = 0.0f;
        this.gemstones.clear();
        this.tempDisable = false;
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventRender3D event) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), event.getPartialTicks());
        }
        BlockPos posUnder = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
        RenderUtil.drawSolidBlockESP(posUnder, new Color(0, 139, 255, 190).getRGB(), event.getPartialTicks());
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        if (block == null) {
            return null;
        }
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
        if (pos == null) {
            return EnumFacing.UP;
        }
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
        Vec3i up = new Vec3i(r, r, r);
        Vec3i down = new Vec3i((double)r, (float)r + 1.1f, (double)r);
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        BlockPos posUnder = new BlockPos(this.mc.thePlayer.getPositionVector()).down();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(up), playerPos.subtract(down))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (!((Boolean)this.under.getValue()).booleanValue() && blockPos.getX() == posUnder.getX() && blockPos.getZ() == posUnder.getZ() || !this.isNeededBlock(blockState)) continue;
                chests.add(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }
        }
        ArrayList<BlockPos> gs = new ArrayList<BlockPos>();
        for (Vec3 v : chests) {
            BlockPos p = new BlockPos(v);
            int x = this.mc.thePlayer.getPosition().getX();
            int y = (int)((float)this.mc.thePlayer.getPosition().getY() + this.mc.thePlayer.getEyeHeight());
            int z = this.mc.thePlayer.getPosition().getZ();
            BlockPos blockPos = new BlockPos((double)x - 0.3, (double)y, (double)z - 0.3);
            if (!((double)MathUtil.distanceToPos(blockPos, p) < (Double)this.range.getValue())) continue;
            gs.add(p);
        }
        if (((Boolean)this.sort.getValue()).booleanValue()) {
            gs.sort(Comparator.comparingDouble(this::lambda$getBlock$0));
        }
        this.gemstones = gs;
        if (!gs.isEmpty()) {
            return new BlockPos((Vec3i)gs.get(0));
        }
        return null;
    }

    private boolean playerWithin10B() {
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(player) || !(this.mc.thePlayer.getDistanceToEntity(player) < 10.0f) || player == this.mc.thePlayer) continue;
            return true;
        }
        return false;
    }

    private boolean isNeededBlock(IBlockState blockState) {
        Gemstone stone = this.getGemstone(blockState);
        return stone != null && stone.name().contains(((Enum)this.mode.getValue()).name());
    }

    private Gemstone getGemstone(IBlockState block) {
        if (block.getBlock() != Blocks.stained_glass && block.getBlock() != Blocks.stained_glass_pane) {
            return null;
        }
        if (!((Boolean)this.pane.getValue()).booleanValue() && block.getBlock() == Blocks.stained_glass_pane) {
            return null;
        }
        EnumDyeColor color = this.firstNotNull(block.getValue(BlockStainedGlass.COLOR), block.getValue(BlockStainedGlassPane.COLOR));
        if (color == Gemstone.RUBY.dyeColor) {
            return Gemstone.RUBY;
        }
        if (color == Gemstone.AMETHYST.dyeColor) {
            return Gemstone.AMETHYST;
        }
        if (color == Gemstone.JADE.dyeColor) {
            return Gemstone.JADE;
        }
        if (color == Gemstone.SAPPHIRE.dyeColor) {
            return Gemstone.SAPPHIRE;
        }
        if (color == Gemstone.AMBER.dyeColor) {
            return Gemstone.AMBER;
        }
        if (color == Gemstone.TOPAZ.dyeColor) {
            return Gemstone.TOPAZ;
        }
        if (color == Gemstone.JASPER.dyeColor) {
            return Gemstone.JASPER;
        }
        if (color == Gemstone.OPAL.dyeColor) {
            return Gemstone.OPAL;
        }
        return null;
    }

    public <T> T firstNotNull(T ... args) {
        for (T arg : args) {
            if (arg == null) continue;
            return arg;
        }
        return null;
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

    private double lambda$getBlock$0(BlockPos pos) {
        return this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }

    static enum Gemstone {
        RUBY(new Color(188, 3, 29), EnumDyeColor.RED),
        AMETHYST(new Color(137, 0, 201), EnumDyeColor.PURPLE),
        JADE(new Color(157, 249, 32), EnumDyeColor.LIME),
        SAPPHIRE(new Color(60, 121, 224), EnumDyeColor.LIGHT_BLUE),
        AMBER(new Color(237, 139, 35), EnumDyeColor.ORANGE),
        TOPAZ(new Color(249, 215, 36), EnumDyeColor.YELLOW),
        JASPER(new Color(214, 15, 150), EnumDyeColor.MAGENTA),
        OPAL(new Color(245, 245, 240), EnumDyeColor.WHITE);

        public Color color;
        public EnumDyeColor dyeColor;

        private Gemstone(Color color, EnumDyeColor dyeColor) {
            this.color = color;
            this.dyeColor = dyeColor;
        }
    }
}

