package xyz.Melody.Utils.animate;

public abstract class Animation {
   protected int time;
   protected boolean enabling;

   public void update() {
      if (this.enabling) {
         ++this.time;
      } else {
         --this.time;
      }

      if (this.time < 0) {
         this.time = 0;
      }

      if (this.time > this.getMaxTime()) {
         this.time = this.getMaxTime();
      }

   }

   public void reset() {
      this.time = 0;
   }

   public int getMaxTime() {
      return 10;
   }

   public int getTime() {
      return this.time;
   }

   public void on() {
      this.enabling = true;
   }

   public void toggle() {
      this.enabling = !this.enabling;
   }

   public void toggle(boolean b) {
      this.enabling = b;
   }

   public void off() {
      this.enabling = false;
   }

   public boolean isEnabled() {
      return this.enabling;
   }

   public abstract void render();
}
