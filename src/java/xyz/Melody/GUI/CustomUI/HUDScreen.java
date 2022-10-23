package xyz.Melody.GUI.CustomUI;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.System.Managers.FileManager;
import xyz.Melody.Utils.render.RenderUtil;

public class HUDScreen extends GuiScreen {
   public static ArrayList HUDWindows = Lists.newArrayList();
   public int scrollVelocity;

   public HUDScreen() {
      if (HUDWindows.isEmpty()) {
         for(int n22 = 0; n22 < HUDManager.getApis().size(); ++n22) {
            HUDApi c2 = (HUDApi)HUDManager.getApis().get(n22);
            HUDWindows.add(new HUDWindow(c2, c2.x, c2.y));
         }
      }

   }

   public void initGui() {
      if (this.mc.theWorld != null) {
         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (this.mc.theWorld == null) {
         RenderUtil.drawImage(new ResourceLocation("Melody/background.png"), 0.0F, 0.0F, (float)this.width, (float)this.height);
         ParticleUtils.drawParticles(mouseX, mouseY);
      }

      if (Mouse.hasWheel()) {
         int wheel = Mouse.getDWheel();
         this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
      }

      FontLoaders.NMSL20.drawStringWithShadow("RightClick To Toggle.", (double)(this.width / 2 - FontLoaders.NMSL20.getStringWidth("RightClick To Toggle.") / 2), (double)(this.height - this.height + 9), (new Color(255, 20, 0)).getRGB());
      HUDWindows.forEach((w2) -> {
         w2.render(mouseX, mouseY);
      });
      HUDManager.getApis().forEach((w2) -> {
         w2.InScreenRender();
      });
      HUDWindows.forEach((w2) -> {
         w2.mouseScroll(mouseX, mouseY, this.scrollVelocity);
      });
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      HUDWindows.forEach((w2) -> {
         w2.click(mouseX, mouseY, mouseButton);
      });
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void onGuiClosed() {
      this.mc.entityRenderer.stopUseShader();
      String x = "";

      HUDApi m;
      for(Iterator var2 = HUDManager.getApis().iterator(); var2.hasNext(); x = x + String.format("%s:%s:%s:%s%s", m.getName(), m.x, m.y, m.isEnabled(), System.lineSeparator())) {
         m = (HUDApi)var2.next();
      }

      FileManager.save("HUD.txt", x, false);
   }
}
