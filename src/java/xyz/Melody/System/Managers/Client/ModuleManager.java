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
import xyz.Melody.module.modules.macros.ForagingMacro;
import xyz.Melody.module.modules.macros.GemstoneNuker;
import xyz.Melody.module.modules.macros.GlowingMushroomNuker;
import xyz.Melody.module.modules.macros.GoldNuker;
import xyz.Melody.module.modules.macros.HardStoneNuker;
import xyz.Melody.module.modules.macros.IceNuker;
import xyz.Melody.module.modules.macros.MithrilNuker;
import xyz.Melody.module.modules.macros.MyceliumNuker;
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
        for (Module module : modules) {
            module.makeCommand();
        }
        EventBus.getInstance().register(this);
        loaded = true;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public Module getModuleByClass(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (module.getClass() != clazz) continue;
            return module;
        }
        return null;
    }

    public static Module getModuleByName(String string) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(string)) continue;
            return module;
        }
        return null;
    }

    public Module getAlias(String string) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(string)) {
                return module;
            }
            for (String string2 : module.getAlias()) {
                if (!string2.equalsIgnoreCase(string)) continue;
                return module;
            }
        }
        return null;
    }

    public List<Module> getModulesInType(ModuleType moduleType) {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Module module : modules) {
            if (module.getType() != moduleType || module.getName().equals("ClickGui")) continue;
            arrayList.add(module);
        }
        return arrayList;
    }

    @EventHandler
    private void onKeyPress(EventKey eventKey) {
        for (Module module : modules) {
            if (module.getKey() != eventKey.getKey()) continue;
            module.setEnabled(!module.isEnabled());
        }
    }

    @EventHandler
    private void onGLHack(EventRender3D eventRender3D) {
        GlStateManager.getFloat(2982, (FloatBuffer)GLUtils.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer)GLUtils.PROJECTION.clear());
        this.glGetInteger(2978, (IntBuffer)GLUtils.VIEWPORT.clear());
    }

    public void glGetInteger(int n, IntBuffer intBuffer) {
        GL11.glGetInteger(n, intBuffer);
    }

    @EventHandler
    private void on2DRender(EventRender2D eventRender2D) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module module : modules) {
                if (!module.enabledOnStartup) continue;
                module.setEnabled(true);
            }
        }
    }

    @EventHandler
    public void onRenderScoreboard(EventRenderScoreboard eventRenderScoreboard) {
        HUD hUD = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hUD.isEnabled() || !((Boolean)hUD.scoreBoard.getValue()).booleanValue()) {
            this.objective = null;
            this.scaledRes = null;
            return;
        }
        this.objective = eventRenderScoreboard.getObjective();
        this.scaledRes = eventRenderScoreboard.getScaledRes();
    }

    @EventHandler
    private void scoreBoard(EventRender2D eventRender2D) {
        HUD hUD = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hUD.isEnabled() || !((Boolean)hUD.scoreBoard.getValue()).booleanValue()) {
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
        ArrayList<Score> arrayList = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>(){

            @Override
            public boolean apply(Score score) {
                return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
            }
        }));
        collection = arrayList.size() > 15 ? Lists.newArrayList(Iterables.skip(arrayList, collection.size() - 15)) : arrayList;
        int n = Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            String string = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + ": " + (Object)((Object)EnumChatFormatting.RED) + score.getScorePoints();
            n = Math.max(n, Minecraft.getMinecraft().fontRendererObj.getStringWidth(string));
        }
        int n2 = collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        int n3 = this.scaledRes.getScaledHeight() / 2 + n2 / 3;
        int n4 = 3;
        int n5 = this.scaledRes.getScaledWidth() - n - n4;
        int n6 = 0;
        for (Score score : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            String string = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName());
            String string2 = UISettings.scoreboardBackground ? (Object)((Object)EnumChatFormatting.RED) + "" + score.getScorePoints() : "";
            int n7 = n3 - ++n6 * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            int n8 = this.scaledRes.getScaledWidth() - n4 + 2;
            if (!UISettings.scoreboardBackground) {
                Gui.drawRect(n5 - 2, n7, n8, n7 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, 0).getRGB());
            } else {
                GuiIngame.drawRect(n5 - 2, n7, n8, n7 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 0x50000000);
            }
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string, n5, n7, 0x20FFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string2, n8 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(string2), n7, 0x20FFFFFF);
            if (n6 != collection.size()) continue;
            String string3 = this.objective.getDisplayName();
            if (UISettings.scoreboardBackground) {
                GuiIngame.drawRect(n5 - 2, n7 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1, n8, n7 - 1, 0x60000000);
                GuiIngame.drawRect(n5 - 2, n7 - 1, n8, n7, 0x50000000);
            }
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string3, n5 + n / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(string3) / 2, n7 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 0x20FFFFFF);
        }
    }
}

