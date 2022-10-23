package xyz.Melody.Event.events.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import xyz.Melody.Event.Event;

public class BlockChangeEvent extends Event {
   private BlockPos position;
   private IBlockState oldBlock;
   private IBlockState newBlock;

   public BlockChangeEvent(BlockPos position, IBlockState oldBlock, IBlockState newBlock) {
      this.position = position;
      this.oldBlock = oldBlock;
      this.newBlock = newBlock;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public IBlockState getOldBlock() {
      return this.oldBlock;
   }

   public IBlockState getNewBlock() {
      return this.newBlock;
   }
}
