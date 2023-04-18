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
    private Minecraft mc = Minecraft.func_71410_x();

    public void init() {
        MinecraftForge.EVENT_BUS.register(new MimicListener());
        EventBus.getInstance().register(this);
    }

    @EventHandler
    public void onEntityUpdate(EventTick eventTick) {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        if (!Client.inDungeons) {
            return;
        }
        if (Client.instance.dungeonUtils.foundMimic) {
            return;
        }
        for (Entity entity2 : this.mc.field_71441_e.field_72996_f) {
            if (!entity2.func_145818_k_() || !entity2.func_95999_t().contains("Mimic")) continue;
            this.possibleMimic = this.mc.field_71441_e.func_175674_a(entity2, entity2.func_174813_aQ().func_72314_b(0.0, 1.0, 0.0), entity -> !(entity instanceof EntityArmorStand) && entity instanceof EntityZombie && ((EntityZombie)entity).func_70631_g_());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent livingDeathEvent) {
        if (Client.inDungeons && !Client.instance.dungeonUtils.foundMimic && !this.possibleMimic.isEmpty() && livingDeathEvent.entity == this.possibleMimic.get(0)) {
            SayMimicKilled sayMimicKilled = (SayMimicKilled)Client.instance.getModuleManager().getModuleByClass(SayMimicKilled.class);
            if (sayMimicKilled.isEnabled()) {
                if (sayMimicKilled.mode.getValue() == SayMimicKilled.Chats.All) {
                    this.mc.field_71439_g.func_71165_d("/ac Mimic Killed.");
                }
                if (sayMimicKilled.mode.getValue() == SayMimicKilled.Chats.Party) {
                    this.mc.field_71439_g.func_71165_d("/pc Mimic Killed.");
                }
            }
            Client.instance.dungeonUtils.foundMimic = true;
        }
    }
}

