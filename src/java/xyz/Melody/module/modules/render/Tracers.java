/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public final class Tracers
extends Module {
    public Tracers() {
        super("Tracers", new String[]{"lines", "tracer"}, ModuleType.Render);
        this.setModInfo("Crosshair ----line---- Players.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private void on3DRender(EventRender3D e) {
        if (Client.inDungeons) {
            return;
        }
        for (Object o : this.mc.theWorld.loadedEntityList) {
            double[] arrd;
            Entity entity = (Entity)o;
            if (!entity.isEntityAlive() || !(entity instanceof EntityPlayer) || entity == this.mc.thePlayer || ((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entity) || entity instanceof EntityArmorStand) continue;
            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)e.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)e.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)e.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            RenderUtil.startDrawing();
            float color = (float)Math.round(255.0 - this.mc.thePlayer.getDistanceSqToEntity(entity) * 255.0 / MathUtil.sq((double)this.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
            if (FriendManager.isFriend(entity.getDisplayName().getUnformattedText())) {
                double[] arrd2 = new double[3];
                arrd2[0] = 0.0;
                arrd2[1] = 1.0;
                arrd = arrd2;
                arrd2[2] = 1.0;
            } else {
                double[] arrd3 = new double[3];
                arrd3[0] = color;
                arrd3[1] = 1.0f - color;
                arrd = arrd3;
                arrd3[2] = 0.0;
            }
            this.drawLine(entity, arrd, posX, posY, posZ);
            RenderUtil.stopDrawing();
        }
    }

    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
        float distance = this.mc.thePlayer.getDistanceToEntity(entity);
        float xD = distance / 48.0f;
        if (xD >= 1.0f) {
            xD = 1.0f;
        }
        GL11.glEnable(2848);
        if (color.length >= 4) {
            if (color[3] <= 0.1) {
                return;
            }
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(2848);
    }
}

