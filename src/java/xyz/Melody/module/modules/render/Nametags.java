package xyz.Melody.module.modules.render;

import java.awt.Color;
import java.util.Iterator;
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
import xyz.Melody.Event.value.Value;
import xyz.Melody.System.Managers.FriendManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.gl.GLUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public class Nametags extends Module {
   private float renderHpWidth;
   private static String lol;
   private final TimerUtil animationStopwatch = new TimerUtil();
   Option mcplayer = new Option("ShowYou", true);

   public Nametags() {
      super("Nametags", new String[]{"tags"}, ModuleType.Render);
      this.setColor((new Color(29, 187, 102)).getRGB());
      this.addValues(new Value[]{this.mcplayer});
      this.setModInfo("Name Tag.");
   }

   @EventHandler
   public void onRender(EventRender3D event) {
      Pattern COLOR_PATTERN = Pattern.compile("(?i)��[0-9A-FK-OR]");
      Iterator var3 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         while(true) {
            if (!var3.hasNext()) {
               return;
            }

            Object o = var3.next();
            entity = (EntityPlayer)o;
            if ((Boolean)this.mcplayer.getValue()) {
               if (entity.isEntityAlive()) {
                  break;
               }
            } else if (entity.isEntityAlive() && entity != this.mc.thePlayer) {
               break;
            }
         }

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

         float size = this.mc.thePlayer.getDistanceToEntity(entity) / 10.0F;
         if (size < 1.1F) {
            size = 1.1F;
         }

         pY += entity.isSneaking() ? 0.5 : 0.7;
         float scale = size * 1.8F;
         scale /= 100.0F;
         String friend = "";
         if (FriendManager.isFriend(entity.getName())) {
            friend = EnumChatFormatting.DARK_PURPLE + "[Friend]";
         }

         lol = friend + EnumChatFormatting.WHITE + tag;
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
         GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0F, 2.0F, 0.0F);
         GL11.glRotatef(this.mc.getRenderManager().playerViewX, 2.0F, 0.0F, 0.0F);
         GL11.glScalef(-scale, -scale, scale);
         GLUtil.setGLCap(2929, false);
         GLUtil.setGLCap(3042, true);
         float nw = (float)(-this.mc.fontRendererObj.getStringWidth(lol) / 2) - 4.6F;
         float width2 = nw - 2.0F * nw;
         float health = entity.getHealth();
         float hpPercentage = health / entity.getMaxHealth();
         hpPercentage = (float)MathHelper.clamp_double((double)hpPercentage, 0.0, 1.0);
         float hpWidth = nw - 2.0F * nw * hpPercentage;
         if (this.animationStopwatch.hasReached(5.0)) {
            this.renderHpWidth = (float)AnimationUtil.animate((double)hpWidth, (double)this.renderHpWidth, 0.05299999937415123);
            this.animationStopwatch.reset();
         }

         float yy = -17.0F;
         RenderUtil.drawFastRoundedRect(nw, yy, width2, -0.1F, 1.0F, (new Color(25, 25, 25, 101)).getRGB());
         RenderUtil.drawFastRoundedRect(nw, -2.0F, width2, -0.1F, 0.0F, (new Color(152, 171, 195)).getRGB());
         RenderUtil.drawFastRoundedRect(nw, -2.0F, this.renderHpWidth, -0.1F, 0.0F, (new Color(123, 104, 238)).getRGB());
         this.mc.fontRendererObj.drawString(lol, (int)(nw + 4.0F), -13, -1);
         this.renderHead(entity, -19.0F, -55.0F);
         GLUtil.revertAllCaps();
         GL11.glPopMatrix();
         GlStateManager.resetColor();
      }

   }

   private void renderHead(EntityPlayer player, float x, float y) {
      Object texture = ((AbstractClientPlayer)player).getLocationSkin();
      this.mc.getTextureManager().bindTexture((ResourceLocation)texture);
      Gui.drawScaledCustomSizeModalRect((int)(x + 3.0F), (int)((double)y + 3.5), 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
   }
}
