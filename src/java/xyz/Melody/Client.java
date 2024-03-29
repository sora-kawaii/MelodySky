/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody;

import com.google.common.collect.Lists;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.ClientLib.UISettings;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.CustomUI.HUDManager;
import xyz.Melody.MelodyInitializer;
import xyz.Melody.Performance.FoamFix.ProxyCommon;
import xyz.Melody.Performance.FoamFix.api.FoamFixAPI;
import xyz.Melody.Performance.FoamFix.common.FoamFixHelper;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;
import xyz.Melody.Performance.Math.AIImprovements;
import xyz.Melody.System.Commands.FML.IRCChatCommand;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.System.Managers.Client.CommandManager;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.System.Managers.Client.ModuleManager;
import xyz.Melody.System.Managers.Dungeons.DungeonListener;
import xyz.Melody.System.Managers.Dungeons.MimicListener;
import xyz.Melody.System.Managers.Gaming.GameListenerHook;
import xyz.Melody.System.Managers.GaoNeng.GaoNengManager;
import xyz.Melody.System.Melody.Account.AltManager;
import xyz.Melody.System.Melody.Authentication.AuthManager;
import xyz.Melody.System.Melody.Authentication.UserObj;
import xyz.Melody.System.Melody.Chat.IRC;
import xyz.Melody.System.Melody.Chat.IRCKeepAlive;
import xyz.Melody.System.PacketHandler;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.fakemc.FakePacket;
import xyz.Melody.Utils.other.DisplayUtils;
import xyz.Melody.module.FMLModules.AlertsListener;
import xyz.Melody.module.FMLModules.ChatMonitor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.QOL.GhostBlock;
import xyz.Melody.module.modules.QOL.Sprint;
import xyz.Melody.module.modules.QOL.Swappings.ItemSwitcher;
import xyz.Melody.module.modules.others.AutoGG;
import xyz.Melody.module.modules.others.ClientCommands;
import xyz.Melody.module.modules.others.HUD;
import xyz.Melody.module.modules.others.MotionBlur;
import xyz.Melody.module.modules.others.OldAnimations;
import xyz.Melody.module.modules.render.Cam;
import xyz.Melody.module.modules.render.ClickGui;
import xyz.Melody.module.modules.render.Nametags;

@Mod(modid="melodysky", useMetadata=true, clientSideOnly=true, acceptedMinecraftVersions="[1.8.9]")
public final class Client {
    public static boolean inSkyblock;
    public static Configuration modsConfig;
    public static String playerName;
    public static Session originalSession;
    public boolean slayerBossSpawned;
    @Mod.Instance(value="melodysky")
    public static Client instance;
    public static boolean clientChat;
    public static boolean pickaxeAbilityReady;
    public static final List<String> wellKnownMods;
    public float rotationPitchHead;
    public boolean authenticatingUser = false;
    public Thread ircThread;
    private ModuleManager modulemanager;
    public PacketHandler packetHandler;
    private static boolean doesGameWantUngrab;
    public final Logger logger = LogManager.getLogger(this.getClass());
    public MimicListener mimic;
    private AIImprovements entityAIs;
    public static boolean firstLaunch;
    private Timer configSaveTimer;
    public static boolean isMMD;
    public boolean ircExeption;
    public static URL path;
    public static final String build;
    public DungeonListener dungeonUtils;
    private int verified = 0;
    private FriendManager friendmanager;
    public static boolean firstMenu;
    private static boolean isUngrabbed;
    public static int stage;
    public final KeyBinding alt = new KeyBinding("key.command", 56, "key.categories.misc");
    public static boolean stopping;
    public static final Charset defaultEncoding;
    public static boolean inDungeons;
    public IRC irc;
    public static boolean sessionChanged;
    public static final String name;
    public AuthManager authManager;
    private static MouseHelper oldMouseHelper;
    public static final Minecraft mc;
    public static final String version;
    public float prevRotationPitchHead;
    private AltManager accountManager;
    public static Session currentSession;
    private HUDManager hudmanager;
    private static final HashMap<String, Property> modProperties;
    public static String customRank;
    public static boolean vanillaMenu;
    public SkyblockArea sbArea;
    public final String[] rat;
    @SidedProxy(clientSide="xyz.Melody.Performance.FoamFix.ProxyClient", serverSide="xyz.Melody.Performance.FoamFix.ProxyCommon", modId="melodysky")
    public static ProxyCommon proxy;
    private CommandManager commandmanager;

