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
        if (this.mc.thePlayer != null && Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
            if (((Boolean)this.sneak.getValue()).booleanValue() && this.mc.thePlayer.isSneaking()) {
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
        if (this.mc.thePlayer == null) {
            return;
        }
        if (!Client.inDungeons || Client.instance.dungeonUtils.inBoss) {
            return;
        }
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        if (this.lastCheckedPosition == null || !this.lastCheckedPosition.equals(blockPos)) {
            this.lastCheckedPosition = blockPos;
            this.loadSecrets();
        }
    }

    private void loadSecrets() {
        int n = ((Double)this.scanRange.getValue()).intValue();
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        blockPos = blockPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        this.blockList.clear();
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                Block block = this.mc.theWorld.getBlockState(blockPos2).getBlock();
                if (!this.shouldEspBlock(block, blockPos2)) continue;
                this.blockList.put(blockPos2, block);
            }
        }
    }

    private BlockPos getClosestSecret() {
        int n = ((Double)this.scanRange.getValue()).intValue();
        BlockPos blockPos2 = this.mc.thePlayer.getPosition();
        blockPos2 = blockPos2.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        if (blockPos2 != null) {
            for (BlockPos blockPos3 : BlockPos.getAllInBox(blockPos2.add(vec3i), blockPos2.subtract(vec3i))) {
                Block block = this.mc.theWorld.getBlockState(blockPos3).getBlock();
                if (block == Blocks.lever && this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX(), blockPos3.getY() - 1, blockPos3.getZ())).getBlock() == Blocks.bedrock) continue;
                Block block2 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX() + 1, blockPos3.getY(), blockPos3.getZ())).getBlock();
                Block block3 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX() + 1, blockPos3.getY(), blockPos3.getZ())).getBlock();
                Block block4 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX() - 1, blockPos3.getY(), blockPos3.getZ())).getBlock();
                Block block5 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX() - 1, blockPos3.getY(), blockPos3.getZ())).getBlock();
                Block block6 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ() + 1)).getBlock();
                Block block7 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ() + 1)).getBlock();
                Block block8 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ() - 1)).getBlock();
                Block block9 = this.mc.theWorld.getBlockState(new BlockPos(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ() - 1)).getBlock();
                boolean bl = block == Blocks.lever && (block2 == Blocks.diamond_block || block2 == Blocks.quartz_block || block2 == Blocks.gold_block || block2 == Blocks.emerald_block || block2 == Blocks.coal_block || block2 == Blocks.stained_hardened_clay || block3 == Blocks.diamond_block || block3 == Blocks.quartz_block || block3 == Blocks.gold_block || block3 == Blocks.emerald_block || block3 == Blocks.coal_block || block3 == Blocks.stained_hardened_clay || block4 == Blocks.diamond_block || block4 == Blocks.quartz_block || block4 == Blocks.gold_block || block4 == Blocks.emerald_block || block4 == Blocks.coal_block || block4 == Blocks.stained_hardened_clay || block5 == Blocks.diamond_block || block5 == Blocks.quartz_block || block5 == Blocks.gold_block || block5 == Blocks.emerald_block || block5 == Blocks.coal_block || block5 == Blocks.stained_hardened_clay || block6 == Blocks.diamond_block || block6 == Blocks.quartz_block || block6 == Blocks.gold_block || block6 == Blocks.emerald_block || block6 == Blocks.coal_block || block6 == Blocks.stained_hardened_clay || block7 == Blocks.diamond_block || block7 == Blocks.quartz_block || block7 == Blocks.gold_block || block7 == Blocks.emerald_block || block7 == Blocks.coal_block || block7 == Blocks.stained_hardened_clay || block8 == Blocks.diamond_block || block8 == Blocks.quartz_block || block8 == Blocks.gold_block || block8 == Blocks.emerald_block || block8 == Blocks.coal_block || block8 == Blocks.stained_hardened_clay || block9 == Blocks.diamond_block || block9 == Blocks.quartz_block || block9 == Blocks.gold_block || block9 == Blocks.emerald_block || block9 == Blocks.coal_block || block9 == Blocks.stained_hardened_clay);
                if (bl || !this.shouldEspBlock(block, blockPos3) || this.usedBlocks.contains(blockPos3)) continue;
                arrayList.add(blockPos3);
            }
            arrayList.sort(Comparator.comparingDouble(blockPos -> this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
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
                if (entry.getValue() instanceof BlockChest && ((BlockChest)entry.getValue()).chestType == 1) {
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
        if (this.currentBlock != null && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), this.currentBlock, EnumFacing.fromAngle(this.mc.thePlayer.rotationYaw), new Vec3(Math.random(), Math.random(), Math.random()))) {
            this.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            this.usedBlocks.add(this.currentBlock);
            this.currentBlock = this.getClosestSecret();
            this.autoTimer.reset();
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        if (this.timer.hasReached(150.0) && this.isEnabled() && this.selectedBlock != null && !this.usedBlocks.contains(this.selectedBlock)) {
            if (this.mc.objectMouseOver != null && this.selectedBlock.equals(this.mc.objectMouseOver.getBlockPos())) {
                return;
            }
            if (playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || playerInteractEvent.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (((Boolean)this.remove.getValue()).booleanValue()) {
                    this.usedBlocks.add(this.selectedBlock);
                }
                if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), this.selectedBlock, EnumFacing.fromAngle(this.mc.thePlayer.rotationYaw), new Vec3(Math.random(), Math.random(), Math.random()))) {
                    this.mc.thePlayer.swingItem();
                    this.timer.reset();
                }
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockChangeEvent blockChangeEvent) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (blockChangeEvent.getPosition().distanceSq(this.mc.thePlayer.getPosition()) > (Double)this.range.getValue()) {
            return;
        }
        if (this.usedBlocks.contains(blockChangeEvent.getPosition())) {
            return;
        }
        if (!this.shouldEspBlock(blockChangeEvent.getNewBlock().getBlock(), blockChangeEvent.getPosition())) {
            return;
        }
        this.blockList.put(blockChangeEvent.getPosition(), blockChangeEvent.getNewBlock().getBlock());
    }

    public boolean shouldEspBlock(Block block, BlockPos blockPos) {
        TileEntitySkull tileEntitySkull;
        if (block instanceof BlockChest || block instanceof BlockLever) {
            return true;
        }
        if (block instanceof BlockSkull && (tileEntitySkull = (TileEntitySkull)this.mc.theWorld.getTileEntity(blockPos)).getSkullType() == 3) {
            Property property = (Property)this.firstOrNull(tileEntitySkull.getPlayerProfile().getProperties().get("textures"));
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

