package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoArrowAlign extends Module {
   private final Set clickedItemFrames = new HashSet();
   private static final Map requiredClicksForEntity = new HashMap();
   private int ticks = 1;
   private boolean foundPattern;

   public AutoArrowAlign() {
      super("AutoArrowAlign", new String[]{"aaa"}, ModuleType.Dungeons);
      this.setModInfo("Auto Do A.A. When CrossHair Hovered.");
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (Client.inDungeons && this.mc.currentScreen == null && this.mc.objectMouseOver != null) {
         if (this.foundPattern && this.mc.objectMouseOver.entityHit instanceof EntityItemFrame) {
            EntityItemFrame itemFrame = (EntityItemFrame)this.mc.objectMouseOver.entityHit;
            if (itemFrame.getDisplayedItem() != null && itemFrame.getDisplayedItem().getItem() == Items.arrow) {
               BlockPos itemFrameFixedPos = new BlockPos(itemFrame.posX, itemFrame.posY, itemFrame.posZ);
               if (!this.clickedItemFrames.contains(itemFrameFixedPos) && requiredClicksForEntity.containsKey(itemFrameFixedPos)) {
                  int requiredRotation = (Integer)requiredClicksForEntity.get(itemFrameFixedPos);
                  int currentRotation = itemFrame.getRotation();
                  if (currentRotation != requiredRotation) {
                     int clickAmount = currentRotation < requiredRotation ? requiredRotation - currentRotation : requiredRotation - currentRotation + 8;

                     for(int i = 0; i < clickAmount; ++i) {
                        Client.rightClick();
                     }
                  }

                  this.clickedItemFrames.add(itemFrameFixedPos);
               }
            }
         }

         if (this.ticks % 70 == 0) {
            this.calculatePattern();
            this.ticks = 0;
         }

         ++this.ticks;
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load event) {
      this.foundPattern = false;
      this.clickedItemFrames.clear();
      requiredClicksForEntity.clear();
   }

   private void calculatePattern() {
      requiredClicksForEntity.clear();
      Map itemFrames = new HashMap();
      List currentItemFrames = new ArrayList();
      List startItemFrames = new ArrayList();
      Set endItemFrames = new HashSet();
      Iterator var5 = this.mc.theWorld.loadedEntityList.iterator();

      while(var5.hasNext()) {
         Entity entity = (Entity)var5.next();
         if (entity instanceof EntityItemFrame) {
            ItemStack displayed = ((EntityItemFrame)entity).getDisplayedItem();
            if (displayed != null) {
               Item item = displayed.getItem();
               if (item == Items.arrow) {
                  itemFrames.put(new BlockPos(entity.posX, entity.posY, entity.posZ), entity);
               } else if (item == Item.getItemFromBlock(Blocks.wool)) {
                  if (EnumDyeColor.byMetadata(displayed.getItemDamage()) == EnumDyeColor.LIME) {
                     startItemFrames.add(new BlockPos(entity.posX, entity.posY, entity.posZ));
                  } else {
                     endItemFrames.add(new BlockPos(entity.posX, entity.posY, entity.posZ));
                  }
               }
            }
         }
      }

      if (itemFrames.size() >= 9 && startItemFrames.size() != 0) {
         var5 = startItemFrames.iterator();

         while(var5.hasNext()) {
            BlockPos pos = (BlockPos)var5.next();
            BlockPos adjacent = pos.up();
            if (itemFrames.containsKey(adjacent)) {
               currentItemFrames.add(adjacent);
            }

            adjacent = pos.down();
            if (itemFrames.containsKey(adjacent)) {
               currentItemFrames.add(adjacent);
            }

            adjacent = pos.south();
            if (itemFrames.containsKey(adjacent)) {
               currentItemFrames.add(adjacent);
            }

            adjacent = pos.north();
            if (itemFrames.containsKey(adjacent)) {
               currentItemFrames.add(adjacent);
            }
         }

         label114:
         for(int i = 0; i < 200; ++i) {
            if (currentItemFrames.size() == 0) {
               if (!this.foundPattern) {
                  this.foundPattern = true;
               }

               return;
            }

            List list = new ArrayList(currentItemFrames);
            currentItemFrames.clear();
            Iterator var15 = list.iterator();

            while(true) {
               while(true) {
                  if (!var15.hasNext()) {
                     continue label114;
                  }

                  BlockPos pos2 = (BlockPos)var15.next();
                  BlockPos adjacent2 = pos2.up();
                  if (endItemFrames.contains(adjacent2)) {
                     requiredClicksForEntity.put(pos2, 7);
                  } else {
                     adjacent2 = pos2.down();
                     if (endItemFrames.contains(adjacent2)) {
                        requiredClicksForEntity.put(pos2, 3);
                     } else {
                        adjacent2 = pos2.south();
                        if (endItemFrames.contains(adjacent2)) {
                           requiredClicksForEntity.put(pos2, 5);
                        } else {
                           adjacent2 = pos2.north();
                           if (endItemFrames.contains(adjacent2)) {
                              requiredClicksForEntity.put(pos2, 1);
                           } else if (!requiredClicksForEntity.containsKey(pos2)) {
                              adjacent2 = pos2.up();
                              if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                                 currentItemFrames.add(adjacent2);
                                 requiredClicksForEntity.put(pos2, 7);
                              } else {
                                 adjacent2 = pos2.down();
                                 if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                                    currentItemFrames.add(adjacent2);
                                    requiredClicksForEntity.put(pos2, 3);
                                 } else {
                                    adjacent2 = pos2.south();
                                    if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                                       currentItemFrames.add(adjacent2);
                                       requiredClicksForEntity.put(pos2, 5);
                                    } else {
                                       adjacent2 = pos2.north();
                                       if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                                          currentItemFrames.add(adjacent2);
                                          requiredClicksForEntity.put(pos2, 1);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         this.foundPattern = false;
      }

   }
}
