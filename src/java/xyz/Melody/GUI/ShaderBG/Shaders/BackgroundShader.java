package xyz.Melody.GUI.ShaderBG.Shaders;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;
import xyz.Melody.GUI.ShaderBG.Shader;

public final class BackgroundShader extends Shader {
   public static int deltaTime;
   public static BackgroundShader BACKGROUND_SHADER = new BackgroundShader();
   private float time;

   public BackgroundShader() {
      super("background.frag");
   }

   public void setupUniforms() {
      this.setupUniform("iResolution");
      this.setupUniform("iTime");
   }

   public void updateUniforms() {
      int resolutionID = this.getUniform("iResolution");
      if (resolutionID > -1) {
         GL20.glUniform2f(resolutionID, (float)Display.getWidth(), (float)Display.getHeight());
      }

      int timeID = this.getUniform("iTime");
      if (timeID > -1) {
         GL20.glUniform1f(timeID, this.time);
      }

      this.time += 0.01F;
   }
}
