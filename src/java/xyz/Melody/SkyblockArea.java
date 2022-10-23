package xyz.Melody;

import xyz.Melody.Utils.PlayerListUtils;

public class SkyblockArea {
   private Areas currentArea;

   public SkyblockArea() {
      this.currentArea = SkyblockArea.Areas.NULL;
   }

   public void setCurrentArea(Areas currentArea) {
      this.currentArea = currentArea;
   }

   public Areas getCurrentArea() {
      return this.currentArea;
   }

   public Areas[] getAllAreas() {
      return SkyblockArea.Areas.values();
   }

   public void updateCurrentArea() {
      if (!Client.inSkyblock) {
         this.currentArea = SkyblockArea.Areas.NULL;
      } else if (PlayerListUtils.tabContains("Area: Hub")) {
         this.currentArea = SkyblockArea.Areas.HUB;
      } else if (PlayerListUtils.tabContains("Area: Jerry's Workshop")) {
         this.currentArea = SkyblockArea.Areas.Jerrys_WorkShop;
      } else if (PlayerListUtils.tabContains("Area: Private Island")) {
         this.currentArea = SkyblockArea.Areas.Private_Island;
      } else if (PlayerListUtils.tabContains("Area: Spider's Den")) {
         this.currentArea = SkyblockArea.Areas.Spider_Den;
      } else if (PlayerListUtils.tabContains("Area: The Park")) {
         this.currentArea = SkyblockArea.Areas.The_Park;
      } else if (PlayerListUtils.tabContains("Area: The End")) {
         this.currentArea = SkyblockArea.Areas.The_End;
      } else if (PlayerListUtils.tabContains("Area: Crimson Isle")) {
         this.currentArea = SkyblockArea.Areas.Crimson_Island;
      } else if (PlayerListUtils.tabContains("Area: Deep Caverns")) {
         this.currentArea = SkyblockArea.Areas.Deep_Caverns;
      } else if (PlayerListUtils.tabContains("Area: Dwarven Mines")) {
         this.currentArea = SkyblockArea.Areas.Dwarven_Mines;
      } else if (PlayerListUtils.tabContains("Area: Gold Mine")) {
         this.currentArea = SkyblockArea.Areas.Gold_Mine;
      } else if (PlayerListUtils.tabContains("Area: The Farming Islands")) {
         this.currentArea = SkyblockArea.Areas.The_Farming_Island;
      } else if (PlayerListUtils.tabContains("Area: Dungeon Hub")) {
         this.currentArea = SkyblockArea.Areas.Dungeon_HUB;
      } else if (PlayerListUtils.tabContains("Area: Crystal Hollows")) {
         this.currentArea = SkyblockArea.Areas.Crystal_Hollows;
      } else if (Client.inDungeons) {
         this.currentArea = SkyblockArea.Areas.In_Dungeon;
      } else {
         this.currentArea = SkyblockArea.Areas.NULL;
      }
   }

   public static enum Areas {
      NULL,
      Jerrys_WorkShop,
      Private_Island,
      HUB,
      Spider_Den,
      The_End,
      Dungeon_HUB,
      The_Park,
      Deep_Caverns,
      Dwarven_Mines,
      Gold_Mine,
      The_Farming_Island,
      Crimson_Island,
      Crystal_Hollows,
      In_Dungeon;
   }
}
