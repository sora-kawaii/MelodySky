package xyz.Melody.Event.events.world;

import xyz.Melody.Event.Event;

public class EventPreUpdate extends Event {
   private float yaw;
   private float pitch;
   public double x;
   public double y;
   public double z;
   private boolean ground;

   public EventPreUpdate(float yaw, float pitch, double x, double y, double z, boolean ground) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.x = x;
      this.y = y;
      this.z = z;
      this.ground = ground;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public boolean isOnground() {
      return this.ground;
   }

   public void setOnground(boolean ground) {
      this.ground = ground;
   }
}
