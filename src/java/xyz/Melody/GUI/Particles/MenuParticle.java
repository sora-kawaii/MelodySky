package xyz.Melody.GUI.Particles;

import java.util.ArrayList;
import java.util.Random;

public class MenuParticle {
   public double x;
   public double y;
   public double preX;
   public double preY;
   public double depth;
   public double motionX;
   public double motionY;
   public float alpha = 1.0F;
   public boolean remove = false;
   public boolean followMouse = false;
   public Random rand = new Random();
   public boolean alphaDecay = false;

   public MenuParticle(double x, double y, double depth, boolean followMouse) {
      this.x = x;
      this.y = y;
      this.depth = depth;
      this.followMouse = followMouse;
   }

   public void update(int mouseX, int mouseY, ArrayList particles) {
      if (this.followMouse) {
         float angle = (float)Math.toDegrees(Math.atan2((double)mouseY - this.y, (double)mouseX - this.x));
         if (angle < 0.0F) {
            angle += 360.0F;
         }

         double mX = Math.cos(Math.toRadians((double)angle));
         double mY = Math.sin(Math.toRadians((double)angle));
         this.motionX += mX * 1.0;
         this.motionY += mY * 1.0;
      }

      this.preX = this.x;
      this.preY = this.y;
      this.x += this.motionX;
      this.y += this.motionY;
      this.motionY *= 0.985;
      this.motionX *= 0.985;
      if (this.alphaDecay) {
         this.alpha *= 0.995F;
         this.motionX *= 1.1;
         this.motionY *= 1.1;
      }

   }

   public MenuParticle addMotion(double x, double y) {
      this.motionX += x;
      this.motionY += y;
      return this;
   }
}
