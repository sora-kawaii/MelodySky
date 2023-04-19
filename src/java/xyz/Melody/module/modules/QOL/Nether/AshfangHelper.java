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
    public void onRenderEntity(EventPreUpdate event) {
        for (Entity ent : this.mc.theWorld.loadedEntityList) {
            if (!(ent instanceof EntityArmorStand)) continue;
            EntityArmorStand entity = (EntityArmorStand)ent;
            if (!entity.hasCustomName()) {
                return;
            }
            String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
            if (!entityName.equals("Blazing Soul")) continue;
            this.onRenderOrb(ent, event);
            return;
        }
    }

    public boolean onRenderOrb(Entity entityToInteract, EventPreUpdate event) {
        Entity ashfang = null;
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityArmorStand) || !entity.hasCustomName() || !StringUtils.stripControlCodes(entity.getCustomNameTag()).contains("Lv200] Ashfang")) continue;
            ashfang = entity;
            break;
        }
        if (ashfang != null) {
            float[] rotations2 = RotationUtil.getRotations(ashfang);
            event.setYaw(rotations2[0]);
            event.setPitch(rotations2[1]);
            if (this.timer.hasReached(100.0)) {
                this.mc.playerController.attackEntity(this.mc.thePlayer, entityToInteract);
                this.timer.reset();
                return true;
            }
        }
        return false;
    }
}

