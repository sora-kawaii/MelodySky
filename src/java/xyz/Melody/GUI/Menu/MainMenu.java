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
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, (float)this.time / 10.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(0.0, (double)MainMenu.this.height + 1.0, 0.0).endVertex();
            worldrenderer.pos(MainMenu.this.width, (double)MainMenu.this.height + 1.0, 0.0).endVertex();
            worldrenderer.pos(MainMenu.this.width, 0.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    };

    public MainMenu(int opacity) {
        this.opacity = new Opacity(opacity);
        this.CO = true;
    }

    public MainMenu() {
        this.CO = false;
    }

    @Override
    public void initGui() {
        if (Client.firstMenu && Client.instance.authManager.verified) {
            this.mc.displayGuiScreen(new GuiWelcome());
        }
        int opac = (int)(this.CO ? this.opacity.getOpacity() : 140.0f);
        this.opacity = new Opacity(opac);
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
        this.buttonList.add(new ClientButton(0, this.width / 2 - 80, this.height / 2 - 60, 160, 20, "Single Player", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(1, this.width / 2 - 80, this.height / 2 - 36, 160, 20, "Multi Player", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(2, this.width / 2 - 80, this.height / 2 - 12, 160, 20, "Config Manager", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(3, this.width / 2 - 80, this.height / 2 + 12, 160, 20, "Settings", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(5, this.width / 2 + 2, this.height / 2 + 36, 78, 18, "ChangeLogs", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(15, this.width / 2 - 80, this.height / 2 + 36, 78, 18, "Languages", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(50, this.width - 5, this.height - 5, this.width + 8, this.height + 8, "", null, new Color(0, 0, 0, 0)));
        this.buttonList.add(new ClientButton(16, this.width / 2 - 102, this.height / 2 + 36, 18, 18, "", new ResourceLocation("Melody/icon/realms.png"), new ResourceLocation("Melody/icon/realms_hovered.png"), -3.0f, -3.0f, 12.0f, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(10, this.width - 43, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/discord.png"), new Color(20, 20, 20, 0)));
        this.buttonList.add(new ClientButton(11, this.width - 78, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/github.png"), new Color(20, 20, 20, 0)));
        this.buttonList.add(new ClientButton(12, this.width - 113, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/cnsbtool.png"), -4.0f, -4.0f, 20.0f, new Color(20, 20, 20, 0)));
        this.buttonList.add(new ClientButton(13, this.width - 148, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/youtube.png"), new Color(20, 20, 20, 0)));
        this.buttonList.add(new ClientButton(4, this.width - 100, 10, 60, 24, "Vanilla", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(19198, this.width - 165, 10, 60, 24, "Hide Mods", null, new Color(20, 20, 20, 80)));
        this.buttonList.add(new ClientButton(14, this.width - 10 - 24, 10, 25, 24, "", new ResourceLocation("Melody/icon/exit.png"), new Color(20, 20, 20, 60)));
        this.anim.on();
        for (int iii = 0; iii < this.particleCount; ++iii) {
            double randomX = -2.0 + 4.0 * this.RANDOM.nextDouble();
            double randomY = -2.0 + 4.0 * this.RANDOM.nextDouble();
            double randomXm = 0.0 + (double)(this.width - 0) * this.RANDOM.nextDouble();
            double randomYm = 0.0 + (double)(this.height - 0) * this.RANDOM.nextDouble();
            double randomDepthm = this.RANDOM.nextDouble() + 0.1;
            int mX = 0;
            int mY = 0;
            MenuParticle part = new MenuParticle(randomXm + 0.0, randomYm + 0.0, randomDepthm + 0.0, true).addMotion(randomX + (double)(mX / 4), randomY + (double)(mY / 4));
            part.alpha = 0.15f;
            this.particles.add(part);
        }
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new ClickUi(true));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 4: {
                Client.vanillaMenu = true;
                this.mc.displayGuiScreen(new GuiMainMenu());
                Client.instance.saveMenuMode();
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiChangeLog());
                break;
            }
            case 10: {
                try {
                    this.open("https://discord.gg/VnNCJfEyhU");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 11: {
                try {
                    this.open("https://github.com/NMSLAndy");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 12: {
                try {
                    new Browser("https://tool.msirp.cn/", "China Skyblock Tool", true, true, false, 800, 550, JWebBrowser.useEdgeRuntime());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 13: {
                try {
                    this.open("https://www.youtube.com/channel/UCM8A_7JEGLyqlUq7I7BwUVQ");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 14: {
                this.mc.shutdown();
                break;
            }
            case 15: {
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                break;
            }
            case 16: {
                this.switchToRealms();
                break;
            }
            case 50: {
                this.mc.displayGuiScreen(new GuiAltManager(new MainMenu(), true));
                break;
            }
            case 19198: {
                this.mc.displayGuiScreen(new GuiHideMods(this));
            }
        }
    }

    private boolean isChristmas() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
        Date date = new Date(System.currentTimeMillis());
        String dStr = formatter.format(date);
        int dint = Integer.parseInt(dStr);
        return dint > 1225 || dint < 105;
    }

    private boolean isWinter() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
        Date date = new Date(System.currentTimeMillis());
        String dStr = formatter.format(date);
        int dint = Integer.parseInt(dStr);
        return dint >= 1015 || dint <= 231;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        CFontRenderer font2 = FontLoaders.CNMD18;
        CFontRenderer font1 = FontLoaders.CNMD45;
        this.drawDefaultBackground();
        this.gblur.renderBlur(this.opacity.getOpacity());
        this.opacity.interp(15.0f, 4.0f);
        if (this.isWinter()) {
            this.winterParticles.render(this.width / 2, this.height / 2);
        }
        ParticleUtils.drawParticles(mouseX, mouseY);
        if (this.restTimer.hasReached(30000.0)) {
            this.mc.displayGuiScreen(new GuiResting());
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
        int cx = 2;
        int cy = -6;
        if (this.isChristmas()) {
            if (this.m) {
                Logo.M(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 21 + cx, this.height / 2 - 101 + cy, 34.0f, 32.0f);
            }
            if (this.e) {
                Logo.e(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 12 + 30 + cx, this.height / 2 - 91 + cy, 20.0f, 18.4f);
            }
            if (this.l) {
                Logo.l(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + cx, this.height / 2 - 108 + cy, 20.0f, 40.0f);
            }
            if (this.o) {
                Logo.o(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 25 + 28 + 28 + 28 + cx, this.height / 2 - 100 + cy, 38.0f, 29.0f);
            }
            if (this.d) {
                Logo.d(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + 28 + 28 + cx, this.height / 2 - 106 + cy, 32.0f, 36.0f);
            }
            if (this.y) {
                Logo.y(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 16 + 28 + 28 + 28 + 28 + 28 + cx, this.height / 2 - 92 + cy, 21.0f, 32.0f);
            }
        }
        String t = this.isChristmas() ? "" : this.title;
        font1.drawString(t, this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 3, this.height / 2 - 107, new Color(138, 43, 226, 160).getRGB());
        if (this.dick.hasReached(600.0)) {
            if (this.isChristmas()) {
                int am = 72;
                int ae = 50;
                int al = 38;
                int ao = 70;
                int ad = 55;
                int ay = 51;
                int w = -170;
                if (this.m && w == -170) {
                    w += am;
                }
                if (this.e && w == -98) {
                    w += ae;
                }
                if (this.l && w == -48) {
                    w += al;
                }
                if (this.o && w == -10) {
                    w += ao;
                }
                if (this.d && w == 60) {
                    w += ad;
                }
                if (this.y && w == 115) {
                    w += ay;
                }
                RenderUtil.drawFastRoundedRect(this.width / 2 - w / 2 + w + 4 - 3, this.height / 2 - 108, this.width / 2 - w / 2 + w + 5 - 3, this.height / 2 - 76, 1.0f, new Color(198, 198, 198).getRGB());
            } else {
                RenderUtil.drawFastRoundedRect(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 + font1.getStringWidth(this.title) + 4 - 3, this.height / 2 - 108, this.width / 2 - font1.getStringWidth("Melody Sky") / 2 + font1.getStringWidth(this.title) + 5 - 3, this.height / 2 - 83, 1.0f, new Color(198, 198, 198).getRGB());
            }
            if (this.dick.hasReached(1200.0)) {
                this.dick.reset();
            }
        }
        this.mc.fontRendererObj.drawString("\u00a92019-2023 MelodyWorkGroup", 4, this.height - 10, new Color(20, 20, 20, 180).getRGB());
        RenderUtil.drawFastRoundedRect(this.width - 153, this.height - 43, this.width - 8, this.height - 5, 1.0f, new Color(100, 180, 255, 20).getRGB());
        RenderUtil.drawFastRoundedRect(10.0f, 10.0f, 186.0f, 50.0f, 1.0f, new Color(20, 20, 20, 100).getRGB());
        font2.drawCenteredString("Logged in as: " + (Object)((Object)EnumChatFormatting.BLUE) + this.mc.getSession().getUsername(), 98.0f, 20.0f, Colors.GRAY.c);
        font2.drawCenteredString((Object)((Object)EnumChatFormatting.GRAY) + "Released Build " + (Object)((Object)EnumChatFormatting.GREEN) + "2.5.2Build1", 98.0f, 34.0f, Colors.GRAY.c);
        this.anim.render();
        if (!this.particles.isEmpty()) {
            GlStateManager.pushMatrix();
            for (MenuParticle particle : this.particles) {
                particle.update(mouseX, mouseY, this.particles);
                if (!(particle.alpha < 0.1f)) continue;
                particle.remove = true;
            }
            Iterator<MenuParticle> iter = this.particles.iterator();
            while (iter.hasNext()) {
                MenuParticle part = iter.next();
                if (!part.remove) continue;
                iter.remove();
            }
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            for (MenuParticle particle : this.particles) {
                GlStateManager.color(0.5f, 0.6f, 1.0f, particle.alpha);
                double x = particle.x;
                double y = particle.y;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                worldrenderer.pos(x, y + 1.0, 0.0).endVertex();
                worldrenderer.pos(x + 0.5, y + 1.0, 0.0).endVertex();
                worldrenderer.pos(x + 0.5, y, 0.0).endVertex();
                worldrenderer.pos(x, y, 0.0).endVertex();
                tessellator.draw();
            }
            GlStateManager.popMatrix();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.restTimer.reset();
        for (MenuParticle part : this.particles) {
            float angle = (float)Math.toDegrees(Math.atan2((double)mouseY - part.y, (double)mouseX - part.x));
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            double xDist = (double)mouseX - part.x;
            double yDist = (double)mouseY - part.y;
            double dist = Math.sqrt(xDist * xDist + yDist * yDist);
            double mX = Math.cos(Math.toRadians(angle));
            double mY = Math.sin(Math.toRadians(angle));
            if (dist < 20.0) {
                dist = 20.0;
            }
            part.motionX -= mX * 200.0 / (dist / 2.0);
            part.motionY -= mY * 200.0 / (dist / 2.0);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void open(String url) throws Exception {
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url);
        } else if (osName.startsWith("Mac OS")) {
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
            openURL.invoke(null, url);
        } else {
            String[] browsers = new String[]{"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; ++count) {
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() != 0) continue;
                browser = browsers[count];
            }
            if (browser == null) {
                throw new RuntimeException("No Browser Was Found.");
            }
            Runtime.getRuntime().exec(new String[]{browser, url});
        }
    }

    @Override
    public void drawDefaultBackground() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator instance = Tessellator.getInstance();
        WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, 0.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        instance.draw();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
    }

    private void switchToRealms() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }
}

