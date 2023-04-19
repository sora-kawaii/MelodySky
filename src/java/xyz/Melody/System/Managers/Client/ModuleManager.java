/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Client;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.UISettings;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.rendering.EventRenderScoreboard;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.render.gl.GLUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AimAssist;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.balance.AntiVelocity;
import xyz.Melody.module.balance.Aura;
import xyz.Melody.module.balance.AutoArmor;
import xyz.Melody.module.balance.AutoBlock;
import xyz.Melody.module.balance.AutoClicker;
import xyz.Melody.module.balance.AutoHead;
import xyz.Melody.module.balance.ChestStealer;
import xyz.Melody.module.balance.InvCleaner;
import xyz.Melody.module.balance.NoHitDelay;
import xyz.Melody.module.balance.NoSlowDown;
import xyz.Melody.module.balance.Reach;
import xyz.Melody.module.modules.Fishing.AutoBaits;
import xyz.Melody.module.modules.Fishing.AutoFish;
import xyz.Melody.module.modules.Fishing.AutoSellInv;
import xyz.Melody.module.modules.Fishing.SlugFishing;
import xyz.Melody.module.modules.QOL.AimDragonCrystals;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;
import xyz.Melody.module.modules.QOL.CombindBooks;
import xyz.Melody.module.modules.QOL.DamageFormat;
import xyz.Melody.module.modules.QOL.DivanTreasure;
import xyz.Melody.module.modules.QOL.Dungeons.Alerts;
import xyz.Melody.module.modules.QOL.Dungeons.AutoCloseChest;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSalvage;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSell;
import xyz.Melody.module.modules.QOL.Dungeons.AutoTerminals;
import xyz.Melody.module.modules.QOL.Dungeons.CrystalGetter;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoArrowAlign;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoShootTheTarget;
import xyz.Melody.module.modules.QOL.Dungeons.Devices.AutoSimonSays;
import xyz.Melody.module.modules.QOL.Dungeons.DungeonChestProfit;
import xyz.Melody.module.modules.QOL.Dungeons.LeverAura;
import xyz.Melody.module.modules.QOL.Dungeons.LividFinder;
import xyz.Melody.module.modules.QOL.Dungeons.SayMimicKilled;
import xyz.Melody.module.modules.QOL.Dungeons.StonkLessStonk;
import xyz.Melody.module.modules.QOL.FrozenTreasureESP;
import xyz.Melody.module.modules.QOL.GemStoneESP;
import xyz.Melody.module.modules.QOL.GhostBlock;
import xyz.Melody.module.modules.QOL.HideSummonings;
import xyz.Melody.module.modules.QOL.InvisEntity;
import xyz.Melody.module.modules.QOL.JerryChineHelper;
import xyz.Melody.module.modules.QOL.MainWorld.MelodyPlayer;
import xyz.Melody.module.modules.QOL.MobTracker;
import xyz.Melody.module.modules.QOL.Nether.AshfangHelper;
import xyz.Melody.module.modules.QOL.Nether.SulphurESP;
import xyz.Melody.module.modules.QOL.NoNBTUpdate;
import xyz.Melody.module.modules.QOL.RenewCHPass;
import xyz.Melody.module.modules.QOL.SapphireMiningPit;
import xyz.Melody.module.modules.QOL.Slayer.BlazeDagger;
import xyz.Melody.module.modules.QOL.SoundsHider;
import xyz.Melody.module.modules.QOL.Sprint;
import xyz.Melody.module.modules.QOL.Swappings.AOTS;
import xyz.Melody.module.modules.QOL.Swappings.AutoZombieSword;
import xyz.Melody.module.modules.QOL.Swappings.EndStoneSword;
import xyz.Melody.module.modules.QOL.Swappings.ItemSwitcher;
import xyz.Melody.module.modules.QOL.Swappings.SoulWhip;
import xyz.Melody.module.modules.QOL.Swappings.WitherImpact;
import xyz.Melody.module.modules.QOL.TerminatorClicker;
import xyz.Melody.module.modules.QOL.WormFishingESP;
import xyz.Melody.module.modules.macros.ActionMacro;
import xyz.Melody.module.modules.macros.AutoRuby;
import xyz.Melody.module.modules.macros.CropNuker;
import xyz.Melody.module.modules.macros.CustomNuker;
import xyz.Melody.module.modules.macros.ForagingMacro;
import xyz.Melody.module.modules.macros.GemstoneNuker;
import xyz.Melody.module.modules.macros.GlowingMushroomNuker;
import xyz.Melody.module.modules.macros.GoldNuker;
import xyz.Melody.module.modules.macros.HardStoneNuker;
import xyz.Melody.module.modules.macros.IceNuker;
import xyz.Melody.module.modules.macros.MithrilNuker;
import xyz.Melody.module.modules.macros.MyceliumNuker;
import xyz.Melody.module.modules.macros.ObsidianNuker;
import xyz.Melody.module.modules.macros.PinglessMining;
import xyz.Melody.module.modules.macros.PowderChestMacro;
import xyz.Melody.module.modules.macros.SandNuker;
import xyz.Melody.module.modules.others.AntiAFK;
import xyz.Melody.module.modules.others.AntiLobbyCommand;
import xyz.Melody.module.modules.others.AutoGG;
import xyz.Melody.module.modules.others.ClientCommands;
import xyz.Melody.module.modules.others.FetchLBinData;
import xyz.Melody.module.modules.others.FreeCam;
import xyz.Melody.module.modules.others.HUD;
import xyz.Melody.module.modules.others.InternetSurfing;
import xyz.Melody.module.modules.others.MotionBlur;
import xyz.Melody.module.modules.others.OldAnimations;
import xyz.Melody.module.modules.others.PlayerList;
import xyz.Melody.module.modules.render.BlockOverlay;
import xyz.Melody.module.modules.render.CHMobESP;
import xyz.Melody.module.modules.render.Cam;
import xyz.Melody.module.modules.render.ClickGui;
import xyz.Melody.module.modules.render.FullBright;
import xyz.Melody.module.modules.render.HideImplosionParticle;
import xyz.Melody.module.modules.render.Nametags;
import xyz.Melody.module.modules.render.NoArmorRender;
import xyz.Melody.module.modules.render.Tracers;

