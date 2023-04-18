/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.value.Option;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class SoundsHider
extends Module {
    private Option<Boolean> emanHurt = new Option<Boolean>("Enderman Hurt", false);
    private Option<Boolean> emanDie = new Option<Boolean>("Enderman Die", true);
    private Option<Boolean> emanAnger = new Option<Boolean>("Enderman Anger", true);
    private Option<Boolean> abcd = new Option<Boolean>("Ability Cooldown", true);
    private Option<Boolean> jerryChine = new Option<Boolean>("Jerry-ChineGun", false);
    private Option<Boolean> explosions = new Option<Boolean>("Explosions", false);

    public SoundsHider() {
        super("SoundsHider", new String[]{"nbt"}, ModuleType.QOL);
        this.addValues(this.emanHurt, this.emanAnger, this.emanDie, this.abcd, this.jerryChine, this.explosions);
        this.setModInfo("Hide Fuckin Sounds.");
    }

    @EventHandler
    private void onPacketRCV(EventPacketRecieve eventPacketRecieve) {
        Packet<?> packet = eventPacketRecieve.getPacket();
        if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect s29PacketSoundEffect = (S29PacketSoundEffect)packet;
            if (((Boolean)this.emanHurt.getValue()).booleanValue() && s29PacketSoundEffect.getSoundName().contains("mob.endermen.hit")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (((Boolean)this.emanDie.getValue()).booleanValue() && s29PacketSoundEffect.getSoundName().contains("mob.endermen.death")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (((Boolean)this.emanAnger.getValue()).booleanValue() && (s29PacketSoundEffect.getSoundName().contains("mob.endermen.scream") || s29PacketSoundEffect.getSoundName().contains("mob.endermen.stare"))) {
                eventPacketRecieve.setCancelled(true);
            }
            if (((Boolean)this.explosions.getValue()).booleanValue() && s29PacketSoundEffect.getSoundName().contains("random.explode")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (((Boolean)this.jerryChine.getValue()).booleanValue()) {
                if (s29PacketSoundEffect.getSoundName().contains("mob.villager.yes") && s29PacketSoundEffect.getVolume() == 0.35f) {
                    eventPacketRecieve.setCancelled(true);
                }
                if (s29PacketSoundEffect.getSoundName().contains("mob.villager.haggle") && s29PacketSoundEffect.getVolume() == 0.5f) {
                    eventPacketRecieve.setCancelled(true);
                }
            }
            if (((Boolean)this.abcd.getValue()).booleanValue() && s29PacketSoundEffect.getSoundName().contains("mob.endermen.portal") && s29PacketSoundEffect.getPitch() == 0.0f && s29PacketSoundEffect.getVolume() == 8.0f) {
                eventPacketRecieve.setCancelled(true);
            }
        }
    }
}

