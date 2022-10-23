package xyz.Melody.module.balance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimeHelper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AntiBot extends Module {
   private TimeHelper timer = new TimeHelper();
   private List invalid = new ArrayList();
   private List whitelist = new ArrayList();
   private List hurtTimeCheck = new ArrayList();
   private List playerList = new ArrayList();
   public Value remove = new Option("AutoRemove", true);
   public Value hurttime = new Numbers("HurtTimeCheck", 10000.0, 5000.0, 20000.0, 100.0);
   public Value touchedGround = new Option("GroundCheck", true);
   public Value toolCheck = new Option("ToolCheck", true);
   public ArrayList bots = new ArrayList();
   private ArrayList playersInGame = new ArrayList();
   private static List removed = new ArrayList();
   public TimeHelper timer2 = new TimeHelper();
   public TimeHelper timer3 = new TimeHelper();
   public List onAirInvalid = new ArrayList();

   public AntiBot() {
      super("AntiBot", ModuleType.Balance);
      this.addValues(new Value[]{this.remove, this.hurttime, this.touchedGround, this.toolCheck});
      this.setEnabled(true);
   }

   @EventHandler
   public void onUpdate(EventPreUpdate e) {
      if (!this.hurtTimeCheck.isEmpty() && this.timer3.isDelayComplete(((Double)this.hurttime.getValue()).longValue())) {
         this.hurtTimeCheck.clear();
         this.timer3.reset();
      }

      if (!this.whitelist.isEmpty() && this.timer2.isDelayComplete(3000L)) {
         this.whitelist.clear();
         this.timer2.reset();
      }

      if (!this.invalid.isEmpty() && this.timer.isDelayComplete(1000L)) {
         this.invalid.clear();
         this.timer.reset();
      }

      Iterator var2 = this.mc.theWorld.getLoadedEntityList().iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         if (o instanceof EntityPlayer) {
            EntityPlayer ent = (EntityPlayer)o;
            if (ent != this.mc.thePlayer && !this.invalid.contains(ent)) {
               String formated = ent.getDisplayName().getFormattedText();
               String custom = ent.getCustomNameTag();
               String name = ent.getName();
               if (!formated.startsWith("��") && formated.endsWith("��r")) {
                  this.invalid.add(ent);
               }

               if (!this.isInTablist(ent)) {
                  this.invalid.add(ent);
               }

               if (ent.hurtTime > 0) {
                  this.hurtTimeCheck.add(ent);
               }

               if (this.hurtTimeCheck.contains(ent) && !this.whitelist.contains(ent)) {
                  this.whitelist.add(ent);
               }

               if (ent.getHeldItem() != null) {
                  this.whitelist.add(ent);
               }

               if (ent.getHeldItem() == null && !this.whitelist.contains(ent) && (Boolean)this.toolCheck.getValue()) {
                  this.invalid.add(ent);
               }

               if (ent.isInvisible() && !custom.equalsIgnoreCase("") && custom.toLowerCase().contains("��c��c") && name.contains("��c")) {
                  this.invalid.add(ent);
               }

               if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("��c") && custom.toLowerCase().contains("��r")) {
                  this.invalid.add(ent);
               }

               if (formated.contains("��8[NPC]")) {
                  this.invalid.add(ent);
               }

               if (!formated.contains("��c") && !custom.equalsIgnoreCase("")) {
                  this.invalid.add(ent);
               }
            }
         }
      }

   }

   public void onEnable() {
      this.onAirInvalid.clear();
      super.onEnable();
   }

   public void onDisable() {
      this.onAirInvalid.clear();
      super.onDisable();
   }

   @EventHandler
   public void onReceivePacket(EventPacketRecieve event) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (event.getPacket() instanceof S14PacketEntity) {
            S14PacketEntity packet = (S14PacketEntity)event.getPacket();
            Entity entity;
            if ((entity = packet.getEntity(this.mc.theWorld)) instanceof EntityPlayer && !packet.getOnGround() && !this.onAirInvalid.contains(entity.getEntityId())) {
               this.onAirInvalid.add(entity.getEntityId());
            }
         }

      }
   }

   public boolean isInTablist(EntityPlayer player) {
      if (Minecraft.getMinecraft().isSingleplayer()) {
         return true;
      } else {
         Iterator var2 = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().iterator();

         NetworkPlayerInfo playerInfo;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            Object o = var2.next();
            playerInfo = (NetworkPlayerInfo)o;
         } while(!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName()));

         return true;
      }
   }

   public boolean isEntityBot(Entity e) {
      if (!Client.instance.getModuleManager().getModuleByClass(AntiBot.class).isEnabled()) {
         return false;
      } else if (e instanceof EntityPlayer && Client.instance.getModuleManager().getModuleByClass(AntiBot.class).isEnabled()) {
         EntityPlayer player = (EntityPlayer)e;
         return this.invalid.contains(player) || !this.onAirInvalid.contains(player.getEntityId());
      } else {
         return false;
      }
   }
}