    public void log(String string) {
        this.logger.info("[Melody] [Debug] " + string);
    }

    private void readMenuMode() {
        this.logger.info("[Melody] [Config] Loading MainMenu Settings.");
        List<String> list = FileManager.read("VanillaMenu.txt");
        for (String string : list) {
            if (string.equals("true")) {
                vanillaMenu = true;
            }
            if (!string.equals("false")) continue;
            vanillaMenu = false;
        }
        this.logger.info("[Melody] [Config] Vanilla Menu: " + vanillaMenu + ".");
    }

    private void readUISettings() {
        this.logger.info("[Melody] [Config] Loading UI Settings.");
        List<String> list = FileManager.read("UI.txt");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String string;
            switch (string = iterator.next()) {
                case "chatTextShadow": {
                    UISettings.chatTextShadow = false;
                    break;
                }
                case "chatBackground": {
                    UISettings.chatBackground = false;
                    break;
                }
                case "scoreboardBackground": {
                    UISettings.scoreboardBackground = false;
                }
            }
        }
        this.logger.info("[Melody] [Config] Succefully Loaded UI Settings!");
    }

    public void saveCustomName() {
        String string = playerName == null ? "" : playerName;
        FileManager.save("CustomName.txt", string, false);
    }

    public void saveMenuMode() {
        if (!this.authManager.verified) {
            return;
        }
        String string = String.valueOf(vanillaMenu);
        FileManager.save("VanillaMenu.txt", string, false);
    }

    public static void regrabMouse() {
        if (!isUngrabbed) {
            return;
        }
        Client.mc.mouseHelper = oldMouseHelper;
        if (!doesGameWantUngrab) {
            Client.mc.mouseHelper.grabMouseCursor();
        }
        oldMouseHelper = null;
        isUngrabbed = false;
    }

    private String illIlil() {
        try {
            Method method = mc.getSession().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(mc.getSession(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = mc.getSession().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(mc.getSession(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }

    public void preModHiderAliase(String string) {
        if (string == null) {
            mc.shutdown();
        }
        if (!string.startsWith("ey") && !string.equals("FML")) {
            mc.shutdown();
        }
        if (string.equals("FML") && !mc.getSession().getPlayerID().startsWith("Player")) {
            mc.shutdown();
        }
        if (string.startsWith("ey") && string.length() < 110) {
            mc.shutdown();
        }
    }

    private void lambda$start$0() {
        while (true) {
            try {
                while (true) {
                    Thread.sleep(2000L);
                    this.preModHiderAliase(this.illIlili());
                    this.preModHiderAliase(this.illIlil());
                    this.preModHiderAliase(this.readClientVersion());
                }
            }
            catch (Exception exception) {
                continue;
            }
            break;
        }
    }

    static {
        version = "2.5.2";
        build = "2.5.2Build1";
        name = "MelodySky";
        mc = Minecraft.getMinecraft();
        firstLaunch = false;
        playerName = null;
        customRank = null;
        isUngrabbed = false;
        defaultEncoding = Charset.defaultCharset();
        wellKnownMods = Lists.newArrayList("FML", "Forge", "mcp");
        modProperties = new HashMap();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) {
        stage = 2;
        proxy.postInit();
        this.entityAIs.postInit(fMLPostInitializationEvent);
        this.logger.info("[Melody] Initialized!");
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @SubscribeEvent
    public void onRender(RenderLivingEvent.Specials.Pre pre) {
        float f = Loader.isModLoaded("xiaojiaaddons") ? -0.28f : 0.0f;
        Cam cam = (Cam)this.getModuleManager().getModuleByClass(Cam.class);
        if (pre.entity == Client.mc.thePlayer) {
            float f2;
            double d;
            Object[] objectArray;
            RendererLivingEntity rendererLivingEntity;
            Method method;
            Method method2;
            if (((Nametags)instance.getModuleManager().getModuleByClass(Nametags.class)).isEnabled()) {
                return;
            }
            double d2 = pre.x;
            double d3 = pre.y;
            double d4 = pre.z;
            String string = Client.mc.thePlayer.getName();
            d3 += (double)((float)Client.mc.fontRendererObj.FONT_HEIGHT * 1.15f * 0.02666667f * 2.0f);
            if (GaoNengManager.getIfIsGaoNeng(Client.mc.thePlayer) != null) {
                try {
                    method2 = this.getRenderMethod((RenderPlayer)pre.renderer);
                    method2.setAccessible(true);
                    method2.invoke(pre.renderer, pre.entity, "\u00a7b[" + (Object)((Object)EnumChatFormatting.WHITE) + GaoNengManager.getIfIsGaoNeng(Client.mc.thePlayer).getRank().replaceAll("&", "\u00a7") + "\u00a7b]", d2, d3 + (double)f - 0.25, d4, 64);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (cam.isEnabled() && ((Boolean)cam.showRank.getValue()).booleanValue()) {
                try {
                    method2 = this.getRenderMethod((RenderPlayer)pre.renderer);
                    method2.setAccessible(true);
                    Object[] objectArray2 = new Object[6];
                    objectArray2[0] = pre.entity;
                    objectArray2[1] = customRank;
                    objectArray2[2] = d2;
                    objectArray2[3] = d3 + (double)f - 0.25;
                    objectArray2[4] = d4;
                    objectArray2[5] = 64;
                    method2.invoke(pre.renderer, objectArray2);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if ((Boolean)cam.name.getValue() == false) return;
            try {
                method2 = this.getRenderMethod((RenderPlayer)pre.renderer);
                method2.setAccessible(true);
                method = method2;
                rendererLivingEntity = pre.renderer;
                Object[] objectArray3 = new Object[6];
                objectArray3[0] = pre.entity;
                objectArray3[1] = string;
                objectArray3[2] = d2;
                Object[] objectArray4 = objectArray3;
                objectArray = objectArray3;
                int n = 3;
                d = d3;
                f2 = Client.mc.thePlayer.isSneaking() ? 0.8f : 0.55f;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
            {
                objectArray4[n] = d - (double)f2;
                objectArray[4] = d4;
                objectArray[5] = 64;
                method.invoke(rendererLivingEntity, objectArray);
                return;
            }
        }
        if (!(pre.entity instanceof EntityOtherPlayerMP)) return;
        if (GaoNengManager.getIfIsGaoNeng((EntityOtherPlayerMP)pre.entity) == null) {
            return;
        }
        if (!(pre.renderer instanceof RenderPlayer)) return;
        double d = pre.x;
        double d5 = pre.y;
        double d6 = pre.z;
        String string = "\u00a7b[" + (Object)((Object)EnumChatFormatting.WHITE) + GaoNengManager.getIfIsGaoNeng((EntityOtherPlayerMP)pre.entity).getRank().replaceAll("&", "\u00a7") + "\u00a7b]";
        d5 += (double)((float)Client.mc.fontRendererObj.FONT_HEIGHT * 1.15f * 0.02666667f * 2.0f);
        try {
            Method method = this.getRenderMethod((RenderPlayer)pre.renderer);
            method.setAccessible(true);
            method.invoke(pre.renderer, pre.entity, string, d, d5 + (double)f, d6, 64);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public AltManager getAccountManager() {
        return this.accountManager;
    }

    private void readModValues() {
        this.logger.info("[Melody] [Config] Loading Module Values.");
        List<String> list = FileManager.read("Values.txt");
        for (String string : list) {
            String string2 = string.split(":")[0];
            String string3 = string.split(":")[1];
            Module module = ModuleManager.getModuleByName(string2);
            if (module == null) continue;
            for (Value<?> value : module.getValues()) {
                if (!value.getName().equalsIgnoreCase(string3)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(string.split(":")[2]));
                    this.logger.info("[Melody] [Config] " + module.getName() + " : " + value.getName() + " : " + Boolean.parseBoolean(string.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(string.split(":")[2]));
                    this.logger.info("[Melody] [Config] " + module.getName() + " : " + value.getName() + " : " + Double.parseDouble(string.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(string.split(":")[2]);
            }
        }
        this.logger.info("[Melody] [Config] Succefully Loaded Module Values.");
    }

    public HUDManager getHudmanager() {
        return this.hudmanager;
    }

    public void saveUISettings(boolean bl) {
        String string = "";
        Iterator<String> iterator = UISettings.settings.iterator();
        while (iterator.hasNext()) {
            String string2;
            switch (string2 = iterator.next()) {
                case "chatTextShadow": {
                    if (UISettings.chatTextShadow) break;
                    string = String.valueOf(string) + String.format("%s%s", "chatTextShadow", System.lineSeparator());
                    break;
                }
                case "chatBackground": {
                    if (UISettings.chatBackground) break;
                    string = String.valueOf(string) + String.format("%s%s", "chatBackground", System.lineSeparator());
                    break;
                }
                case "scoreboardBackground": {
                    if (UISettings.scoreboardBackground) break;
                    string = String.valueOf(string) + String.format("%s%s", "scoreboardBackground", System.lineSeparator());
                }
            }
        }
        FileManager.save("UI.txt", string, false);
        if (bl) {
            this.logger.info("[Melody] [Config] UI Settings Saved!");
        }
    }

    public static void preCharset() {
        try {
            Charset charset = Charset.defaultCharset();
            LogManager.getLogger(Client.class).info("[Melody] Charset Pre: " + Charset.defaultCharset());
            if (charset != Charset.forName("UTF-8")) {
                Class<?> clazz = Class.forName("java.nio.charset.Charset");
                Field field = clazz.getDeclaredField("defaultCharset");
                field.setAccessible(true);
                field.set(charset, Charset.forName("UTF-8"));
            }
            LogManager.getLogger(Client.class).info("[Melody] Charset Post: " + Charset.defaultCharset());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void leftClick() {
        if (!Client.invoke(mc.getClass(), "func_147116_af")) {
            Client.invoke(mc.getClass(), "clickMouse");
        }
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public void authDaemon() {
        ++this.verified;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent fMLPreInitializationEvent) {
        this.logger.info("[Melody] Pre Initializatiing...");
        this.logger.info("[Melody] [Preinitialization] Charset: " + Charset.defaultCharset());
        FoamFixAPI.HELPER = new FoamFixHelper();
        stage = 0;
        FoamFixShared.config.init(fMLPreInitializationEvent.getSuggestedConfigurationFile(), false);
        proxy.preInit();
    }

    private void lambda$start$3() {
        UISettings.init();
        UISettings.chatBackground = true;
        UISettings.chatTextShadow = true;
        UISettings.scoreboardBackground = true;
        this.readCustomName();
        this.readCustomRank();
        this.readMenuMode();
        this.readUISettings();
        this.logger.info("[Melody] [CONSOLE] UI Settings Loaded.");
    }

    private void readBinds() {
        this.logger.info("[Melody] [Config] Loading Binds.");
        List<String> list = FileManager.read("Binds.txt");
        for (String string : list) {
            String string2 = string.split(":")[0];
            String string3 = string.split(":")[1];
            Module module = ModuleManager.getModuleByName(string2);
            if (module == null) continue;
            module.setKey(Keyboard.getKeyIndex(string3.toUpperCase()));
            this.logger.info("[Melody] [Config] " + module.getName() + " bound to " + Keyboard.getKeyIndex(string3.toUpperCase()));
        }
        this.logger.info("[Melody] [Config] Succefully Loaded Binds!");
    }

    private void lambda$null$1() {
        while (true) {
            try {
                while (true) {
                    Thread.sleep(1000L);
                    if (mc.getSession() == currentSession) continue;
                    sessionChanged = true;
                    if (Client.instance.ircThread.isAlive()) {
                        Client.instance.logger.error("[Melody] [IRC] Session Changed Disconnected from IRC.");
                        this.irc.disconnect();
                        this.irc.shouldThreadStop = true;
                    }
                    currentSession = mc.getSession();
                    this.authManager.setUser(new UserObj(mc.getSession().getUsername(), mc.getSession().getProfile().getId().toString(), this.readClientVersion()));
                    instance.superAss();
                    FakePacket.getServer("After-Switch - ");
                    FakePacket.readPacketData();
                    Thread.sleep(1000L);
                    sessionChanged = false;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                continue;
            }
            break;
        }
    }

    public static void rightClick() {
        if (!Client.invoke(mc.getClass(), "func_147121_ag")) {
            Client.invoke(mc.getClass(), "rightClickMouse");
        }
    }

    public ModuleManager getModuleManager() {
        return this.modulemanager;
    }

    private void lambda$readConfig$4() {
        this.logger.info("[Melody] [CONSOLE] Start Loading Configs.");
        this.readCustomIS();
        this.readBinds();
        this.readModValues();
        this.readEnabledMods();
        this.readUISettings();
        this.logger.info("[Melody] [CONSOLE] Config Loaded Successfully.");
    }

    private void initModHider() {
        File file = new File(FileManager.getClientDir(), "HideMods.txt");
        modsConfig = new Configuration(file);
        modsConfig.load();
        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
            modProperties.put(modContainer.getModId(), modsConfig.get("general", modContainer.getName(), wellKnownMods.contains(modContainer.getModId()), modContainer.getModId()));
        }
    }

    @EventHandler
    private void nmsl(EventTick eventTick) {
        if (Client.mc.thePlayer != null && Client.mc.theWorld != null) {
            this.prevRotationPitchHead = this.rotationPitchHead;
        }
        if (Client.instance.authManager.verified) {
            return;
        }
        Client client = this;
        for (Module module : client.modulemanager.getModules()) {
            if (module.getType() == ModuleType.Balance || module instanceof Sprint || module instanceof Nametags || module instanceof ClickGui || module instanceof ClientCommands || module instanceof MotionBlur || module instanceof HUD || module instanceof OldAnimations || module instanceof GhostBlock || module instanceof AutoGG || module instanceof Cam || !module.isEnabled()) continue;
            if (Client.mc.theWorld != null && Client.mc.thePlayer != null) {
                Helper.sendMessage("MelodySky Will Not Work Cause of Failed to Verify Your UUID.");
            }
            module.setEnabled(false);
        }
    }

    public void readConfig() {
        new Thread(this::lambda$readConfig$4, "MelodySky CFGInitializer").start();
    }

    private String readClientVersion() {
        try {
            Method method = mc.getSession().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(mc.getSession(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = mc.getSession().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(mc.getSession(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }

    private void superAss() {
        StackTraceElement[] stackTraceElementArray;
        Throwable throwable;
        this.authenticatingUser = true;
        if (mc.getSession().getToken().equals("FML")) {
            this.authManager.verified = true;
            this.authenticatingUser = false;
            return;
        }
        boolean bl = false;
        boolean bl2 = false;
        DisplayUtils.init();
        while (DisplayUtils.t.isAlive()) {
            Thread.sleep(10L);
        }
        try {
            bl = this.authManager.verified;
        }
        catch (Exception exception) {
            throwable = new Throwable(exception.getMessage());
            stackTraceElementArray = new StackTraceElement[]{};
            throwable.setStackTrace(stackTraceElementArray);
            throwable.printStackTrace();
        }
        Client.instance.logger.info("m1Verified: " + bl);
        if (!bl) {
            this.log("[Melody] [AUTHENTICATION] Trying Auth Method 2.");
            try {
                String string = this.authManager.getChecked();
                if (string.contains(this.authManager.getUser().getUuid())) {
                    bl2 = true;
                }
            }
            catch (Exception exception) {
                throwable = new Throwable(exception.getMessage());
                stackTraceElementArray = new StackTraceElement[]{};
                throwable.setStackTrace(stackTraceElementArray);
                throwable.printStackTrace();
            }
            Client.instance.logger.info("m2Verified: " + bl2);
        }
        if (bl || bl2) {
            this.authManager.verified = true;
            this.authenticatingUser = false;
        }
        this.authenticatingUser = false;
    }

    public Client() {
        this.configSaveTimer = new Timer();
        this.rat = new String[]{"                   ..----.._                  ", "                .' .--.    '-.(O)_            ", "    '-.__.-'''=:|  ,  _)_ |__ . c'-..        ", "                 ''------'---''---'-'         "};
        this.entityAIs = new AIImprovements();
        this.packetHandler = new PacketHandler();
    }

    public static void drawTitle(String string, EnumChatFormatting enumChatFormatting) {
        Client.mc.ingameGUI.displayTitle((Object)((Object)enumChatFormatting) + string, null, 0, 2000, 100);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent fMLInitializationEvent) {
        this.logger.info("[Melody] Initializatiing...");
        MelodyInitializer.initModList();
        MelodyInitializer.initModMap();
        MelodyInitializer.initActivedMods();
        MelodyInitializer.initIServerMods();
        stage = 1;
        MinecraftForge.EVENT_BUS.register(proxy);
        this.initFMLFeature();
        proxy.init();
    }

    public void start() {
        stopping = false;
        this.logger.info("[Melody] [CONSOLE] Preinitializing Client...");
        this.logger.info("[Melody] [CONSOLE] Default Charset: " + defaultEncoding + ".");
        WindowsNotification.init();
        this.logger.info("[Melody] [CONSOLE] Starting Client Module Thread.");
        this.logger.info("[Melody] [AUTHENTICATION] Verifing User.");
        originalSession = mc.getSession();
        currentSession = originalSession;
        try {
            path = new URL("https://raw.githubusercontent.com/NMSLAndy/MelodySky-UUID/main/UUIDs.json");
        }
        catch (Exception exception) {
            // empty catch block
        }
        new Thread(this::lambda$start$0, "LWJGL Daemon").start();
        this.authManager = new AuthManager();
        this.authManager.setUser(new UserObj(mc.getSession().getUsername(), mc.getSession().getProfile().getId().toString(), this.illIlil()));
        this.authManager.init();
        FakePacket.getServer("StartUP - ");
        FakePacket.readPacketData();
        this.logger.info("[Melody] [AUTHENTICATION] User Verified: " + this.authManager.verified);
        FileManager.init();
        this.logger.info("[Melody] [CONSOLE] FileManager Initialized.");
        this.logger.info("[Melody] [MODHIDER] Initializing...");
        this.initModHider();
        this.logger.info("[Melody] [MODHIDER] Initialized.");
        new GameListenerHook().init();
        firstMenu = true;
        inDungeons = false;
        inSkyblock = false;
        this.slayerBossSpawned = false;
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.logger.info("[Melody] [CONSOLE] CommandManager Initialized.");
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.logger.info("[Melody] [CONSOLE] FriendManager Initialized.");
        this.accountManager = new AltManager();
        this.accountManager.readAlt();
        this.logger.info("[Melody] [CONSOLE] AccountManager Initialized.");
        this.hudmanager = new HUDManager();
        this.hudmanager.init();
        this.logger.info("[Melody] [CONSOLE] HUDManager Initialized.");
        new Thread(this::lambda$start$2).start();
        this.logger.info("[Melody] [CONSOLE] IRC Thread Started.");
        new Thread(this::lambda$start$3, "MelodySky UIInitializer").start();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.logger.info("[Melody] [CONSOLE] ModuleManager Initialized.");
        this.dungeonUtils = new DungeonListener();
        this.dungeonUtils.init();
        this.mimic = new MimicListener();
        this.mimic.init();
        Client.instance.logger.info("[Melody] [CONSOLE] All Thread Started.");
        this.logger.info("[Melody] [CONSOLE] Dungeon System Initialized.");
        AhBzManager.registerTimer();
        GaoNengManager.registerTimer();
        this.packetHandler.registerEvent();
        this.configSaveTimer.schedule(new TimerTask(){

            @Override
            public void run() {
                if (!((Client)Client.this).modulemanager.enabledNeededMod) {
                    Client.this.saveConfig(false);
                    Client.this.saveCustomName();
                    Client.this.saveMenuMode();
                    Client.this.saveUISettings(false);
                }
            }
        }, 1L, 60000L);
        this.sbArea = new SkyblockArea();
        isMMD = false;
        this.getModuleManager().getModuleByClass(ClickGui.class).setKey(54);
        if (this.verified < 1) {
            Throwable throwable = new Throwable("MelodySky: \u64cd\u4f60\u5988\u7684\u7834\u89e3\u6211\u662f\u5427 : Verify " + this.verified);
            StackTraceElement[] stackTraceElementArray = new StackTraceElement[]{};
            throwable.setStackTrace(stackTraceElementArray);
            mc.displayCrashReport(new CrashReport("MelodySky: \u64cd\u4f60\u5988\u7684\u7834\u89e3\u6211\u662f\u5427", throwable));
        }
        this.logger.info("[Melody] [CONSOLE] Client Module Thread Started.");
    }

    private void initFMLFeature() {
        this.logger.info("[Melody] [CONSOLE] Initializat FML Modules.");
        MinecraftForge.EVENT_BUS.register(this);
        EventBus.getInstance().register(this);
        MinecraftForge.EVENT_BUS.register(new AlertsListener());
        MinecraftForge.EVENT_BUS.register(new ChatMonitor());
        EventBus.getInstance().register(new IRCKeepAlive());
        ClientCommandHandler.instance.registerCommand(new IRCChatCommand());
    }

    private void readCustomRank() {
        List<String> list = FileManager.read("CustomRank.txt");
        for (String string : list) {
            if (string == "" || string == null) continue;
            customRank = string;
        }
    }

    public void auth() {
        this.superAss();
    }

    private void readCustomIS() {
        this.logger.info("[Melody] [Config] Loading Custom ItemSwitcher Values.");
        List<String> list = FileManager.read("CustomIS.txt");
        for (String string : list) {
            ItemSwitcher itemSwitcher = (ItemSwitcher)instance.getModuleManager().getModuleByClass(ItemSwitcher.class);
            itemSwitcher.setCustomItemID(string);
            this.logger.info("[Melody] [Config] Set Custom ItemSwitcher ItemID to " + string + " .");
        }
        this.logger.info("[Melody] [Config] Succefully Loaded CustomItemSwitcher!");
    }

    private void readCustomName() {
        List<String> list = FileManager.read("CustomName.txt");
        for (String string : list) {
            if (string == "" || string == null) continue;
            playerName = string;
        }
    }

    private void readEnabledMods() {
        this.logger.info("[Melody] [Config] Loading Enabled Modules.");
        List<String> list = FileManager.read("Enabled.txt");
        for (String string : list) {
            Module module = ModuleManager.getModuleByName(string);
            if (module == null) continue;
            module.enabledOnStartup = true;
            this.logger.info("[Melody] [Config] Set " + module.getName() + " EnabledOnStartUp True.");
        }
        this.logger.info("[Melody] [Config] Succefully Loaded Enables!");
    }

    public static void middleClick() {
        if (!Client.invoke(mc.getClass(), "func_147112_ai")) {
            Client.invoke(mc.getClass(), "middleClickMouse");
        }
    }

    public void stop() {
        this.logger.info("[Melody] [CONSOLE] Shutting Down...");
        this.logger.info("[Melody] [CONSOL] Quitting IRC...");
        if (this.irc != null) {
            this.irc.sendMessage("CLOSE");
        }
        stopping = true;
        this.logger.info("[Melody] [CONSOL] Reader and Writer Closed.");
        this.logger.info("[Melody] [CONSOL] IRC Thread Stopped.");
        if (!this.modulemanager.enabledNeededMod) {
            this.logger.info("[Melody] [Config] Save Settings...");
            this.saveConfig(true);
            this.saveMenuMode();
            this.saveCustomName();
            this.saveCustomRank();
            this.saveUISettings(true);
            this.logger.info("[Melody] [Config] Config Saved!");
        }
        this.accountManager.saveAlt();
        WindowsNotification.stop();
        this.logger.info("[Melody] [CONSOLE] Client Module Thread Closed!");
    }

    private String illIlili() {
        try {
            Method method = mc.getSession().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(mc.getSession(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = mc.getSession().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(mc.getSession(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }

    private static boolean invoke(Class<?> clazz, String string) {
        try {
            Method method = clazz.getDeclaredMethod(string, new Class[0]);
            method.setAccessible(true);
            method.invoke(Minecraft.getMinecraft(), new Object[0]);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private void lambda$start$2() {
        this.irc = new IRC();
        this.irc.start(true);
        this.irc.ircDaemon = new Thread(this::lambda$null$1);
        this.irc.ircDaemon.setName("Melody -> IRC Daemon Thread");
        this.irc.ircDaemon.start();
    }

    public static void ungrabMouse() {
        if (!Client.mc.inGameHasFocus || isUngrabbed) {
            return;
        }
        if (oldMouseHelper == null) {
            oldMouseHelper = Client.mc.mouseHelper;
        }
        Client.mc.gameSettings.pauseOnLostFocus = false;
        doesGameWantUngrab = !Mouse.isGrabbed();
        oldMouseHelper.ungrabMouseCursor();
        Client.mc.inGameHasFocus = true;
        Client.mc.mouseHelper = new MouseHelper(){

            @Override
            public void mouseXYChange() {
            }

            @Override
            public void grabMouseCursor() {
                doesGameWantUngrab = false;
            }

            @Override
            public void ungrabMouseCursor() {
                doesGameWantUngrab = true;
            }
        };
        isUngrabbed = true;
    }

    public void saveCustomRank() {
        String string = customRank == null ? "" : customRank;
        FileManager.save("CustomRank.txt", string, false);
    }

    public static boolean isWhitelisted(String string) {
        return modProperties.get(string).getBoolean();
    }

    private Method getRenderMethod(RenderPlayer renderPlayer) throws NoSuchMethodException {
        for (Class<?> clazz = renderPlayer.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Method[] methodArray;
            Method[] methodArray2 = methodArray = clazz.getDeclaredMethods();
            int n = methodArray.length;
            for (int i = 0; i < n; ++i) {
                Method method = methodArray2[i];
                if (!method.getName().equals("renderLivingLabel") && !method.getName().equals("func_147906_a")) continue;
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public void saveConfig(boolean bl) {
        if (!this.authManager.verified) {
            return;
        }
        String string = "";
        instance.getModuleManager();
        for (Module object : ModuleManager.getModules()) {
            for (Value<?> value : object.getValues()) {
                string = String.valueOf(string) + String.format("%s:%s:%s%s", object.getName(), value.getName(), value.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", string, false);
        if (bl) {
            this.logger.info("[Melody] [Config] Module Value Saved!");
        }
        Object object = "";
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            object = String.valueOf(object) + String.format("%s%s", module.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", (String)object, false);
        if (bl) {
            this.logger.info("[Melody] [Config] Enabled Modules Saved!");
        }
    }
}

