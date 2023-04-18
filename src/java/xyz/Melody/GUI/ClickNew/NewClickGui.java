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

    public void func_73866_w_() {
        this.opacity = new Opacity(1);
        this.blurOpacity = new Opacity(1);
        this.translate = new Translate(0.0f, 0.0f);
        this.startX = (float)this.field_146294_l / 3.7f;
        this.startY = this.field_146295_m / 5;
        this.SearchText = "Search...";
        this.field_146292_n.add(new ClientButton(0, 5, this.field_146295_m - 106, 90, 20, "ChatTextShadow", new Color(138, 43, 226, 80)));
        this.field_146292_n.add(new ClientButton(1, 5, this.field_146295_m - 84, 90, 20, "ChatBackground", new Color(138, 43, 226, 80)));
        this.field_146292_n.add(new ClientButton(2, 5, this.field_146295_m - 62, 120, 20, "ScoreboardBackground", new Color(138, 43, 226, 80)));
        this.field_146292_n.add(new ClientButton(3, 5, this.field_146295_m - 40, 80, 20, "Edit Locations", new Color(221, 160, 221, 80)));
        super.func_73866_w_();
    }

    protected void func_146284_a(GuiButton guiButton) throws IOException {
        switch (guiButton.field_146127_k) {
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
                this.field_146297_k.func_147108_a(new HUDScreen());
            }
        }
        super.func_146284_a(guiButton);
    }

    protected void func_73869_a(char c, int n) throws IOException {
        if (this.currentModule != null && this.binding) {
            this.currentModule.setKey(n);
            this.binding = false;
        }
        if (this.inSearch) {
            if (n == 14) {
                try {
                    this.SearchText = this.SearchText.substring(0, this.SearchText.length() - 1);
                    this.SearchText.substring(0, this.SearchText.length() - 1);
                }
                catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                    this.SearchText = "";
                }
            }
            if (ChatAllowedCharacters.func_71566_a((char)c)) {
                this.SearchText = this.SearchText + Character.toString(c);
            }
        }
        super.func_73869_a(c, n);
    }

    public void func_73863_a(int n, int n2, float f) {
        int n3;
        HUD hUD = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (((Boolean)hUD.cgblur.getValue()).booleanValue()) {
            this.gb.renderBlur(this.blurOpacity.getOpacity());
            this.blurOpacity.interp(50.0f, 5.0f);
        }
        this.opacity.interpolate(160.0f);
        RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), this.field_146294_l - 160, this.field_146295_m - 40, 32.0f, 32.0f);
        FontLoaders.CNMD34.drawString("MelodySky", this.field_146294_l - 125, this.field_146295_m - 34, -1);
        FontLoaders.CNMD24.drawString(UISettings.chatTextShadow + "", 100.0f, this.field_146295_m - 101, -1);
        FontLoaders.CNMD24.drawString(UISettings.chatBackground + "", 100.0f, this.field_146295_m - 79, -1);
        FontLoaders.CNMD24.drawString(UISettings.scoreboardBackground + "", 130.0f, this.field_146295_m - 57, -1);
        if (!Client.instance.authManager.verified) {
            FontLoaders.CNMD28.drawCenteredString("MelodySky Will Not Work Cause of Failed to Verify Your UUID.", this.field_146294_l / 2, 20.0f, Colors.BLUE.c);
        }
        this.mouseX = n;
        this.mouseY = n2;
        if (this.field_146297_k.field_71462_r != null && !(this.field_146297_k.field_71462_r instanceof NewClickGui)) {
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
        if (this.field_146297_k.field_71462_r != null && this.field_146297_k.field_71462_r != null && !(this.field_146297_k.field_71462_r instanceof NewClickGui)) {
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
        if (this.isHovered(this.startX, this.startY - 25.0f, this.startX + 400.0f, this.startY + 25.0f, n, n2) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)n - this.startX;
                this.moveY = (float)n2 - this.startY;
            } else {
                this.startX = (float)n - this.moveX;
                this.startY = (float)n2 - this.moveY;
            }
            this.previousmouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        GL11.glPushMatrix();
        this.translate.interpolate(this.field_146294_l, this.field_146295_m, 8.0);
        double d = (float)(this.field_146294_l / 2) - this.translate.getX() / 2.0f;
        double d2 = (float)(this.field_146295_m / 2) - this.translate.getY() / 2.0f;
        GlStateManager.func_179137_b((double)d, (double)d2, (double)0.0);
        GlStateManager.func_179152_a((float)(this.translate.getX() / (float)this.field_146294_l), (float)(this.translate.getY() / (float)this.field_146295_m), (float)1.0f);
        RenderUtil.drawFastRoundedRect(this.startX, this.startY, this.startX + 200.0f, this.startY + 320.0f, 1.0f, new Color(30, 30, 30, (int)this.opacity.getOpacity()).getRGB());
        ClickGuiRenderUtil.drawRect(this.startX + 200.0f, this.startY, this.startX + 431.0f, this.startY + 320.0f, new Color(40, 40, 40, (int)this.opacity.getOpacity()).getRGB());
        ClickGuiRenderUtil.drawRainbowRect(this.startX, this.startY, this.startX + 430.0f, this.startY + 1.0f);
        for (n3 = 0; n3 < ModuleType.values().length; ++n3) {
            ModuleType[] moduleTypeArray = ModuleType.values();
            if (moduleTypeArray[n3] != this.currentModuleType || this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
                if (moduleTypeArray[n3].toString() == "QOL") {
                    FontLoaders.NMSL25.drawString("QOL", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Dungeons") {
                    FontLoaders.NMSL25.drawString("Dungeon", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Swapping") {
                    FontLoaders.NMSL25.drawString("Swaps", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Fishing") {
                    FontLoaders.NMSL25.drawString("Fishing", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Nether") {
                    FontLoaders.NMSL25.drawString("Nether", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Render") {
                    FontLoaders.NMSL25.drawString("Render", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Macros") {
                    FontLoaders.NMSL25.drawString("Macros", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Others") {
                    FontLoaders.NMSL25.drawString("Others", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Balance") {
                    FontLoaders.NMSL25.drawString("Balance", this.startX + 7.0f, this.startY + 20.0f + (float)(n3 * 30) + 3.0f, new Color(255, 255, 255).getRGB());
                }
            } else if (this.SearchText == "Search..." || this.SearchText == null || this.SearchText == "") {
                ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0f, this.startY + 16.0f + (float)(n3 * 30), this.startX + 5.5f, this.startY + 39.0f + (float)(n3 * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255).getRGB());
                ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0f, this.startY + 16.0f + (float)(n3 * 30), this.startX + 60.0f, this.startY + 39.0f + (float)(n3 * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255, 100).getRGB());
                if (moduleTypeArray[n3].toString() == "QOL") {
                    FontLoaders.NMSL25.drawString("QOL", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Dungeons") {
                    FontLoaders.NMSL25.drawString("Dungeon", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Swapping") {
                    FontLoaders.NMSL25.drawString("Swaps", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Fishing") {
                    FontLoaders.NMSL25.drawString("Fishing", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Nether") {
                    FontLoaders.NMSL25.drawString("Nether", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Render") {
                    FontLoaders.NMSL25.drawString("Render", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Macros") {
                    FontLoaders.NMSL25.drawString("Macros", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Others") {
                    FontLoaders.NMSL25.drawString("Others", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
                if (moduleTypeArray[n3].toString() == "Balance") {
                    FontLoaders.NMSL25.drawString("Balance", this.startX + 8.0f, this.startY + 20.0f + (float)(n3 * 30) + 2.0f, new Color(255, 255, 200).getRGB());
                }
            }
            try {
                if (!this.isCategoryHovered(this.startX + 7.0f, this.startY + 15.0f + (float)(n3 * 30), this.startX + 60.0f, this.startY + 45.0f + (float)(n3 * 30), n, n2) || !Mouse.isButtonDown(0)) continue;
                this.SearchText = "Search...";
                this.inSearch = false;
                this.currentModuleType = moduleTypeArray[n3];
                this.currentModule = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() != 0 ? Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(0) : null;
                this.moduleStart = 0;
                continue;
            }
            catch (Exception exception) {
                System.err.println(exception);
            }
        }
        this.mouseWheel = Mouse.getDWheel();
        if (this.isCategoryHovered(this.startX + 60.0f, this.startY, this.startX + 200.0f, this.startY + 320.0f, n, n2)) {
            if (this.mouseWheel < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() - 1) {
                ++this.moduleStart;
            }
            if (this.mouseWheel > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        if (this.isCategoryHovered(this.startX + 200.0f, this.startY, this.startX + 420.0f, this.startY + 320.0f, n, n2)) {
            if (this.mouseWheel < 0 && this.valueStart < this.currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }
            if (this.mouseWheel > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        GL11.glPopMatrix();
        boolean bl = this.mouseWithinBounds(n, n2, this.field_146294_l / 2 - 100, 40, 200, 20);
        RenderUtil.drawFastRoundedRect(this.field_146294_l / 2 - 100, 40.0f, this.field_146294_l / 2 + 100, 60.0f, 1.0f, new Color(55, 55, 55, 190).getRGB());
        FontLoaders.CNMD20.drawStringWithShadow(this.SearchText, this.field_146294_l / 2 - 95, 46.0, new Color(255, 255, 255, 190).getRGB());
        RenderUtil.drawBorderedRect(this.field_146294_l / 2 - 100, 40.0f, this.field_146294_l / 2 + 100, 60.0f, 0.5f, this.inSearch ? new Color(255, 255, 255, 190).getRGB() : new Color(100, 100, 100, 100).getRGB(), new Color(180, 180, 180, 0).getRGB());
        if (bl && Mouse.isButtonDown(0)) {
            this.inSearch = true;
            this.SearchText = "";
        } else if (!bl && Mouse.isButtonDown(0)) {
            this.inSearch = false;
        }
        if (this.SearchText == "" && !this.inSearch) {
            this.SearchText = "Search...";
        }
        if (this.inSearch) {
            if (this.idkTimer.hasReached(500.0)) {
                FontLoaders.CNMD20.drawString("|", this.field_146294_l / 2 - 95 + FontLoaders.CNMD20.getStringWidth(this.SearchText), 46.0f, -1);
                if (this.idkTimer.hasReached(1000.0)) {
                    this.idkTimer.reset();
                }
            }
        } else {
            this.idkTimer.reset();
        }
        GL11.glPushMatrix();
        GlStateManager.func_179137_b((double)d, (double)d2, (double)0.0);
        GlStateManager.func_179152_a((float)(this.translate.getX() / (float)this.field_146294_l), (float)(this.translate.getY() / (float)this.field_146295_m), (float)1.0f);
        if (this.currentModule != null) {
            RenderUtil.drawBorderedRect(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, 1.0f, new Color(90, 90, 90, (int)(1.5 * (double)this.opacity.getOpacity())).getRGB(), new Color(30, 30, 30, (int)(0.5 * (double)this.opacity.getOpacity())).getRGB());
            if (!this.binding) {
                FontLoaders.NMSL18.drawString("Current Key: " + Keyboard.getKeyName(this.currentModule.getKey()), this.startX + 215.0f, this.startY + 300.0f, -1);
                FontLoaders.NMSL18.drawString((Object)((Object)EnumChatFormatting.GRAY) + "Click to Bind.", this.startX + 360.0f, this.startY + 300.0f, -1);
            } else {
                FontLoaders.NMSL18.drawString("Waitting...", this.startX + 215.0f, this.startY + 300.0f, -1);
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, n, n2) && Mouse.isButtonDown(0) && !this.binding) {
                this.binding = true;
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, n, n2) && Mouse.isButtonDown(1) && this.binding) {
                this.binding = false;
            }
            if (this.isButtonHovered(this.startX + 210.0f, this.startY + 295.0f, this.startX + 420.0f, this.startY + 310.0f, n, n2) && Mouse.isButtonDown(1) && !this.binding) {
                this.currentModule.setKey(0);
            }
        }
        ClickGuiRenderUtil.drawRoundedRect(this.startX + 199.0f, this.startY - 150.0f + (float)this.moduleStart - 85.0f + (float)(n3 * 30), this.startX + 201.0f, this.startY - 50.0f + (float)this.moduleStart + 10.0f + (float)(n3 * 30), new Color(101, 81, 255).getRGB(), new Color(101, 81, 255, 180).getRGB());
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
            boolean bl2;
            Object object;
            Value<?> value;
            int n4;
            float f2 = this.startY + 30.0f;
            for (n4 = 0; n4 < ModuleManager.getModules().size(); ++n4) {
                value = ModuleManager.getModules().get(n4);
                String string = ModuleManager.getModules().get(n4).getName().toUpperCase();
                object = ModuleManager.getModules().get(n4).getModInfo().toUpperCase();
                String string2 = "";
                for (Value<?> value2 : ((Module)((Object)value)).getValues()) {
                    string2 = string2 + (value2.getName() + " ").toUpperCase();
                }
                boolean bl3 = bl2 = ((Module)((Object)value)).getValues().size() == 0 ? false : string2.contains(this.SearchText.toUpperCase());
                if (!string.contains(this.SearchText.toUpperCase()) && !((String)object).contains(this.SearchText.toUpperCase()) && !bl2) continue;
                Module f4 = ModuleManager.getModules().get(n4);
                if (f2 > this.startY + 300.0f) break;
                RenderUtil.drawFastRoundedRect(this.startX + 195.0f, f2, this.startX + 65.0f, f2 + 20.0f, 1.0f, new Color(40, 40, 40, 20).getRGB());
                if (n4 < this.moduleStart) continue;
                FontLoaders.NMSL16.drawString(f4.getName(), this.startX + 86.0f, f2 + 8.0f, new Color(248, 248, 248, (int)this.opacity.getOpacity()).getRGB());
                if (!f4.isEnabled()) {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, f2 + 10.0f, 3.0, new Color(255, 0, 0).getRGB(), 50);
                } else {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, f2 + 10.0f, 3.0, new Color(0, 255, 0).getRGB(), 50);
                }
                RenderUtil.drawFastRoundedRect(this.startX + 65.0f, f2, this.startX + 183.0f, f2 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, 2.0f, ((Module)((Object)value)).isEnabled() ? new Color(153, 51, 250, 75).getRGB() : new Color(100, 100, 100, 45).getRGB());
                if (this.isSettingsButtonHovered(this.startX + 65.0f, f2, this.startX + 183.0f, f2 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, n, n2)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        if (f4.isEnabled()) {
                            f4.setEnabled(false);
                        } else {
                            f4.setEnabled(true);
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
                if (this.isSettingsButtonHovered(this.startX + 90.0f, f2, this.startX + 100.0f + (float)FontLoaders.NMSL20.getStringWidth(f4.getName()), f2 + 8.0f + (float)FontLoaders.NMSL20.getHeight(), n, n2) && Mouse.isButtonDown(1)) {
                    this.currentModule = f4;
                    this.valueStart = 0;
                }
                f2 += 25.0f;
            }
            f2 = this.startY + 30.0f;
            for (n4 = 0; n4 < this.currentModule.getValues().size() && f2 <= this.startY + 280.0f; ++n4) {
                if (n4 < this.valueStart) continue;
                value = this.currentModule.getValues().get(n4);
                if (value instanceof Numbers) {
                    double d11;
                    float f3 = this.startX + 300.0f;
                    double d4 = 68.0f * (((Number)value.getValue()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue()) / (((Number)((Numbers)value).getMaximum()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue());
                    ClickGuiRenderUtil.drawRect(f3 - 6.0f, f2 + 2.0f, (float)((double)f3 + 75.0), f2 + 3.0f, new Color(50, 50, 50, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f3 - 6.0f, f2 + 2.0f, (float)((double)f3 + d4 + 6.5), f2 + 3.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect((float)((double)f3 + d4 + 2.0), f2, (float)((double)f3 + d4 + 7.0), f2 + 5.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    bl2 = (float)this.mouseX > f3 + 80.0f && (float)n < f3 + 90.0f && (float)n2 > f2 - 2.5f && (float)n2 < f2 + 7.5f;
                    RenderUtil.drawFastRoundedRect(f3 + 80.0f, f2 - 2.5f, f3 + 90.0f, f2 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("+", f3 + 85.0f, f2 - 0.5f, -1);
                    if (bl2 && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)value.getValue()).floatValue() >= ((Number)((Numbers)value).getMaximum()).doubleValue()) {
                            this.lcTimer.reset();
                        } else {
                            float bl4 = ((Number)((Numbers)value).inc).floatValue();
                            DecimalFormat f5 = new DecimalFormat("0.00");
                            double decimalFormat = Double.parseDouble(f5.format(((Number)value.getValue()).floatValue() + bl4));
                            value.setValue(decimalFormat);
                            this.lcTimer.reset();
                        }
                    }
                    boolean bl6 = (float)this.mouseX > f3 + 93.0f && (float)n < f3 + 103.0f && (float)n2 > f2 - 2.5f && (float)n2 < f2 + 7.5f;
                    RenderUtil.drawFastRoundedRect(f3 + 93.0f, f2 - 2.5f, f3 + 103.0f, f2 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("-", f3 + 97.5f, f2 - 0.5f, -1);
                    if (bl6 && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)value.getValue()).floatValue() <= ((Number)((Numbers)value).getMinimum()).doubleValue()) {
                            this.rcTimer.reset();
                        } else {
                            float d6 = ((Number)((Numbers)value).inc).floatValue();
                            DecimalFormat d13 = new DecimalFormat("0.00");
                            d11 = Double.parseDouble(d13.format(((Number)value.getValue()).floatValue() - d6));
                            value.setValue(d11);
                            this.rcTimer.reset();
                        }
                    }
                    FontLoaders.NMSL18.drawStringWithShadow(value.getName() + ": " + value.getValue(), this.startX + 210.0f, f2, -1);
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    if (this.isButtonHovered(f3, f2 - 2.0f, f3 + 75.0f, f2 + 7.0f, n, n2) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            d4 = ((Number)((Numbers)value).getMinimum()).doubleValue();
                            double decimalFormat = ((Number)((Numbers)value).getMaximum()).doubleValue();
                            d11 = ((Number)((Numbers)value).getIncrement()).doubleValue();
                            double d15 = (double)n - ((double)f3 + 1.0);
                            double d16 = d15 / 68.0;
                            d16 = Math.min(Math.max(0.0, d16), 1.0);
                            double d17 = (decimalFormat - d4) * d16;
                            double d18 = d4 + d17;
                            d18 = (double)Math.round(d18 * (1.0 / d11)) / (1.0 / d11);
                            ((Numbers)value).setValue(d18);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                    }
                    f2 += 20.0f;
                }
                if (value instanceof Option) {
                    float f6 = this.startX + 300.0f;
                    ClickGuiRenderUtil.drawRect(f6 + 56.0f, f2, f6 + 76.0f, f2 + 1.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f6 + 56.0f, f2 + 8.0f, f6 + 76.0f, f2 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f6 + 56.0f, f2, f6 + 57.0f, f2 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f6 + 77.0f, f2, f6 + 76.0f, f2 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawStringWithShadow(value.getName(), this.startX + 210.0f, f2, -1);
                    if (((Boolean)value.getValue()).booleanValue()) {
                        ClickGuiRenderUtil.drawRect(f6 + 67.0f, f2 + 2.0f, f6 + 75.0f, f2 + 7.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    } else {
                        ClickGuiRenderUtil.drawRect(f6 + 58.0f, f2 + 2.0f, f6 + 65.0f, f2 + 7.0f, new Color(150, 150, 150, (int)this.opacity.getOpacity()).getRGB());
                    }
                    if (this.isCheckBoxHovered(f6 + 56.0f, f2, f6 + 76.0f, f2 + 9.0f, n, n2)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            value.setValue((Boolean)value.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    f2 += 20.0f;
                }
                if (!(value instanceof Mode)) continue;
                float f7 = this.startX + 300.0f;
                ClickGuiRenderUtil.drawRect(f7 - 5.0f, f2 - 5.0f, f7 + 90.0f, f2 + 15.0f, new Color(56, 56, 56, (int)this.opacity.getOpacity()).getRGB());
                ClickGuiRenderUtil.drawBorderRect(f7 - 5.0f, f2 - 5.0f, f7 + 90.0f, f2 + 15.0f, new Color(101, 81, 255, (int)this.opacity.getOpacity()).getRGB(), 2.0);
                FontLoaders.NMSL18.drawStringWithShadow(value.getName(), this.startX + 210.0f, f2 + 2.0f, -1);
                FontLoaders.NMSL18.drawStringWithShadow(((Mode)value).getModeAsString(), f7 + 40.0f - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)value).getModeAsString()) / 2), f2 + 2.0f, -1);
                if (this.isStringHovered(f7, f2 - 5.0f, f7 + 100.0f, f2 + 15.0f, n, n2)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        object = (Enum)((Mode)value).getValue();
                        int n5 = ((Enum)object).ordinal() + 1 >= ((Mode)value).getModes().length ? 0 : ((Enum)object).ordinal() + 1;
                        value.setValue(((Mode)value).getModes()[n5]);
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                f2 += 25.0f;
            }
        } else if (this.currentModule != null) {
            Value<?> value;
            int n6;
            float f8 = this.startY + 30.0f;
            for (n6 = 0; n6 < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size(); ++n6) {
                value = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(n6);
                Module module = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(n6);
                if (f8 > this.startY + 300.0f) break;
                RenderUtil.drawFastRoundedRect(this.startX + 195.0f, f8, this.startX + 65.0f, f8 + 20.0f, 1.0f, new Color(40, 40, 40, 20).getRGB());
                if (n6 < this.moduleStart) continue;
                FontLoaders.NMSL16.drawString(module.getName(), this.startX + 86.0f, f8 + 8.0f, new Color(248, 248, 248, (int)this.opacity.getOpacity()).getRGB());
                if (!module.isEnabled()) {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, f8 + 10.0f, 3.0, new Color(255, 0, 0).getRGB(), 50);
                } else {
                    ClickGuiRenderUtil.drawFilledCircle(this.startX + 75.0f, f8 + 10.0f, 3.0, new Color(0, 255, 0).getRGB(), 50);
                }
                RenderUtil.drawFastRoundedRect(this.startX + 65.0f, f8, this.startX + 183.0f, f8 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, 2.0f, ((Module)((Object)value)).isEnabled() ? new Color(153, 51, 250, 75).getRGB() : new Color(100, 100, 100, 45).getRGB());
                if (this.isSettingsButtonHovered(this.startX + 65.0f, f8, this.startX + 183.0f, f8 + 16.0f + (float)FontLoaders.NMSL20.getHeight() - 5.0f, n, n2)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        if (module.isEnabled()) {
                            module.setEnabled(false);
                        } else {
                            module.setEnabled(true);
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
                if (this.isSettingsButtonHovered(this.startX + 90.0f, f8, this.startX + 100.0f + (float)FontLoaders.NMSL20.getStringWidth(module.getName()), f8 + 8.0f + (float)FontLoaders.NMSL20.getHeight(), n, n2) && Mouse.isButtonDown(1)) {
                    this.currentModule = module;
                    this.valueStart = 0;
                }
                f8 += 25.0f;
            }
            f8 = this.startY + 30.0f;
            for (n6 = 0; n6 < this.currentModule.getValues().size() && f8 <= this.startY + 280.0f; ++n6) {
                if (n6 < this.valueStart) continue;
                value = this.currentModule.getValues().get(n6);
                if (value instanceof Numbers) {
                    double d3;
                    float f9 = this.startX + 300.0f;
                    double d12 = 68.0f * (((Number)value.getValue()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue()) / (((Number)((Numbers)value).getMaximum()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue());
                    ClickGuiRenderUtil.drawRect(f9 - 6.0f, f8 + 2.0f, (float)((double)f9 + 75.0), f8 + 3.0f, new Color(50, 50, 50, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f9 - 6.0f, f8 + 2.0f, (float)((double)f9 + d12 + 6.5), f8 + 3.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect((float)((double)f9 + d12 + 2.0), f8, (float)((double)f9 + d12 + 7.0), f8 + 5.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    boolean bl5 = (float)this.mouseX > f9 + 80.0f && (float)n < f9 + 90.0f && (float)n2 > f8 - 2.5f && (float)n2 < f8 + 7.5f;
                    RenderUtil.drawFastRoundedRect(f9 + 80.0f, f8 - 2.5f, f9 + 90.0f, f8 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("+", f9 + 85.0f, f8 - 0.5f, -1);
                    if (bl5 && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)value.getValue()).floatValue() >= ((Number)((Numbers)value).getMaximum()).doubleValue()) {
                            this.lcTimer.reset();
                        } else {
                            float f2 = ((Number)((Numbers)value).inc).floatValue();
                            DecimalFormat f11 = new DecimalFormat("0.00");
                            double decimalFormat = Double.parseDouble(f11.format(((Number)value.getValue()).floatValue() + f2));
                            value.setValue(decimalFormat);
                            this.lcTimer.reset();
                        }
                    }
                    boolean f10 = (float)this.mouseX > f9 + 93.0f && (float)n < f9 + 103.0f && (float)n2 > f8 - 2.5f && (float)n2 < f8 + 7.5f;
                    RenderUtil.drawFastRoundedRect(f9 + 93.0f, f8 - 2.5f, f9 + 103.0f, f8 + 7.5f, 0.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawCenteredString("-", f9 + 97.5f, f8 - 0.5f, -1);
                    if (f10 && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)value.getValue()).floatValue() <= ((Number)((Numbers)value).getMinimum()).doubleValue()) {
                            this.rcTimer.reset();
                        } else {
                            float d14 = ((Number)((Numbers)value).inc).floatValue();
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            d3 = Double.parseDouble(decimalFormat.format(((Number)value.getValue()).floatValue() - d14));
                            value.setValue(d3);
                            this.rcTimer.reset();
                        }
                    }
                    FontLoaders.NMSL18.drawStringWithShadow(value.getName() + ": " + value.getValue(), this.startX + 210.0f, f8, -1);
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    if (this.isButtonHovered(f9, f8 - 2.0f, f9 + 75.0f, f8 + 7.0f, n, n2) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            d12 = ((Number)((Numbers)value).getMinimum()).doubleValue();
                            double d4 = ((Number)((Numbers)value).getMaximum()).doubleValue();
                            d3 = ((Number)((Numbers)value).getIncrement()).doubleValue();
                            double d5 = (double)n - ((double)f9 + 1.0);
                            double d6 = d5 / 68.0;
                            d6 = Math.min(Math.max(0.0, d6), 1.0);
                            double d7 = (d4 - d12) * d6;
                            double d8 = d12 + d7;
                            d8 = (double)Math.round(d8 * (1.0 / d3)) / (1.0 / d3);
                            ((Numbers)value).setValue(d8);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                    }
                    f8 += 20.0f;
                }
                if (value instanceof Option) {
                    float f12 = this.startX + 300.0f;
                    ClickGuiRenderUtil.drawRect(f12 + 56.0f, f8, f12 + 76.0f, f8 + 1.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f12 + 56.0f, f8 + 8.0f, f12 + 76.0f, f8 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f12 + 56.0f, f8, f12 + 57.0f, f8 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    ClickGuiRenderUtil.drawRect(f12 + 77.0f, f8, f12 + 76.0f, f8 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    FontLoaders.NMSL18.drawStringWithShadow(value.getName(), this.startX + 210.0f, f8, -1);
                    if (((Boolean)value.getValue()).booleanValue()) {
                        ClickGuiRenderUtil.drawRect(f12 + 67.0f, f8 + 2.0f, f12 + 75.0f, f8 + 7.0f, new Color(61, 141, 255, (int)this.opacity.getOpacity()).getRGB());
                    } else {
                        ClickGuiRenderUtil.drawRect(f12 + 58.0f, f8 + 2.0f, f12 + 65.0f, f8 + 7.0f, new Color(150, 150, 150, (int)this.opacity.getOpacity()).getRGB());
                    }
                    if (this.isCheckBoxHovered(f12 + 56.0f, f8, f12 + 76.0f, f8 + 9.0f, n, n2)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            value.setValue((Boolean)value.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                    f8 += 20.0f;
                }
                if (!(value instanceof Mode)) continue;
                float f13 = this.startX + 300.0f;
                ClickGuiRenderUtil.drawRect(f13 - 5.0f, f8 - 5.0f, f13 + 90.0f, f8 + 15.0f, new Color(56, 56, 56, (int)this.opacity.getOpacity()).getRGB());
                ClickGuiRenderUtil.drawBorderRect(f13 - 5.0f, f8 - 5.0f, f13 + 90.0f, f8 + 15.0f, new Color(101, 81, 255, (int)this.opacity.getOpacity()).getRGB(), 2.0);
                FontLoaders.NMSL18.drawStringWithShadow(value.getName(), this.startX + 210.0f, f8 + 2.0f, -1);
                FontLoaders.NMSL18.drawStringWithShadow(((Mode)value).getModeAsString(), f13 + 40.0f - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)value).getModeAsString()) / 2), f8 + 2.0f, -1);
                if (this.isStringHovered(f13, f8 - 5.0f, f13 + 100.0f, f8 + 15.0f, n, n2)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        Enum enum_ = (Enum)((Mode)value).getValue();
                        int n7 = enum_.ordinal() + 1 >= ((Mode)value).getModes().length ? 0 : enum_.ordinal() + 1;
                        value.setValue(((Mode)value).getModes()[n7]);
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                f8 += 25.0f;
            }
        }
        GL11.glPopMatrix();
        super.func_73863_a(n, n2, f);
    }

    public void func_146281_b() {
        Client.instance.saveConfig(false);
        Client.instance.saveUISettings(false);
        this.opacity.setOpacity(0.0f);
    }

    public boolean isStringHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean isSettingsButtonHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean isButtonHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean isCheckBoxHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean isCategoryHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean isHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f3 && (float)n2 >= f2 && (float)n2 <= f4;
    }

    public boolean mouseWithinBounds2(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        if (n3 > n5) {
            n7 = n3;
            n3 = n5;
            n5 = n7;
        }
        if (n4 > n6) {
            n7 = n4;
            n4 = n6;
            n6 = n7;
        }
        return n >= n3 && n <= n5 && n2 >= n4 && n2 <= n6;
    }

    public boolean mouseWithinBounds(int n, int n2, int n3, int n4, int n5, int n6) {
        return this.mouseWithinBounds2(n, n2, n3, n4, n3 + n5, n4 + n6);
    }
}

