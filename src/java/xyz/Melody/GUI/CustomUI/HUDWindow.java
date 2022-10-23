package xyz.Melody.GUI.CustomUI;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import xyz.Melody.Utils.render.RenderUtil;

public class HUDWindow {
   public HUDApi api;
   public boolean drag;
   public int x;
   public int y;
   public int dragX;
   public int dragY;

   public HUDWindow(HUDApi api, int x2, int y2) {
      this.api = api;
      this.x = x2;
      this.y = y2;
   }

   public void render(int mouseX, int mouseY) {
      if (this.api.isEnabled()) {
         RenderUtil.drawFilledCircle(this.api.x - 4, this.api.y - 4, 4.0F, new Color(0, 177, 35));
      } else {
         RenderUtil.drawFilledCircle(this.api.x - 4, this.api.y - 4, 4.0F, new Color(45, 49, 45));
      }

      if (this.drag) {
         if (!Mouse.isButtonDown(0)) {
            this.drag = false;
         }

         this.x = mouseX - this.dragX;
         this.y = mouseY - this.dragY;
         this.api.setXY(this.x, this.y);
      }

   }

   public void mouseScroll(int mouseX, int mouseY, int amount) {
   }

   public void click(int mouseX, int mouseY, int button) {
      if (mouseX > this.api.x - 8 && mouseX < this.api.x && mouseY > this.api.y - 8 && mouseY < this.api.y) {
         if (button == 0) {
            this.drag = true;
            this.dragX = mouseX - this.x;
            this.dragY = mouseY - this.y;
         }

         if (button == 1) {
            this.api.setEnabled(!this.api.isEnabled());
         }
      }

   }
}
