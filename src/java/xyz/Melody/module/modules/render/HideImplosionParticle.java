/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class HideImplosionParticle
extends Module {
    public HideImplosionParticle() {
        super("ImplosionParticle", new String[]{"implosionparticle"}, ModuleType.Render);
        this.setModInfo("Hide Implosion(Wither Impact) Particles.");
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @EventHandler
    private void onPacketRCV(EventPacketRecieve eventPacketRecieve) {
        if (eventPacketRecieve.getPacket() instanceof S2APacketParticles) {
            S2APacketParticles s2APacketParticles = (S2APacketParticles)eventPacketRecieve.getPacket();
            boolean bl = s2APacketParticles.isLongDistance();
            float f = s2APacketParticles.getParticleSpeed();
            int n = s2APacketParticles.getParticleCount();
            float f2 = s2APacketParticles.getXOffset();
            float f3 = s2APacketParticles.getYOffset();
            float f4 = s2APacketParticles.getZOffset();
            EnumParticleTypes enumParticleTypes = s2APacketParticles.getParticleType();
            if (enumParticleTypes == EnumParticleTypes.EXPLOSION_LARGE && bl && f == 8.0f && n == 8 && f2 == 0.0f && f3 == 0.0f && f4 == 0.0f) {
                eventPacketRecieve.setCancelled(true);
            }
        }
    }
}

