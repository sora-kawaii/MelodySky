package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;

public class GuiChangeLog extends GuiScreen {
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
      CFontRenderer font = FontLoaders.NMSL18;
      this.drawDefaultBackground();
      RenderUtil.drawFastRoundedRect(0.0F, 59.0F, (float)this.width, 61.0F, 1.0F, (new Color(120, 120, 120, 60)).getRGB());
      FontLoaders.CNMD45.drawCenteredString("ChangeLogs", (float)(this.width / 2), 25.0F, (new Color(246, 88, 80, 100)).getRGB());
      FontLoaders.CNMD16.drawString("©2019-2022 MelodyWorkGroup", 4.0F, (float)(this.height - 10), (new Color(30, 30, 30, 180)).getRGB());
      String[] logs = new String[]{"[2.1.4] [+] Auto Mode in StonkLessStonk.", "[2.1.4] [/] Fixed Free Cam.", "[2.1.4] [/] Ghost Block Optimization.", "[2.1.3] [+] CropNuker.", "[2.1.3] [/] Ghost Block Optimization.", "[2.1.3] [/] Auto Fish Escape Optimization.", "[2.1.3] [/] MainMenu Optimization.", "[2.1.2] [B2] [/] Old Animations Optimization x2.", "[2.1.2] [B2] [/] Fixed Alerts Module.", "[2.1.2] [+] FreeCam.", "[2.1.2] [/] Old Animations Optimization.", "[2.1.2] [+] Hide Explosions in Sounds Hider.", "[2.1.1] [+] OldAnimations.", "[2.1.0] [Build2] [/] Fixed WormFishingESP.", "[2.1.0] [Build2] [/] Mob Tracker Optimization.", "[2.1.0] [/] Recode Auto Shoot The Target.", "[2.1.0] [/] WormFishingESP Optimizations.", "[2.1.0] [/] Fixed Worm Fishing ESP.", "[2.1.0] [/] Fixed Macro Protect in Mithril Nuker.", "[2.1.0] [-] GemstoneESP.", "[2.0.9] [/] Recode Click Gui.", "[2.0.9] [/] Module Categories Redistribution.", "[2.0.9] [+] Search Bar in Click Gui.", "[2.0.9] [+] Imprived Admin Check in AutoFish.", "[2.0.9] [+] Death Check in AutoFish.", "[2.0.9] [+] Slayer MiniBoss ESP.", "[2.0.9] [+] Ashfang Helper.", "[2.0.9] [+] Boss Entry Check in LeverAura.", "[2.0.9] [/] Fixed Dungeon Boss Check.", "[2.0.9] [/] Add Click Delay in Stonk Less Stonk.", "[2.0.8] [+] JerryChine Vertical Helper in Balance -> AntiKB.", "[2.0.8] [+] Auto Swap Blaze Slayer Dagger.", "[2.0.8] [/] AutoFish Optimization.", "[2.0.7] [+] Implosion Particles Hider.", "[2.0.7] [+] Day Counter in CustomUI.", "[^] Other Tuning.", "Fixed Known Bugs."};
      int i = 0;
      String[] var7 = logs;
      int var8 = logs.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String log = var7[var9];
         font.drawString(log, 6.0F, (float)(i * 16 + 71), (new Color(255, 255, 255, 200)).getRGB());
         ++i;
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.mc.displayGuiScreen(new MainMenu());
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

   public void handleKeyboardInput() throws IOException {
      super.handleKeyboardInput();
   }

   public void onGuiClosed() {
      this.mc.entityRenderer.switchUseShader();
   }
}
