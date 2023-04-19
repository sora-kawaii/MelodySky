/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Menu.MainMenu;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.Alt;
import xyz.Melody.System.Melody.Account.SlidingCalculation;
import xyz.Melody.System.Melody.Account.altimpl.MicrosoftAlt;
import xyz.Melody.System.Melody.Account.altimpl.OriginalAlt;
import xyz.Melody.System.Melody.Account.altimpl.XBLTokenAlt;
import xyz.Melody.System.Melody.Account.gui.AccessToken.AddTokenAuthGui;
import xyz.Melody.System.Melody.Account.gui.AccessToken.TokenAuthGui;
import xyz.Melody.System.Melody.Account.gui.AddOfflineGui;
import xyz.Melody.System.Melody.Account.gui.RefreshToken.AddRefreshAuth;
import xyz.Melody.System.Melody.Account.gui.RefreshToken.RefreshTokenAuth;
import xyz.Melody.System.Melody.Account.gui.XBoxLive.AddXBLTokenAuth;
import xyz.Melody.System.Melody.Account.gui.XBoxLive.XBLTokenAuth;
import xyz.Melody.System.Melody.Account.microsoft.GuiAddMicrosoftAlt;
import xyz.Melody.System.Melody.Account.microsoft.GuiMicrosoftLogin;
import xyz.Melody.System.Melody.Account.microsoft.MicrosoftLogin;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.WindowsNotification;
import xyz.Melody.Utils.render.DrawEntity;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class GuiAltManager
extends GuiScreen {
    private GuiScreen parentScreen;
    private SlidingCalculation slidingCalculation = new SlidingCalculation(44.0, 44.0);
    private GuiButton buttonLogin;
    private GuiButton buttonRemove;
    public String status = (Object)((Object)EnumChatFormatting.YELLOW) + "Waitting...";
    private WorldClient world = null;
    private EntityPlayerSP player = null;
    private Opacity opacity = new Opacity(20);
    private boolean shabi = false;
    private GaussianBlur gb = new GaussianBlur();
    private DrawEntity entityDrawer = new DrawEntity();
    private MicrosoftLogin microsoftLogin;
    private static Alt selectAlt;
    private Map<String, ThreadDownloadImageData> avatars = new HashMap<String, ThreadDownloadImageData>();
    private boolean tStarted = false;

    public GuiAltManager(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.shabi = false;
        Client.instance.rotationPitchHead = 0.0f;
        Client.instance.prevRotationPitchHead = 0.0f;
    }

    public GuiAltManager(GuiScreen parentScreen, boolean sb) {
        this.parentScreen = parentScreen;
        this.shabi = sb;
        Client.instance.rotationPitchHead = 0.0f;
        Client.instance.prevRotationPitchHead = 0.0f;
    }

    @Override
    public void initGui() {
        this.opacity = new Opacity(20);
        Client.instance.getAccountManager().getAltList().sort(Comparator.comparingDouble(alt -> -alt.getAccountType().toString().length()));
        int appX = 0;
        this.buttonList.add(new ClientButton(0, (int)((float)this.width / 1.43f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Back", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(1, (int)((float)this.width / 1.43f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "Add Offline", new Color(10, 10, 10, 110)));
        this.buttonLogin = new ClientButton(2, (int)((float)this.width / 6.66f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "Login", new Color(10, 10, 10, 110));
        this.buttonList.add(this.buttonLogin);
        this.buttonRemove = new ClientButton(3, (int)((float)this.width / 6.66f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Remove", new Color(10, 10, 10, 110));
        this.buttonList.add(this.buttonRemove);
        this.buttonList.add(new ClientButton(5, (int)((float)this.width / 2.09f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "Microsoft", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(6, (int)((float)this.width / 2.09f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add Microsoft", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(7, (int)((float)this.width / 1.7f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "TokenAuth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(8, (int)((float)this.width / 1.7f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add TokenAuth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(11, (int)((float)this.width / 3.85f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "XBLToken Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(12, (int)((float)this.width / 3.85f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add XBLT Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(15, (int)((float)this.width / 2.7f) + appX, this.height - 66, (int)((float)this.width / 9.6f), 20, "RefreshTkn Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(16, (int)((float)this.width / 2.7f) + appX, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add RefTkn Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(17, (int)((float)this.width / 5.2f + (float)this.width / 9.6f + 4.0f) + appX, this.height - 130, (int)((float)this.width / 9.6f), 20, "Cookie Login", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(18, (int)((float)this.width / 5.2f + (float)this.width / 9.6f + 4.0f) + appX, this.height - 153, (int)((float)this.width / 9.6f), 20, "Copy UUID:ID", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(9, (int)((float)((int)((float)this.width / 13.0f)) + (float)this.width / 9.6f + 4.0f), this.height - 130, (int)((float)this.width / 9.6f), 20, "Reset Session", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(10, (int)((float)this.width / 14.0f), this.height - 130, (int)((float)this.width / 9.6f), 20, "Melody Auth", new Color(10, 10, 10, 110)));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            this.drawDefaultBackground();
            float appendWidth = 30.0f;
            if (this.shabi) {
                this.opacity.interp(140.0f, 5.0f);
            }
            if (!this.shabi) {
                this.opacity.setOpacity(140.0f);
            }
            if (this.opacity.getOpacity() == 140.0f) {
                this.shabi = false;
            }
            if (Client.sessionChanged) {
                this.mc.thePlayer = null;
                this.mc.theWorld = null;
                this.mc.getRenderManager().livingPlayer = null;
                this.mc.getRenderManager().worldObj = null;
                this.entityDrawer = new DrawEntity();
                Client.sessionChanged = false;
            }
            this.gb.renderBlur(this.opacity.getOpacity());
            if (Client.instance.authenticatingUser) {
                for (GuiButton b : this.buttonList) {
                    if (b.id == 2 || b.id == 3) continue;
                    b.enabled = false;
                }
            } else {
                this.buttonRemove.enabled = selectAlt != null;
                this.buttonLogin.enabled = this.buttonRemove.enabled;
                for (GuiButton b : this.buttonList) {
                    if (b.id == 2 || b.id == 3) continue;
                    b.enabled = true;
                }
            }
            RenderUtil.drawFastRoundedRect((float)this.width / 2.4f + appendWidth, 50.0f, (float)(this.width - 100) + appendWidth, this.height - 100, 1.0f, new Color(0, 0, 0, 140).getRGB());
            RenderUtil.drawBorderedRect((float)this.width / 2.4f + appendWidth, 50.0f, (float)(this.width - 100) + appendWidth, this.height - 100, 1.0f, Colors.DARKMAGENTA.c, 0);
            int shabiX = -30;
            int shabiY = 5;
            try {
                int sf = new ScaledResolution(this.mc).getScaleFactor();
                String jb = "0." + sf;
                float factor = Float.parseFloat(jb) * 5.0f;
                int scale = (int)(1.0f / factor * 73.0f);
                if (scale >= 64) {
                    this.entityDrawer.draw((int)((float)this.width / 5.5f), (int)((float)this.height / 1.45f), scale, this.width / 21 - 2 - mouseX / 4, this.height / 16 - mouseY / 7);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.drawPlayerAvatar(this.mc.getSession().getUsername(), this.mc.getSession().getProfile().getId().toString(), (float)(this.width / 20 + shabiX) - (float)shabiX * 0.2f, (float)(33 + shabiY) - (float)shabiY * 0.3f, 1.45f);
            FontLoaders.CNMD22.drawString((Object)((Object)EnumChatFormatting.YELLOW) + "Current: " + this.mc.getSession().getUsername(), this.width / 20 + 102 + shabiX, 74 + shabiY, -1);
            FontLoaders.CNMD20.drawString("UUID: " + this.mc.getSession().getProfile().getId().toString(), this.width / 20 + 102 + shabiX, 87 + shabiY, -1);
            String msUser = Client.instance.authenticatingUser ? (Object)((Object)EnumChatFormatting.YELLOW) + "Authenticating User..." : (Client.instance.authManager.verified ? (Object)((Object)EnumChatFormatting.GREEN) + "true" : (Object)((Object)EnumChatFormatting.GRAY) + "false");
            FontLoaders.CNMD20.drawString("MelodySky Verified: " + msUser, this.width / 20 + 102 + shabiX, 100 + shabiY, -1);
            FontLoaders.CNMD22.drawString(this.status, this.width / 20 + 102 + shabiX, 113 + shabiY, -1);
            if (this.tStarted) {
                this.player = null;
                this.tStarted = false;
            }
            float altY = (float)(52.0 - this.slidingCalculation.getCurrent());
            int jbSize = Client.instance.getAccountManager().getAltList().size();
            float jbY = (float)this.slidingCalculation.getCurrent() / (float)jbSize * 9.4f;
            RenderUtil.drawFastRoundedRect((float)(this.width - 100) + appendWidth, 52.0f + jbY, (float)(this.width - 98) + appendWidth, 62.0f + jbY, 1.0f, Colors.SLOWLY3.c);
            for (Alt alt : Client.instance.getAccountManager().getAltList()) {
                int i;
                String uid;
                char[] dick;
                String name = alt.getUserName();
                String uuid = "";
                if (alt instanceof OriginalAlt) {
                    if (!((OriginalAlt)alt).getUUID().contains("-")) {
                        dick = ((OriginalAlt)alt).getUUID().toCharArray();
                        uid = "";
                        for (i = 0; i < dick.length; ++i) {
                            uid = i == 7 || i == 11 || i == 15 || i == 19 ? uid + dick[i] + "-" : uid + dick[i];
                        }
                        uuid = uid;
                    } else {
                        uuid = ((OriginalAlt)alt).getUUID();
                    }
                } else if (alt instanceof MicrosoftAlt) {
                    dick = ((MicrosoftAlt)alt).getUUID().toCharArray();
                    uid = "";
                    for (i = 0; i < dick.length; ++i) {
                        uid = i == 7 || i == 11 || i == 15 || i == 19 ? uid + dick[i] + "-" : uid + dick[i];
                    }
                    uuid = uid;
                } else if (alt instanceof XBLTokenAlt) {
                    dick = ((XBLTokenAlt)alt).getUUID().toCharArray();
                    uid = "";
                    for (i = 0; i < dick.length; ++i) {
                        uid = i == 7 || i == 11 || i == 15 || i == 19 ? uid + dick[i] + "-" : uid + dick[i];
                    }
                    uuid = uid;
                } else {
                    uuid = EntityPlayer.getOfflineUUID(name).toString();
                }
                if (altY > 44.0f && altY < 396.0f) {
                    RenderUtil.drawFastRoundedRect((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, 1.0f, new Color(50, 50, 50, 150).getRGB());
                    RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, 1.0f, Colors.GRAY.c, 0);
                    if (this.isHovered((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, mouseX, mouseY)) {
                        RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, 1.0f, Colors.WHITE.c, 0);
                    }
                    if (alt.getUserName().equals(this.mc.getSession().getUsername())) {
                        FontLoaders.NMSL45.drawString("<", (float)(this.width - 98) + appendWidth, altY + 8.0f, -1);
                    }
                    if (alt == selectAlt) {
                        RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, 1.0f, Colors.BLUE.c, 0);
                    } else if (this.isHovered((float)this.width / 2.4f + 2.0f + appendWidth, altY, (float)(this.width - 102) + appendWidth, altY + 41.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        selectAlt = alt;
                    }
                    this.drawPlayerAvatar(name, uuid, (float)this.width / 2.4f - 13.0f + appendWidth, altY - 15.0f, 1.0f);
                    FontLoaders.CNMD20.drawString("Name: " + name, (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 4, FadeUtil.PURPLE.getColor().getRGB());
                    FontLoaders.CNMD20.drawString("UUID: " + uuid, (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 29, new Color(160, 160, 160, 190).getRGB());
                    switch (lI.$SwitchMap$xyz$Melody$System$Melody$Account$AccountEnum[alt.getAccountType().ordinal()]) {
                        case 1: {
                            FontLoaders.CNMD20.drawString("Offline", (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 16, Colors.GREEN.c);
                            break;
                        }
                        case 2: {
                            FontLoaders.CNMD20.drawString("Microsoft Account", (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 16, Colors.AQUA.c);
                            break;
                        }
                        case 3: {
                            FontLoaders.CNMD20.drawString("Token Auth", (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 16, Colors.YELLOW.c);
                            break;
                        }
                        case 4: {
                            FontLoaders.CNMD20.drawString("XBoxLive Token", (float)this.width / 2.4f + 10.0f + appendWidth + 35.0f, (int)altY + 16, Colors.ORANGE.c);
                        }
                    }
                }
                this.slidingCalculation.calculation();
                if (this.slidingCalculation.getCurrent() < 0.0) {
                    this.slidingCalculation.setCurrent(0.0);
                }
                if (this.slidingCalculation.getCurrent() > (double)(44 * (jbSize - 1))) {
                    this.slidingCalculation.setCurrent((jbSize - 1) * 44);
                }
                altY += 44.0f;
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        Client.instance.getAccountManager().saveAlt();
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Client.instance.getAccountManager().saveAlt();
            this.mc.displayGuiScreen(new MainMenu((int)this.opacity.getOpacity()));
        } else if (button.id == 1) {
            this.mc.displayGuiScreen(new AddOfflineGui(this));
        } else if (button.id == 2) {
            if (selectAlt != null) {
                Thread shabi = new Thread(this::lambda$actionPerformed$1, "Authentication Thread");
                shabi.start();
            }
        } else if (button.id == 3) {
            if (selectAlt != null) {
                Client.instance.getAccountManager().getAltList().remove(selectAlt);
                selectAlt = null;
            }
        } else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiMicrosoftLogin(this));
        } else if (button.id == 6) {
            this.mc.displayGuiScreen(new GuiAddMicrosoftAlt(this));
        } else if (button.id == 7) {
            this.mc.displayGuiScreen(new TokenAuthGui(this));
        } else if (button.id == 8) {
            this.mc.displayGuiScreen(new AddTokenAuthGui(this));
        } else if (button.id == 9) {
            try {
                ((MCA)((Object)this.mc)).setSession(Client.originalSession);
                this.player = null;
            }
            catch (Exception e) {
                this.status = "\u00a7cError: Couldn't Restore Session (check mc logs).";
                e.printStackTrace();
            }
        } else if (button.id == 10) {
            new Thread(() -> Client.instance.auth(), "Account Authentication Thread").start();
        } else if (button.id == 11) {
            this.mc.displayGuiScreen(new XBLTokenAuth(this));
        } else if (button.id == 12) {
            this.mc.displayGuiScreen(new AddXBLTokenAuth(this));
        } else if (button.id == 15) {
            this.mc.displayGuiScreen(new RefreshTokenAuth(this));
        } else if (button.id == 16) {
            this.mc.displayGuiScreen(new AddRefreshAuth(this));
        } else if (button.id == 17) {
            this.status = "Waitting For Actions From Browser...";
            new Thread("Cookie Login Thread"){

                @Override
                public void run() {
                    try {
                        GuiAltManager.this.microsoftLogin = new MicrosoftLogin(true);
                        while (GuiAltManager.this.mc.currentScreen instanceof GuiAltManager) {
                            if (!((GuiAltManager)GuiAltManager.this).microsoftLogin.logged) continue;
                            GuiAltManager.this.microsoftLogin.close();
                            ((GuiAltManager)GuiAltManager.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + ((GuiAltManager)GuiAltManager.this).microsoftLogin.userName;
                            Client.instance.getAccountManager().addAlt(new MicrosoftAlt(((GuiAltManager)GuiAltManager.this).microsoftLogin.userName, ((GuiAltManager)GuiAltManager.this).microsoftLogin.refreshToken, ((GuiAltManager)GuiAltManager.this).microsoftLogin.msToken, ((GuiAltManager)GuiAltManager.this).microsoftLogin.xblToken, ((GuiAltManager)GuiAltManager.this).microsoftLogin.xsts1, ((GuiAltManager)GuiAltManager.this).microsoftLogin.xsts2, ((GuiAltManager)GuiAltManager.this).microsoftLogin.accessToken, ((GuiAltManager)GuiAltManager.this).microsoftLogin.uuid));
                            NotificationPublisher.queue("Success!", "Added Alt as " + ((GuiAltManager)GuiAltManager.this).microsoftLogin.userName + ".", NotificationType.SUCCESS, 10000);
                            WindowsNotification.show("MelodySky", "Success! Now you can return to the game.");
                            break;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        GuiAltManager.this.status = (Object)((Object)EnumChatFormatting.RED) + e.getClass().getName() + ":" + e.getMessage();
                        if (GuiAltManager.this.microsoftLogin != null) {
                            ((GuiAltManager)GuiAltManager.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed " + e.getClass().getName() + ":" + e.getMessage();
                            GuiAltManager.this.microsoftLogin.close();
                            NotificationPublisher.queue("Failed.", e.getClass().getName() + ":" + e.getMessage(), NotificationType.ERROR, 5000);
                        }
                        GuiAltManager.this.microsoftLogin = null;
                    }
                    this.interrupt();
                }
            }.start();
        } else if (button.id == 18) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection tText = new StringSelection(this.mc.getSession().getProfile().getId().toString() + ":" + this.mc.getSession().getUsername());
            clip.setContents(tText, null);
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Copied UUID:ID To Your Clipboard.";
        }
        super.actionPerformed(button);
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

    private void drawPlayerAvatar(String name, String uuid, float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.scale(scale, scale, 0.0f);
        GlStateManager.translate(-scale, -scale, 0.0f);
        try {
            ThreadDownloadImageData ab;
            if (this.avatars.containsKey(uuid)) {
                ab = this.avatars.get(uuid);
            } else {
                ab = this.getDownloadImageHead(AbstractClientPlayer.getLocationSkin(uuid), uuid);
                this.avatars.put(uuid, ab);
            }
            ab.loadTexture(Minecraft.getMinecraft().getResourceManager());
            this.mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(uuid));
            Gui.drawModalRectWithCustomSizedTexture((int)(x + 18.0f), (int)(y + 18.0f), 37.0f, 37.0f, 37, 37, 297.0f, 297.0f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        GlStateManager.popMatrix();
    }

    public ThreadDownloadImageData getDownloadImageHead(ResourceLocation resourceLocationIn, String uuid) {
        TextureManager tm = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData tex = new ThreadDownloadImageData(null, "https://crafatar.com/skins/" + uuid, DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(uuid)), new ImageBufferDownload());
        tm.loadTexture(resourceLocationIn, tex);
        return tex;
    }

    private String getAccessToken(String xstsToken, String uhs) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
        Client.instance.logger.info("Getting access token");
        URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        JsonObject input = new JsonObject();
        input.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xstsToken);
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), new Gson().toJson(input));
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return jsonObject.get("access_token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String xboxLiveToken) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info...";
        Client.instance.logger.info("Getting xsts token and user hash");
        URL ConnectUrl = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(xboxLiveToken);
        JsonObject xbl_param = new JsonObject();
        JsonObject xbl_properties = new JsonObject();
        xbl_properties.addProperty("SandboxId", "RETAIL");
        xbl_properties.add("UserTokens", new JsonParser().parse(new Gson().toJson(tokens)));
        xbl_param.add("Properties", xbl_properties);
        xbl_param.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        xbl_param.addProperty("TokenType", "JWT");
        String param = new Gson().toJson(xbl_param);
        HttpURLConnection connection = (HttpURLConnection)ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        String token = response_obj.get("Token").getAsString();
        String uhs = response_obj.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
        return new String[]{token, uhs};
    }

    public String getXBoxLiveToken(String microsoftToken) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
        Client.instance.logger.info("Getting xbox live token");
        URL connectUrl = new URL("https://user.auth.xboxlive.com/user/authenticate");
        JsonObject xbl_param = new JsonObject();
        JsonObject xbl_properties = new JsonObject();
        xbl_properties.addProperty("AuthMethod", "RPS");
        xbl_properties.addProperty("SiteName", "user.auth.xboxlive.com");
        xbl_properties.addProperty("RpsTicket", "d=" + microsoftToken);
        xbl_param.add("Properties", xbl_properties);
        xbl_param.addProperty("RelyingParty", "http://auth.xboxlive.com");
        xbl_param.addProperty("TokenType", "JWT");
        String param = new Gson().toJson(xbl_param);
        HttpURLConnection connection = (HttpURLConnection)connectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return response_obj.get("Token").getAsString();
    }

    private void write(BufferedWriter writer, String s) throws IOException {
        writer.write(s);
        writer.close();
    }

    private String read(InputStream stream) throws IOException {
        String s;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            stringBuilder.append(s);
        }
        stream.close();
        reader.close();
        return stringBuilder.toString();
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    private void lambda$actionPerformed$1() {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Loggingin...";
        switch (lI.$SwitchMap$xyz$Melody$System$Melody$Account$AccountEnum[selectAlt.getAccountType().ordinal()]) {
            case 1: {
                ((MCA)((Object)this.mc)).setSession(new Session(selectAlt.getUserName(), "", "", "mojang"));
                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                break;
            }
            case 2: {
                try {
                    MicrosoftLogin microsoftLogin = new MicrosoftLogin(((MicrosoftAlt)selectAlt).getRefreshToken(), this);
                    if (!microsoftLogin.logged) break;
                    ((MCA)((Object)this.mc)).setSession(new Session(microsoftLogin.userName, microsoftLogin.uuid, microsoftLogin.accessToken, "mojang"));
                    Client.instance.getAccountManager().getAltList().remove(selectAlt);
                    Client.instance.getAccountManager().addAlt(new MicrosoftAlt(microsoftLogin.userName, microsoftLogin.refreshToken, microsoftLogin.msToken, microsoftLogin.xblToken, microsoftLogin.xsts1, microsoftLogin.xsts2, microsoftLogin.accessToken, microsoftLogin.uuid));
                    this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                    NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                    this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed. " + ioException.getClass().getName() + ": " + ioException.getMessage();
                }
                break;
            }
            case 3: {
                OriginalAlt originalAlt = (OriginalAlt)selectAlt;
                ((MCA)((Object)this.mc)).setSession(new Session(originalAlt.getUserName(), originalAlt.getUUID(), originalAlt.getAccessToken(), originalAlt.getType()));
                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                break;
            }
            case 4: {
                XBLTokenAlt xbltAlt = (XBLTokenAlt)selectAlt;
                try {
                    String XBLToken = xbltAlt.getXblToken();
                    String[] xstsTokenAndHash = this.getXSTSTokenAndUserHash(XBLToken);
                    String accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
                    this.status = "Logging in from AccessToken...";
                    HttpURLConnection c = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                    c.setRequestProperty("Content-type", "application/json");
                    c.setRequestProperty("Authorization", "Bearer " + accessToken);
                    c.setDoOutput(true);
                    JsonObject json = new JsonParser().parse(IOUtils.toString(c.getInputStream())).getAsJsonObject();
                    String username = json.get("name").getAsString();
                    String xstsToken_f = xstsTokenAndHash[0];
                    String xstsToken_s = xstsTokenAndHash[1];
                    String uuid = json.get("id").getAsString();
                    String token = accessToken;
                    ((MCA)((Object)this.mc)).setSession(new Session(username, uuid, token, "mojang"));
                    this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + username;
                    Client.instance.getAccountManager().getAltList().remove(xbltAlt);
                    xbltAlt = new XBLTokenAlt(username, XBLToken, xstsToken_f, xstsToken_s, token, uuid, "mojang");
                    Client.instance.getAccountManager().addAlt(xbltAlt);
                    this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                    NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                }
                catch (Exception e) {
                    this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: " + e.getMessage();
                    e.printStackTrace();
                }
                break;
            }
        }
        this.tStarted = true;
    }
}

