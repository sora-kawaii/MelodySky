/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.FMLModules.Utils;

public final class HealthData {
    private String name;
    private int health;
    private int maxHealth;
    private boolean attackable;

    public HealthData(String string, int n, int n2, boolean bl) {
        this.name = string;
        this.health = n;
        this.maxHealth = n2;
        this.attackable = bl;
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

