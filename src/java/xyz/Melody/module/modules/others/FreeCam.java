/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import java.awt.Color;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventPacketSend;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.injection.mixins.packets.C03Accessor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class FreeCam
extends Module {
    private EntityOtherPlayerMP playerEntity;
    public Numbers<Double> speed = new Numbers<Double>("Speed", 3.0, 0.1, 5.0, 0.1);
    public Option<Boolean> tracer = new Option<Boolean>("Tracer", false);
    private double x;
    private double y;
    private double z = 0.0;

    public FreeCam() {
        super("FreeCam", new String[]{"fc", "fcm", "freecam"}, ModuleType.Others);
        this.setModInfo("Specter Mode in 1.8.");
        this.addValues(this.speed, this.tracer);
    }

    @Override
    public void onEnable() {
        if (!this.mc.thePlayer.onGround) {
            Helper.sendMessage("[WARNING] FreeCam can only be used on Ground.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.thePlayer.onGround) {
            this.playerEntity = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
            this.playerEntity.copyLocationAndAnglesFrom(this.mc.thePlayer);
            this.playerEntity.onGround = this.mc.thePlayer.onGround;
            this.x = this.mc.thePlayer.posX;
            this.y = this.mc.thePlayer.posY;
            this.z = this.mc.thePlayer.posZ;
            this.mc.theWorld.addEntityToWorld(-114810, this.playerEntity);
        }
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || this.playerEntity == null) {
            return;
        }
        this.mc.thePlayer.noClip = false;
        this.mc.thePlayer.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
        this.mc.theWorld.removeEntityFromWorld(-114810);
        this.playerEntity = null;
        this.mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.fallDistance = 0.0f;
        this.mc.thePlayer.onGround = false;
        this.mc.thePlayer.capabilities.isFlying = false;
        this.mc.thePlayer.motionY = 0.0;
        if (!this.isMoving()) {
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.motionX = 0.0;
        }
        double d = (Double)this.speed.getValue() * 0.25;
        this.mc.thePlayer.jumpMovementFactor = (float)d;
        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.mc.thePlayer.motionY += d * 3.0;
        }
        if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
            this.mc.thePlayer.motionY -= d * 3.0;
        }
    }

    @EventHandler
    public void onRenderWorld(EventRender3D eventRender3D) {
        if (!this.mc.thePlayer.onGround) {
            return;
        }
        if (this.playerEntity != null && ((Boolean)this.tracer.getValue()).booleanValue()) {
            EntityOtherPlayerMP entityOtherPlayerMP = this.playerEntity;
            double d = entityOtherPlayerMP.lastTickPosX + (entityOtherPlayerMP.posX - entityOtherPlayerMP.lastTickPosX) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double d2 = entityOtherPlayerMP.lastTickPosY + (entityOtherPlayerMP.posY - entityOtherPlayerMP.lastTickPosY) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double d3 = entityOtherPlayerMP.lastTickPosZ + (entityOtherPlayerMP.posZ - entityOtherPlayerMP.lastTickPosZ) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            this.drawLine(this.playerEntity, new Color(255, 255, 255), d, d2, d3);
            RenderUtil.drawFilledESP(this.playerEntity, new Color(255, 255, 255), eventRender3D);
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load load) {
        this.setEnabled(false);
    }

    @EventHandler
    public void onPacketRCV(EventPacketRecieve eventPacketRecieve) {
        if (eventPacketRecieve.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)eventPacketRecieve.getPacket();
            this.x = s08PacketPlayerPosLook.getX();
            this.y = s08PacketPlayerPosLook.getY();
            this.z = s08PacketPlayerPosLook.getZ();
            this.playerEntity.setPosition(this.x, this.y, this.z);
        }
    }

    @EventHandler
    public void onPacketSend(EventPacketSend eventPacketSend) {
        if (eventPacketSend.getPacket() instanceof C03PacketPlayer) {
            ((C03Accessor)((Object)eventPacketSend.getPacket())).setOnGround(true);
            ((C03Accessor)((Object)eventPacketSend.getPacket())).setX(this.x);
            ((C03Accessor)((Object)eventPacketSend.getPacket())).setY(this.y);
            ((C03Accessor)((Object)eventPacketSend.getPacket())).setZ(this.z);
        }
    }

    public boolean isMoving() {
        return this.mc.thePlayer != null && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f);
    }

    private void drawLine(Entity entity, Color color, double d, double d2, double d3) {
        float f = this.mc.thePlayer.getDistanceToEntity(entity);
        float f2 = f / 48.0f;
        if (f2 >= 1.0f) {
            f2 = 1.0f;
        }
        GlStateManager.resetColor();
        GL11.glEnable(2848);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.resetColor();
    }
}

