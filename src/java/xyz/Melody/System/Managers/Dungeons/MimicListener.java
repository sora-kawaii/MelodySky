/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Dungeons;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.modules.QOL.Dungeons.SayMimicKilled;

public final class MimicListener {
    private List<Entity> possibleMimic = new ArrayList<Entity>();
    private Minecraft mc = Minecraft.getMinecraft();

    public void init() {
        MinecraftForge.EVENT_BUS.register(new MimicListener());
        EventBus.getInstance().register(this);
    }

    @EventHandler
    public void onEntityUpdate(EventTick event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!Client.inDungeons) {
            return;
        }
        if (Client.instance.dungeonUtils.foundMimic) {
            return;
        }
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (!entity.hasCustomName() || !entity.getCustomNameTag().contains("Mimic")) continue;
            this.possibleMimic = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(0.0, 1.0, 0.0), e -> !(e instanceof EntityArmorStand) && e instanceof EntityZombie && ((EntityZombie)e).isChild());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (Client.inDungeons && !Client.instance.dungeonUtils.foundMimic && !this.possibleMimic.isEmpty() && event.entity == this.possibleMimic.get(0)) {
            SayMimicKilled smk = (SayMimicKilled)Client.instance.getModuleManager().getModuleByClass(SayMimicKilled.class);
            if (smk.isEnabled()) {
                if (smk.mode.getValue() == SayMimicKilled.Chats.All) {
                    this.mc.thePlayer.sendChatMessage("/ac Mimic Killed.");
                }
                if (smk.mode.getValue() == SayMimicKilled.Chats.Party) {
                    this.mc.thePlayer.sendChatMessage("/pc Mimic Killed.");
                }
            }
            Client.instance.dungeonUtils.foundMimic = true;
        }
    }
}

