package xyz.Melody.GUI;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public final class ClientButton extends GuiButton {
   private ResourceLocation image;
   private ResourceLocation hoveredImage;
   private float imgScale;
   private float imgXShift;
   private float imgYShift;
   private int r1;
   private int g1;
   private int b1;
   private int alpha;
   private int alpha1;
   private int whiteAlpha = 0;
   private float buttonZ = 0.0F;

   public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Color color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.alpha = color.getAlpha();
      this.image = null;
      this.hoveredImage = null;
      this.r1 = color.getRed();
      this.g1 = color.getGreen();
      this.b1 = color.getBlue();
      this.alpha1 = color.getAlpha();
      this.imgScale = (float)this.height / 2.6F;
   }

   public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation image, Color color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.alpha = color.getAlpha();
      this.image = image;
      this.hoveredImage = null;
      this.r1 = color.getRed();
      this.g1 = color.getGreen();
      this.b1 = color.getBlue();
      this.alpha1 = color.getAlpha();
      this.imgScale = (float)this.height / 2.6F;
      this.imgXShift = 0.0F;
      this.imgYShift = 0.0F;
   }

   public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation image, float imgXShift, float imgYShift, float imgScale, Color color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.alpha = color.getAlpha();
      this.image = image;
      this.hoveredImage = null;
      this.r1 = color.getRed();
      this.g1 = color.getGreen();
      this.b1 = color.getBlue();
      this.alpha1 = color.getAlpha();
      this.imgScale = imgScale;
      this.imgXShift = imgXShift;
      this.imgYShift = imgYShift;
   }

   public ClientButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation image, ResourceLocation hoveredImage, float imgXShift, float imgYShift, float imgScale, Color color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.alpha = color.getAlpha();
      this.image = image;
      this.hoveredImage = hoveredImage;
      this.r1 = color.getRed();
      this.g1 = color.getGreen();
      this.b1 = color.getBlue();
      this.alpha1 = color.getAlpha();
      this.imgScale = imgScale;
      this.imgXShift = imgXShift;
      this.imgYShift = imgYShift;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      CFontRenderer font = FontLoaders.CNMD18;
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.blendFunc(770, 771);
      if (this.hovered) {
         if (this.alpha1 > 15) {
            this.alpha1 -= 15;
         }

         if (this.whiteAlpha < 130) {
            this.whiteAlpha += 15;
         }

         if (this.buttonZ < 1.0F) {
            this.buttonZ += 0.2F;
         }
      }

      if (!this.hovered) {
         if (this.alpha1 <= this.alpha) {
            this.alpha1 += 15;
         }

         if (this.whiteAlpha > 0) {
            this.whiteAlpha -= 15;
         }

         if (this.buttonZ > 0.0F) {
            this.buttonZ -= 0.2F;
         }
      }

      if (this.hovered && Mouse.isButtonDown(0)) {
         RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8F, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8F, 2.0F, (new Color(this.r1, this.g1, this.b1, this.alpha1)).getRGB());
         RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8F, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8F, 2.0F, (new Color(140, 140, 140, this.whiteAlpha)).getRGB());
      } else {
         if (this.alpha != 0) {
            RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8F, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8F, 2.0F, (new Color(this.r1, this.g1, this.b1, this.alpha1)).getRGB());
         }

         RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8F, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8F, 2.0F, (new Color(222, 222, 222, this.whiteAlpha)).getRGB());
      }

      if (this.image != null) {
         RenderUtil.drawImage(this.image, (float)this.xPosition + (float)this.height / 3.0F + this.imgXShift, (float)this.yPosition + (float)this.height / 3.0F + this.imgYShift, this.imgScale, this.imgScale);
      }

      if (this.hoveredImage != null && this.hovered) {
         RenderUtil.drawImage(this.hoveredImage, (float)this.xPosition + (float)this.height / 3.0F + this.imgXShift, (float)this.yPosition + (float)this.height / 3.0F + this.imgYShift, this.imgScale, this.imgScale);
      }

      GL11.glColor3f(2.55F, 2.55F, 2.55F);
      this.mouseDragged(mc, mouseX, mouseY);
      GL11.glPushMatrix();
      GL11.glPushAttrib(1048575);
      GL11.glScaled(1.0, 1.0, 1.0);
      font.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + this.height / 2) - 2.7F, this.whiteAlpha > 60 ? Color.DARK_GRAY.getRGB() : -1);
      GL11.glPopAttrib();
      GL11.glPopMatrix();
   }
}