public final class ModuleManager
implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    private ScoreObjective objective;
    private ScaledResolution scaledRes;
    public boolean enabledNeededMod = true;
    public boolean nicetry = true;
    public static boolean loaded;

    @Override
    public void init() {
        modules.add(new CustomNuker());
        modules.add(new ObsidianNuker());
        modules.add(new DivanTreasure());
        modules.add(new PinglessMining());
        modules.add(new CHMobESP());
        modules.add(new JerryChineHelper());
        modules.add(new AutoSellInv());
        modules.add(new InvCleaner());
        modules.add(new ChestStealer());
        modules.add(new AutoArmor());
        modules.add(new AutoRuby());
        modules.add(new InternetSurfing());
        modules.add(new AutoBaits());
        modules.add(new MyceliumNuker());
        modules.add(new SandNuker());
        modules.add(new SulphurESP());
        modules.add(new GlowingMushroomNuker());
        modules.add(new SapphireMiningPit());
        modules.add(new AntiAFK());
        modules.add(new BlockOverlay());
        modules.add(new NoSlowDown());
        modules.add(new AimAssist());
        modules.add(new AimDragonCrystals());
        modules.add(new IceNuker());
        modules.add(new FrozenTreasureESP());
        modules.add(new HardStoneNuker());
        modules.add(new PlayerList());
        modules.add(new MotionBlur());
        modules.add(new GemStoneESP());
        modules.add(new SlugFishing());
        modules.add(new CropNuker());
        modules.add(new FreeCam());
        modules.add(new OldAnimations());
        modules.add(new AshfangHelper());
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
        for (Module m : modules) {
            m.makeCommand();
        }
        EventBus.getInstance().register(this);
        loaded = true;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getAlias(String name) {
        for (Module f : modules) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
            for (String s : f.getAlias()) {
                if (!s.equalsIgnoreCase(name)) continue;
                return f;
            }
        }
        return null;
    }

    public List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t || m.getName().equals("ClickGui")) continue;
            output.add(m);
        }
        return output;
    }

    @EventHandler
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
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
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabled(true);
            }
        }
    }

    @EventHandler
    public void onRenderScoreboard(EventRenderScoreboard event) {
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hud.isEnabled() || !((Boolean)hud.scoreBoard.getValue()).booleanValue()) {
            this.objective = null;
            this.scaledRes = null;
            return;
        }
        this.objective = event.getObjective();
        this.scaledRes = event.getScaledRes();
    }

    @EventHandler
    private void scoreBoard(EventRender2D e) {
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hud.isEnabled() || !((Boolean)hud.scoreBoard.getValue()).booleanValue()) {
            return;
        }
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (this.objective == null || this.scaledRes == null) {
            return;
        }
        Scoreboard scoreboard = this.objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(this.objective);
        ArrayList<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>(){

            @Override
            public boolean apply(Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }

            @Override
            public boolean apply(Object object) {
                return this.apply((Score)object);
            }
        }));
        collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
        int i = Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + (Object)((Object)EnumChatFormatting.RED) + score.getScorePoints();
            i = Math.max(i, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
        }
        int i1 = collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        int j1 = this.scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = this.scaledRes.getScaledWidth() - i - k1;
        int j = 0;
        for (Score score1 : collection) {
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = UISettings.scoreboardBackground ? (Object)((Object)EnumChatFormatting.RED) + "" + score1.getScorePoints() : "";
            int k = j1 - ++j * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            int n = this.scaledRes.getScaledWidth() - k1 + 2;
            if (!UISettings.scoreboardBackground) {
                Gui.drawRect(l1 - 2, k, n, k + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, 0).getRGB());
            } else {
                GuiIngame.drawRect(l1 - 2, k, n, k + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 0x50000000);
            }
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s1, l1, k, 0x20FFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s2, n - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2), k, 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = this.objective.getDisplayName();
            if (UISettings.scoreboardBackground) {
                GuiIngame.drawRect(l1 - 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1, n, k - 1, 0x60000000);
                GuiIngame.drawRect(l1 - 2, k - 1, n, k, 0x50000000);
            }
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s3, l1 + i / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s3) / 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 0x20FFFFFF);
        }
    }
}

