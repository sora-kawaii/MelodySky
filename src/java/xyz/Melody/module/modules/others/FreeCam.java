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
        if (!this.mc.field_71439_g.field_70122_E) {
            Helper.sendMessage("[WARNING] FreeCam can only be used on Ground.");
            this.setEnabled(false);
            return;
        }
        if (this.mc.field_71439_g.field_70122_E) {
            this.playerEntity = new EntityOtherPlayerMP(this.mc.field_71441_e, this.mc.field_71439_g.func_146103_bH());
            this.playerEntity.func_82149_j(this.mc.field_71439_g);
            this.playerEntity.field_70122_E = this.mc.field_71439_g.field_70122_E;
            this.x = this.mc.field_71439_g.field_70165_t;
            this.y = this.mc.field_71439_g.field_70163_u;
            this.z = this.mc.field_71439_g.field_70161_v;
            this.mc.field_71441_e.func_73027_a(-114810, this.playerEntity);
        }
    }

    @Override
    public void onDisable() {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || this.playerEntity == null) {
            return;
        }
        this.mc.field_71439_g.field_70145_X = false;
        this.mc.field_71439_g.func_70107_b(this.playerEntity.field_70165_t, this.playerEntity.field_70163_u, this.playerEntity.field_70161_v);
        this.mc.field_71441_e.func_73028_b(-114810);
        this.playerEntity = null;
        this.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        this.mc.field_71439_g.field_70145_X = true;
        this.mc.field_71439_g.field_70143_R = 0.0f;
        this.mc.field_71439_g.field_70122_E = false;
        this.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
        this.mc.field_71439_g.field_70181_x = 0.0;
        if (!this.isMoving()) {
            this.mc.field_71439_g.field_70179_y = 0.0;
            this.mc.field_71439_g.field_70159_w = 0.0;
        }
        double d = (Double)this.speed.getValue() * 0.25;
        this.mc.field_71439_g.field_70747_aH = (float)d;
        if (this.mc.field_71474_y.field_74314_A.func_151470_d()) {
            this.mc.field_71439_g.field_70181_x += d * 3.0;
        }
        if (this.mc.field_71474_y.field_74311_E.func_151470_d()) {
            this.mc.field_71439_g.field_70181_x -= d * 3.0;
        }
    }

    @EventHandler
    public void onRenderWorld(EventRender3D eventRender3D) {
        if (!this.mc.field_71439_g.field_70122_E) {
            return;
        }
        if (this.playerEntity != null && ((Boolean)this.tracer.getValue()).booleanValue()) {
            EntityOtherPlayerMP entityOtherPlayerMP = this.playerEntity;
            double d = entityOtherPlayerMP.field_70142_S + (entityOtherPlayerMP.field_70165_t - entityOtherPlayerMP.field_70142_S) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78730_l;
            double d2 = entityOtherPlayerMP.field_70137_T + (entityOtherPlayerMP.field_70163_u - entityOtherPlayerMP.field_70137_T) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78731_m;
            double d3 = entityOtherPlayerMP.field_70136_U + (entityOtherPlayerMP.field_70161_v - entityOtherPlayerMP.field_70136_U) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78728_n;
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
            this.x = s08PacketPlayerPosLook.func_148932_c();
            this.y = s08PacketPlayerPosLook.func_148928_d();
            this.z = s08PacketPlayerPosLook.func_148933_e();
            this.playerEntity.func_70107_b(this.x, this.y, this.z);
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
        return this.mc.field_71439_g != null && (this.mc.field_71439_g.field_70701_bs != 0.0f || this.mc.field_71439_g.field_70702_br != 0.0f);
    }

    private void drawLine(Entity entity, Color color, double d, double d2, double d3) {
        float f = this.mc.field_71439_g.func_70032_d(entity);
        float f2 = f / 48.0f;
        if (f2 >= 1.0f) {
            f2 = 1.0f;
        }
        GlStateManager.func_179117_G();
        GL11.glEnable(2848);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.field_71439_g.func_70047_e(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.func_179117_G();
    }
}

