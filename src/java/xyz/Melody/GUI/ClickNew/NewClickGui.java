/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.UISettings;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.ClickNew.ClickGuiRenderUtil;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.System.Managers.Client.ModuleManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.Translate;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.others.HUD;

public final class NewClickGui
extends GuiScreen
implements GuiYesNoCallback {
    public ModuleType currentModuleType = ModuleType.QOL;
    public Module currentModule = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() != 0 ? Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(0) : null;
    public float startX = 100.0f;
    public float startY = 100.0f;
    public int moduleStart = 0;
    public int valueStart = 0;
    boolean previousmouse = true;
    boolean mouse;
    public Opacity opacity = new Opacity(1);
    public Opacity blurOpacity = new Opacity(1);
    public int opacityx = 255;
    public float moveX = 0.0f;
    public float moveY = 0.0f;
    public boolean binding = false;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;
    public int mouseWheel;
    public int mouseX;
    public int mouseY;
    private TimerUtil lcTimer = new TimerUtil();
    private TimerUtil rcTimer = new TimerUtil();
    private TimerUtil idkTimer = new TimerUtil();
    private boolean inSearch;
    private String SearchText = "Search...";
    private Translate translate = new Translate(0.0f, 0.0f);
    private GaussianBlur gb = new GaussianBlur();

    @Override
    public void initGui() {
        this.opacity = new Opacity(1);
        this.blurOpacity = new Opacity(1);
        this.translate = new Translate(0.0f, 0.0f);
        this.startX = (float)this.width / 3.7f;
        this.startY = this.height / 5;
        this.SearchText = "Search...";
        this.buttonList.add(new ClientButton(0, 5, this.height - 106, 90, 20, "ChatTextShadow", new Color(138, 43, 226, 80)));
        this.buttonList.add(new ClientButton(1, 5, this.height - 84, 90, 20, "ChatBackground", new Color(138, 43, 226, 80)));
        this.buttonList.add(new ClientButton(2, 5, this.height - 62, 120, 20, "ScoreboardBackground", new Color(138, 43, 226, 80)));
        this.buttonList.add(new ClientButton(3, 5, this.height - 40, 80, 20, "Edit Locations", new Color(221, 160, 221, 80)));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton gay) throws IOException {
        switch (gay.id) {
            case 0: {
                UISettings.chatTextShadow = !UISettings.chatTextShadow;
                break;
            }
            case 1: {
                UISettings.chatBackground = !UISettings.chatBackground;
                break;
            }
            case 2: {
                UISettings.scoreboardBackground = !UISettings.scoreboardBackground;
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new HUDScreen());
            }
        }
        super.actionPerformed(gay);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.currentModule != null && this.binding) {
            this.currentModule.setKey(keyCode);
            this.binding = false;
        }
        if (this.inSearch) {
            if (keyCode == 14) {
                try {
                    this.SearchText = this.SearchText.substring(0, this.SearchText.length() - 1);
                    this.SearchText.substring(0, this.SearchText.length() - 1);
                }
                catch (StringIndexOutOfBoundsException e) {
                    this.SearchText = "";
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                this.SearchText = this.SearchText + Character.toString(typedChar);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int m;
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (((Boolean)hud.cgblur.getValue()).booleanValue()) {
            this.gb.renderBlur(this.blurOpacity.getOpacity());
            this.blurOpacity.interp(50.0f, 5.0f);
        }
        this.opacity.interpolate(160.0f);
        RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), this.width - 160, this.height - 40, 32.0f, 32.0f);
        FontLoaders.CNMD34.drawString("MelodySky", this.width - 125, this.height - 34, -1);
        FontLoaders.CNMD24.drawString(UISettings.chatTextShadow + "", 100.0f, this.height - 101, -1);
        FontLoaders.CNMD24.drawString(UISettings.chatBackground + "", 100.0f, this.height - 79, -1);
        FontLoaders.CNMD24.drawString(UISettings.scoreboardBackground + "", 130.0f, this.height - 57, -1);
        if (!Client.instance.authManager.verified) {
            FontLoaders.CNMD28.drawCenteredString("MelodySky Will Not Work Cause of Failed to Verify Your UUID.", this.width / 2, 20.0f, Colors.BLUE.c);
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof NewClickGui)) {
            this.lastOutro = this.outro;
            if ((double)this.outro < 1.7) {
                this.outro += 0.1f;
                this.outro = (float)((double)this.outro + ((1.7 - (double)this.outro) / 3.0 - 0.001));
            }
            if ((double)this.outro > 1.7) {
                this.outro = 1.7f;
            }
            if (this.outro < 1.0f) {
                this.outro = 1.0f;
            }
        }
        if (this.mc.currentScreen != null && this.mc.currentScreen != null && !(this.mc.currentScreen instanceof NewClickGui)) {
            return;
        }
        this.lastPercent = this.percent;
        this.lastPercent2 = this.percent2;
        if ((double)this.percent > 0.98) {
            this.percent = (float)((double)this.percent + ((0.98 - (double)this.percent) / (double)1.45f - 0.001));
        }
        if ((double)this.percent <= 0.98 && this.percent2 < 1.0f) {
            this.percent2 = (float)((double)this.percent2 + ((double)((1.0f - this.percent2) / 2.8f) + 0.002));
        }
        if (this.isHovered(this.startX, this.startY - 25.0f, this.startX + 400.0f, this.startY + 25.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)mouseX - this.startX;
                this.moveY = (float)mouseY - this.startY;
            } else {
                this.startX = (float)mouseX - this.moveX;
                this.startY = (float)mouseY - this.moveY;
            }
            this.previousmouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        GL11.glPushMatrix();
        this.translate.interpolate(this.width, this.height, 8.0);
        double xmod = (float)(this.width / 2) - this.translate.getX() / 2.0f;
        double ymod = (float)(this.height / 2) - this.translate.getY() / 2.0f;
        GlStateManager.translate(xmod, ymod, 0.0);
        GlStateManager.scale(this.translate.getX() / (float)this.width, this.translate.getY() / (float)this.height, 1.0f);
        RenderUtil.drawFastRoundedRect(this.startX, this.startY, this.startX + 200.0f, this.startY + 320.0f, 1.0f, new Color(30, 30, 30, (int)this.opacity.getOpacity()).getRGB());
        ClickGuiRenderUtil.drawRect(this.startX + 200.0f, this.startY, this.startX + 431.0f, this.startY + 320.0f, new Color(40, 40, 40, (int)this.opacity.getOpacity()).getRGB());
        ClickGuiRenderUtil.drawRainbowRect(this.startX, this.startY, this.startX + 430.0f, this.startY + 1.0f);
        for (m = 0; m < ModuleType.values().length; ++m) {
            ModuleType[] mY = ModuleType.values();
            if (mY[m] != this.currentModuleType || this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
                if (mY[m].toString() == "QOL") {
                    FontLoaders.NMSL25.drawString("QOL", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Dungeons") {
                    FontLoaders.NMSL25.drawString("Dungeon", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Swapping") {
                    FontLoaders.NMSL25.drawString("Swaps", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Fishing") {
                    FontLoaders.NMSL25.drawString("Fishing", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Nether") {
                    FontLoaders.NMSL25.drawString("Nether", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Render") {
                    FontLoaders.NMSL25.drawString("Render", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Macros") {
                    FontLoaders.NMSL25.drawString("Macros", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Others") {
                    FontLoaders.NMSL25.drawString("Others", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (mY[m].toString() == "Balance") {
                    FontLoaders.NMSL25.drawString("Balance", this.startX + 7.0f, this.startY + 20.0f + (float)(m * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
            } else if (this.SearchText == "Search..." || this.SearchText == null || this.SearchText == "") {
                ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0f, this.startY + 16.0f + (float)(m * 30), this.startX + 5.5f, this.startY + 39.0f + (float)(m * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255).getRGB());
                ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0f, this.startY + 16.0f + (float)(m * 30), this.startX + 60.0f, this.startY + 39.0f + (float)(m * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255, 100).getRGB());
                if (mY[m].toString() == "QOL") {
                    FontLoaders.NMSL25.drawString("QOL", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Dungeons") {
                    FontLoaders.NMSL25.drawString("Dungeon", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Swapping") {
                    FontLoaders.NMSL25.drawString("Swaps", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Fishing") {
                    FontLoaders.NMSL25.drawString("Fishing", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Nether") {
                    FontLoaders.NMSL25.drawString("Nether", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Render") {
                    FontLoaders.NMSL25.drawString("Render", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Macros") {
                    FontLoaders.NMSL25.drawString("Macros", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Others") {
                    FontLoaders.NMSL25.drawString("Others", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (mY[m].toString() == "Balance") {
                    FontLoaders.NMSL25.drawString("Balance", this.startX + 8.0f, this.startY + 20.0f + (float)(m * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
            }
            try {
                if (!this.isCategoryHovered(this.startX + 7.0f, this.startY + 15.0f + (float)(m * 30), this.startX + 60.0f, this.startY + 45.0f + (float)(m * 30), mouseX, mouseY) || !Mouse.isButtonDown(0)) continue;
                this.SearchText = "Search...";
                this.inSearch = false;
                this.currentModuleType = mY[m];
                this.currentModule = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() != 0 ? Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(0) : null;
                this.moduleStart = 0;
                continue;
            }
            catch (Exception var23) {
                System.err.println(var23);
            }
        }
        this.mouseWheel = Mouse.getDWheel();
        if (this.isCategoryHovered(this.startX + 60.0f, this.startY, this.startX + 200.0f, this.startY + 320.0f, mouseX, mouseY)) {
            if (this.mouseWheel < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() - 1) {
                ++this.moduleStart;
            }
            if (this.mouseWheel > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        if (this.isCategoryHovered(this.startX + 200.0f, this.startY, this.startX + 420.0f, this.startY + 320.0f, mouseX, mouseY)) {
            if (this.mouseWheel < 0 && this.valueStart < this.currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }
            if (this.mouseWheel > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        GL11.glPopMatrix();
        boolean searchHover = this.mouseWithinBounds(mouseX, mouseY, this.width / 2 - 100, 40, 200, 20);
        RenderUtil.drawFastRoundedRect(this.width / 2 - 100, 40.0f, this.width / 2 + 100, 60.0f, 1.0f, new Color(55, 55, 55, 190).getRGB());
        FontLoaders.CNMD20.drawStringWithShadow(this.SearchText, this.width / 2 - 95, 46.0, new Color(255, 255, 255, 190).getRGB());
        RenderUtil.drawBorderedRect(this.width / 2 - 100, 40.0f, this.width / 2 + 100, 60.0f, 0.5f, this.inSearch ? new Color(255, 255, 255, 190).getRGB() : new Color(100, 100, 100, 100).getRGB(), new Color(180, 180, 180, 0).getRGB());
        if (searchHover && Mouse.isButtonDown(0)) {
            this.inSearch = true;
            this.SearchText = "";
        } else if (!searchHover && Mouse.isButtonDown(0)) {
            this.inSearch = false;
        }
        if (this.SearchText == "" && !this.inSearch) {
            this.SearchText = "Search...";
        }
        if (this.inSearch) {
            if (this.idkTimer.hasReached(500.0)) {
                FontLoaders.CNMD20.drawString("|", this.width / 2 - 95 + FontLoaders.CNMD20.getStringWidth(this.SearchText), 46.0f, -1);
                if (this.idkTimer.hasReached(1000.0)) {
                    this.idkTimer.reset();
                }
            }
        } else {
            this.idkTimer.reset();
        }
        GL11.glPushMatrix();
        GlStateManager.translate(xmod, ymod, 0.0);
        GlStateManager.scale(this.translate.getX() / (float)this.width, this.translate.getY() / (float)this.height, 1.0f);
        if (this.currentModule != null) {
            RenderUtil.drawBorderedRect(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, 1.0f, new Color(90, 90, 90, (int)(1.5 * (double)this.opacity.getOpacity())).getRGB(), new Color(30, 30, 30, (int)(0.5 * (double)this.opacity.getOpacity())).getRGB());
            if (!this.binding) {
                FontLoaders.NMSL18.drawString("Current Key: " + Keyboard.getKeyName(this.currentModule.getKey()), this.startX + 215.0f, this.startY + 300.0f, -1);
                FontLoaders.NMSL18.drawString((Object)((Object)EnumChatFormatting.GRAY) + "Click to Bind.", this.startX + 360.0f, this.startY + 300.0f, -1);
            } else {
                FontLoaders.NMSL18.drawString("Waitting...", this.startX + 215.0f, this.startY + 300.0f, -1);
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, mouseX, mouseY) && Mouse.isButtonDown(0) && !this.binding) {
                this.binding = true;
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, mouseX, mouseY) && Mouse.isButtonDown(1) && this.binding) {
                this.binding = false;
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, mouseX, mouseY) && Mouse.isButtonDown(1) && !this.binding) {
                this.currentModule.setKey(0);
            }
        }
        ClickGuiRenderUtil.drawRoundedRect(this.startX + 199.0f, this.startY - 150.0f + (float)this.moduleStart - 85.0f + (float)(m * 30), this.startX + 201.0f, this.startY - 50.0f + (float)this.moduleStart + 10.0f + (float)(m * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255, 180).getRGB());
        if (this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
            FontLoaders.NMSL18.drawString("Search -> " + this.currentModule.getType().toString() + " ->", this.startX + 70.0f, this.startY + 12.5f, new Color(248, 248, 248).getRGB());
        } else {
            FontLoaders.NMSL18.drawString(this.currentModule == null ? this.currentModuleType.toString() : this.currentModuleType.toString(), this.startX + 70.0f, this.startY + 12.5f, new Color(248, 248, 248).getRGB());
        }
        if (this.currentModule != null) {
            FontLoaders.NMSL18.drawString(this.currentModule.getName(), this.startX + 210.5f, this.startY + 10.5f, Colors.AQUA.c);
            if (this.currentModule.getValues().isEmpty()) {
                FontLoaders.NMSL18.drawString("No Values Available.", this.startX + 270.0f, this.startY + 150.0f, Colors.AQUA.c);
            }
        }
        if (this.currentModule != null) {
            FontLoaders.NMSL14.drawString(this.currentModule.getModInfo(), this.startX + 215.5f + (float)FontLoaders.NMSL18.getStringWidth(this.currentModule.getName()), this.startY + 12.0f, -1);
        }
        if (this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
            int i;
            float var24 = this.startY + 30.0f;
            for (i = 0; i < ModuleManager.getModules().size(); ++i) {
                boolean valContains;
                Module mod = ModuleManager.getModules().get(i);
                String curMod = ModuleManager.getModules().get(i).getName().toUpperCase();
                String curModInfo = ModuleManager.getModules().get(i).getModInfo().toUpperCase();
                String val = "";
                for (Value<?> v : mod.getValues()) {
                    val = val + (v.getName() + " ").toUpperCase();
                }
                boolean bl = valContains = mod.getValues().size() == 0 ? false : val.contains(this.SearchText.toUpperCase());
                if (!curMod.contains(this.SearchText.toUpperCase()) && !curModInfo.contains(this.SearchText.toUpperCase()) && !valContains) continue;
                Module value = ModuleManager.getModules().get(i);
                if (var24 > this.startY + 300.0f) break;
                RenderUtil.drawFastRoundedRect(this.startX + 195.0f, var24, this.startX + 65.0f, var24 + 20.0f, 1.0f, new Color(40, 40, 40, 20).getRGB());
                if (i < this.moduleStart) continue;
                FontLoaders.NMSL16.drawString(value.getName(), this.startX + 86.0f, var24 + 8.0f, new Color(248, 248, 248, (int)this.opacity.getOpacity()).getRGB());
                if (!value.isEnabled()) {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, var24 + 10.0f, 3.0, new Color(255, 0, 0).getRGB(), 50);
                } else {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, var24 + 10.0f, 3.0, new Color(0, 255, 0).getRGB(), 50);
                }
                RenderUtil.drawFastRoundedRect(this.startX + 65.0f, var24, this.startX + 183.0f, var24 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, 2.0f, mod.isEnabled() ? new Color(153, 51, 250, 75).getRGB() : new Color(100, 100, 100, 45).getRGB());
                if (this.isSettingsButtonHovered(this.startX + 65.0f, var24, this.startX + 183.0f, var24 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        if (value.isEnabled()) {
                            value.setEnabled(false);
                        } else {
                            value.setEnabled(true);
                        }
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown(1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isSettingsButtonHovered(this.startX + 90.0f, var24, this.startX + 100.0f + (float)FontLoaders.NMSL20.getStringWidth(value.getName()), var24 + 8.0f + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    this.currentModule = value;
                    this.valueStart = 0;
                }
                var24 += 25.0f;
            }
            var24 = this.startY + 30.0f;
            for (i = 0; i < this.currentModule.getValues().size() && var24 <= this.startY + 280.0f; ++i) {
                if (i < this.valueStart) continue;
                Value<?> var25 = this.currentModule.getValues().get(i);
                if (var25 instanceof Numbers) {
                    float x = this.startX + 300.0f;
                    double current = 68.0f * (((Number)var25.getValue()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue()) / (((Number)((Numbers)var25).getMaximum()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue());
                    ClickGuiRenderUtil.drawRect(x - 6.0f, var24 + 2.0f, (float)((double)x + 75.0), var24 + 3.0f, new Color(50, 50, 50, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x - 6.0f, var24 + 2.0f, (float)((double)x + current + 6.5), var24 + 3.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect((float)((double)x + current + 2.0), var24, (float)((double)x + current + 7.0), var24 + 5.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    boolean mouseAtPlus = (float)this.mouseX > x + 80.0f && (float)mouseX < x + 90.0f && (float)mouseY > var24 - 2.5f && (float)mouseY < var24 + 7.5f;
                    RenderUtil.drawFastRoundedRect(x + 80.0f, var24 - 2.5f, x + 90.0f, var24 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("+", x + 85.0f, var24 - 0.5f, -1);
                    if (mouseAtPlus && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() >= ((Number)((Numbers)var25).getMaximum()).doubleValue()) {
                            this.lcTimer.reset();
                        } else {
                            float append = ((Number)((Numbers)var25).inc).floatValue();
                            DecimalFormat df = new DecimalFormat("0.00");
                            double finalPlus = Double.parseDouble(df.format(((Number)var25.getValue()).floatValue() + append));
                            var25.setValue(finalPlus);
                            this.lcTimer.reset();
                        }
                    }
                    boolean mouseAtCut = (float)this.mouseX > x + 93.0f && (float)mouseX < x + 103.0f && (float)mouseY > var24 - 2.5f && (float)mouseY < var24 + 7.5f;
                    RenderUtil.drawFastRoundedRect(x + 93.0f, var24 - 2.5f, x + 103.0f, var24 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("-", x + 97.5f, var24 - 0.5f, -1);
                    if (mouseAtCut && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() <= ((Number)((Numbers)var25).getMinimum()).doubleValue()) {
                            this.rcTimer.reset();
                        } else {
                            float append = ((Number)((Numbers)var25).inc).floatValue();
                            DecimalFormat df = new DecimalFormat("0.00");
                            double finalPlus = Double.parseDouble(df.format(((Number)var25.getValue()).floatValue() - append));
                            var25.setValue(finalPlus);
                            this.rcTimer.reset();
                        }
                    }
                    FontLoaders.NMSL18.drawStringWithShadow(var25.getName() + ": " + var25.getValue(), this.startX + 210.0f, var24, -1);
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    if (this.isButtonHovered(x, var24 - 2.0f, x + 75.0f, var24 + 7.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            current = ((Number)((Numbers)var25).getMinimum()).doubleValue();
                            double max = ((Number)((Numbers)var25).getMaximum()).doubleValue();
                            double inc = ((Number)((Numbers)var25).getIncrement()).doubleValue();
                            double valAbs = (double)mouseX - ((double)x + 1.0);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            double valRel = (max - current) * perc;
                            double val = current + valRel;
                            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            ((Numbers)var25).setValue(val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                    }
                    var24 += 20.0f;
                }
                if (var25 instanceof Option) {
                    float x = this.startX + 300.0f;
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24, x + 76.0f, var24 + 1.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24 + 8.0f, x + 76.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24, x + 57.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 77.0f, var24, x + 76.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), this.startX + 210.0f, var24, -1);
                    if (((Boolean)var25.getValue()).booleanValue()) {
                        ClickGuiRenderUtil.drawRect(x + 67.0f, var24 + 2.0f, x + 75.0f, var24 + 7.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    } else {
                        ClickGuiRenderUtil.drawRect(x + 58.0f, var24 + 2.0f, x + 65.0f, var24 + 7.0f, new Color(150, 150, 150, (int)this.opacity.getOpacity()).getRGB());
                    }
                    if (this.isCheckBoxHovered(x + 56.0f, var24, x + 76.0f, var24 + 9.0f, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            var25.setValue((Boolean)var25.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    var24 += 20.0f;
                }
                if (!(var25 instanceof Mode)) continue;
                float x = this.startX + 300.0f;
                ClickGuiRenderUtil.drawRect(x - 5.0f, var24 - 5.0f, x + 90.0f, var24 + 15.0f, new Color(56, 56, 56, (int)this.opacity.getOpacity()).getRGB());
                ClickGuiRenderUtil.drawBorderRect(x - 5.0f, var24 - 5.0f, x + 90.0f, var24 + 15.0f, new Color(101, 81, 255, (int)this.opacity.getOpacity()).getRGB(), 2.0);
                FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), this.startX + 210.0f, var24 + 2.0f, -1);
                FontLoaders.NMSL18.drawStringWithShadow(((Mode)var25).getModeAsString(), x + 40.0f - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)var25).getModeAsString()) / 2), var24 + 2.0f, -1);
                if (this.isStringHovered(x, var24 - 5.0f, x + 100.0f, var24 + 15.0f, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        Enum var26 = (Enum)((Mode)var25).getValue();
                        int next = var26.ordinal() + 1 >= ((Mode)var25).getModes().length ? 0 : var26.ordinal() + 1;
                        var25.setValue(((Mode)var25).getModes()[next]);
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                var24 += 25.0f;
            }
        } else if (this.currentModule != null) {
            int i;
            float var24 = this.startY + 30.0f;
            for (i = 0; i < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size(); ++i) {
                Module mod = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(i);
                Module value = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(i);
                if (var24 > this.startY + 300.0f) break;
                RenderUtil.drawFastRoundedRect(this.startX + 195.0f, var24, this.startX + 65.0f, var24 + 20.0f, 1.0f, new Color(40, 40, 40, 20).getRGB());
                if (i < this.moduleStart) continue;
                FontLoaders.NMSL16.drawString(value.getName(), this.startX + 86.0f, var24 + 8.0f, new Color(248, 248, 248, (int)this.opacity.getOpacity()).getRGB());
                if (!value.isEnabled()) {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, var24 + 10.0f, 3.0, new Color(255, 0, 0).getRGB(), 50);
                } else {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, var24 + 10.0f, 3.0, new Color(0, 255, 0).getRGB(), 50);
                }
                RenderUtil.drawFastRoundedRect(this.startX + 65.0f, var24, this.startX + 183.0f, var24 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, 2.0f, mod.isEnabled() ? new Color(153, 51, 250, 75).getRGB() : new Color(100, 100, 100, 45).getRGB());
                if (this.isSettingsButtonHovered(this.startX + 65.0f, var24, this.startX + 183.0f, var24 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        if (value.isEnabled()) {
                            value.setEnabled(false);
                        } else {
                            value.setEnabled(true);
                        }
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown(1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isSettingsButtonHovered(this.startX + 90.0f, var24, this.startX + 100.0f + (float)FontLoaders.NMSL20.getStringWidth(value.getName()), var24 + 8.0f + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    this.currentModule = value;
                    this.valueStart = 0;
                }
                var24 += 25.0f;
            }
            var24 = this.startY + 30.0f;
            for (i = 0; i < this.currentModule.getValues().size() && var24 <= this.startY + 280.0f; ++i) {
                if (i < this.valueStart) continue;
                Value<?> var25 = this.currentModule.getValues().get(i);
                if (var25 instanceof Numbers) {
                    float x = this.startX + 300.0f;
                    double current = 68.0f * (((Number)var25.getValue()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue()) / (((Number)((Numbers)var25).getMaximum()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue());
                    ClickGuiRenderUtil.drawRect(x - 6.0f, var24 + 2.0f, (float)((double)x + 75.0), var24 + 3.0f, new Color(50, 50, 50, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x - 6.0f, var24 + 2.0f, (float)((double)x + current + 6.5), var24 + 3.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect((float)((double)x + current + 2.0), var24, (float)((double)x + current + 7.0), var24 + 5.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    boolean mouseAtPlus = (float)this.mouseX > x + 80.0f && (float)mouseX < x + 90.0f && (float)mouseY > var24 - 2.5f && (float)mouseY < var24 + 7.5f;
                    RenderUtil.drawFastRoundedRect(x + 80.0f, var24 - 2.5f, x + 90.0f, var24 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("+", x + 85.0f, var24 - 0.5f, -1);
                    if (mouseAtPlus && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() >= ((Number)((Numbers)var25).getMaximum()).doubleValue()) {
                            this.lcTimer.reset();
                        } else {
                            float append = ((Number)((Numbers)var25).inc).floatValue();
                            DecimalFormat df = new DecimalFormat("0.00");
                            double finalPlus = Double.parseDouble(df.format(((Number)var25.getValue()).floatValue() + append));
                            var25.setValue(finalPlus);
                            this.lcTimer.reset();
                        }
                    }
                    boolean mouseAtCut = (float)this.mouseX > x + 93.0f && (float)mouseX < x + 103.0f && (float)mouseY > var24 - 2.5f && (float)mouseY < var24 + 7.5f;
                    RenderUtil.drawFastRoundedRect(x + 93.0f, var24 - 2.5f, x + 103.0f, var24 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("-", x + 97.5f, var24 - 0.5f, -1);
                    if (mouseAtCut && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() <= ((Number)((Numbers)var25).getMinimum()).doubleValue()) {
                            this.rcTimer.reset();
                        } else {
                            float append = ((Number)((Numbers)var25).inc).floatValue();
                            DecimalFormat df = new DecimalFormat("0.00");
                            double finalPlus = Double.parseDouble(df.format(((Number)var25.getValue()).floatValue() - append));
                            var25.setValue(finalPlus);
                            this.rcTimer.reset();
                        }
                    }
                    FontLoaders.NMSL18.drawStringWithShadow(var25.getName() + ": " + var25.getValue(), this.startX + 210.0f, var24, -1);
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    if (this.isButtonHovered(x, var24 - 2.0f, x + 75.0f, var24 + 7.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            current = ((Number)((Numbers)var25).getMinimum()).doubleValue();
                            double max = ((Number)((Numbers)var25).getMaximum()).doubleValue();
                            double inc = ((Number)((Numbers)var25).getIncrement()).doubleValue();
                            double valAbs = (double)mouseX - ((double)x + 1.0);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            double valRel = (max - current) * perc;
                            double val = current + valRel;
                            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            ((Numbers)var25).setValue(val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                    }
                    var24 += 20.0f;
                }
                if (var25 instanceof Option) {
                    float x = this.startX + 300.0f;
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24, x + 76.0f, var24 + 1.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24 + 8.0f, x + 76.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 56.0f, var24, x + 57.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(x + 77.0f, var24, x + 76.0f, var24 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), this.startX + 210.0f, var24, -1);
                    if (((Boolean)var25.getValue()).booleanValue()) {
                        ClickGuiRenderUtil.drawRect(x + 67.0f, var24 + 2.0f, x + 75.0f, var24 + 7.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    } else {
                        ClickGuiRenderUtil.drawRect(x + 58.0f, var24 + 2.0f, x + 65.0f, var24 + 7.0f, new Color(150, 150, 150, (int)this.opacity.getOpacity()).getRGB());
                    }
                    if (this.isCheckBoxHovered(x + 56.0f, var24, x + 76.0f, var24 + 9.0f, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            var25.setValue((Boolean)var25.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    var24 += 20.0f;
                }
                if (!(var25 instanceof Mode)) continue;
                float x = this.startX + 300.0f;
                ClickGuiRenderUtil.drawRect(x - 5.0f, var24 - 5.0f, x + 90.0f, var24 + 15.0f, new Color(56, 56, 56, (int)this.opacity.getOpacity()).getRGB());
                ClickGuiRenderUtil.drawBorderRect(x - 5.0f, var24 - 5.0f, x + 90.0f, var24 + 15.0f, new Color(101, 81, 255, (int)this.opacity.getOpacity()).getRGB(), 2.0);
                FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), this.startX + 210.0f, var24 + 2.0f, -1);
                FontLoaders.NMSL18.drawStringWithShadow(((Mode)var25).getModeAsString(), x + 40.0f - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)var25).getModeAsString()) / 2), var24 + 2.0f, -1);
                if (this.isStringHovered(x, var24 - 5.0f, x + 100.0f, var24 + 15.0f, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        Enum var26 = (Enum)((Mode)var25).getValue();
                        int next = var26.ordinal() + 1 >= ((Mode)var25).getModes().length ? 0 : var26.ordinal() + 1;
                        var25.setValue(((Mode)var25).getModes()[next]);
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                var24 += 25.0f;
            }
        }
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        Client.instance.saveConfig(false);
        Client.instance.saveUISettings(false);
        this.opacity.setOpacity(0.0f);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean mouseWithinBounds2(int mouseX, int mouseY, int x, int y, int x1, int y1) {
        if (x > x1) {
            int i = x;
            x = x1;
            x1 = i;
        }
        if (y > y1) {
            int j = y;
            y = y1;
            y1 = j;
        }
        return mouseX >= x && mouseX <= x1 && mouseY >= y && mouseY <= y1;
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, int x, int y, int x1, int y1) {
        return this.mouseWithinBounds2(mouseX, mouseY, x, y, x + x1, y + y1);
    }
}

