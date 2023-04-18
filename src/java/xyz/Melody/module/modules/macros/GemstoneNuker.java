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
    private void destoryBlock(EventPreUpdate eventPreUpdate) {
        Object object;
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        if (this.tempDisable) {
            return;
        }
        AutoRuby autoRuby = (AutoRuby)Client.instance.getModuleManager().getModuleByClass(AutoRuby.class);
        if (!autoRuby.started && autoRuby.isEnabled()) {
            return;
        }
        if (this.blockPos == null) {
            this.blockPos = this.getBlock();
            return;
        }
        if (this.mc.field_71439_g.func_70011_f(this.blockPos.func_177958_n(), this.blockPos.func_177956_o(), this.blockPos.func_177952_p()) > 6.0) {
            this.gemstones.remove(this.blockPos);
            this.blockPos = null;
            return;
        }
        if (((Boolean)this.pickaxeCheck.getValue()).booleanValue()) {
            if (this.mc.field_71439_g.func_70694_bm() != null) {
                object = ItemUtils.getSkyBlockID(this.mc.field_71439_g.func_70694_bm());
                if (this.mc.field_71439_g.func_70694_bm().func_77973_b() != Items.field_179562_cC && !((String)object).contains("GEMSTONE_GAUNTLET") && !(this.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemPickaxe)) {
                    return;
                }
            } else {
                return;
            }
        }
        if (!Client.pickaxeAbilityReady || this.mc.field_71442_b == null) {
            this.timer.reset();
        }
        if (Client.pickaxeAbilityReady && this.mc.field_71442_b != null) {
            if (((Boolean)this.blueEgg.getValue()).booleanValue()) {
                if (!this.holdingBlueEgg) {
                    this.lastSlot = this.mc.field_71439_g.field_71071_by.field_70461_c;
                    this.mc.field_71439_g.field_71071_by.field_70461_c = ((Double)this.blueSlot.getValue()).intValue() - 1;
                    System.out.println(this.mc.field_71439_g.field_71071_by.field_70461_c);
                }
                if (this.timer.hasReached(250.0) && this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c) != null && this.mc.field_71439_g.field_71071_by.field_70461_c == ((Double)this.blueSlot.getValue()).intValue() - 1) {
                    this.mc.field_71442_b.func_78769_a(this.mc.field_71439_g, this.mc.field_71441_e, this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c));
                    Client.pickaxeAbilityReady = false;
                    if (this.timer.hasReached(450.0)) {
                        this.mc.field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
                        this.holdingBlueEgg = false;
                        this.timer.reset();
                    }
                }
            } else if (this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c) != null) {
                this.mc.field_71442_b.func_78769_a(this.mc.field_71439_g, this.mc.field_71441_e, this.mc.field_71439_g.field_71071_by.func_70301_a(this.mc.field_71439_g.field_71071_by.field_70461_c));
                Client.pickaxeAbilityReady = false;
            }
        }
        if (this.currentDamage > 40.0f) {
            this.currentDamage = 0.0f;
        }
        if (this.blockPos != null && this.mc.field_71441_e != null && ((object = this.mc.field_71441_e.func_180495_p(this.blockPos)).func_177230_c() == Blocks.field_150357_h || object.func_177230_c() == Blocks.field_150350_a)) {
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
                this.mc.field_71439_g.field_71174_a.func_147297_a(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            }
            this.mc.field_71439_g.func_71038_i();
            this.currentDamage += 1.0f;
        }
        if (((Boolean)this.rot.getValue()).booleanValue()) {
            object = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos));
            if (object == null) {
                return;
            }
            Object object2 = object[0];
            Object object3 = object[1];
            this.mc.field_71439_g.field_70177_z = this.smoothRotation(this.mc.field_71439_g.field_70177_z, (float)object2, 70.0f);
            this.mc.field_71439_g.field_70125_A = this.smoothRotation(this.mc.field_71439_g.field_70125_A, (float)object3, 70.0f);
        }
    }

    @EventHandler
    private void onAdminDetected(EventTick eventTick) {
        if (((Boolean)this.admin.getValue()).booleanValue() && (PlayerListUtils.tabContains("[ADMIN]") || PlayerListUtils.tabContains("[GM]"))) {
            Helper.sendMessage("[GemstoneNuker] Admin Detected, Warping to Private Island.");
            this.mc.field_71439_g.func_71165_d("/l");
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onAdminChat(ClientChatReceivedEvent clientChatReceivedEvent) {
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
            Helper.sendMessage("[GemstoneNuker] Admin Detected, Quitting Server.");
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
    private void onTick(EventTick eventTick) {
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
    public void onTick(EventRender3D eventRender3D) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), eventRender3D.getPartialTicks());
        }
        BlockPos blockPos = new BlockPos(this.mc.field_71439_g.func_174791_d()).func_177977_b();
        RenderUtil.drawSolidBlockESP(blockPos, new Color(0, 139, 255, 190).getRGB(), eventRender3D.getPartialTicks());
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        if (blockPos == null) {
            return null;
        }
        double d = (double)blockPos.func_177958_n() + 0.5 - this.mc.field_71439_g.field_70165_t + (double)enumFacing.func_82601_c() / 2.0;
        double d2 = (double)blockPos.func_177952_p() + 0.5 - this.mc.field_71439_g.field_70161_v + (double)enumFacing.func_82599_e() / 2.0;
        double d3 = this.mc.field_71439_g.field_70163_u + (double)this.mc.field_71439_g.func_70047_e() - ((double)blockPos.func_177956_o() + 0.5);
        double d4 = MathHelper.func_76133_a((double)(d * d + d2 * d2));
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    public EnumFacing getClosestEnum(BlockPos blockPos) {
        if (blockPos == null) {
            return EnumFacing.UP;
        }
        EnumFacing enumFacing = EnumFacing.UP;
        float f = MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[0]);
        if (f >= 45.0f && f <= 135.0f) {
            enumFacing = EnumFacing.EAST;
        } else if (f >= 135.0f && f <= 180.0f || f <= -135.0f && f >= -180.0f) {
            enumFacing = EnumFacing.SOUTH;
        } else if (f <= -45.0f && f >= -135.0f) {
            enumFacing = EnumFacing.WEST;
        } else if (f >= -45.0f && f <= 0.0f || f <= 45.0f && f >= 0.0f) {
            enumFacing = EnumFacing.NORTH;
        }
        if (MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) > 75.0f || MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) < -75.0f) {
            enumFacing = EnumFacing.UP;
        }
        return enumFacing;
    }

    private BlockPos getBlock() {
        int n = ((Double)this.range.getValue()).intValue();
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        BlockPos blockPos2 = this.mc.field_71439_g.func_180425_c();
        blockPos2 = blockPos2.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        Vec3i vec3i2 = new Vec3i((double)n, (float)n + 1.1f, (double)n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        BlockPos blockPos3 = new BlockPos(this.mc.field_71439_g.func_174791_d()).func_177977_b();
        if (blockPos2 != null) {
            for (Object object : BlockPos.func_177980_a((BlockPos)blockPos2.func_177971_a(vec3i), (BlockPos)blockPos2.func_177973_b(vec3i2))) {
                IBlockState object2 = this.mc.field_71441_e.func_180495_p((BlockPos)object);
                if (!((Boolean)this.under.getValue()).booleanValue() && object.func_177958_n() == blockPos3.func_177958_n() && object.func_177952_p() == blockPos3.func_177952_p() || !this.isNeededBlock(object2)) continue;
                arrayList.add(new Vec3(object.func_177958_n(), object.func_177956_o(), object.func_177952_p()));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Vec3 vec3 : arrayList) {
            BlockPos blockPos4 = new BlockPos(vec3);
            int n2 = this.mc.field_71439_g.func_180425_c().func_177958_n();
            int n3 = (int)((float)this.mc.field_71439_g.func_180425_c().func_177956_o() + this.mc.field_71439_g.func_70047_e());
            int n4 = this.mc.field_71439_g.func_180425_c().func_177952_p();
            BlockPos blockPos5 = new BlockPos((double)n2 - 0.3, (double)n3, (double)n4 - 0.3);
            if (!((double)MathUtil.distanceToPos(blockPos5, blockPos4) < (Double)this.range.getValue())) continue;
            arrayList2.add(blockPos4);
        }
        if (((Boolean)this.sort.getValue()).booleanValue()) {
            arrayList2.sort(Comparator.comparingDouble(blockPos -> this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p())));
        }
        this.gemstones = arrayList2;
        if (!arrayList2.isEmpty()) {
            return new BlockPos((Vec3i)arrayList2.get(0));
        }
        return null;
    }

    private boolean playerWithin10B() {
        for (EntityPlayer entityPlayer : this.mc.field_71441_e.field_73010_i) {
            if (((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer) || !(this.mc.field_71439_g.func_70032_d(entityPlayer) < 10.0f) || entityPlayer == this.mc.field_71439_g) continue;
            return true;
        }
        return false;
    }

    private boolean isNeededBlock(IBlockState iBlockState) {
        Gemstone gemstone = this.getGemstone(iBlockState);
        return gemstone != null && gemstone.name().contains(((Enum)this.mode.getValue()).name());
    }

    private Gemstone getGemstone(IBlockState iBlockState) {
        if (iBlockState.func_177230_c() != Blocks.field_150399_cn && iBlockState.func_177230_c() != Blocks.field_150397_co) {
            return null;
        }
        if (!((Boolean)this.pane.getValue()).booleanValue() && iBlockState.func_177230_c() == Blocks.field_150397_co) {
            return null;
        }
        EnumDyeColor enumDyeColor = this.firstNotNull((EnumDyeColor)((Object)iBlockState.func_177229_b(BlockStainedGlass.field_176547_a)), (EnumDyeColor)((Object)iBlockState.func_177229_b(BlockStainedGlassPane.field_176245_a)));
        if (enumDyeColor == Gemstone.RUBY.dyeColor) {
            return Gemstone.RUBY;
        }
        if (enumDyeColor == Gemstone.AMETHYST.dyeColor) {
            return Gemstone.AMETHYST;
        }
        if (enumDyeColor == Gemstone.JADE.dyeColor) {
            return Gemstone.JADE;
        }
        if (enumDyeColor == Gemstone.SAPPHIRE.dyeColor) {
            return Gemstone.SAPPHIRE;
        }
        if (enumDyeColor == Gemstone.AMBER.dyeColor) {
            return Gemstone.AMBER;
        }
        if (enumDyeColor == Gemstone.TOPAZ.dyeColor) {
            return Gemstone.TOPAZ;
        }
        if (enumDyeColor == Gemstone.JASPER.dyeColor) {
            return Gemstone.JASPER;
        }
        if (enumDyeColor == Gemstone.OPAL.dyeColor) {
            return Gemstone.OPAL;
        }
        return null;
    }

    public <T> T firstNotNull(T ... TArray) {
        for (T t : TArray) {
            if (t == null) continue;
            return t;
        }
        return null;
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

        private Gemstone(Color color, EnumDyeColor enumDyeColor) {
            this.color = color;
            this.dyeColor = enumDyeColor;
        }
    }
}

