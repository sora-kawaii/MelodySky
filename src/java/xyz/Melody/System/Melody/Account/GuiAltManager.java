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
    public volatile String status = (Object)((Object)EnumChatFormatting.YELLOW) + "Waitting...";
    private WorldClient world = null;
    private EntityPlayerSP player = null;
    private Opacity opacity = new Opacity(20);
    private boolean shabi = false;
    private GaussianBlur gb = new GaussianBlur();
    private DrawEntity entityDrawer = new DrawEntity();
    private volatile MicrosoftLogin microsoftLogin;
    private static Alt selectAlt;
    private Map<String, ThreadDownloadImageData> avatars = new HashMap<String, ThreadDownloadImageData>();
    private boolean tStarted = false;

    public GuiAltManager(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
        this.shabi = false;
        Client.instance.rotationPitchHead = 0.0f;
        Client.instance.prevRotationPitchHead = 0.0f;
    }

    public GuiAltManager(GuiScreen guiScreen, boolean bl) {
        this.parentScreen = guiScreen;
        this.shabi = bl;
        Client.instance.rotationPitchHead = 0.0f;
        Client.instance.prevRotationPitchHead = 0.0f;
    }

    @Override
    public void initGui() {
        this.opacity = new Opacity(20);
        Client.instance.getAccountManager().getAltList().sort(Comparator.comparingDouble(alt -> -alt.getAccountType().toString().length()));
        int n = 0;
        this.buttonList.add(new ClientButton(0, (int)((float)this.width / 1.43f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Back", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(1, (int)((float)this.width / 1.43f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "Add Offline", new Color(10, 10, 10, 110)));
        this.buttonLogin = new ClientButton(2, (int)((float)this.width / 6.66f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "Login", new Color(10, 10, 10, 110));
        this.buttonList.add(this.buttonLogin);
        this.buttonRemove = new ClientButton(3, (int)((float)this.width / 6.66f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Remove", new Color(10, 10, 10, 110));
        this.buttonList.add(this.buttonRemove);
        this.buttonList.add(new ClientButton(5, (int)((float)this.width / 2.09f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "Microsoft", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(6, (int)((float)this.width / 2.09f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add Microsoft", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(7, (int)((float)this.width / 1.7f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "TokenAuth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(8, (int)((float)this.width / 1.7f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add TokenAuth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(11, (int)((float)this.width / 3.85f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "XBLToken Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(12, (int)((float)this.width / 3.85f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add XBLT Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(15, (int)((float)this.width / 2.7f) + n, this.height - 66, (int)((float)this.width / 9.6f), 20, "RefreshTkn Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(16, (int)((float)this.width / 2.7f) + n, this.height - 44, (int)((float)this.width / 9.6f), 20, "Add RefTkn Auth", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(17, (int)((float)this.width / 5.2f + (float)this.width / 9.6f + 4.0f) + n, this.height - 130, (int)((float)this.width / 9.6f), 20, "Cookie Login", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(18, (int)((float)this.width / 5.2f + (float)this.width / 9.6f + 4.0f) + n, this.height - 153, (int)((float)this.width / 9.6f), 20, "Copy UUID:ID", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(9, (int)((float)((int)((float)this.width / 13.0f)) + (float)this.width / 9.6f + 4.0f), this.height - 130, (int)((float)this.width / 9.6f), 20, "Reset Session", new Color(10, 10, 10, 110)));
        this.buttonList.add(new ClientButton(10, (int)((float)this.width / 14.0f), this.height - 130, (int)((float)this.width / 9.6f), 20, "Melody Auth", new Color(10, 10, 10, 110)));
        super.initGui();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        try {
            this.drawDefaultBackground();
            float f2 = 30.0f;
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
                for (GuiButton guiButton : this.buttonList) {
                    if (guiButton.id == 2 || guiButton.id == 3) continue;
                    guiButton.enabled = false;
                }
            } else {
                this.buttonRemove.enabled = selectAlt != null;
                this.buttonLogin.enabled = this.buttonRemove.enabled;
                for (GuiButton guiButton : this.buttonList) {
                    if (guiButton.id == 2 || guiButton.id == 3) continue;
                    guiButton.enabled = true;
                }
            }
            RenderUtil.drawFastRoundedRect((float)this.width / 2.4f + f2, 50.0f, (float)(this.width - 100) + f2, this.height - 100, 1.0f, new Color(0, 0, 0, 140).getRGB());
            RenderUtil.drawBorderedRect((float)this.width / 2.4f + f2, 50.0f, (float)(this.width - 100) + f2, this.height - 100, 1.0f, Colors.DARKMAGENTA.c, 0);
            int n3 = -30;
            int n4 = 5;
            try {
                int n5 = new ScaledResolution(this.mc).getScaleFactor();
                String string = "0." + n5;
                float f3 = Float.parseFloat(string) * 5.0f;
                int n6 = (int)(1.0f / f3 * 73.0f);
                if (n6 >= 64) {
                    this.entityDrawer.draw((int)((float)this.width / 5.5f), (int)((float)this.height / 1.45f), n6, this.width / 21 - 2 - n / 4, this.height / 16 - n2 / 7);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            this.drawPlayerAvatar(this.mc.getSession().getUsername(), this.mc.getSession().getProfile().getId().toString(), (float)(this.width / 20 + n3) - (float)n3 * 0.2f, (float)(33 + n4) - (float)n4 * 0.3f, 1.45f);
            FontLoaders.CNMD22.drawString((Object)((Object)EnumChatFormatting.YELLOW) + "Current: " + this.mc.getSession().getUsername(), this.width / 20 + 102 + n3, 74 + n4, -1);
            FontLoaders.CNMD20.drawString("UUID: " + this.mc.getSession().getProfile().getId().toString(), this.width / 20 + 102 + n3, 87 + n4, -1);
            String string = Client.instance.authenticatingUser ? (Object)((Object)EnumChatFormatting.YELLOW) + "Authenticating User..." : (Client.instance.authManager.verified ? (Object)((Object)EnumChatFormatting.GREEN) + "true" : (Object)((Object)EnumChatFormatting.GRAY) + "false");
            FontLoaders.CNMD20.drawString("MelodySky Verified: " + string, this.width / 20 + 102 + n3, 100 + n4, -1);
            FontLoaders.CNMD22.drawString(this.status, this.width / 20 + 102 + n3, 113 + n4, -1);
            if (this.tStarted) {
                this.player = null;
                this.tStarted = false;
            }
            float f4 = (float)(52.0 - this.slidingCalculation.getCurrent());
            int n7 = Client.instance.getAccountManager().getAltList().size();
            float f5 = (float)this.slidingCalculation.getCurrent() / (float)n7 * 9.4f;
            RenderUtil.drawFastRoundedRect((float)(this.width - 100) + f2, 52.0f + f5, (float)(this.width - 98) + f2, 62.0f + f5, 1.0f, Colors.SLOWLY3.c);
            for (Alt alt : Client.instance.getAccountManager().getAltList()) {
                int n8;
                String string2;
                char[] cArray;
                String string3 = alt.getUserName();
                String string4 = "";
                if (alt instanceof OriginalAlt) {
                    if (!((OriginalAlt)alt).getUUID().contains("-")) {
                        cArray = ((OriginalAlt)alt).getUUID().toCharArray();
                        string2 = "";
                        for (n8 = 0; n8 < cArray.length; ++n8) {
                            string2 = n8 == 7 || n8 == 11 || n8 == 15 || n8 == 19 ? string2 + cArray[n8] + "-" : string2 + cArray[n8];
                        }
                        string4 = string2;
                    } else {
                        string4 = ((OriginalAlt)alt).getUUID();
                    }
                } else if (alt instanceof MicrosoftAlt) {
                    cArray = ((MicrosoftAlt)alt).getUUID().toCharArray();
                    string2 = "";
                    for (n8 = 0; n8 < cArray.length; ++n8) {
                        string2 = n8 == 7 || n8 == 11 || n8 == 15 || n8 == 19 ? string2 + cArray[n8] + "-" : string2 + cArray[n8];
                    }
                    string4 = string2;
                } else if (alt instanceof XBLTokenAlt) {
                    cArray = ((XBLTokenAlt)alt).getUUID().toCharArray();
                    string2 = "";
                    for (n8 = 0; n8 < cArray.length; ++n8) {
                        string2 = n8 == 7 || n8 == 11 || n8 == 15 || n8 == 19 ? string2 + cArray[n8] + "-" : string2 + cArray[n8];
                    }
                    string4 = string2;
                } else {
                    string4 = EntityPlayer.getOfflineUUID(string3).toString();
                }
                if (f4 > 44.0f && f4 < 396.0f) {
                    RenderUtil.drawFastRoundedRect((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, 1.0f, new Color(50, 50, 50, 150).getRGB());
                    RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, 1.0f, Colors.GRAY.c, 0);
                    if (this.isHovered((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, n, n2)) {
                        RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, 1.0f, Colors.WHITE.c, 0);
                    }
                    if (alt.getUserName().equals(this.mc.getSession().getUsername())) {
                        FontLoaders.NMSL45.drawString("<", (float)(this.width - 98) + f2, f4 + 8.0f, -1);
                    }
                    if (alt == selectAlt) {
                        RenderUtil.drawBorderedRect((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, 1.0f, Colors.BLUE.c, 0);
                    } else if (this.isHovered((float)this.width / 2.4f + 2.0f + f2, f4, (float)(this.width - 102) + f2, f4 + 41.0f, n, n2) && Mouse.isButtonDown(0)) {
                        selectAlt = alt;
                    }
                    this.drawPlayerAvatar(string3, string4, (float)this.width / 2.4f - 13.0f + f2, f4 - 15.0f, 1.0f);
                    FontLoaders.CNMD20.drawString("Name: " + string3, (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 4, FadeUtil.PURPLE.getColor().getRGB());
                    FontLoaders.CNMD20.drawString("UUID: " + string4, (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 29, new Color(160, 160, 160, 190).getRGB());
                    switch (alt.getAccountType()) {
                        case OFFLINE: {
                            FontLoaders.CNMD20.drawString("Offline", (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 16, Colors.GREEN.c);
                            break;
                        }
                        case MICROSOFT: {
                            FontLoaders.CNMD20.drawString("Microsoft Account", (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 16, Colors.AQUA.c);
                            break;
                        }
                        case ORIGINAL: {
                            FontLoaders.CNMD20.drawString("Token Auth", (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 16, Colors.YELLOW.c);
                            break;
                        }
                        case XBLTOKEN: {
                            FontLoaders.CNMD20.drawString("XBoxLive Token", (float)this.width / 2.4f + 10.0f + f2 + 35.0f, (int)f4 + 16, Colors.ORANGE.c);
                        }
                    }
                }
                this.slidingCalculation.calculation();
                if (this.slidingCalculation.getCurrent() < 0.0) {
                    this.slidingCalculation.setCurrent(0.0);
                }
                if (this.slidingCalculation.getCurrent() > (double)(44 * (n7 - 1))) {
                    this.slidingCalculation.setCurrent((n7 - 1) * 44);
                }
                f4 += 44.0f;
            }
            super.drawScreen(n, n2, f);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        Client.instance.getAccountManager().saveAlt();
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 0) {
            Client.instance.getAccountManager().saveAlt();
            this.mc.displayGuiScreen(new MainMenu((int)this.opacity.getOpacity()));
        } else if (guiButton.id == 1) {
            this.mc.displayGuiScreen(new AddOfflineGui(this));
        } else if (guiButton.id == 2) {
            if (selectAlt != null) {
                Thread thread = new Thread(() -> {
                    this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Loggingin...";
                    switch (selectAlt.getAccountType()) {
                        case OFFLINE: {
                            ((MCA)((Object)this.mc)).setSession(new Session(selectAlt.getUserName(), "", "", "mojang"));
                            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                            NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                            break;
                        }
                        case MICROSOFT: {
                            try {
                                MicrosoftLogin microsoftLogin = new MicrosoftLogin(((MicrosoftAlt)selectAlt).getRefreshToken(), this);
                                if (!microsoftLogin.logged) break;
                                ((MCA)((Object)this.mc)).setSession(new Session(microsoftLogin.userName, microsoftLogin.uuid, microsoftLogin.accessToken, "mojang"));
                                Client.instance.getAccountManager().getAltList().remove(selectAlt);
                                Client.instance.getAccountManager().addAlt(new MicrosoftAlt(microsoftLogin.userName, microsoftLogin.refreshToken, microsoftLogin.msToken, microsoftLogin.xblToken, microsoftLogin.xsts1, microsoftLogin.xsts2, microsoftLogin.accessToken, microsoftLogin.uuid));
                                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                                NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                                this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed. " + iOException.getClass().getName() + ": " + iOException.getMessage();
                            }
                            break;
                        }
                        case ORIGINAL: {
                            OriginalAlt originalAlt = (OriginalAlt)selectAlt;
                            ((MCA)((Object)this.mc)).setSession(new Session(originalAlt.getUserName(), originalAlt.getUUID(), originalAlt.getAccessToken(), originalAlt.getType()));
                            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                            NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                            break;
                        }
                        case XBLTOKEN: {
                            XBLTokenAlt xBLTokenAlt = (XBLTokenAlt)selectAlt;
                            try {
                                String string = xBLTokenAlt.getXblToken();
                                String[] stringArray = this.getXSTSTokenAndUserHash(string);
                                String string2 = this.getAccessToken(stringArray[0], stringArray[1]);
                                this.status = "Logging in from AccessToken...";
                                HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                                httpURLConnection.setRequestProperty("Content-type", "application/json");
                                httpURLConnection.setRequestProperty("Authorization", "Bearer " + string2);
                                httpURLConnection.setDoOutput(true);
                                JsonObject jsonObject = new JsonParser().parse(IOUtils.toString(httpURLConnection.getInputStream())).getAsJsonObject();
                                String string3 = jsonObject.get("name").getAsString();
                                String string4 = stringArray[0];
                                String string5 = stringArray[1];
                                String string6 = jsonObject.get("id").getAsString();
                                String string7 = string2;
                                ((MCA)((Object)this.mc)).setSession(new Session(string3, string6, string7, "mojang"));
                                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + string3;
                                Client.instance.getAccountManager().getAltList().remove(xBLTokenAlt);
                                xBLTokenAlt = new XBLTokenAlt(string3, string, string4, string5, string7, string6, "mojang");
                                Client.instance.getAccountManager().addAlt(xBLTokenAlt);
                                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                                NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                            }
                            catch (Exception exception) {
                                this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: " + exception.getMessage();
                                exception.printStackTrace();
                            }
                            break;
                        }
                    }
                    this.tStarted = true;
                }, "Authentication Thread");
                thread.start();
            }
        } else if (guiButton.id == 3) {
            if (selectAlt != null) {
                Client.instance.getAccountManager().getAltList().remove(selectAlt);
                selectAlt = null;
            }
        } else if (guiButton.id == 5) {
            this.mc.displayGuiScreen(new GuiMicrosoftLogin(this));
        } else if (guiButton.id == 6) {
            this.mc.displayGuiScreen(new GuiAddMicrosoftAlt(this));
        } else if (guiButton.id == 7) {
            this.mc.displayGuiScreen(new TokenAuthGui(this));
        } else if (guiButton.id == 8) {
            this.mc.displayGuiScreen(new AddTokenAuthGui(this));
        } else if (guiButton.id == 9) {
            try {
                ((MCA)((Object)this.mc)).setSession(Client.originalSession);
                this.player = null;
            }
            catch (Exception exception) {
                this.status = "\u00a7cError: Couldn't Restore Session (check mc logs).";
                exception.printStackTrace();
            }
        } else if (guiButton.id == 10) {
            new Thread(() -> Client.instance.auth(), "Account Authentication Thread").start();
        } else if (guiButton.id == 11) {
            this.mc.displayGuiScreen(new XBLTokenAuth(this));
        } else if (guiButton.id == 12) {
            this.mc.displayGuiScreen(new AddXBLTokenAuth(this));
        } else if (guiButton.id == 15) {
            this.mc.displayGuiScreen(new RefreshTokenAuth(this));
        } else if (guiButton.id == 16) {
            this.mc.displayGuiScreen(new AddRefreshAuth(this));
        } else if (guiButton.id == 17) {
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
                    catch (Exception exception) {
                        exception.printStackTrace();
                        GuiAltManager.this.status = (Object)((Object)EnumChatFormatting.RED) + exception.getClass().getName() + ":" + exception.getMessage();
                        if (GuiAltManager.this.microsoftLogin != null) {
                            ((GuiAltManager)GuiAltManager.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed " + exception.getClass().getName() + ":" + exception.getMessage();
                            GuiAltManager.this.microsoftLogin.close();
                            NotificationPublisher.queue("Failed.", exception.getClass().getName() + ":" + exception.getMessage(), NotificationType.ERROR, 5000);
                        }
                        GuiAltManager.this.microsoftLogin = null;
                    }
                    this.interrupt();
                }
            }.start();
        } else if (guiButton.id == 18) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(this.mc.getSession().getProfile().getId().toString() + ":" + this.mc.getSession().getUsername());
            clipboard.setContents(stringSelection, null);
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Copied UUID:ID To Your Clipboard.";
        }
        super.actionPerformed(guiButton);
    }

    @Override
    public void drawDefaultBackground() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, 0.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        tessellator.draw();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
    }

    private void drawPlayerAvatar(String string, String string2, float f, float f2, float f3) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.scale(f3, f3, 0.0f);
        GlStateManager.translate(-f3, -f3, 0.0f);
        try {
            ThreadDownloadImageData threadDownloadImageData;
            if (this.avatars.containsKey(string2)) {
                threadDownloadImageData = this.avatars.get(string2);
            } else {
                threadDownloadImageData = this.getDownloadImageHead(AbstractClientPlayer.getLocationSkin(string2), string2);
                this.avatars.put(string2, threadDownloadImageData);
            }
            threadDownloadImageData.loadTexture(Minecraft.getMinecraft().getResourceManager());
            this.mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(string2));
            Gui.drawModalRectWithCustomSizedTexture((int)(f + 18.0f), (int)(f2 + 18.0f), 37.0f, 37.0f, 37, 37, 297.0f, 297.0f);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        GlStateManager.popMatrix();
    }

    public ThreadDownloadImageData getDownloadImageHead(ResourceLocation resourceLocation, String string) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, "https://crafatar.com/skins/" + string, DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(string)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        return threadDownloadImageData;
    }

    private String getAccessToken(String string, String string2) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
        Client.instance.logger.info("Getting access token");
        URL uRL = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identityToken", "XBL3.0 x=" + string2 + ";" + string);
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), new Gson().toJson(jsonObject));
        JsonObject jsonObject2 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return jsonObject2.get("access_token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String string) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info...";
        Client.instance.logger.info("Getting xsts token and user hash");
        URL uRL = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(string);
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("SandboxId", "RETAIL");
        jsonObject2.add("UserTokens", new JsonParser().parse(new Gson().toJson(arrayList)));
        jsonObject.add("Properties", jsonObject2);
        jsonObject.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        jsonObject.addProperty("TokenType", "JWT");
        String string2 = new Gson().toJson(jsonObject);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject3 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        String string3 = jsonObject3.get("Token").getAsString();
        String string4 = jsonObject3.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
        return new String[]{string3, string4};
    }

    public String getXBoxLiveToken(String string) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
        Client.instance.logger.info("Getting xbox live token");
        URL uRL = new URL("https://user.auth.xboxlive.com/user/authenticate");
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("AuthMethod", "RPS");
        jsonObject2.addProperty("SiteName", "user.auth.xboxlive.com");
        jsonObject2.addProperty("RpsTicket", "d=" + string);
        jsonObject.add("Properties", jsonObject2);
        jsonObject.addProperty("RelyingParty", "http://auth.xboxlive.com");
        jsonObject.addProperty("TokenType", "JWT");
        String string2 = new Gson().toJson(jsonObject);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject3 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return jsonObject3.get("Token").getAsString();
    }

    private void write(BufferedWriter bufferedWriter, String string) throws IOException {
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

    private String read(InputStream inputStream) throws IOException {
        String string;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string);
        }
        inputStream.close();
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public boolean isHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }
}

