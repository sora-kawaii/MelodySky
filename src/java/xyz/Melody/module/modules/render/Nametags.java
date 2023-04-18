/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import java.util.regex.Pattern;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.gl.GLUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public final class Nametags
extends Module {
    private float renderHpWidth;
    private static String lol;
    private final TimerUtil animationStopwatch = new TimerUtil();
    Option<Boolean> mcplayer = new Option<Boolean>("ShowYou", true);

    public Nametags() {
        super("Nametags", new String[]{"tags"}, ModuleType.Render);
        this.setColor(new Color(29, 187, 102).getRGB());
        this.addValues(this.mcplayer);
        this.setModInfo("Name Tag.");
    }

    @EventHandler
    public void onRender(EventRender3D eventRender3D) {
        Pattern pattern = Pattern.compile("(?i)\ufffd\ufffd[0-9A-FK-OR]");
        for (Object e : this.mc.theWorld.playerEntities) {
            EntityPlayer entityPlayer = (EntityPlayer)e;
            if (!((Boolean)this.mcplayer.getValue() != false ? entityPlayer.isEntityAlive() : entityPlayer.isEntityAlive() && entityPlayer != this.mc.thePlayer)) continue;
            double d = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double d2 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double d3 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)eventRender3D.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            this.renderNameTag(entityPlayer, pattern.matcher(entityPlayer.getDisplayName().getUnformattedText()).replaceAll(""), d, d2, d3);
        }
    }

    private void renderNameTag(EntityPlayer entityPlayer, String string, double d, double d2, double d3) {
        if (!entityPlayer.isInvisible()) {
            if (entityPlayer.getName().toUpperCase().contains("CRYPT DREADLORD") || entityPlayer.getName().toUpperCase().contains("DECOY") || entityPlayer.getName().toUpperCase().contains("ZOMBIE COMMANDER") || entityPlayer.getName().toUpperCase().contains("SKELETOR PRIME") || entityPlayer.getName().toUpperCase().contains("CRYPT SOULEATER") || ((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer)) {
                return;
            }
            float f = this.mc.thePlayer.getDistanceToEntity(entityPlayer) / 10.0f;
            if (f < 1.1f) {
                f = 1.1f;
            }
            d2 += entityPlayer.isSneaking() ? 0.5 : 0.7;
            float f2 = f * 1.8f;
            f2 /= 100.0f;
            String string2 = "";
            if (FriendManager.isFriend(entityPlayer.getName())) {
                string2 = (Object)((Object)EnumChatFormatting.DARK_PURPLE) + "[Friend]";
            }
            lol = string2 + (Object)((Object)EnumChatFormatting.WHITE) + string;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef((float)d, (float)d2 + 1.4f, (float)d3);
            GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
            GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
            GL11.glScalef(-f2, -f2, f2);
            GLUtil.setGLCap(2929, false);
            GLUtil.setGLCap(3042, true);
            float f3 = (float)(-this.mc.fontRendererObj.getStringWidth(lol) / 2) - 4.6f;
            float f4 = f3 - 2.0f * f3;
            float f5 = entityPlayer.getHealth();
            float f6 = f5 / entityPlayer.getMaxHealth();
            f6 = (float)MathHelper.clamp_double(f6, 0.0, 1.0);
            float f7 = f3 - 2.0f * f3 * f6;
            if (this.animationStopwatch.hasReached(5.0)) {
                this.renderHpWidth = (float)AnimationUtil.animate(f7, this.renderHpWidth, 0.053f);
                this.animationStopwatch.reset();
            }
            float f8 = -17.0f;
            RenderUtil.drawFastRoundedRect(f3, f8, f4, -0.1f, 1.0f, new Color(25, 25, 25, 101).getRGB());
            RenderUtil.drawFastRoundedRect(f3, -2.0f, f4, -0.1f, 0.0f, new Color(152, 171, 195).getRGB());
            RenderUtil.drawFastRoundedRect(f3, -2.0f, this.renderHpWidth, -0.1f, 0.0f, new Color(123, 104, 238).getRGB());
            this.mc.fontRendererObj.drawString(lol, (int)(f3 + 4.0f), -13, -1);
            this.renderHead(entityPlayer, -19.0f, -55.0f);
            GLUtil.revertAllCaps();
            GL11.glPopMatrix();
            GlStateManager.resetColor();
        }
    }

    private void renderHead(EntityPlayer entityPlayer, float f, float f2) {
        ResourceLocation resourceLocation = ((AbstractClientPlayer)entityPlayer).getLocationSkin();
        this.mc.getTextureManager().bindTexture(resourceLocation);
        Gui.drawScaledCustomSizeModalRect((int)(f + 3.0f), (int)((double)f2 + 3.5), 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
    }
}

