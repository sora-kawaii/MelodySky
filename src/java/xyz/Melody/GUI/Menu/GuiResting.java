package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;

public class GuiResting extends GuiScreen {
   private final ParticleUtils particle = new ParticleUtils();
   private int nmsl = 0;
   private static String Welcome;
   private static long startTime = 0L;
   int alpha = 255;
   TimerUtil timer = new TimerUtil();
   private float currentX;
   private float targetX;
   private float currentY;
   private float targetY;
   int textalpha = 255;
   double Anitext = 0.0;

   public void initGui() {
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      Calendar c = Calendar.getInstance();
      int hour = c.get(11);
      int min = c.get(12);
      int sec = c.get(13);
      String time = hour + " : " + min + " : " + sec;
      ScaledResolution sr = new ScaledResolution(this.mc);
      CFontRenderer titleFont = FontLoaders.CNMD35;
      CFontRenderer timeFont = FontLoaders.NMSL28;
      this.drawDefaultBackground();
      titleFont.drawCenteredString("Click or Tap the Keyboard to Continue.", (float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F - 15.0F - (float)this.Anitext, (new Color(255, 255, 255, 170)).getRGB());
      timeFont.drawCenteredString(time, (float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F + 10.0F - (float)this.Anitext, (new Color(180, 180, 180, 130)).getRGB());
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.mc.displayGuiScreen(new MainMenu());
   }

   public void handleKeyboardInput() throws IOException {
      this.mc.displayGuiScreen(new MainMenu());
      super.handleKeyboardInput();
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

   public void onGuiClosed() {
      this.mc.entityRenderer.switchUseShader();
   }
}
