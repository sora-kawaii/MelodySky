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
    private void on3DRender(EventRender3D eventRender3D) {
        if (Client.inDungeons) {
            return;
        }
        for (Object e : this.mc.theWorld.loadedEntityList) {
            double[] dArray;
            double[] dArray2;
            Entity entity = (Entity)e;
            if (!entity.isEntityAlive() || !(entity instanceof EntityPlayer) || entity == this.mc.thePlayer || ((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entity) || entity instanceof EntityArmorStand) continue;
            double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            RenderUtil.startDrawing();
            float f = (float)Math.round(255.0 - this.mc.thePlayer.getDistanceSqToEntity(entity) * 255.0 / MathUtil.sq((double)this.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
            if (FriendManager.isFriend(entity.getDisplayName().getUnformattedText())) {
                dArray2 = new double[3];
                dArray2[0] = 0.0;
                dArray2[1] = 1.0;
                dArray = dArray2;
                dArray2[2] = 1.0;
            } else {
                dArray2 = new double[3];
                dArray2[0] = f;
                dArray2[1] = 1.0f - f;
                dArray = dArray2;
                dArray2[2] = 0.0;
            }
            this.drawLine(entity, dArray, d, d2, d3);
            RenderUtil.stopDrawing();
        }
    }

    private void drawLine(Entity entity, double[] dArray, double d, double d2, double d3) {
        float f = this.mc.thePlayer.getDistanceToEntity(entity);
        float f2 = f / 48.0f;
        if (f2 >= 1.0f) {
            f2 = 1.0f;
        }
        GL11.glEnable(2848);
        if (dArray.length >= 4) {
            if (dArray[3] <= 0.1) {
                return;
            }
            GL11.glColor4d(dArray[0], dArray[1], dArray[2], dArray[3]);
        } else {
            GL11.glColor3d(dArray[0], dArray[1], dArray[2]);
        }
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
    }
}

