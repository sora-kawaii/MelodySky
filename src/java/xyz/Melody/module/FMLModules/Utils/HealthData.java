package xyz.Melody.module.FMLModules.Utils;

public class HealthData {
   private String name;
   private int health;
   private int maxHealth;
   private boolean attackable;

   public HealthData(String name, int health, int maxHP, boolean attackable) {
      this.name = name;
      this.health = health;
      this.maxHealth = maxHP;
      this.attackable = attackable;
   }

   public int getHealth() {
      return this.health;
   }

   public int getMaxHealth() {
      return this.maxHealth;
   }

   public String getName() {
      return this.name;
   }

   public boolean isAttackable() {
      return this.attackable;
   }
}
