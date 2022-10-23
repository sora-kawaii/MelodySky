package xyz.Melody.GUI.ShaderBG.Shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;
import xyz.Melody.GUI.ShaderBG.Shader;

public final class BackgroundShader2 extends Shader {
   public static final BackgroundShader2 BACKGROUND_SHADER = new BackgroundShader2();
   private float time;

   public BackgroundShader2() {
      super("background2.frag");
   }

   public void setupUniforms() {
      this.setupUniform("iResolution");
      this.setupUniform("iTime");
   }

   public void updateUniforms() {
      ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
      int resolutionID = this.getUniform("iResolution");
      if (resolutionID > -1) {
         GL20.glUniform2f(resolutionID, (float)scaledResolution.getScaledWidth() * 2.0F, (float)scaledResolution.getScaledHeight() * 2.0F);
      }

      int timeID = this.getUniform("iTime");
      if (timeID > -1) {
         GL20.glUniform1f(timeID, this.time);
      }

      this.time += 0.1F;
   }
}
