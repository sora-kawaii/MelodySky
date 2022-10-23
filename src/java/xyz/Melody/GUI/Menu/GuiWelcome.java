package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.Melody.Client;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.FadeUtil;

public class GuiWelcome extends GuiScreen {
   private int shabiAlpha = 0;
   private int alpha = 0;
   private int contentAlpha = 0;
   private float titleY = 0.0F;
   private TimerUtil timer = new TimerUtil();
   private int continueAlpha = 0;

   public void initGui() {
      this.alpha = 0;
      this.titleY = 0.0F;
      this.contentAlpha = 0;
      this.continueAlpha = 0;
      this.timer.reset();
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      CFontRenderer font = FontLoaders.CNMD35;
      CFontRenderer titleFont = FontLoaders.CNMD45;
      CFontRenderer contentFont = FontLoaders.NMSL22;
      CFontRenderer continueFont = FontLoaders.CNMD30;
      this.drawDefaultBackground();
      if (Client.firstLaunch) {
         if (this.alpha < 210) {
            this.alpha += 3;
         }

         if (this.alpha >= 210 && this.titleY < (float)(this.height / 3)) {
            this.titleY += (float)(this.height / 130);
         }

         if (this.titleY >= (float)(this.height / 3) && this.contentAlpha < 210) {
            this.contentAlpha += 7;
         }

         titleFont.drawCenteredString("Melody Skyblock", (float)this.width / 2.0F, (float)this.height / 2.0F - 3.0F - this.titleY, (new Color(255, 255, 255, this.alpha)).getRGB());
         if (this.contentAlpha > 0) {
            contentFont.drawCenteredString("What is MelodySky?   This is a Mod that improves The Quality of Life of Hypixel Skyblock (QOL Mod).", (float)this.width / 2.0F, (float)this.height / 3.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            contentFont.drawCenteredString("What Would This Offer?   Auto Fishing, Auto Do Experiment Table For Main World. Auto Terminals, Livid Finder For Dungeoning. And so on..", (float)this.width / 2.0F, (float)this.height / 3.0F + 25.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            contentFont.drawCenteredString("How To Use?   Type /.bind clickgui rshift/ to Set the Binding of Click Gui to Right Shift.", (float)this.width / 2.0F, (float)this.height / 3.0F + 50.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            contentFont.drawCenteredString("- - - - - In Click Gui, Left Click on a Module to Toggle, Right Click to Show Settings.", (float)this.width / 2.0F, (float)this.height / 3.0F + 75.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            contentFont.drawCenteredString("- - - - - Type /.help/ to Show All Client Commands and Useage.", (float)this.width / 2.0F, (float)this.height / 3.0F + 100.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            contentFont.drawCenteredString("If You Have Any Questions, +QQ Group 1169679382 to Get Help.", (float)this.width / 2.0F, (float)this.height / 3.0F + 125.0F, (new Color(255, 255, 255, this.contentAlpha)).getRGB());
            if (this.timer.hasReached(10000.0)) {
               if (this.continueAlpha > 0 && this.continueAlpha < 210) {
                  continueFont.drawCenteredString("Click To Continue.", (float)this.width / 2.0F, (float)(this.height - 100), (new Color(255, 255, 255, this.continueAlpha)).getRGB());
               }

               if (this.continueAlpha >= 210) {
                  continueFont.drawCenteredString("Click To Continue.", (float)this.width / 2.0F, (float)(this.height - 100), FadeUtil.fade(new Color(255, 255, 255, this.continueAlpha)).getRGB());
               }

               if (this.continueAlpha < 210) {
                  this.continueAlpha += 7;
               }
            }
         }
      }

      if (!Client.firstLaunch) {
         if (this.shabiAlpha < 210) {
            this.shabiAlpha += 10;
         }

         font.drawCenteredString("Welcome back to MelodySky", (float)this.width / 2.0F, (float)this.height / 2.0F - 3.0F, (new Color(255, 255, 255, this.shabiAlpha)).getRGB());
         Client.firstMenu = false;
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (Client.firstLaunch) {
         if (this.continueAlpha >= 210) {
            this.mc.displayGuiScreen(this);
            Client.firstLaunch = false;
         }
      } else {
         this.mc.displayGuiScreen(new MainMenu());
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
}
