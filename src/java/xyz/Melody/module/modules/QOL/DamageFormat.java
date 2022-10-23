package xyz.Melody.module.modules.QOL;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class DamageFormat extends Module {
   private String[] colors = new String[]{"§f", "§e", "§6", "§c", "§f", "§f", "§f", "§f"};
   private static final NavigableMap suffixes = new TreeMap();

   public DamageFormat() {
      super("DamageFormat", new String[]{"gb"}, ModuleType.QOL);
      this.setModInfo("Change The Damage Numbers to xxxM or xxxk.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      if (this.mc.theWorld != null) {
         Iterator var2 = this.mc.theWorld.loadedEntityList.iterator();

         while(true) {
            EntityArmorStand as;
            char idk;
            do {
               do {
                  do {
                     do {
                        do {
                           Entity entity;
                           do {
                              do {
                                 if (!var2.hasNext()) {
                                    return;
                                 }

                                 entity = (Entity)var2.next();
                              } while(this.mc.thePlayer.getDistanceToEntity(entity) > 24.0F);
                           } while(!(entity instanceof EntityArmorStand));

                           as = (EntityArmorStand)entity;
                           idk = '"';
                        } while(!as.hasCustomName());
                     } while(!as.getCustomNameTag().contains("✧") && !as.getCustomNameTag().contains("✧") && !as.getCustomNameTag().contains("✯") && !as.getCustomNameTag().contains("✯"));
                  } while(as.getCustomNameTag().contains("M"));
               } while(as.getCustomNameTag().contains("b"));
            } while(as.getCustomNameTag().contains("k"));

            String num = StringUtils.stripControlCodes(as.getName().replaceAll(",", "").replaceAll("✧", "").replaceAll("✯", "").replaceAll("✯", "").replaceAll("❤", "").replaceAll("✧", "").replaceAll(String.valueOf(idk), "").replaceAll("'", "").replaceAll("text", "").replaceAll(":", "").replaceAll("}", "").replaceFirst("\\{", ""));
            long number = Long.parseLong(num);
            String finalName = this.format(number);
            String formattedName = "";

            for(int i = 0; i < finalName.length(); ++i) {
               char c = finalName.charAt(i);
               String colorPrefix = this.colors[0];
               if (i >= this.colors.length) {
                  colorPrefix = this.colors[0];
               } else {
                  colorPrefix = this.colors[i];
               }

               formattedName = formattedName + colorPrefix + c;
            }

            as.setCustomNameTag("§r✧" + formattedName + "§e✧§r");
         }
      }
   }

   public String format(long value) {
      if (value == Long.MIN_VALUE) {
         return this.format(-9223372036854775807L);
      } else if (value < 0L) {
         return "-" + this.format(-value);
      } else if (value < 1000L) {
         return Long.toString(value);
      } else {
         Map.Entry e = suffixes.floorEntry(value);
         Long divideBy = (Long)e.getKey();
         String suffix = (String)e.getValue();
         long truncated = value / (divideBy / 10L);
         boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
         return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
      }
   }

   static {
      suffixes.put(1000L, "k");
      suffixes.put(1000000L, "M");
      suffixes.put(1000000000L, "b");
   }
}
