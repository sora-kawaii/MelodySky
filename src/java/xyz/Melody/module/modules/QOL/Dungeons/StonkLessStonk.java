/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.RayMarchUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class StonkLessStonk
extends Module {
    private Option<Boolean> auto = new Option<Boolean>("Auto", false);
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 0.0, 6.0, 0.5);
    private Numbers<Double> scanRange = new Numbers<Double>("ScanRange", 6.0, 0.0, 15.0, 1.0);
    private Option<Boolean> sneak = new Option<Boolean>("Sneak", false);
    private Option<Boolean> remove = new Option<Boolean>("Remove", true);
    private HashMap<BlockPos, Block> blockList = new HashMap();
    private BlockPos selectedBlock = null;
    private BlockPos currentBlock = null;
    private BlockPos lastCheckedPosition = null;
    private HashSet<BlockPos> usedBlocks = new HashSet();
    private String witherEssenceSkin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=";
    private int essenceSkinHash = this.witherEssenceSkin.hashCode();
    private TimerUtil timer = new TimerUtil();
    private TimerUtil autoTimer = new TimerUtil();
    int ticks = 0;

    public StonkLessStonk() {
        super("StonkLessStonk", new String[]{"sls"}, ModuleType.Dungeons);
        this.addValues(this.auto, this.range, this.scanRange, this.sneak, this.remove);
        this.setModInfo("Click Secrets Thru walls.");
    }

    private boolean shoulddick() {
        if (this.mc.field_71439_g != null && Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
            if (((Boolean)this.sneak.getValue()).booleanValue() && this.mc.field_71439_g.func_70093_af()) {
                return true;
            }
            if (!((Boolean)this.sneak.getValue()).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    private void tickDungeon(EventTick eventTick) {
        if (this.mc.field_71439_g == null) {
            return;
        }
        if (!Client.inDungeons || Client.instance.dungeonUtils.inBoss) {
            return;
        }
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        if (this.lastCheckedPosition == null || !this.lastCheckedPosition.equals(blockPos)) {
            this.lastCheckedPosition = blockPos;
            this.loadSecrets();
        }
    }

    private void loadSecrets() {
        int n = ((Double)this.scanRange.getValue()).intValue();
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        this.blockList.clear();
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                Block block = this.mc.field_71441_e.func_180495_p(blockPos2).func_177230_c();
                if (!this.shouldEspBlock(block, blockPos2)) continue;
                this.blockList.put(blockPos2, block);
            }
        }
    }

    private BlockPos getClosestSecret() {
        int n = ((Double)this.scanRange.getValue()).intValue();
        BlockPos blockPos2 = this.mc.field_71439_g.func_180425_c();
        blockPos2 = blockPos2.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        if (blockPos2 != null) {
            for (BlockPos blockPos3 : BlockPos.func_177980_a((BlockPos)blockPos2.func_177971_a(vec3i), (BlockPos)blockPos2.func_177973_b(vec3i))) {
                Block block = this.mc.field_71441_e.func_180495_p(blockPos3).func_177230_c();
                if (block == Blocks.field_150442_at && this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n(), blockPos3.func_177956_o() - 1, blockPos3.func_177952_p())).func_177230_c() == Blocks.field_150357_h) continue;
                Block block2 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n() + 1, blockPos3.func_177956_o(), blockPos3.func_177952_p())).func_177230_c();
                Block block3 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n() + 1, blockPos3.func_177956_o(), blockPos3.func_177952_p())).func_177230_c();
                Block block4 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n() - 1, blockPos3.func_177956_o(), blockPos3.func_177952_p())).func_177230_c();
                Block block5 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n() - 1, blockPos3.func_177956_o(), blockPos3.func_177952_p())).func_177230_c();
                Block block6 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n(), blockPos3.func_177956_o(), blockPos3.func_177952_p() + 1)).func_177230_c();
                Block block7 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n(), blockPos3.func_177956_o(), blockPos3.func_177952_p() + 1)).func_177230_c();
                Block block8 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n(), blockPos3.func_177956_o(), blockPos3.func_177952_p() - 1)).func_177230_c();
                Block block9 = this.mc.field_71441_e.func_180495_p(new BlockPos(blockPos3.func_177958_n(), blockPos3.func_177956_o(), blockPos3.func_177952_p() - 1)).func_177230_c();
                boolean bl = block == Blocks.field_150442_at && (block2 == Blocks.field_150484_ah || block2 == Blocks.field_150371_ca || block2 == Blocks.field_150340_R || block2 == Blocks.field_150475_bE || block2 == Blocks.field_150402_ci || block2 == Blocks.field_150406_ce || block3 == Blocks.field_150484_ah || block3 == Blocks.field_150371_ca || block3 == Blocks.field_150340_R || block3 == Blocks.field_150475_bE || block3 == Blocks.field_150402_ci || block3 == Blocks.field_150406_ce || block4 == Blocks.field_150484_ah || block4 == Blocks.field_150371_ca || block4 == Blocks.field_150340_R || block4 == Blocks.field_150475_bE || block4 == Blocks.field_150402_ci || block4 == Blocks.field_150406_ce || block5 == Blocks.field_150484_ah || block5 == Blocks.field_150371_ca || block5 == Blocks.field_150340_R || block5 == Blocks.field_150475_bE || block5 == Blocks.field_150402_ci || block5 == Blocks.field_150406_ce || block6 == Blocks.field_150484_ah || block6 == Blocks.field_150371_ca || block6 == Blocks.field_150340_R || block6 == Blocks.field_150475_bE || block6 == Blocks.field_150402_ci || block6 == Blocks.field_150406_ce || block7 == Blocks.field_150484_ah || block7 == Blocks.field_150371_ca || block7 == Blocks.field_150340_R || block7 == Blocks.field_150475_bE || block7 == Blocks.field_150402_ci || block7 == Blocks.field_150406_ce || block8 == Blocks.field_150484_ah || block8 == Blocks.field_150371_ca || block8 == Blocks.field_150340_R || block8 == Blocks.field_150475_bE || block8 == Blocks.field_150402_ci || block8 == Blocks.field_150406_ce || block9 == Blocks.field_150484_ah || block9 == Blocks.field_150371_ca || block9 == Blocks.field_150340_R || block9 == Blocks.field_150475_bE || block9 == Blocks.field_150402_ci || block9 == Blocks.field_150406_ce);
                if (bl || !this.shouldEspBlock(block, blockPos3) || this.usedBlocks.contains(blockPos3)) continue;
                arrayList.add(blockPos3);
            }
            arrayList.sort(Comparator.comparingDouble(blockPos -> this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p())));
        }
        if (!arrayList.isEmpty()) {
            return (BlockPos)arrayList.get(0);
        }
        return null;
    }

    @EventHandler
    private void onDraw(EventRender3D eventRender3D) {
        if (this.currentBlock != null) {
            RenderUtil.drawSolidBlockESP(this.currentBlock, Colors.MAGENTA.c, eventRender3D.getPartialTicks());
        }
        if (this.shoulddick()) {
            for (Map.Entry<BlockPos, Block> entry : this.blockList.entrySet()) {
                if (this.usedBlocks.contains(entry.getKey())) continue;
                if (this.selectedBlock == null) {
                    if (RayMarchUtils.isFacingBlock(entry.getKey(), ((Double)this.range.getValue()).floatValue())) {
                        this.selectedBlock = entry.getKey();
                    }
                } else if (!RayMarchUtils.isFacingBlock(this.selectedBlock, ((Double)this.range.getValue()).floatValue())) {
                    this.selectedBlock = null;
                }
                Color color = new Color(192, 192, 192, 75);
                if (entry.getValue() instanceof BlockSkull) {
                    color = new Color(51, 0, 118, 75);
                }
                if (entry.getValue() instanceof BlockLever) {
                    color = new Color(51, 208, 118, 75);
                }
                if (entry.getValue() instanceof BlockChest && ((BlockChest)entry.getValue()).field_149956_a == 1) {
                    color = new Color(211, 0, 118, 75);
                }
                if (entry.getKey().equals(this.selectedBlock)) {
                    color = new Color(65, 105, 255, 100);
                }
                RenderUtil.drawSolidBlockESP(entry.getKey(), color.getRGB(), eventRender3D.getPartialTicks());
            }
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (!((Boolean)this.auto.getValue()).booleanValue()) {
            return;
        }
        if (this.ticks % 20 != 0) {
            ++this.ticks;
            return;
        }
        if (!Client.inDungeons || Client.instance.dungeonUtils.inBoss) {
            return;
        }
        if (!this.autoTimer.hasReached(100.0)) {
            return;
        }
        if (this.currentBlock == null) {
            this.currentBlock = this.getClosestSecret();
        }
        if (this.currentBlock != null && this.mc.field_71442_b.func_178890_a(this.mc.field_71439_g, this.mc.field_71441_e, this.mc.field_71439_g.field_71071_by.func_70448_g(), this.currentBlock, EnumFacing.func_176733_a((double)this.mc.field_71439_g.field_70177_z), new Vec3(Math.random(), Math.random(), Math.random()))) {
            this.mc.func_147114_u().func_147297_a(new C0APacketAnimation());
            this.usedBlocks.add(this.currentBlock);
            this.currentBlock = this.getClosestSecret();
            this.autoTimer.reset();
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        if (this.timer.hasReached(150.0) && this.isEnabled() && this.selectedBlock != null && !this.usedBlocks.contains(this.selectedBlock)) {
            if (this.mc.field_71476_x != null && this.selectedBlock.equals(this.mc.field_71476_x.func_178782_a())) {
                return;
            }
            if (playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (((Boolean)this.remove.getValue()).booleanValue()) {
                    this.usedBlocks.add(this.selectedBlock);
                }
                if (this.mc.field_71442_b.func_178890_a(this.mc.field_71439_g, this.mc.field_71441_e, this.mc.field_71439_g.field_71071_by.func_70448_g(), this.selectedBlock, EnumFacing.func_176733_a((double)this.mc.field_71439_g.field_70177_z), new Vec3(Math.random(), Math.random(), Math.random()))) {
                    this.mc.field_71439_g.func_71038_i();
                    this.timer.reset();
                }
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockChangeEvent blockChangeEvent) {
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            return;
        }
        if (blockChangeEvent.getPosition().func_177951_i(this.mc.field_71439_g.func_180425_c()) > (Double)this.range.getValue()) {
            return;
        }
        if (this.usedBlocks.contains(blockChangeEvent.getPosition())) {
            return;
        }
        if (!this.shouldEspBlock(blockChangeEvent.getNewBlock().func_177230_c(), blockChangeEvent.getPosition())) {
            return;
        }
        this.blockList.put(blockChangeEvent.getPosition(), blockChangeEvent.getNewBlock().func_177230_c());
    }

    public boolean shouldEspBlock(Block block, BlockPos blockPos) {
        TileEntitySkull tileEntitySkull;
        if (block instanceof BlockChest || block instanceof BlockLever) {
            return true;
        }
        if (block instanceof BlockSkull && (tileEntitySkull = (TileEntitySkull)this.mc.field_71441_e.func_175625_s(blockPos)).func_145904_a() == 3) {
            Property property = (Property)this.firstOrNull(tileEntitySkull.func_152108_a().getProperties().get("textures"));
            return property != null && property.getValue().hashCode() == this.essenceSkinHash;
        }
        return false;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load load) {
        this.blockList.clear();
        this.usedBlocks.clear();
        this.selectedBlock = null;
        this.lastCheckedPosition = null;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.blockList.clear();
        this.usedBlocks.clear();
        this.selectedBlock = null;
        this.currentBlock = null;
        this.lastCheckedPosition = null;
        super.onDisable();
    }

    public <T> T firstOrNull(Iterable<T> iterable) {
        return Iterables.getFirst(iterable, null);
    }
}

