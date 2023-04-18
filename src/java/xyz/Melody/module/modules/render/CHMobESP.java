/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.render.gl.GLUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CHMobESP
extends Module {
    private ArrayList<Entity> entities = new ArrayList();
    private int ticks = 0;

    public CHMobESP() {
        super("CHMobESP", new String[]{"chme"}, ModuleType.Render);
        this.setModInfo("RenderMob ESP & Tags in Crystall Hollows.");
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        if (this.entities.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.entities.size(); ++i) {
            Entity entity = this.entities.get(i);
            if (entity instanceof EntityGolem) {
                this.renderTag(entity, "Automation", eventRender3D.getPartialTicks());
            }
            if (entity instanceof EntitySlime && !(entity instanceof EntityMagmaCube)) {
                this.renderTag(entity, "Sludge", eventRender3D.getPartialTicks());
            }
            if (entity instanceof EntityMagmaCube) {
                this.renderTag(entity, "Yog", eventRender3D.getPartialTicks());
            }
            if (entity instanceof EntityPlayer) {
                String string = entity.getName().toLowerCase();
                if (string.contains("team treasurite")) {
                    this.renderTag(entity, "Team Treasurite", eventRender3D.getPartialTicks());
                }
                if (string.contains("goblin") || string.contains("weaklin")) {
                    this.renderTag(entity, "Goblin", eventRender3D.getPartialTicks());
                }
            }
            if (entity instanceof EntityZombie) {
                this.renderTag(entity, "Zombie", eventRender3D.getPartialTicks());
            }
            if (!(entity instanceof EntityEndermite)) continue;
            this.renderTag(entity, "Endermite", eventRender3D.getPartialTicks());
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (!Client.inSkyblock || Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.entities.clear();
            return;
        }
        if (this.ticks < 20) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (entity == null || !entity.isEntityAlive() || !(entity instanceof EntityLivingBase) || entity.getName() == null || entity.isInvisible()) continue;
            arrayList.add(entity);
        }
        this.entities.clear();
        this.entities.addAll(arrayList);
    }

    @Override
    public void onDisable() {
        this.entities.clear();
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        this.entities.clear();
    }

    private void renderTag(Entity entity, String string, float f) {
        float f2 = MathUtil.distanceToEntity(this.mc.thePlayer, entity) / 8.0f;
        if (f2 < 1.1f) {
            f2 = 1.1f;
        }
        float f3 = f2 * 1.8f;
        f3 /= 100.0f;
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f - this.mc.getRenderManager().viewerPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f - this.mc.getRenderManager().viewerPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f - this.mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslated(d, d2, d3);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
        GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
        GL11.glScalef(-f3, -f3, f3);
        GLUtil.setGLCap(2929, false);
        string = string + "[" + (int)MathUtil.distanceToEntity(this.mc.thePlayer, entity) + "m]";
        float f4 = (float)(-this.mc.fontRendererObj.getStringWidth(string) / 2) - 4.6f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.fontRendererObj.drawString(string, (int)f4 + 4, -20, -1);
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }
}

