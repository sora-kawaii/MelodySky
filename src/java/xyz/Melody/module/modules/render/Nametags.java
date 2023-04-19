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
    public void onRender(EventRender3D event) {
        Pattern COLOR_PATTERN = Pattern.compile("(?i)\ufffd\ufffd[0-9A-FK-OR]");
        for (Object o : this.mc.theWorld.playerEntities) {
            EntityPlayer entity = (EntityPlayer)o;
            if (!((Boolean)this.mcplayer.getValue() != false ? entity.isEntityAlive() : entity.isEntityAlive() && entity != this.mc.thePlayer)) continue;
            double pX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double pY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double pZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            this.renderNameTag(entity, COLOR_PATTERN.matcher(entity.getDisplayName().getUnformattedText()).replaceAll(""), pX, pY, pZ);
        }
    }

    private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        if (!entity.isInvisible()) {
            if (entity.getName().toUpperCase().contains("CRYPT DREADLORD") || entity.getName().toUpperCase().contains("DECOY") || entity.getName().toUpperCase().contains("ZOMBIE COMMANDER") || entity.getName().toUpperCase().contains("SKELETOR PRIME") || entity.getName().toUpperCase().contains("CRYPT SOULEATER") || ((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entity)) {
                return;
            }
            float size = this.mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
            if (size < 1.1f) {
                size = 1.1f;
            }
            pY += entity.isSneaking() ? 0.5 : 0.7;
            float scale = size * 1.8f;
            scale /= 100.0f;
            String friend = "";
            if (FriendManager.isFriend(entity.getName())) {
                friend = (Object)((Object)EnumChatFormatting.DARK_PURPLE) + "[Friend]";
            }
            lol = friend + (Object)((Object)EnumChatFormatting.WHITE) + tag;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef((float)pX, (float)pY + 1.4f, (float)pZ);
            GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
            GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
            GL11.glScalef(-scale, -scale, scale);
            GLUtil.setGLCap(2929, false);
            GLUtil.setGLCap(3042, true);
            float nw = (float)(-this.mc.fontRendererObj.getStringWidth(lol) / 2) - 4.6f;
            float width2 = nw - 2.0f * nw;
            float health = entity.getHealth();
            float hpPercentage = health / entity.getMaxHealth();
            hpPercentage = (float)MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
            float hpWidth = nw - 2.0f * nw * hpPercentage;
            if (this.animationStopwatch.hasReached(5.0)) {
                this.renderHpWidth = (float)AnimationUtil.animate(hpWidth, this.renderHpWidth, 0.053f);
                this.animationStopwatch.reset();
            }
            float yy = -17.0f;
            RenderUtil.drawFastRoundedRect(nw, yy, width2, -0.1f, 1.0f, new Color(25, 25, 25, 101).getRGB());
            RenderUtil.drawFastRoundedRect(nw, -2.0f, width2, -0.1f, 0.0f, new Color(152, 171, 195).getRGB());
            RenderUtil.drawFastRoundedRect(nw, -2.0f, this.renderHpWidth, -0.1f, 0.0f, new Color(123, 104, 238).getRGB());
            this.mc.fontRendererObj.drawString(lol, (int)(nw + 4.0f), -13, -1);
            this.renderHead(entity, -19.0f, -55.0f);
            GLUtil.revertAllCaps();
            GL11.glPopMatrix();
            GlStateManager.resetColor();
        }
    }

    private void renderHead(EntityPlayer player, float x, float y) {
        ResourceLocation texture = ((AbstractClientPlayer)player).getLocationSkin();
        this.mc.getTextureManager().bindTexture(texture);
        Gui.drawScaledCustomSizeModalRect((int)(x + 3.0f), (int)((double)y + 3.5), 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
    }
}

