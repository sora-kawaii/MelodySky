package xyz.Melody.GUI.Click;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.System.Managers.ModuleManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.render.HUD;

public class Window {
   CFontRenderer font;
   public ModuleType category;
   public ArrayList buttons;
   public boolean drag;
   public boolean extended;
   public int x;
   public int y;
   public int expand;
   public int dragX;
   public int dragY;
   public int max;
   public int scroll;
   public int scrollTo;
   public double angel;
   private TimerUtil timer = new TimerUtil();

   public Window(ModuleType category, int x, int y) {
      this.font = FontLoaders.NMSL18;
      this.buttons = new ArrayList();
      this.category = category;
      this.x = x;
      this.y = y;
      this.max = 120;
      int y2 = y + 22;
      Iterator var5 = ModuleManager.getModules().iterator();

      while(var5.hasNext()) {
         Module c = (Module)var5.next();
         if (c.getType() == category && !(c instanceof HUD)) {
            this.buttons.add(new Button(c, x + 5, y2));
            y2 += 15;
         }
      }

      var5 = this.buttons.iterator();

      while(var5.hasNext()) {
         Button b2 = (Button)var5.next();
         b2.setParent(this);
      }

   }

   public void render(int mouseX, int mouseY) {
      int current = 0;

      for(Iterator var4 = this.buttons.iterator(); var4.hasNext(); current += 15) {
         Button b3 = (Button)var4.next();
         if (b3.expand) {
            for(Iterator var6 = b3.buttons.iterator(); var6.hasNext(); current += 15) {
               ValueButton v = (ValueButton)var6.next();
            }
         }
      }

      int height = 15 + current;
      if (this.extended) {
         this.expand = this.expand + 5 < height ? (this.expand += 5) : height;
         this.angel = this.angel + 20.0 < 180.0 ? (this.angel += 20.0) : 180.0;
      } else {
         this.expand = this.expand - 5 > 0 ? (this.expand -= 5) : 0;
         this.angel = this.angel - 20.0 > 0.0 ? (this.angel -= 20.0) : 0.0;
      }

      RenderUtil.drawFastRoundedRect((float)(this.x - 2), (float)this.y, (float)(this.x + 92), (float)(this.y + 17), 1.0F, (new Color(255, 255, 255)).getRGB());
      GlStateManager.resetColor();
      this.font.drawString(this.category.name(), (float)(this.x + 15), (float)(this.y + 6), (new Color(108, 108, 108)).getRGB());
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(this.x + 90 - 10), (float)(this.y + 5), 0.0F);
      GlStateManager.rotate((float)this.angel, 0.0F, 0.0F, -1.0F);
      GlStateManager.translate((float)(-this.x + 90 - 10), (float)(-this.y + 5), 0.0F);
      GlStateManager.popMatrix();
      if (this.expand > 0) {
         this.buttons.forEach((b2) -> {
            b2.render(mouseX, mouseY);
         });
      }

      if (this.drag) {
         if (!Mouse.isButtonDown(0)) {
            this.drag = false;
         }

         this.x = mouseX - this.dragX;
         this.y = mouseY - this.dragY;
         ((Button)this.buttons.get(0)).y = this.y + 22 - this.scroll;

         Button b4;
         for(Iterator var9 = this.buttons.iterator(); var9.hasNext(); b4.x = this.x + 5) {
            b4 = (Button)var9.next();
         }
      }

   }

   public void key(char typedChar, int keyCode) {
      this.buttons.forEach((b2) -> {
         b2.key(typedChar, keyCode);
      });
   }

   public void mouseScroll(int mouseX, int mouseY, int amount) {
      if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
         this.scrollTo -= (int)((float)(amount / 120 * 28));
      }

   }

   public void click(int mouseX, int mouseY, int button) {
      if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
         if (button == 1) {
            this.extended = !this.extended;
         }

         if (button == 0) {
            this.drag = true;
            this.dragX = mouseX - this.x;
            this.dragY = mouseY - this.y;
         }
      }

      if (this.extended) {
         this.buttons.stream().filter((b2) -> {
            return b2.y < this.y + this.expand;
         }).forEach((b2) -> {
            b2.click(mouseX, mouseY, button);
         });
      }

   }
}
