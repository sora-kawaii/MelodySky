/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Nether;

import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AshfangHelper
extends Module {
    private TimerUtil timer = new TimerUtil();

    public AshfangHelper() {
        super("AshfangHelper", new String[]{"mbe"}, ModuleType.Nether);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Auto Shoot Orbs to Ashfang.");
    }

    @EventHandler
    public void onRenderEntity(EventPreUpdate eventPreUpdate) {
        for (Entity entity : this.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityArmorStand)) continue;
            EntityArmorStand entityArmorStand = (EntityArmorStand)entity;
            if (!entityArmorStand.func_145818_k_()) {
                return;
            }
            String string = StringUtils.func_76338_a((String)entityArmorStand.func_95999_t());
            if (!string.equals("Blazing Soul")) continue;
            this.onRenderOrb(entity, eventPreUpdate);
            return;
        }
    }

    public boolean onRenderOrb(Entity entity, EventPreUpdate eventPreUpdate) {
        Entity entity2 = null;
        for (Entity entity3 : this.mc.field_71441_e.field_72996_f) {
            if (!(entity3 instanceof EntityArmorStand) || !entity3.func_145818_k_() || !StringUtils.func_76338_a((String)entity3.func_95999_t()).contains("Lv200] Ashfang")) continue;
            entity2 = entity3;
            break;
        }
        if (entity2 != null) {
            Object object = RotationUtil.getRotations(entity2);
            eventPreUpdate.setYaw((float)object[0]);
            eventPreUpdate.setPitch((float)object[1]);
            if (this.timer.hasReached(100.0)) {
                this.mc.field_71442_b.func_78764_a(this.mc.field_71439_g, entity);
                this.timer.reset();
                return true;
            }
        }
        return false;
    }
}

