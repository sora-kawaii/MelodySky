/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Menu;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import xyz.Melody.Client;
import xyz.Melody.GUI.Click.ClickUi;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Menu.GuiChangeLog;
import xyz.Melody.GUI.Menu.GuiHideMods;
import xyz.Melody.GUI.Menu.GuiResting;
import xyz.Melody.GUI.Menu.GuiWelcome;
import xyz.Melody.GUI.Particles.MenuParticle;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.Particles.Winter.ParticleEngine;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.GuiAltManager;
import xyz.Melody.Utils.Browser;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Logo;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.Animation;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class MainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private TimerUtil restTimer = new TimerUtil();
    private String title = "";
    private boolean m = false;
    private boolean e = false;
    private boolean l = false;
    private boolean o = false;
    private boolean d = false;
    private boolean y = false;
    private TimerUtil timer = new TimerUtil();
    private TimerUtil dick = new TimerUtil();
    private TimerUtil saveTimer = new TimerUtil();
    private int letterDrawn = 0;
    private boolean titleDone = false;
    private ArrayList<MenuParticle> particles = new ArrayList();
    private Random RANDOM = new Random();
    private int particleCount = 7000;
    private ParticleEngine winterParticles = new ParticleEngine();
    private Opacity opacity;
    private boolean CO = false;
    private GaussianBlur gblur = new GaussianBlur();
    Animation anim = new Animation(){

        @Override
        public int getMaxTime() {
            return 0;
        }

        @Override
        public void render() {
            Tessellator tessellator = Tessellator.func_178181_a();
            WorldRenderer worldRenderer = tessellator.func_178180_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)((float)this.time / 10.0f));
            worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
            worldRenderer.func_181662_b(0.0, (double)MainMenu.this.field_146295_m + 1.0, 0.0).func_181675_d();
            worldRenderer.func_181662_b(MainMenu.this.field_146294_l, (double)MainMenu.this.field_146295_m + 1.0, 0.0).func_181675_d();
            worldRenderer.func_181662_b(MainMenu.this.field_146294_l, 0.0, 0.0).func_181675_d();
            worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
            tessellator.func_78381_a();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
        }
    };

    public MainMenu(int n) {
        this.opacity = new Opacity(n);
        this.CO = true;
    }

    public MainMenu() {
        this.CO = false;
    }

    public void func_73866_w_() {
        if (Client.firstMenu && Client.instance.authManager.verified) {
            this.field_146297_k.func_147108_a(new GuiWelcome());
        }
        int n = (int)(this.CO ? this.opacity.getOpacity() : 140.0f);
        this.opacity = new Opacity(n);
        this.CO = false;
        this.m = false;
        this.e = false;
        this.l = false;
        this.o = false;
        this.d = false;
        this.y = false;
        this.winterParticles.particles.clear();
        this.particles.clear();
        this.restTimer.reset();
        this.timer.reset();
        this.letterDrawn = 0;
        this.title = "";
        this.titleDone = false;
        this.field_146292_n.add(new ClientButton(0, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 60, 160, 20, "Single Player", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(1, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 36, 160, 20, "Multi Player", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(2, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 12, 160, 20, "Config Manager", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(3, this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 12, 160, 20, "Settings", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(5, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + 36, 78, 18, "ChangeLogs", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(15, this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 36, 78, 18, "Languages", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(50, this.field_146294_l - 5, this.field_146295_m - 5, this.field_146294_l + 8, this.field_146295_m + 8, "", null, new Color(0, 0, 0, 0)));
        this.field_146292_n.add(new ClientButton(16, this.field_146294_l / 2 - 102, this.field_146295_m / 2 + 36, 18, 18, "", new ResourceLocation("Melody/icon/realms.png"), new ResourceLocation("Melody/icon/realms_hovered.png"), -3.0f, -3.0f, 12.0f, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(10, this.field_146294_l - 43, this.field_146295_m - 40, 32, 32, "", new ResourceLocation("Melody/icon/discord.png"), new Color(20, 20, 20, 0)));
        this.field_146292_n.add(new ClientButton(11, this.field_146294_l - 78, this.field_146295_m - 40, 32, 32, "", new ResourceLocation("Melody/icon/github.png"), new Color(20, 20, 20, 0)));
        this.field_146292_n.add(new ClientButton(12, this.field_146294_l - 113, this.field_146295_m - 40, 32, 32, "", new ResourceLocation("Melody/icon/cnsbtool.png"), -4.0f, -4.0f, 20.0f, new Color(20, 20, 20, 0)));
        this.field_146292_n.add(new ClientButton(13, this.field_146294_l - 148, this.field_146295_m - 40, 32, 32, "", new ResourceLocation("Melody/icon/youtube.png"), new Color(20, 20, 20, 0)));
        this.field_146292_n.add(new ClientButton(4, this.field_146294_l - 100, 10, 60, 24, "Vanilla", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(19198, this.field_146294_l - 165, 10, 60, 24, "Hide Mods", null, new Color(20, 20, 20, 80)));
        this.field_146292_n.add(new ClientButton(14, this.field_146294_l - 10 - 24, 10, 25, 24, "", new ResourceLocation("Melody/icon/exit.png"), new Color(20, 20, 20, 60)));
        this.anim.on();
        for (int i = 0; i < this.particleCount; ++i) {
            double d = -2.0 + 4.0 * this.RANDOM.nextDouble();
            double d2 = -2.0 + 4.0 * this.RANDOM.nextDouble();
            double d3 = 0.0 + (double)(this.field_146294_l - 0) * this.RANDOM.nextDouble();
            double d4 = 0.0 + (double)(this.field_146295_m - 0) * this.RANDOM.nextDouble();
            double d5 = this.RANDOM.nextDouble() + 0.1;
            int n2 = 0;
            int n3 = 0;
            MenuParticle menuParticle = new MenuParticle(d3 + 0.0, d4 + 0.0, d5 + 0.0, true).addMotion(d + (double)(n2 / 4), d2 + (double)(n3 / 4));
            menuParticle.alpha = 0.15f;
            this.particles.add(menuParticle);
        }
        super.func_73866_w_();
    }

    protected void func_146284_a(GuiButton guiButton) {
        switch (guiButton.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(new GuiSelectWorld(this));
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a(new ClickUi(true));
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a(new GuiOptions(this, this.field_146297_k.field_71474_y));
                break;
            }
            case 4: {
                Client.vanillaMenu = true;
                this.field_146297_k.func_147108_a(new GuiMainMenu());
                Client.instance.saveMenuMode();
                break;
            }
            case 5: {
                this.field_146297_k.func_147108_a(new GuiChangeLog());
                break;
            }
            case 10: {
                try {
                    this.open("https://discord.gg/VnNCJfEyhU");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 11: {
                try {
                    this.open("https://github.com/NMSLAndy");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 12: {
                try {
                    new Browser("https://tool.msirp.cn/", "China Skyblock Tool", true, true, false, 800, 550, JWebBrowser.useEdgeRuntime());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 13: {
                try {
                    this.open("https://www.youtube.com/channel/UCM8A_7JEGLyqlUq7I7BwUVQ");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 14: {
                this.field_146297_k.func_71400_g();
                break;
            }
            case 15: {
                this.field_146297_k.func_147108_a(new GuiLanguage(this, this.field_146297_k.field_71474_y, this.field_146297_k.func_135016_M()));
                break;
            }
            case 16: {
                this.switchToRealms();
                break;
            }
            case 50: {
                this.field_146297_k.func_147108_a(new GuiAltManager(new MainMenu(), true));
                break;
            }
            case 19198: {
                this.field_146297_k.func_147108_a(new GuiHideMods(this));
            }
        }
    }

    private boolean isChristmas() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
        Date date = new Date(System.currentTimeMillis());
        String string = simpleDateFormat.format(date);
        int n = Integer.parseInt(string);
        return n > 1225 || n < 105;
    }

    private boolean isWinter() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
        Date date = new Date(System.currentTimeMillis());
        String string = simpleDateFormat.format(date);
        int n = Integer.parseInt(string);
        return n >= 1015 || n <= 231;
    }

    public void func_73863_a(int n, int n2, float f) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        CFontRenderer cFontRenderer = FontLoaders.CNMD18;
        CFontRenderer cFontRenderer2 = FontLoaders.CNMD45;
        this.func_146276_q_();
        this.gblur.renderBlur(this.opacity.getOpacity());
        this.opacity.interp(15.0f, 4.0f);
        if (this.isWinter()) {
            this.winterParticles.render(this.field_146294_l / 2, this.field_146295_m / 2);
        }
        ParticleUtils.drawParticles(n, n2);
        if (this.restTimer.hasReached(30000.0)) {
            this.field_146297_k.func_147108_a(new GuiResting());
        }
        if (this.saveTimer.hasReached(1000.0)) {
            Client.instance.saveMenuMode();
            this.saveTimer.reset();
        }
        if (this.timer.hasReached(200.0) && !this.titleDone) {
            if (this.letterDrawn < 1) {
                this.title = this.title + "M";
                this.m = true;
                ++this.letterDrawn;
            }
            if (this.timer.hasReached(450.0)) {
                if (this.letterDrawn < 2) {
                    this.title = this.title + "e";
                    this.e = true;
                    ++this.letterDrawn;
                }
                if (this.timer.hasReached(700.0)) {
                    if (this.letterDrawn < 3) {
                        this.title = this.title + "l";
                        this.l = true;
                        ++this.letterDrawn;
                    }
                    if (this.timer.hasReached(950.0)) {
                        if (this.letterDrawn < 4) {
                            this.title = this.title + "o";
                            this.o = true;
                            ++this.letterDrawn;
                        }
                        if (this.timer.hasReached(1200.0)) {
                            if (this.letterDrawn < 5) {
                                this.title = this.title + "d";
                                this.d = true;
                                ++this.letterDrawn;
                            }
                            if (this.timer.hasReached(1450.0)) {
                                if (this.letterDrawn < 6) {
                                    this.title = this.title + "y";
                                    this.y = true;
                                    ++this.letterDrawn;
                                }
                                if (!this.isChristmas() && this.timer.hasReached(1700.0)) {
                                    if (this.letterDrawn < 7) {
                                        this.title = this.title + " ";
                                        ++this.letterDrawn;
                                    }
                                    if (this.timer.hasReached(1950.0)) {
                                        if (this.letterDrawn < 8) {
                                            this.title = this.title + "S";
                                            ++this.letterDrawn;
                                        }
                                        if (this.timer.hasReached(2200.0)) {
                                            if (this.letterDrawn < 9) {
                                                this.title = this.title + "k";
                                                ++this.letterDrawn;
                                            }
                                            if (this.timer.hasReached(2450.0) && this.letterDrawn < 10) {
                                                this.title = this.title + "y";
                                                ++this.letterDrawn;
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
        int n3 = 2;
        int n4 = -6;
        if (this.isChristmas()) {
            if (this.m) {
                Logo.M(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 21 + n3, this.field_146295_m / 2 - 101 + n4, 34.0f, 32.0f);
            }
            if (this.e) {
                Logo.e(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 12 + 30 + n3, this.field_146295_m / 2 - 91 + n4, 20.0f, 18.4f);
            }
            if (this.l) {
                Logo.l(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + n3, this.field_146295_m / 2 - 108 + n4, 20.0f, 40.0f);
            }
            if (this.o) {
                Logo.o(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 25 + 28 + 28 + 28 + n3, this.field_146295_m / 2 - 100 + n4, 38.0f, 29.0f);
            }
            if (this.d) {
                Logo.d(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + 28 + 28 + n3, this.field_146295_m / 2 - 106 + n4, 32.0f, 36.0f);
            }
            if (this.y) {
                Logo.y(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + 28 + 28 + 28 + n3, this.field_146295_m / 2 - 92 + n4, 21.0f, 32.0f);
            }
        }
        String string = this.isChristmas() ? "" : this.title;
        cFontRenderer2.drawString(string, this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 - 3, this.field_146295_m / 2 - 107, new Color(138, 43, 226, 160).getRGB());
        if (this.dick.hasReached(600.0)) {
            if (this.isChristmas()) {
                int n5 = 72;
                int n6 = 50;
                int n7 = 38;
                int n8 = 70;
                int n9 = 55;
                int n10 = 51;
                int n11 = -170;
                if (this.m && n11 == -170) {
                    n11 += n5;
                }
                if (this.e && n11 == -98) {
                    n11 += n6;
                }
                if (this.l && n11 == -48) {
                    n11 += n7;
                }
                if (this.o && n11 == -10) {
                    n11 += n8;
                }
                if (this.d && n11 == 60) {
                    n11 += n9;
                }
                if (this.y && n11 == 115) {
                    n11 += n10;
                }
                RenderUtil.drawFastRoundedRect(this.field_146294_l / 2 - n11 / 2 + n11 + 4 - 3, this.field_146295_m / 2 - 108, this.field_146294_l / 2 - n11 / 2 + n11 + 5 - 3, this.field_146295_m / 2 - 76, 1.0f, new Color(198, 198, 198).getRGB());
            } else {
                RenderUtil.drawFastRoundedRect(this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 + cFontRenderer2.getStringWidth(this.title) + 4 - 3, this.field_146295_m / 2 - 108, this.field_146294_l / 2 - cFontRenderer2.getStringWidth("Melody Sky") / 2 + cFontRenderer2.getStringWidth(this.title) + 5 - 3, this.field_146295_m / 2 - 83, 1.0f, new Color(198, 198, 198).getRGB());
            }
            if (this.dick.hasReached(1200.0)) {
                this.dick.reset();
            }
        }
        this.field_146297_k.field_71466_p.func_78276_b("\u00a92019-2023 MelodyWorkGroup", 4, this.field_146295_m - 10, new Color(20, 20, 20, 180).getRGB());
        RenderUtil.drawFastRoundedRect(this.field_146294_l - 153, this.field_146295_m - 43, this.field_146294_l - 8, this.field_146295_m - 5, 1.0f, new Color(100, 180, 255, 20).getRGB());
        RenderUtil.drawFastRoundedRect(10.0f, 10.0f, 186.0f, 50.0f, 1.0f, new Color(20, 20, 20, 100).getRGB());
        cFontRenderer.drawCenteredString("Logged in as: " + (Object)((Object)EnumChatFormatting.BLUE) + this.field_146297_k.func_110432_I().func_111285_a(), 98.0f, 20.0f, Colors.GRAY.c);
        cFontRenderer.drawCenteredString((Object)((Object)EnumChatFormatting.GRAY) + "Released Build " + (Object)((Object)EnumChatFormatting.GREEN) + "2.5.1Build1", 98.0f, 34.0f, Colors.GRAY.c);
        this.anim.render();
        if (!this.particles.isEmpty()) {
            GlStateManager.func_179094_E();
            for (MenuParticle menuParticle : this.particles) {
                menuParticle.update(n, n2, this.particles);
                if (!(menuParticle.alpha < 0.1f)) continue;
                menuParticle.remove = true;
            }
            Iterator<MenuParticle> iterator = this.particles.iterator();
            while (iterator.hasNext()) {
                MenuParticle menuParticle = iterator.next();
                if (!menuParticle.remove) continue;
                iterator.remove();
            }
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            for (MenuParticle menuParticle : this.particles) {
                GlStateManager.func_179131_c((float)0.5f, (float)0.6f, (float)1.0f, (float)menuParticle.alpha);
                double d = menuParticle.x;
                double d2 = menuParticle.y;
                worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
                worldRenderer.func_181662_b(d, d2 + 1.0, 0.0).func_181675_d();
                worldRenderer.func_181662_b(d + 0.5, d2 + 1.0, 0.0).func_181675_d();
                worldRenderer.func_181662_b(d + 0.5, d2, 0.0).func_181675_d();
                worldRenderer.func_181662_b(d, d2, 0.0).func_181675_d();
                tessellator.func_78381_a();
            }
            GlStateManager.func_179121_F();
        }
        super.func_73863_a(n, n2, f);
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        this.restTimer.reset();
        for (MenuParticle menuParticle : this.particles) {
            float f = (float)Math.toDegrees(Math.atan2((double)n2 - menuParticle.y, (double)n - menuParticle.x));
            if (f < 0.0f) {
                f += 360.0f;
            }
            double d = (double)n - menuParticle.x;
            double d2 = (double)n2 - menuParticle.y;
            double d3 = Math.sqrt(d * d + d2 * d2);
            double d4 = Math.cos(Math.toRadians(f));
            double d5 = Math.sin(Math.toRadians(f));
            if (d3 < 20.0) {
                d3 = 20.0;
            }
            menuParticle.motionX -= d4 * 200.0 / (d3 / 2.0);
            menuParticle.motionY -= d5 * 200.0 / (d3 / 2.0);
        }
        super.func_73864_a(n, n2, n3);
    }

    private void open(String string) throws Exception {
        String string2 = System.getProperty("os.name", "");
        if (string2.startsWith("Windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + string);
        } else if (string2.startsWith("Mac OS")) {
            Class<?> clazz = Class.forName("com.apple.eio.FileManager");
            Method method = clazz.getDeclaredMethod("openURL", String.class);
            method.invoke(null, string);
        } else {
            String[] stringArray = new String[]{"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String string3 = null;
            for (int i = 0; i < stringArray.length && string3 == null; ++i) {
                if (Runtime.getRuntime().exec(new String[]{"which", stringArray[i]}).waitFor() != 0) continue;
                string3 = stringArray[i];
            }
            if (string3 == null) {
                throw new RuntimeException("No Browser Was Found.");
            }
            Runtime.getRuntime().exec(new String[]{string3, string});
        }
    }

    public void func_146276_q_() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(0.0, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, 0.0, 0.0).func_181675_d();
        worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
        tessellator.func_78381_a();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
    }

    private void switchToRealms() {
        RealmsBridge realmsBridge = new RealmsBridge();
        realmsBridge.switchToRealms(this);
    }
}

