package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.Click.ClickUi;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.MenuParticle;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.Animation;
import xyz.Melody.Utils.render.RenderUtil;

public class MainMenu extends GuiScreen implements GuiYesNoCallback {
   private TimerUtil restTimer = new TimerUtil();
   private String title = "";
   private TimerUtil timer = new TimerUtil();
   private TimerUtil dick = new TimerUtil();
   private TimerUtil saveTimer = new TimerUtil();
   private int letterDrawn = 0;
   private boolean titleDone = false;
   private ArrayList particles = new ArrayList();
   private Random RANDOM = new Random();
   private int particleCount = 7000;
   Animation anim = new Animation() {
      public int getMaxTime() {
         return 0;
      }

      public void render() {
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.color(1.0F, 1.0F, 1.0F, (float)this.time / 10.0F);
         worldrenderer.begin(7, DefaultVertexFormats.POSITION);
         worldrenderer.pos(0.0, (double)MainMenu.this.height + 1.0, 0.0).endVertex();
         worldrenderer.pos((double)MainMenu.this.width, (double)MainMenu.this.height + 1.0, 0.0).endVertex();
         worldrenderer.pos((double)MainMenu.this.width, 0.0, 0.0).endVertex();
         worldrenderer.pos(0.0, 0.0, 0.0).endVertex();
         tessellator.draw();
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }
   };

