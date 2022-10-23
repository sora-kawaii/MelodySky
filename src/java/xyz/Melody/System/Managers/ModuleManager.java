package xyz.Melody.System.Managers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Utils.render.gl.GLUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.balance.AntiVelocity;
import xyz.Melody.module.balance.Aura;
import xyz.Melody.module.balance.AutoBlock;
import xyz.Melody.module.balance.AutoClicker;
import xyz.Melody.module.balance.AutoHead;
import xyz.Melody.module.balance.HitBox;
import xyz.Melody.module.balance.NoHitDelay;
import xyz.Melody.module.balance.Reach;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;
import xyz.Melody.module.modules.QOL.CombindBooks;
import xyz.Melody.module.modules.QOL.DamageFormat;
import xyz.Melody.module.modules.QOL.GhostBlock;
import xyz.Melody.module.modules.QOL.HideSummonings;
import xyz.Melody.module.modules.QOL.InvisEntity;
import xyz.Melody.module.modules.QOL.MobTracker;
import xyz.Melody.module.modules.QOL.NoNBTUpdate;
import xyz.Melody.module.modules.QOL.RenewCHPass;
import xyz.Melody.module.modules.QOL.SoundsHider;
import xyz.Melody.module.modules.QOL.Sprint;
import xyz.Melody.module.modules.QOL.TerminatorClicker;
import xyz.Melody.module.modules.QOL.WormFishingESP;
import xyz.Melody.module.modules.QOL.Dungeons.Alerts;
import xyz.Melody.module.modules.QOL.Dungeons.AutoCloseChest;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSalvage;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSell;
import xyz.Melody.module.modules.QOL.Dungeons.AutoTerminals;
import xyz.Melody.module.modules.QOL.Dungeons.CrystalGetter;
import xyz.Melody.module.modules.QOL.Dungeons.DungeonChestProfit;
import xyz.Melody.module.modules.QOL.Dungeons.DungeonMap;
import xyz.Melody.module.modules.QOL.Dungeons.LeverAura;
import xyz.Melody.module.modules.QOL.Dungeons.LividFinder;
import xyz.Melody.module.modules.QOL.Dungeons.SayMimicKilled;
import xyz.Melody.module.modules.QOL.Dungeons.StonkLessStonk;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoArrowAlign;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoShootTheTarget;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoSimonSays;
import xyz.Melody.module.modules.QOL.MainWorld.MelodyPlayer;
import xyz.Melody.module.modules.QOL.Nether.AshfangHelper;
import xyz.Melody.module.modules.QOL.Slayer.BlazeDagger;
import xyz.Melody.module.modules.QOL.Slayer.SlayerESP;
import xyz.Melody.module.modules.QOL.Swappings.AOTS;
import xyz.Melody.module.modules.QOL.Swappings.AutoZombieSword;
import xyz.Melody.module.modules.QOL.Swappings.EndStoneSword;
import xyz.Melody.module.modules.QOL.Swappings.ItemSwitcher;
import xyz.Melody.module.modules.QOL.Swappings.SoulWhip;
import xyz.Melody.module.modules.QOL.Swappings.WitherImpact;
import xyz.Melody.module.modules.macros.ActionMacro;
import xyz.Melody.module.modules.macros.AutoFish;
import xyz.Melody.module.modules.macros.CropNuker;
import xyz.Melody.module.modules.macros.ForagingMacro;
import xyz.Melody.module.modules.macros.GemstoneNuker;
import xyz.Melody.module.modules.macros.GoldNuker;
import xyz.Melody.module.modules.macros.MithrilNuker;
import xyz.Melody.module.modules.macros.PowderChestMacro;
import xyz.Melody.module.modules.others.AntiLobbyCommand;
import xyz.Melody.module.modules.others.AutoGG;
import xyz.Melody.module.modules.others.ClientCommands;
import xyz.Melody.module.modules.others.FetchLBinData;
import xyz.Melody.module.modules.others.FreeCam;
import xyz.Melody.module.modules.others.OldAnimations;
import xyz.Melody.module.modules.render.Cam;
import xyz.Melody.module.modules.render.ClickGui;
import xyz.Melody.module.modules.render.FullBright;
import xyz.Melody.module.modules.render.HUD;
import xyz.Melody.module.modules.render.HideImplosionParticle;
import xyz.Melody.module.modules.render.Nametags;
import xyz.Melody.module.modules.render.NoArmorRender;
import xyz.Melody.module.modules.render.Tracers;

public class ModuleManager implements Manager {
   public static List modules = new ArrayList();
   private boolean enabledNeededMod = true;
   public boolean nicetry = true;
   public static boolean loaded;

