package xyz.Melody.Event.events.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import xyz.Melody.Event.Event;

public class EventRenderEntityModel extends Event {
   private EntityLivingBase entity;
   private float limbSwing;
   private float limbSwingAmount;
   private float ageInTicks;
   private float headYaw;
   private float headPitch;
   private float scaleFactor;
   private ModelBase model;

   public EventRenderEntityModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, ModelBase model) {
      this.entity = entity;
      this.limbSwing = limbSwing;
      this.limbSwingAmount = limbSwingAmount;
      this.ageInTicks = ageInTicks;
      this.headYaw = headYaw;
      this.headPitch = headPitch;
      this.scaleFactor = scaleFactor;
      this.model = model;
   }

   public float getAgeInTicks() {
      return this.ageInTicks;
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }

   public float getHeadPitch() {
      return this.headPitch;
   }

   public float getHeadYaw() {
      return this.headYaw;
   }

   public float getLimbSwing() {
      return this.limbSwing;
   }

   public float getLimbSwingAmount() {
      return this.limbSwingAmount;
   }

   public ModelBase getModel() {
      return this.model;
   }

   public float getScaleFactor() {
      return this.scaleFactor;
   }
}
