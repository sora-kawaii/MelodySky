/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class HideSummonings
extends Module {
    private final ArrayList<String> summonItemIDs = new ArrayList<String>(Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET", "ROTTEN_HELMET", "SKELETON_SOLDIER_HELMET"));

    public HideSummonings() {
        super("HideSummonings", new String[]{"hidemob"}, ModuleType.QOL);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Hide Summoning Mobs.");
    }

    public boolean isSummon(Entity entity) {
        if (entity instanceof EntityPlayer) {
            String string = entity.getName();
            return string.equals("Lost Adventurer") || string.equals("Frozen Adventurer") || string.equals("Shadow Assassin") || string.equals("Shadow Assassin") || string.equals("Angry Archaeologist") || string.equals("King Midas") || string.equals("Redstone Warrior") || string.equals("Crypt Dreadlord") || string.equals("Crypt Soulstealer");
        }
        if (entity instanceof EntityZombie || entity instanceof EntitySkeleton) {
            for (int i = 0; i < 5; ++i) {
                ItemStack itemStack = ((EntityMob)entity).getEquipmentInSlot(i);
                if (!this.summonItemIDs.contains(ItemUtils.getSkyBlockID(itemStack))) continue;
                return true;
            }
        }
        return false;
    }

    @EventHandler
    private void onUpdate(EventTick eventTick) {
        if (Client.inDungeons || !Client.inSkyblock) {
            return;
        }
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (!this.isSummon(entity)) continue;
            this.mc.theWorld.removeEntity(entity);
        }
    }
}

