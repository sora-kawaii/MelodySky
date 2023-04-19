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
    private void on3D(EventRender3D event) {
        if (this.entities.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.entities.size(); ++i) {
            Entity e = this.entities.get(i);
            if (e instanceof EntityGolem) {
                this.renderTag(e, "Automation", event.getPartialTicks());
            }
            if (e instanceof EntitySlime && !(e instanceof EntityMagmaCube)) {
                this.renderTag(e, "Sludge", event.getPartialTicks());
            }
            if (e instanceof EntityMagmaCube) {
                this.renderTag(e, "Yog", event.getPartialTicks());
            }
            if (e instanceof EntityPlayer) {
                String name = e.getName().toLowerCase();
                if (name.contains("team treasurite")) {
                    this.renderTag(e, "Team Treasurite", event.getPartialTicks());
                }
                if (name.contains("goblin") || name.contains("weaklin")) {
                    this.renderTag(e, "Goblin", event.getPartialTicks());
                }
            }
            if (e instanceof EntityZombie) {
                this.renderTag(e, "Zombie", event.getPartialTicks());
            }
            if (!(e instanceof EntityEndermite)) continue;
            this.renderTag(e, "Endermite", event.getPartialTicks());
        }
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (!Client.inSkyblock || Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.entities.clear();
            return;
        }
        if (this.ticks < 20) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        ArrayList<Entity> es = new ArrayList<Entity>();
        for (Entity e : this.mc.theWorld.loadedEntityList) {
            if (e == null || !e.isEntityAlive() || !(e instanceof EntityLivingBase) || e.getName() == null || e.isInvisible()) continue;
            es.add(e);
        }
        this.entities.clear();
        this.entities.addAll(es);
    }

    @Override
    public void onDisable() {
        this.entities.clear();
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        this.entities.clear();
    }

    private void renderTag(Entity e, String name, float f) {
        float size = MathUtil.distanceToEntity(this.mc.thePlayer, e) / 8.0f;
        if (size < 1.1f) {
            size = 1.1f;
        }
        float scale = size * 1.8f;
        scale /= 100.0f;
        double pX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)f - this.mc.getRenderManager().viewerPosX;
        double pY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)f - this.mc.getRenderManager().viewerPosY;
        double pZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)f - this.mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslated(pX, pY, pZ);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
        GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GLUtil.setGLCap(2929, false);
        name = name + "[" + (int)MathUtil.distanceToEntity(this.mc.thePlayer, e) + "m]";
        float nw = (float)(-this.mc.fontRendererObj.getStringWidth(name) / 2) - 4.6f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.fontRendererObj.drawString(name, (int)nw + 4, -20, -1);
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }
}