   public void init() {
      modules.add(new CropNuker());
      modules.add(new FreeCam());
      modules.add(new OldAnimations());
      modules.add(new AshfangHelper());
      modules.add(new SlayerESP());
      modules.add(new BlazeDagger());
      modules.add(new HideImplosionParticle());
      modules.add(new CombindBooks());
      modules.add(new NoHitDelay());
      modules.add(new SoulWhip());
      modules.add(new Reach());
      modules.add(new DamageFormat());
      modules.add(new AutoArrowAlign());
      modules.add(new AutoShootTheTarget());
      modules.add(new AutoSimonSays());
      modules.add(new DungeonChestProfit());
      modules.add(new FetchLBinData());
      modules.add(new SoundsHider());
      modules.add(new TerminatorClicker());
      modules.add(new ForagingMacro());
      modules.add(new SayMimicKilled());
      modules.add(new StonkLessStonk());
      modules.add(new AntiLobbyCommand());
      modules.add(new DungeonMap());
      modules.add(new AutoSalvage());
      modules.add(new AutoSell());
      modules.add(new AutoEnchantTable());
      modules.add(new AutoCloseChest());
      modules.add(new HideSummonings());
      modules.add(new WormFishingESP());
      modules.add(new RenewCHPass());
      modules.add(new Tracers());
      modules.add(new LividFinder());
      modules.add(new LeverAura());
      modules.add(new AutoGG());
      modules.add(new GemstoneNuker());
      modules.add(new ActionMacro());
      modules.add(new PowderChestMacro());
      modules.add(new NoArmorRender());
      modules.add(new NoNBTUpdate());
      modules.add(new ClientCommands());
      modules.add(new Alerts());
      modules.add(new AutoHead());
      modules.add(new AutoBlock());
      modules.add(new AntiBot());
      modules.add(new Aura());
      modules.add(new HitBox());
      modules.add(new GoldNuker());
      modules.add(new MithrilNuker());
      modules.add(new AutoTerminals());
      modules.add(new AOTS());
      modules.add(new EndStoneSword());
      modules.add(new AutoFish());
      modules.add(new AutoZombieSword());
      modules.add(new WitherImpact());
      modules.add(new CrystalGetter());
      modules.add(new Nametags());
      modules.add(new ItemSwitcher());
      modules.add(new ClickGui());
      modules.add(new MelodyPlayer());
      modules.add(new AutoClicker());
      modules.add(new MobTracker());
      modules.add(new GhostBlock());
      modules.add(new InvisEntity());
      modules.add(new HUD());
      modules.add(new Cam());
      modules.add(new Sprint());
      modules.add(new AntiVelocity());
      modules.add(new FullBright());
      Client.instance.readConfig();
      Iterator var1 = modules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.makeCommand();
      }

      EventBus.getInstance().register(this);
      loaded = true;
   }

   public static List getModules() {
      return modules;
   }

   public Module getModuleByClass(Class cls) {
      Iterator var2 = modules.iterator();

      Module m;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         m = (Module)var2.next();
      } while(m.getClass() != cls);

      return m;
   }

   public static Module getModuleByName(String name) {
      Iterator var1 = modules.iterator();

      Module m;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         m = (Module)var1.next();
      } while(!m.getName().equalsIgnoreCase(name));

      return m;
   }

   public Module getAlias(String name) {
      Iterator var2 = modules.iterator();

      while(var2.hasNext()) {
         Module f = (Module)var2.next();
         if (f.getName().equalsIgnoreCase(name)) {
            return f;
         }

         String[] alias = f.getAlias();
         int length = alias.length;

         for(int i = 0; i < length; ++i) {
            String s = alias[i];
            if (s.equalsIgnoreCase(name)) {
               return f;
            }
         }
      }

      return null;
   }

   public List getModulesInType(ModuleType t) {
      ArrayList output = new ArrayList();
      Iterator var3 = modules.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         if (m.getType() == t && !m.getName().equals("ClickGui")) {
            output.add(m);
         }
      }

      return output;
   }

   @EventHandler
   private void onKeyPress(EventKey e) {
      Iterator var2 = modules.iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();
         if (m.getKey() == e.getKey()) {
            m.setEnabled(!m.isEnabled());
         }
      }

   }

   @EventHandler
   private void onGLHack(EventRender3D e) {
      GlStateManager.getFloat(2982, (FloatBuffer)GLUtils.MODELVIEW.clear());
      GlStateManager.getFloat(2983, (FloatBuffer)GLUtils.PROJECTION.clear());
      this.glGetInteger(2978, (IntBuffer)GLUtils.VIEWPORT.clear());
   }

   public void glGetInteger(int parameterName, IntBuffer parameters) {
      GL11.glGetInteger(parameterName, parameters);
   }

   @EventHandler
   private void on2DRender(EventRender2D e) {
      if (this.enabledNeededMod) {
         this.enabledNeededMod = false;
         Iterator var2 = modules.iterator();

         while(var2.hasNext()) {
            Module m = (Module)var2.next();
            if (m.enabledOnStartup) {
               m.setEnabled(true);
            }
         }
      }

   }
}
