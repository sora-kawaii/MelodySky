package xyz.Melody.GUI.Click;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.UISettings;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.ModuleType;

public class ClickUi extends GuiScreen {
   private TimerUtil timer = new TimerUtil();
   public static ArrayList windows = new ArrayList();
   public double opacity;
   public int scrollVelocity;
   public static boolean binding = false;
   private int alpha = 0;
   private float animpos;
   private boolean jb;

   public ClickUi() {
      this.jb = false;
      this.opacity = 0.0;
      this.animpos = 75.0F;
      if (windows.isEmpty()) {
         int x = 5;
         ModuleType[] var2 = ModuleType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ModuleType c = var2[var4];
            windows.add(new Window(c, x, 5));
            x += 105;
         }
      }

   }

   public ClickUi(boolean jb) {
      this.jb = jb;
      this.opacity = 0.0;
      this.animpos = 75.0F;
      if (windows.isEmpty()) {
         int x = 5;
         ModuleType[] var3 = ModuleType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ModuleType c = var3[var5];
            windows.add(new Window(c, x, 5));
            x += 105;
         }
      }

   }

   public void initGui() {
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
            if (this.mc.theWorld == null) {
               JOptionPane.showConfirmDialog((Component)null, "This Feature is Not Available in Menus.");
            } else {
               this.mc.displayGuiScreen(new HUDScreen());
            }
      }

      super.actionPerformed(gay);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.animpos = AnimationUtil.moveUD(this.animpos, 1.0F, 0.1F, 0.1F);
      if (this.jb) {
         this.drawDefaultBackground();
      }

      if (this.timer.hasReached(1.0) && this.alpha <= 130) {
         this.alpha += 4;
         this.timer.reset();
      }

      RenderUtil.drawFastRoundedRect(0.0F, 0.0F, (float)this.width, (float)this.height, 0.0F, (new Color(18, 18, 18, this.alpha)).getRGB());
      RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), (float)(this.width - 160), (float)(this.height - 40), 32.0F, 32.0F);
      FontLoaders.CNMD34.drawStringWithShadow("MelodySky", (double)(this.width - 125), (double)(this.height - 34), -1);
      GlStateManager.rotate(this.animpos, 0.0F, 0.0F, 0.0F);
      GlStateManager.translate(0.0F, this.animpos, 0.0F);
      this.opacity = this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0;
      GlStateManager.pushMatrix();
      windows.forEach((w) -> {
         w.render(mouseX, mouseY);
      });
      GlStateManager.popMatrix();
      if (Mouse.hasWheel()) {
         int wheel = Mouse.getDWheel();
         this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 120 : 0);
      }

      windows.forEach((w) -> {
         w.mouseScroll(mouseX, mouseY, this.scrollVelocity);
      });
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      windows.forEach((w) -> {
         w.click(mouseX, mouseY, mouseButton);
      });
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1 && !binding) {
         this.mc.displayGuiScreen((GuiScreen)null);
      } else {
         windows.forEach((w) -> {
            w.key(typedChar, keyCode);
         });
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

   public void onGuiClosed() {
      this.alpha = 0;
      this.timer.reset();
      Client.instance.saveConfig();
      Client.instance.saveUISettings();
      this.mc.entityRenderer.stopUseShader();
   }

   public synchronized void sendToFront(Window window) {
      int panelIndex = 0;

      for(int i = 0; i < windows.size(); ++i) {
         if (windows.get(i) == window) {
            panelIndex = i;
            break;
         }
      }

      Window t = (Window)windows.get(windows.size() - 1);
      windows.set(windows.size() - 1, windows.get(panelIndex));
      windows.set(panelIndex, t);
   }
}