   public void initGui() {
      super.initGui();
      this.particles.clear();
      this.restTimer.reset();
      this.timer.reset();
      this.letterDrawn = 0;
      this.title = "";
      this.titleDone = false;
      this.buttonList.add(new ClientButton(0, this.width / 2 - 80, this.height / 2 - 60, 160, 20, "Single Player", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(1, this.width / 2 - 80, this.height / 2 - 36, 160, 20, "Multi Player", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(2, this.width / 2 - 80, this.height / 2 - 12, 160, 20, "Config Manager", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(3, this.width / 2 - 80, this.height / 2 + 12, 160, 20, "Settings", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(5, this.width / 2 + 2, this.height / 2 + 36, 78, 18, "ChangeLogs", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(15, this.width / 2 - 80, this.height / 2 + 36, 78, 18, "Languages", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(16, this.width / 2 - 102, this.height / 2 + 36, 18, 18, "", new ResourceLocation("Melody/icon/realms.png"), new ResourceLocation("Melody/icon/realms_hovered.png"), -3.0F, -3.0F, 12.0F, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(10, this.width - 43, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/discord.png"), new Color(20, 20, 20, 0)));
      this.buttonList.add(new ClientButton(11, this.width - 78, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/github.png"), new Color(20, 20, 20, 0)));
      this.buttonList.add(new ClientButton(12, this.width - 113, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/google.png"), new Color(20, 20, 20, 0)));
      this.buttonList.add(new ClientButton(13, this.width - 148, this.height - 40, 32, 32, "", new ResourceLocation("Melody/icon/youtube.png"), new Color(20, 20, 20, 0)));
      this.buttonList.add(new ClientButton(4, this.width - 100, 10, 60, 24, "Vanilla", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(14, this.width - 10 - 24, 10, 25, 24, "", new ResourceLocation("Melody/icon/exit.png"), new Color(20, 20, 20, 60)));
      this.anim.on();

      for(int iii = 0; iii < this.particleCount; ++iii) {
         double randomX = -2.0 + 4.0 * this.RANDOM.nextDouble();
         double randomY = -2.0 + 4.0 * this.RANDOM.nextDouble();
         double randomXm = 0.0 + (double)(this.width - 0) * this.RANDOM.nextDouble();
         double randomYm = 0.0 + (double)(this.height - 0) * this.RANDOM.nextDouble();
         double randomDepthm = this.RANDOM.nextDouble() + 0.1;
         int mX = 0;
         int mY = 0;
         MenuParticle part = (new MenuParticle(randomXm + 0.0, randomYm + 0.0, randomDepthm + 0.0, true)).addMotion(randomX + (double)(mX / 4), randomY + (double)(mY / 4));
         part.alpha = 0.15F;
         this.particles.add(part);
      }

   }

   protected void actionPerformed(GuiButton button) {
      switch (button.id) {
         case 0:
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
            break;
         case 1:
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
            break;
         case 2:
            this.mc.displayGuiScreen(new ClickUi(true));
            break;
         case 3:
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            break;
         case 4:
            Client.vanillaMenu = true;
            this.mc.displayGuiScreen(new GuiMainMenu());
            Client.instance.saveMenuMode();
            break;
         case 5:
            this.mc.displayGuiScreen(new GuiChangeLog());
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            break;
         case 10:
            try {
               this.open("https://discord.gg/VnNCJfEyhU");
            } catch (Exception var6) {
               var6.printStackTrace();
            }
            break;
         case 11:
            try {
               this.open("https://github.com/NMSLAndy");
            } catch (Exception var5) {
               var5.printStackTrace();
            }
            break;
         case 12:
            try {
               this.open("https://mail.google.com/chat/u/0/#chat/space/AAAAzBno-5k");
            } catch (Exception var4) {
               var4.printStackTrace();
            }
            break;
         case 13:
            try {
               this.open("https://www.youtube.com/channel/UCM8A_7JEGLyqlUq7I7BwUVQ");
            } catch (Exception var3) {
               var3.printStackTrace();
            }
            break;
         case 14:
            this.mc.shutdown();
            break;
         case 15:
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
            break;
         case 16:
            this.switchToRealms();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (Client.firstMenu && Client.instance.authManager.verified) {
         this.mc.displayGuiScreen(new GuiWelcome());
      }

      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      CFontRenderer font2 = FontLoaders.CNMD18;
      CFontRenderer font1 = FontLoaders.CNMD45;
      this.drawDefaultBackground();
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
            ++this.letterDrawn;
         }

         if (this.timer.hasReached(450.0)) {
            if (this.letterDrawn < 2) {
               this.title = this.title + "e";
               ++this.letterDrawn;
            }

            if (this.timer.hasReached(700.0)) {
               if (this.letterDrawn < 3) {
                  this.title = this.title + "l";
                  ++this.letterDrawn;
               }

               if (this.timer.hasReached(950.0)) {
                  if (this.letterDrawn < 4) {
                     this.title = this.title + "o";
                     ++this.letterDrawn;
                  }

                  if (this.timer.hasReached(1200.0)) {
                     if (this.letterDrawn < 5) {
                        this.title = this.title + "d";
                        ++this.letterDrawn;
                     }

                     if (this.timer.hasReached(1450.0)) {
                        if (this.letterDrawn < 6) {
                           this.title = this.title + "y";
                           ++this.letterDrawn;
                        }

                        if (this.timer.hasReached(1700.0)) {
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

      font1.drawString(this.title, (float)(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 - 3), (float)(this.height / 2 - 107), (new Color(138, 43, 226, 160)).getRGB());
      if (this.dick.hasReached(600.0)) {
         RenderUtil.drawFastRoundedRect((float)(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 + font1.getStringWidth(this.title) + 4 - 3), (float)(this.height / 2 - 108), (float)(this.width / 2 - font1.getStringWidth("Melody Sky") / 2 + font1.getStringWidth(this.title) + 5 - 3), (float)(this.height / 2 - 83), 1.0F, (new Color(198, 198, 198)).getRGB());
         if (this.dick.hasReached(1200.0)) {
            this.dick.reset();
         }
      }

      FontLoaders.CNMD16.drawString("©2019-2022 MelodyWorkGroup", 4.0F, (float)(this.height - 10), (new Color(20, 20, 20, 180)).getRGB());
      RenderUtil.drawFastRoundedRect((float)(this.width - 153), (float)(this.height - 43), (float)(this.width - 8), (float)(this.height - 5), 1.0F, (new Color(100, 180, 255, 20)).getRGB());
      RenderUtil.drawFastRoundedRect(10.0F, 10.0F, 186.0F, 50.0F, 1.0F, (new Color(20, 20, 20, 100)).getRGB());
      font2.drawCenteredString("Logged in as: " + EnumChatFormatting.BLUE + this.mc.getSession().getUsername(), 98.0F, 20.0F, Colors.GRAY.c);
      font2.drawCenteredString(EnumChatFormatting.GRAY + "Released Build " + EnumChatFormatting.GREEN + "2.1.4 Build1", 98.0F, 34.0F, Colors.GRAY.c);
      this.anim.render();
      if (!this.particles.isEmpty()) {
         GlStateManager.pushMatrix();
         Iterator iter = this.particles.iterator();

         MenuParticle part;
         while(iter.hasNext()) {
            part = (MenuParticle)iter.next();
            part.update(mouseX, mouseY, this.particles);
            if (part.alpha < 0.1F) {
               part.remove = true;
            }
         }

         iter = this.particles.iterator();

         while(iter.hasNext()) {
            part = (MenuParticle)iter.next();
            if (part.remove) {
               iter.remove();
            }
         }

         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Iterator var15 = this.particles.iterator();

         while(var15.hasNext()) {
            MenuParticle particle = (MenuParticle)var15.next();
            GlStateManager.color(0.5F, 0.6F, 1.0F, particle.alpha);
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

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.restTimer.reset();

      MenuParticle part;
      double dist;
      double mY;
      for(Iterator var4 = this.particles.iterator(); var4.hasNext(); part.motionY -= mY * 200.0 / (dist / 2.0)) {
         part = (MenuParticle)var4.next();
         float angle = (float)Math.toDegrees(Math.atan2((double)mouseY - part.y, (double)mouseX - part.x));
         if (angle < 0.0F) {
            angle += 360.0F;
         }

         double xDist = (double)mouseX - part.x;
         double yDist = (double)mouseY - part.y;
         dist = Math.sqrt(xDist * xDist + yDist * yDist);
         double mX = Math.cos(Math.toRadians((double)angle));
         mY = Math.sin(Math.toRadians((double)angle));
         if (dist < 20.0) {
            dist = 20.0;
         }

         part.motionX -= mX * 200.0 / (dist / 2.0);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   private void open(String url) throws Exception {
      String osName = System.getProperty("os.name", "");
      if (osName.startsWith("Windows")) {
         Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url);
      } else if (osName.startsWith("Mac OS")) {
         Class fileMgr = Class.forName("com.apple.eio.FileManager");
         Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
         openURL.invoke((Object)null, url);
      } else {
         String[] browsers = new String[]{"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
         String browser = null;

         for(int count = 0; count < browsers.length && browser == null; ++count) {
            if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
               browser = browsers[count];
            }
         }

         if (browser == null) {
            throw new RuntimeException("No Browser Was Found.");
         }

         Runtime.getRuntime().exec(new String[]{browser, url});
      }

   }

   public void drawDefaultBackground() {
      BackgroundShader.BACKGROUND_SHADER.startShader();
      Tessellator instance = Tessellator.getInstance();
      WorldRenderer worldRenderer = instance.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(0.0, (double)this.height, 0.0).endVertex();
      worldRenderer.pos((double)this.width, (double)this.height, 0.0).endVertex();
      worldRenderer.pos((double)this.width, 0.0, 0.0).endVertex();
      worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
      instance.draw();
      BackgroundShader.BACKGROUND_SHADER.stopShader();
      ParticleUtils.drawParticles();
   }

   private void switchToRealms() {
      RealmsBridge realmsbridge = new RealmsBridge();
      realmsbridge.switchToRealms(this);
   }
}
