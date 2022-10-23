package xyz.Melody.GUI.Click;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.module.Module;

public class KeyBindButton extends ValueButton {
   public Module cheat;
   public double opacity = 0.0;
   public boolean bind;
   CFontRenderer font;

   public KeyBindButton(Module cheat, int x, int y) {
      super((Value)null, x, y);
      this.font = FontLoaders.NMSL18;
      this.custom = true;
      this.bind = false;
      this.cheat = cheat;
   }

   public void render(int mouseX, int mouseY) {
      Gui.drawRect(0, 0, 0, 0, 0);
      Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, (new Color(220, 220, 220)).getRGB());
      this.mfont.drawString("Bind", (float)(this.x - 5), (float)(this.y + 2), (new Color(108, 108, 108)).getRGB());
      this.mfont.drawString((this.bind ? "" : "") + Keyboard.getKeyName(this.cheat.getKey()), (float)(this.x + 76 - this.mfont.getStringWidth(Keyboard.getKeyName(this.cheat.getKey()))), (float)(this.y + 2), (new Color(108, 108, 108)).getRGB());
   }

   public void key(char typedChar, int keyCode) {
      if (this.bind) {
         this.cheat.setKey(keyCode);
         ClickUi.binding = false;
         this.bind = false;
      }

      super.key(typedChar, keyCode);
   }

   public void click(int mouseX, int mouseY, int button) {
      if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.font.getStringHeight(this.cheat.getName()) + 5 && button == 0) {
         boolean b = !this.bind;
         this.bind = b;
         ClickUi.binding = b;
      }

      if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.font.getStringHeight(this.cheat.getName()) + 5 && button == 1) {
         this.cheat.setKey(0);
      }

      super.click(mouseX, mouseY, button);
   }
}
