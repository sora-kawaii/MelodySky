package xyz.Melody.module.modules.QOL.Dungeons;

import com.mojang.authlib.properties.Property;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.ArrayUtils;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.RayMarchUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class StonkLessStonk extends Module {
   private Option auto = new Option("Auto", false);
   private Numbers range = new Numbers("Range", 5.0, 0.0, 6.0, 0.5);
   private Numbers scanRange = new Numbers("ScanRange", 6.0, 0.0, 15.0, 1.0);
   private Option sneak = new Option("Sneak", false);
   private Option remove = new Option("Remove", true);
   private HashMap blockList = new HashMap();
   private BlockPos selectedBlock = null;
   private BlockPos currentBlock = null;
   private BlockPos lastCheckedPosition = null;
   private HashSet usedBlocks = new HashSet();
   private String witherEssenceSkin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=";
   private int essenceSkinHash;
   private TimerUtil timer;
   private TimerUtil autoTimer;
   int dicks;
   int ticks;

   public StonkLessStonk() {
      super("StonkLessStonk", new String[]{"sls"}, ModuleType.Dungeons);
      this.essenceSkinHash = this.witherEssenceSkin.hashCode();
      this.timer = new TimerUtil();
      this.autoTimer = new TimerUtil();
      this.dicks = 0;
      this.ticks = 0;
      this.addValues(new Value[]{this.auto, this.range, this.scanRange, this.sneak, this.remove});
      this.setModInfo("Click Secrets Thru walls.");
   }

   private boolean shoulddick() {
      if (this.mc.thePlayer != null && Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
         if ((Boolean)this.sneak.getValue() && this.mc.thePlayer.isSneaking()) {
            return true;
         }

         if (!(Boolean)this.sneak.getValue()) {
            return true;
         }
      }

      return false;
   }

   @EventHandler
   private void tickDungeon(EventTick event) {
      if (this.mc.thePlayer != null) {
         if (Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
            BlockPos playerPosition = this.mc.thePlayer.getPosition();
            if (this.lastCheckedPosition == null || !this.lastCheckedPosition.equals(playerPosition)) {
               this.lastCheckedPosition = playerPosition;
               this.loadSecrets();
            }

         }
      }
   }

   private void loadSecrets() {
      int r = ((Double)this.scanRange.getValue()).intValue();
      BlockPos playerPos = this.mc.thePlayer.getPosition();
      playerPos = playerPos.add(0, 1, 0);
      Vec3i vec3i = new Vec3i(r, r, r);
      this.blockList.clear();
      if (playerPos != null) {
         Iterator var4 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

         while(var4.hasNext()) {
            BlockPos blockPos = (BlockPos)var4.next();
            Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
            if (this.shouldEspBlock(block, blockPos)) {
               this.blockList.put(blockPos, block);
            }
         }
      }

   }

   private BlockPos getClosestSecret() {
      int r = ((Double)this.scanRange.getValue()).intValue();
      BlockPos playerPos = this.mc.thePlayer.getPosition();
      playerPos = playerPos.add(0, 1, 0);
      Vec3i vec3i = new Vec3i(r, r, r);
      ArrayList poses = new ArrayList();
      if (playerPos != null) {
         Iterator var5 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

         while(true) {
            BlockPos blockPos;
            Block block;
            do {
               if (!var5.hasNext()) {
                  poses.sort(Comparator.comparingDouble((pos) -> {
                     return this.mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                  }));
                  return !poses.isEmpty() ? (BlockPos)poses.get(0) : null;
               }

               blockPos = (BlockPos)var5.next();
               block = this.mc.theWorld.getBlockState(blockPos).getBlock();
            } while(block == Blocks.lever && this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())).getBlock() == Blocks.bedrock);

            Block left = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ())).getBlock();
            Block right = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ())).getBlock();
            Block left1 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ())).getBlock();
            Block right1 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ())).getBlock();
            Block left2 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1)).getBlock();
            Block right2 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1)).getBlock();
            Block left3 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1)).getBlock();
            Block right3 = this.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1)).getBlock();
            boolean isWaterButton = block == Blocks.lever && (left == Blocks.diamond_block || left == Blocks.quartz_block || left == Blocks.gold_block || left == Blocks.emerald_block || left == Blocks.coal_block || left == Blocks.stained_hardened_clay || right == Blocks.diamond_block || right == Blocks.quartz_block || right == Blocks.gold_block || right == Blocks.emerald_block || right == Blocks.coal_block || right == Blocks.stained_hardened_clay || left1 == Blocks.diamond_block || left1 == Blocks.quartz_block || left1 == Blocks.gold_block || left1 == Blocks.emerald_block || left1 == Blocks.coal_block || left1 == Blocks.stained_hardened_clay || right1 == Blocks.diamond_block || right1 == Blocks.quartz_block || right1 == Blocks.gold_block || right1 == Blocks.emerald_block || right1 == Blocks.coal_block || right1 == Blocks.stained_hardened_clay || left2 == Blocks.diamond_block || left2 == Blocks.quartz_block || left2 == Blocks.gold_block || left2 == Blocks.emerald_block || left2 == Blocks.coal_block || left2 == Blocks.stained_hardened_clay || right2 == Blocks.diamond_block || right2 == Blocks.quartz_block || right2 == Blocks.gold_block || right2 == Blocks.emerald_block || right2 == Blocks.coal_block || right2 == Blocks.stained_hardened_clay || left3 == Blocks.diamond_block || left3 == Blocks.quartz_block || left3 == Blocks.gold_block || left3 == Blocks.emerald_block || left3 == Blocks.coal_block || left3 == Blocks.stained_hardened_clay || right3 == Blocks.diamond_block || right3 == Blocks.quartz_block || right3 == Blocks.gold_block || right3 == Blocks.emerald_block || right3 == Blocks.coal_block || right3 == Blocks.stained_hardened_clay);
            if (!isWaterButton && this.shouldEspBlock(block, blockPos) && !this.usedBlocks.contains(blockPos)) {
               poses.add(blockPos);
            }
         }
      } else {
         return !poses.isEmpty() ? (BlockPos)poses.get(0) : null;
      }
   }

   @EventHandler
   private void onDraw(EventRender3D event) {
      if (this.currentBlock != null) {
         RenderUtil.drawSolidBlockESP(this.currentBlock, Colors.MAGENTA.c, event.getPartialTicks());
      }

      if (this.shoulddick()) {
         Iterator var2 = this.blockList.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry block = (Map.Entry)var2.next();
            if (!this.usedBlocks.contains(block.getKey())) {
               if (this.selectedBlock == null) {
                  if (RayMarchUtils.isFacingBlock((BlockPos)block.getKey(), ((Double)this.range.getValue()).floatValue())) {
                     this.selectedBlock = (BlockPos)block.getKey();
                  }
               } else if (!RayMarchUtils.isFacingBlock(this.selectedBlock, ((Double)this.range.getValue()).floatValue())) {
                  this.selectedBlock = null;
               }

               Color color = new Color(192, 192, 192, 75);
               if (block.getValue() instanceof BlockSkull) {
                  color = new Color(51, 0, 118, 75);
               }

               if (block.getValue() instanceof BlockLever) {
                  color = new Color(51, 208, 118, 75);
               }

               if (block.getValue() instanceof BlockChest && ((BlockChest)block.getValue()).chestType == 1) {
                  color = new Color(211, 0, 118, 75);
               }

               if (((BlockPos)block.getKey()).equals(this.selectedBlock)) {
                  color = new Color(65, 105, 255, 100);
               }

               RenderUtil.drawSolidBlockESP((BlockPos)block.getKey(), color.getRGB(), event.getPartialTicks());
            }
         }
      }

   }

   @EventHandler
   private void onTick2(EventTick event) {
      if (this.dicks < 41) {
         ++this.dicks;
      } else {
         if (this.getClosestSecret() == null) {
            this.usedBlocks.clear();
         }

         this.dicks = 0;
      }
   }

   @EventHandler
   private void onTick(EventTick event) {
      if ((Boolean)this.auto.getValue()) {
         if (this.ticks % 20 != 0) {
            ++this.ticks;
         } else if (Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
            if (this.autoTimer.hasReached(100.0)) {
               if (this.currentBlock == null) {
                  this.currentBlock = this.getClosestSecret();
               }

               if (this.currentBlock != null && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), this.currentBlock, EnumFacing.fromAngle((double)this.mc.thePlayer.rotationYaw), new Vec3(Math.random(), Math.random(), Math.random()))) {
                  this.mc.thePlayer.swingItem();
                  this.usedBlocks.add(this.currentBlock);
                  this.currentBlock = this.getClosestSecret();
                  this.autoTimer.reset();
               }

            }
         }
      }
   }

   @SubscribeEvent
   public void onInteract(PlayerInteractEvent event) {
      if (this.timer.hasReached(150.0) && this.isEnabled() && this.selectedBlock != null && !this.usedBlocks.contains(this.selectedBlock)) {
         if (this.mc.objectMouseOver != null && this.selectedBlock.equals(this.mc.objectMouseOver.getBlockPos())) {
            return;
         }

         if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            if ((Boolean)this.remove.getValue()) {
               this.usedBlocks.add(this.selectedBlock);
            }

            if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), this.selectedBlock, EnumFacing.fromAngle((double)this.mc.thePlayer.rotationYaw), new Vec3(Math.random(), Math.random(), Math.random()))) {
               this.mc.thePlayer.swingItem();
               this.timer.reset();
            }
         }
      }

   }

   @EventHandler
   public void onBlockChange(BlockChangeEvent event) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (!(event.getPosition().distanceSq(this.mc.thePlayer.getPosition()) > (Double)this.range.getValue())) {
            if (!this.usedBlocks.contains(event.getPosition())) {
               if (this.shouldEspBlock(event.getNewBlock().getBlock(), event.getPosition())) {
                  this.blockList.put(event.getPosition(), event.getNewBlock().getBlock());
               }
            }
         }
      }
   }

   public boolean shouldEspBlock(Block block, BlockPos position) {
      if (!(block instanceof BlockChest) && !(block instanceof BlockLever)) {
         if (block instanceof BlockSkull) {
            TileEntitySkull tileEntity = (TileEntitySkull)this.mc.theWorld.getTileEntity(position);
            if (tileEntity.getSkullType() == 3) {
               Property property = (Property)ArrayUtils.firstOrNull(tileEntity.getPlayerProfile().getProperties().get("textures"));
               return property != null && property.getValue().hashCode() == this.essenceSkinHash;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load event) {
      this.blockList.clear();
      this.usedBlocks.clear();
      this.selectedBlock = null;
      this.lastCheckedPosition = null;
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      this.blockList.clear();
      this.usedBlocks.clear();
      this.selectedBlock = null;
      this.currentBlock = null;
      this.lastCheckedPosition = null;
      super.onDisable();
   }
}
