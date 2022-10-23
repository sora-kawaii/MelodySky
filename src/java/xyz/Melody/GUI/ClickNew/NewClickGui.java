package xyz.Melody.GUI.ClickNew;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.UISettings;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.System.Managers.ModuleManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class NewClickGui extends GuiScreen implements GuiYesNoCallback {
   public ModuleType currentModuleType;
   public Module currentModule;
   public float startX;
   public float startY;
   public int moduleStart;
   public int valueStart;
   boolean previousmouse;
   boolean mouse;
   public Opacity opacity;
   public int opacityx;
   public float moveX;
   public float moveY;
   public boolean binding;
   public float lastPercent;
   public float percent;
   public float percent2;
   public float lastPercent2;
   public float outro;
   public float lastOutro;
   public int mouseWheel;
   public int mouseX;
   public int mouseY;
   private TimerUtil lcTimer;
   private TimerUtil rcTimer;
   private TimerUtil idkTimer;
   private boolean inSearch;
   private String SearchText;

   public NewClickGui() {
      this.currentModuleType = ModuleType.QOL;
      this.currentModule = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() != 0 ? (Module)Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(0) : null;
      this.startX = 100.0F;
      this.startY = 100.0F;
      this.moduleStart = 0;
      this.valueStart = 0;
      this.previousmouse = true;
      this.opacity = new Opacity(0);
      this.opacityx = 255;
      this.moveX = 0.0F;
      this.moveY = 0.0F;
      this.binding = false;
      this.lcTimer = new TimerUtil();
      this.rcTimer = new TimerUtil();
      this.idkTimer = new TimerUtil();
      this.SearchText = "Search...";
   }

   public void initGui() {
      this.startX = 250.0F;
      this.startY = 90.0F;
      this.SearchText = "Search...";
      this.buttonList.add(new ClientButton(0, 5, this.height - 106, 90, 20, "ChatTextShadow", new Color(138, 43, 226, 80)));
      this.buttonList.add(new ClientButton(1, 5, this.height - 84, 90, 20, "ChatBackground", new Color(138, 43, 226, 80)));
      this.buttonList.add(new ClientButton(2, 5, this.height - 62, 120, 20, "ScoreboardBackground", new Color(138, 43, 226, 80)));
      this.buttonList.add(new ClientButton(3, 5, this.height - 40, 80, 20, "Edit Locations", new Color(221, 160, 221, 80)));
      if (this.mc.theWorld != null) {
         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

      super.initGui();
   }

   protected void actionPerformed(GuiButton gay) throws IOException {
      switch (gay.id) {
         case 0:
            UISettings.chatTextShadow = !UISettings.chatTextShadow;
            break;
         case 1:
            UISettings.chatBackground = !UISettings.chatBackground;
            break;
         case 2:
            UISettings.scoreboardBackground = !UISettings.scoreboardBackground;
            break;
         case 3:
            this.mc.displayGuiScreen(new HUDScreen());
      }

      super.actionPerformed(gay);
   }

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
            } catch (StringIndexOutOfBoundsException var4) {
               this.SearchText = "";
            }
         }

         if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            this.SearchText = this.SearchText + Character.toString(typedChar);
         }
      }

      super.keyTyped(typedChar, keyCode);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), (float)(this.width - 160), (float)(this.height - 40), 32.0F, 32.0F);
      FontLoaders.CNMD34.drawString("MelodySky", (float)(this.width - 125), (float)(this.height - 34), -1);
      FontLoaders.CNMD24.drawString(UISettings.chatTextShadow + "", 100.0F, (float)(this.height - 101), -1);
      FontLoaders.CNMD24.drawString(UISettings.chatBackground + "", 100.0F, (float)(this.height - 79), -1);
      FontLoaders.CNMD24.drawString(UISettings.scoreboardBackground + "", 130.0F, (float)(this.height - 57), -1);
      if (!Client.instance.authManager.verified) {
         FontLoaders.CNMD28.drawCenteredString("MelodySky Will Not Work Cause of Failed to Verify Your UUID.", (float)(this.width / 2), 20.0F, Colors.BLUE.c);
      }

      this.mouseX = mouseX;
      this.mouseY = mouseY;
      if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof NewClickGui)) {
         this.lastOutro = this.outro;
         if ((double)this.outro < 1.7) {
            this.outro += 0.1F;
            this.outro = (float)((double)this.outro + ((1.7 - (double)this.outro) / 3.0 - 0.001));
         }

         if ((double)this.outro > 1.7) {
            this.outro = 1.7F;
         }

         if (this.outro < 1.0F) {
            this.outro = 1.0F;
         }
      }

      if (this.mc.currentScreen == null || this.mc.currentScreen == null || this.mc.currentScreen instanceof NewClickGui) {
         this.lastPercent = this.percent;
         this.lastPercent2 = this.percent2;
         if ((double)this.percent > 0.98) {
            this.percent = (float)((double)this.percent + ((0.98 - (double)this.percent) / 1.4500000476837158 - 0.001));
         }

         if ((double)this.percent <= 0.98 && this.percent2 < 1.0F) {
            this.percent2 = (float)((double)this.percent2 + (double)((1.0F - this.percent2) / 2.8F) + 0.002);
         }

         if (this.isHovered(this.startX, this.startY - 25.0F, this.startX + 400.0F, this.startY + 25.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0F && this.moveY == 0.0F) {
               this.moveX = (float)mouseX - this.startX;
               this.moveY = (float)mouseY - this.startY;
            } else {
               this.startX = (float)mouseX - this.moveX;
               this.startY = (float)mouseY - this.moveY;
            }

            this.previousmouse = true;
         } else if (this.moveX != 0.0F || this.moveY != 0.0F) {
            this.moveX = 0.0F;
            this.moveY = 0.0F;
         }

         this.opacity.interpolate(160.0F);
         RenderUtil.drawFastRoundedRect(this.startX, this.startY, this.startX + 200.0F, this.startY + 320.0F, 1.0F, (new Color(30, 30, 30, (int)this.opacity.getOpacity())).getRGB());
         ClickGuiRenderUtil.drawRect(this.startX + 200.0F, this.startY, this.startX + 431.0F, this.startY + 320.0F, (new Color(40, 40, 40, (int)this.opacity.getOpacity())).getRGB());
         ClickGuiRenderUtil.drawRainbowRect((double)this.startX, (double)this.startY, (double)(this.startX + 430.0F), (double)(this.startY + 1.0F));

         int m;
         for(m = 0; m < ModuleType.values().length; ++m) {
            ModuleType[] mY = ModuleType.values();
            if (mY[m] != this.currentModuleType || this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
               if (mY[m].toString() == "QOL") {
                  FontLoaders.NMSL25.drawString("QOL", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Dungeons") {
                  FontLoaders.NMSL25.drawString("Dungeon", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Swapping") {
                  FontLoaders.NMSL25.drawString("Swaps", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Slayer") {
                  FontLoaders.NMSL25.drawString("Slayer", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Nether") {
                  FontLoaders.NMSL25.drawString("Nether", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Render") {
                  FontLoaders.NMSL25.drawString("Render", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Macros") {
                  FontLoaders.NMSL25.drawString("Macros", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Others") {
                  FontLoaders.NMSL25.drawString("Others", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }

               if (mY[m].toString() == "Balance") {
                  FontLoaders.NMSL25.drawString("Balance", this.startX + 7.0F, this.startY + 20.0F + (float)(m * 30) + 3.0F, (new Color(255, 255, 255)).getRGB());
               }
            } else if (this.SearchText == "Search..." || this.SearchText == null || this.SearchText == "") {
               ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0F, this.startY + 16.0F + (float)(m * 30), this.startX + 5.5F, this.startY + 39.0F + (float)(m * 30), (new Color(101, 81, 255)).getRGB(), (new Color(101, 81, 255)).getRGB());
               ClickGuiRenderUtil.drawRoundedRect(this.startX + 4.0F, this.startY + 16.0F + (float)(m * 30), this.startX + 60.0F, this.startY + 39.0F + (float)(m * 30), (new Color(101, 81, 255)).getRGB(), (new Color(101, 81, 255, 100)).getRGB());
               if (mY[m].toString() == "QOL") {
                  FontLoaders.NMSL25.drawString("QOL", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Dungeons") {
                  FontLoaders.NMSL25.drawString("Dungeon", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Swapping") {
                  FontLoaders.NMSL25.drawString("Swaps", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Slayer") {
                  FontLoaders.NMSL25.drawString("Slayer", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Nether") {
                  FontLoaders.NMSL25.drawString("Nether", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Render") {
                  FontLoaders.NMSL25.drawString("Render", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Macros") {
                  FontLoaders.NMSL25.drawString("Macros", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Others") {
                  FontLoaders.NMSL25.drawString("Others", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }

               if (mY[m].toString() == "Balance") {
                  FontLoaders.NMSL25.drawString("Balance", this.startX + 8.0F, this.startY + 20.0F + (float)(m * 30) + 2.0F, (new Color(255, 255, 200)).getRGB());
               }
            }

            try {
               if (this.isCategoryHovered(this.startX + 7.0F, this.startY + 15.0F + (float)(m * 30), this.startX + 60.0F, this.startY + 45.0F + (float)(m * 30), mouseX, mouseY) && Mouse.isButtonDown(0)) {
                  this.SearchText = "Search...";
                  this.inSearch = false;
                  this.currentModuleType = mY[m];
                  this.currentModule = Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() != 0 ? (Module)Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(0) : null;
                  this.moduleStart = 0;
               }
            } catch (Exception var26) {
               System.err.println(var26);
            }
         }

         this.mouseWheel = Mouse.getDWheel();
         if (this.isCategoryHovered(this.startX + 60.0F, this.startY, this.startX + 200.0F, this.startY + 320.0F, mouseX, mouseY)) {
            if (this.mouseWheel < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size() - 1) {
               ++this.moduleStart;
            }

            if (this.mouseWheel > 0 && this.moduleStart > 0) {
               --this.moduleStart;
            }
         }

         if (this.isCategoryHovered(this.startX + 200.0F, this.startY, this.startX + 420.0F, this.startY + 320.0F, mouseX, mouseY)) {
            if (this.mouseWheel < 0 && this.valueStart < this.currentModule.getValues().size() - 1) {
               ++this.valueStart;
            }

            if (this.mouseWheel > 0 && this.valueStart > 0) {
               --this.valueStart;
            }
         }

         boolean searchHover = this.mouseWithinBounds(mouseX, mouseY, this.width / 2 - 100, 40, 200, 20);
         RenderUtil.drawFastRoundedRect((float)(this.width / 2 - 100), 40.0F, (float)(this.width / 2 + 100), 60.0F, 1.0F, (new Color(55, 55, 55, 190)).getRGB());
         FontLoaders.CNMD20.drawStringWithShadow(this.SearchText, (double)(this.width / 2 - 95), 46.0, (new Color(255, 255, 255, 190)).getRGB());
         RenderUtil.drawBorderedRect((float)(this.width / 2 - 100), 40.0F, (float)(this.width / 2 + 100), 60.0F, 0.5F, this.inSearch ? (new Color(255, 255, 255, 190)).getRGB() : (new Color(100, 100, 100, 100)).getRGB(), (new Color(180, 180, 180, 0)).getRGB());
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
               FontLoaders.CNMD20.drawString("|", (float)(this.width / 2 - 95 + FontLoaders.CNMD20.getStringWidth(this.SearchText)), 46.0F, -1);
               if (this.idkTimer.hasReached(1000.0)) {
                  this.idkTimer.reset();
               }
            }
         } else {
            this.idkTimer.reset();
         }

         if (this.currentModule != null) {
            RenderUtil.drawBorderedRect(this.startX + 210.0F, this.startY + 295.0F, this.startX + 420.0F, this.startY + 310.0F, 1.0F, (new Color(90, 90, 90, (int)(1.5 * (double)this.opacity.getOpacity()))).getRGB(), (new Color(30, 30, 30, (int)(0.5 * (double)this.opacity.getOpacity()))).getRGB());
            if (!this.binding) {
               FontLoaders.NMSL18.drawString("Current Key: " + Keyboard.getKeyName(this.currentModule.getKey()), this.startX + 215.0F, this.startY + 300.0F, -1);
               FontLoaders.NMSL18.drawString(EnumChatFormatting.GRAY + "Click to Bind.", this.startX + 360.0F, this.startY + 300.0F, -1);
            } else {
               FontLoaders.NMSL18.drawString("Waitting...", this.startX + 215.0F, this.startY + 300.0F, -1);
            }

            if (this.isButtonHovered(this.startX + 210.0F, this.startY + 295.0F, this.startX + 420.0F, this.startY + 310.0F, mouseX, mouseY) && Mouse.isButtonDown(0) && !this.binding) {
               this.binding = true;
            }

            if (this.isButtonHovered(this.startX + 210.0F, this.startY + 295.0F, this.startX + 420.0F, this.startY + 310.0F, mouseX, mouseY) && Mouse.isButtonDown(1) && this.binding) {
               this.binding = false;
            }

            if (this.isButtonHovered(this.startX + 210.0F, this.startY + 295.0F, this.startX + 420.0F, this.startY + 310.0F, mouseX, mouseY) && Mouse.isButtonDown(1) && !this.binding) {
               this.currentModule.setKey(0);
            }
         }

         ClickGuiRenderUtil.drawRoundedRect(this.startX + 199.0F, this.startY - 150.0F + (float)this.moduleStart - 85.0F + (float)(m * 30), this.startX + 201.0F, this.startY - 50.0F + (float)this.moduleStart + 10.0F + (float)(m * 30), (new Color(101, 81, 255)).getRGB(), (new Color(101, 81, 255, 180)).getRGB());
         if (this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
            FontLoaders.NMSL18.drawString("Search -> " + this.currentModule.getType().toString() + " ->", this.startX + 70.0F, this.startY + 12.5F, (new Color(248, 248, 248)).getRGB());
         } else {
            FontLoaders.NMSL18.drawString(this.currentModule == null ? this.currentModuleType.toString() : this.currentModuleType.toString(), this.startX + 70.0F, this.startY + 12.5F, (new Color(248, 248, 248)).getRGB());
         }

         if (this.currentModule != null) {
            FontLoaders.NMSL18.drawString(this.currentModule.getName(), this.startX + 210.5F, this.startY + 10.5F, Colors.AQUA.c);
            if (this.currentModule.getValues().isEmpty()) {
               FontLoaders.NMSL18.drawString("No Values Available.", this.startX + 270.0F, this.startY + 150.0F, Colors.AQUA.c);
            }
         }

         if (this.currentModule != null) {
            FontLoaders.NMSL14.drawString(this.currentModule.getModInfo(), this.startX + 215.5F + (float)FontLoaders.NMSL18.getStringWidth(this.currentModule.getName()), this.startY + 12.0F, -1);
         }

         float var24;
         int i;
         Module value;
         float x;
         double current;
         int next;
         boolean mouseAtPlus;
         float append;
         DecimalFormat df;
         double finalPlus;
         double inc;
         double valAbs;
         double perc;
         double valRel;
         double val;
         Value var25;
         Enum var26;
         boolean mouseAtCut;
         float append;
         double max;
         DecimalFormat df;
         if (this.SearchText != "Search..." && this.SearchText != null && this.SearchText != "") {
            var24 = this.startY + 30.0F;

            for(i = 0; i < ModuleManager.getModules().size(); ++i) {
               value = (Module)ModuleManager.getModules().get(i);
               String curMod = ((Module)ModuleManager.getModules().get(i)).getName().toUpperCase();
               String curModInfo = ((Module)ModuleManager.getModules().get(i)).getModInfo().toUpperCase();
               String val = "";

               Value v;
               for(Iterator var33 = value.getValues().iterator(); var33.hasNext(); val = val + (v.getName() + " ").toUpperCase()) {
                  v = (Value)var33.next();
               }

               mouseAtPlus = value.getValues().size() == 0 ? false : val.contains(this.SearchText.toUpperCase());
               if (curMod.contains(this.SearchText.toUpperCase()) || curModInfo.contains(this.SearchText.toUpperCase()) || mouseAtPlus) {
                  Module value = (Module)ModuleManager.getModules().get(i);
                  if (var24 > this.startY + 300.0F) {
                     break;
                  }

                  RenderUtil.drawFastRoundedRect(this.startX + 195.0F, var24, this.startX + 65.0F, var24 + 20.0F, 1.0F, (new Color(40, 40, 40, 20)).getRGB());
                  if (i >= this.moduleStart) {
                     FontLoaders.NMSL16.drawString(value.getName(), this.startX + 86.0F, var24 + 8.0F, (new Color(248, 248, 248, (int)this.opacity.getOpacity())).getRGB());
                     if (!value.isEnabled()) {
                        ClickGuiRenderUtil.drawFilledCircle((double)(this.startX + 75.0F), (double)(var24 + 10.0F), 3.0, (new Color(255, 0, 0)).getRGB(), 50);
                     } else {
                        ClickGuiRenderUtil.drawFilledCircle((double)(this.startX + 75.0F), (double)(var24 + 10.0F), 3.0, (new Color(0, 255, 0)).getRGB(), 50);
                     }

                     if (this.isSettingsButtonHovered(this.startX + 90.0F, var24 + 2.0F, this.startX + 183.0F, var24 + 16.0F + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY)) {
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

                     if (this.isSettingsButtonHovered(this.startX + 90.0F, var24, this.startX + 100.0F + (float)FontLoaders.NMSL20.getStringWidth(value.getName()), var24 + 8.0F + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                        this.currentModule = value;
                        this.valueStart = 0;
                     }

                     var24 += 25.0F;
                  }
               }
            }

            var24 = this.startY + 30.0F;

            for(i = 0; i < this.currentModule.getValues().size() && var24 <= this.startY + 280.0F; ++i) {
               if (i >= this.valueStart) {
                  var25 = (Value)this.currentModule.getValues().get(i);
                  if (var25 instanceof Numbers) {
                     x = this.startX + 300.0F;
                     current = (double)(68.0F * (((Number)var25.getValue()).floatValue() - ((Numbers)var25).getMinimum().floatValue()) / (((Numbers)var25).getMaximum().floatValue() - ((Numbers)var25).getMinimum().floatValue()));
                     ClickGuiRenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float)((double)x + 75.0), var24 + 3.0F, (new Color(50, 50, 50, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float)((double)x + current + 6.5), var24 + 3.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect((float)((double)x + current + 2.0), var24, (float)((double)x + current + 7.0), var24 + 5.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     mouseAtPlus = (float)this.mouseX > x + 80.0F && (float)mouseX < x + 90.0F && (float)mouseY > var24 - 2.5F && (float)mouseY < var24 + 7.5F;
                     RenderUtil.drawFastRoundedRect(x + 80.0F, var24 - 2.5F, x + 90.0F, var24 + 7.5F, 0.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawCenteredString("+", x + 85.0F, var24 - 0.5F, -1);
                     if (mouseAtPlus && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() >= ((Numbers)var25).getMaximum().doubleValue()) {
                           this.lcTimer.reset();
                        } else {
                           append = ((Numbers)var25).inc.floatValue();
                           df = new DecimalFormat("0.00");
                           finalPlus = Double.parseDouble(df.format((double)(((Number)var25.getValue()).floatValue() + append)));
                           var25.setValue(finalPlus);
                           this.lcTimer.reset();
                        }
                     }

                     mouseAtCut = (float)this.mouseX > x + 93.0F && (float)mouseX < x + 103.0F && (float)mouseY > var24 - 2.5F && (float)mouseY < var24 + 7.5F;
                     RenderUtil.drawFastRoundedRect(x + 93.0F, var24 - 2.5F, x + 103.0F, var24 + 7.5F, 0.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawCenteredString("-", x + 97.5F, var24 - 0.5F, -1);
                     if (mouseAtCut && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() <= ((Numbers)var25).getMinimum().doubleValue()) {
                           this.rcTimer.reset();
                        } else {
                           append = ((Numbers)var25).inc.floatValue();
                           df = new DecimalFormat("0.00");
                           inc = Double.parseDouble(df.format((double)(((Number)var25.getValue()).floatValue() - append)));
                           var25.setValue(inc);
                           this.rcTimer.reset();
                        }
                     }

                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName() + ": " + var25.getValue(), (double)(this.startX + 210.0F), (double)var24, -1);
                     if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                     }

                     if (this.isButtonHovered(x, var24 - 2.0F, x + 75.0F, var24 + 7.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                           current = ((Numbers)var25).getMinimum().doubleValue();
                           max = ((Numbers)var25).getMaximum().doubleValue();
                           inc = ((Numbers)var25).getIncrement().doubleValue();
                           valAbs = (double)mouseX - ((double)x + 1.0);
                           perc = valAbs / 68.0;
                           perc = Math.min(Math.max(0.0, perc), 1.0);
                           valRel = (max - current) * perc;
                           val = current + valRel;
                           val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                           ((Numbers)var25).setValue(val);
                        }

                        if (!Mouse.isButtonDown(0)) {
                           this.previousmouse = false;
                        }
                     }

                     var24 += 20.0F;
                  }

                  if (var25 instanceof Option) {
                     x = this.startX + 300.0F;
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24, x + 76.0F, var24 + 1.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24 + 8.0F, x + 76.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24, x + 57.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 77.0F, var24, x + 76.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), (double)(this.startX + 210.0F), (double)var24, -1);
                     if ((Boolean)var25.getValue()) {
                        ClickGuiRenderUtil.drawRect(x + 67.0F, var24 + 2.0F, x + 75.0F, var24 + 7.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     } else {
                        ClickGuiRenderUtil.drawRect(x + 58.0F, var24 + 2.0F, x + 65.0F, var24 + 7.0F, (new Color(150, 150, 150, (int)this.opacity.getOpacity())).getRGB());
                     }

                     if (this.isCheckBoxHovered(x + 56.0F, var24, x + 76.0F, var24 + 9.0F, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                           this.previousmouse = true;
                           this.mouse = true;
                        }

                        if (this.mouse) {
                           var25.setValue(!(Boolean)var25.getValue());
                           this.mouse = false;
                        }
                     }

                     if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                     }

                     var24 += 20.0F;
                  }

                  if (var25 instanceof Mode) {
                     x = this.startX + 300.0F;
                     ClickGuiRenderUtil.drawRect(x - 5.0F, var24 - 5.0F, x + 90.0F, var24 + 15.0F, (new Color(56, 56, 56, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawBorderRect((double)(x - 5.0F), (double)(var24 - 5.0F), (double)(x + 90.0F), (double)(var24 + 15.0F), (new Color(101, 81, 255, (int)this.opacity.getOpacity())).getRGB(), 2.0);
                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), (double)(this.startX + 210.0F), (double)(var24 + 2.0F), -1);
                     FontLoaders.NMSL18.drawStringWithShadow(((Mode)var25).getModeAsString(), (double)(x + 40.0F - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)var25).getModeAsString()) / 2)), (double)(var24 + 2.0F), -1);
                     if (this.isStringHovered(x, var24 - 5.0F, x + 100.0F, var24 + 15.0F, mouseX, mouseY)) {
                        if (Mouse.isButtonDown(0) && !this.previousmouse) {
                           var26 = (Enum)((Mode)var25).getValue();
                           next = var26.ordinal() + 1 >= ((Mode)var25).getModes().length ? 0 : var26.ordinal() + 1;
                           var25.setValue(((Mode)var25).getModes()[next]);
                           this.previousmouse = true;
                        }

                        if (!Mouse.isButtonDown(0)) {
                           this.previousmouse = false;
                        }
                     }

                     var24 += 25.0F;
                  }
               }
            }
         } else if (this.currentModule != null) {
            var24 = this.startY + 30.0F;

            for(i = 0; i < Client.instance.getModuleManager().getModulesInType(this.currentModuleType).size(); ++i) {
               value = (Module)Client.instance.getModuleManager().getModulesInType(this.currentModuleType).get(i);
               if (var24 > this.startY + 300.0F) {
                  break;
               }

               RenderUtil.drawFastRoundedRect(this.startX + 195.0F, var24, this.startX + 65.0F, var24 + 20.0F, 1.0F, (new Color(40, 40, 40, 20)).getRGB());
               if (i >= this.moduleStart) {
                  FontLoaders.NMSL16.drawString(value.getName(), this.startX + 86.0F, var24 + 8.0F, (new Color(248, 248, 248, (int)this.opacity.getOpacity())).getRGB());
                  if (!value.isEnabled()) {
                     ClickGuiRenderUtil.drawFilledCircle((double)(this.startX + 75.0F), (double)(var24 + 10.0F), 3.0, (new Color(255, 0, 0)).getRGB(), 50);
                  } else {
                     ClickGuiRenderUtil.drawFilledCircle((double)(this.startX + 75.0F), (double)(var24 + 10.0F), 3.0, (new Color(0, 255, 0)).getRGB(), 50);
                  }

                  if (this.isSettingsButtonHovered(this.startX + 90.0F, var24 + 2.0F, this.startX + 183.0F, var24 + 16.0F + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY)) {
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

                  if (this.isSettingsButtonHovered(this.startX + 90.0F, var24, this.startX + 100.0F + (float)FontLoaders.NMSL20.getStringWidth(value.getName()), var24 + 8.0F + (float)FontLoaders.NMSL20.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                     this.currentModule = value;
                     this.valueStart = 0;
                  }

                  var24 += 25.0F;
               }
            }

            var24 = this.startY + 30.0F;

            for(i = 0; i < this.currentModule.getValues().size() && var24 <= this.startY + 280.0F; ++i) {
               if (i >= this.valueStart) {
                  var25 = (Value)this.currentModule.getValues().get(i);
                  if (var25 instanceof Numbers) {
                     x = this.startX + 300.0F;
                     current = (double)(68.0F * (((Number)var25.getValue()).floatValue() - ((Numbers)var25).getMinimum().floatValue()) / (((Numbers)var25).getMaximum().floatValue() - ((Numbers)var25).getMinimum().floatValue()));
                     ClickGuiRenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float)((double)x + 75.0), var24 + 3.0F, (new Color(50, 50, 50, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x - 6.0F, var24 + 2.0F, (float)((double)x + current + 6.5), var24 + 3.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect((float)((double)x + current + 2.0), var24, (float)((double)x + current + 7.0), var24 + 5.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     mouseAtPlus = (float)this.mouseX > x + 80.0F && (float)mouseX < x + 90.0F && (float)mouseY > var24 - 2.5F && (float)mouseY < var24 + 7.5F;
                     RenderUtil.drawFastRoundedRect(x + 80.0F, var24 - 2.5F, x + 90.0F, var24 + 7.5F, 0.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawCenteredString("+", x + 85.0F, var24 - 0.5F, -1);
                     if (mouseAtPlus && Mouse.isButtonDown(0) && this.lcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() >= ((Numbers)var25).getMaximum().doubleValue()) {
                           this.lcTimer.reset();
                        } else {
                           append = ((Numbers)var25).inc.floatValue();
                           df = new DecimalFormat("0.00");
                           finalPlus = Double.parseDouble(df.format((double)(((Number)var25.getValue()).floatValue() + append)));
                           var25.setValue(finalPlus);
                           this.lcTimer.reset();
                        }
                     }

                     mouseAtCut = (float)this.mouseX > x + 93.0F && (float)mouseX < x + 103.0F && (float)mouseY > var24 - 2.5F && (float)mouseY < var24 + 7.5F;
                     RenderUtil.drawFastRoundedRect(x + 93.0F, var24 - 2.5F, x + 103.0F, var24 + 7.5F, 0.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawCenteredString("-", x + 97.5F, var24 - 0.5F, -1);
                     if (mouseAtCut && Mouse.isButtonDown(0) && this.rcTimer.hasReached(150.0)) {
                        if ((double)((Number)var25.getValue()).floatValue() <= ((Numbers)var25).getMinimum().doubleValue()) {
                           this.rcTimer.reset();
                        } else {
                           append = ((Numbers)var25).inc.floatValue();
                           df = new DecimalFormat("0.00");
                           inc = Double.parseDouble(df.format((double)(((Number)var25.getValue()).floatValue() - append)));
                           var25.setValue(inc);
                           this.rcTimer.reset();
                        }
                     }

                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName() + ": " + var25.getValue(), (double)(this.startX + 210.0F), (double)var24, -1);
                     if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                     }

                     if (this.isButtonHovered(x, var24 - 2.0F, x + 75.0F, var24 + 7.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                           current = ((Numbers)var25).getMinimum().doubleValue();
                           max = ((Numbers)var25).getMaximum().doubleValue();
                           inc = ((Numbers)var25).getIncrement().doubleValue();
                           valAbs = (double)mouseX - ((double)x + 1.0);
                           perc = valAbs / 68.0;
                           perc = Math.min(Math.max(0.0, perc), 1.0);
                           valRel = (max - current) * perc;
                           val = current + valRel;
                           val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                           ((Numbers)var25).setValue(val);
                        }

                        if (!Mouse.isButtonDown(0)) {
                           this.previousmouse = false;
                        }
                     }

                     var24 += 20.0F;
                  }

                  if (var25 instanceof Option) {
                     x = this.startX + 300.0F;
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24, x + 76.0F, var24 + 1.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24 + 8.0F, x + 76.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 56.0F, var24, x + 57.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawRect(x + 77.0F, var24, x + 76.0F, var24 + 9.0F, (new Color(255, 255, 255, (int)this.opacity.getOpacity())).getRGB());
                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), (double)(this.startX + 210.0F), (double)var24, -1);
                     if ((Boolean)var25.getValue()) {
                        ClickGuiRenderUtil.drawRect(x + 67.0F, var24 + 2.0F, x + 75.0F, var24 + 7.0F, (new Color(61, 141, 255, (int)this.opacity.getOpacity())).getRGB());
                     } else {
                        ClickGuiRenderUtil.drawRect(x + 58.0F, var24 + 2.0F, x + 65.0F, var24 + 7.0F, (new Color(150, 150, 150, (int)this.opacity.getOpacity())).getRGB());
                     }

                     if (this.isCheckBoxHovered(x + 56.0F, var24, x + 76.0F, var24 + 9.0F, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                           this.previousmouse = true;
                           this.mouse = true;
                        }

                        if (this.mouse) {
                           var25.setValue(!(Boolean)var25.getValue());
                           this.mouse = false;
                        }
                     }

                     if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                     }

                     var24 += 20.0F;
                  }

                  if (var25 instanceof Mode) {
                     x = this.startX + 300.0F;
                     ClickGuiRenderUtil.drawRect(x - 5.0F, var24 - 5.0F, x + 90.0F, var24 + 15.0F, (new Color(56, 56, 56, (int)this.opacity.getOpacity())).getRGB());
                     ClickGuiRenderUtil.drawBorderRect((double)(x - 5.0F), (double)(var24 - 5.0F), (double)(x + 90.0F), (double)(var24 + 15.0F), (new Color(101, 81, 255, (int)this.opacity.getOpacity())).getRGB(), 2.0);
                     FontLoaders.NMSL18.drawStringWithShadow(var25.getName(), (double)(this.startX + 210.0F), (double)(var24 + 2.0F), -1);
                     FontLoaders.NMSL18.drawStringWithShadow(((Mode)var25).getModeAsString(), (double)(x + 40.0F - (float)(FontLoaders.NMSL18.getStringWidth(((Mode)var25).getModeAsString()) / 2)), (double)(var24 + 2.0F), -1);
                     if (this.isStringHovered(x, var24 - 5.0F, x + 100.0F, var24 + 15.0F, mouseX, mouseY)) {
                        if (Mouse.isButtonDown(0) && !this.previousmouse) {
                           var26 = (Enum)((Mode)var25).getValue();
                           next = var26.ordinal() + 1 >= ((Mode)var25).getModes().length ? 0 : var26.ordinal() + 1;
                           var25.setValue(((Mode)var25).getModes()[next]);
                           this.previousmouse = true;
                        }

                        if (!Mouse.isButtonDown(0)) {
                           this.previousmouse = false;
                        }
                     }

                     var24 += 25.0F;
                  }
               }
            }
         }

         super.drawScreen(mouseX, mouseY, partialTicks);
      }
   }

   public void onGuiClosed() {
      Client.instance.saveConfig();
      Client.instance.saveUISettings();
      this.opacity.setOpacity(0.0F);
      this.mc.entityRenderer.switchUseShader();
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
      int j;
      if (x > x1) {
         j = x;
         x = x1;
         x1 = j;
      }

      if (y > y1) {
         j = y;
         y = y1;
         y1 = j;
      }

      return mouseX >= x && mouseX <= x1 && mouseY >= y && mouseY <= y1;
   }

   public boolean mouseWithinBounds(int mouseX, int mouseY, int x, int y, int x1, int y1) {
      return this.mouseWithinBounds2(mouseX, mouseY, x, y, x + x1, y + y1);
   }
}
