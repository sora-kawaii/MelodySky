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
        for (Object e : this.mc.field_71441_e.field_73010_i) {
            EntityPlayer entityPlayer = (EntityPlayer)e;
            if (!((Boolean)this.mcplayer.getValue() != false ? entityPlayer.func_70089_S() : entityPlayer.func_70089_S() && entityPlayer != this.mc.field_71439_g)) continue;
            double d = entityPlayer.field_70142_S + (entityPlayer.field_70165_t - entityPlayer.field_70142_S) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78730_l;
            double d2 = entityPlayer.field_70137_T + (entityPlayer.field_70163_u - entityPlayer.field_70137_T) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78731_m;
            double d3 = entityPlayer.field_70136_U + (entityPlayer.field_70161_v - entityPlayer.field_70136_U) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78728_n;
            this.renderNameTag(entityPlayer, pattern.matcher(entityPlayer.func_145748_c_().func_150260_c()).replaceAll(""), d, d2, d3);
        }
    }

    private void renderNameTag(EntityPlayer entityPlayer, String string, double d, double d2, double d3) {
        if (!entityPlayer.func_82150_aj()) {
            if (entityPlayer.func_70005_c_().toUpperCase().contains("CRYPT DREADLORD") || entityPlayer.func_70005_c_().toUpperCase().contains("DECOY") || entityPlayer.func_70005_c_().toUpperCase().contains("ZOMBIE COMMANDER") || entityPlayer.func_70005_c_().toUpperCase().contains("SKELETOR PRIME") || entityPlayer.func_70005_c_().toUpperCase().contains("CRYPT SOULEATER") || ((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(entityPlayer)) {
                return;
            }
            float f = this.mc.field_71439_g.func_70032_d(entityPlayer) / 10.0f;
            if (f < 1.1f) {
                f = 1.1f;
            }
            d2 += entityPlayer.func_70093_af() ? 0.5 : 0.7;
            float f2 = f * 1.8f;
            f2 /= 100.0f;
            String string2 = "";
            if (FriendManager.isFriend(entityPlayer.func_70005_c_())) {
                string2 = (Object)((Object)EnumChatFormatting.DARK_PURPLE) + "[Friend]";
            }
            lol = string2 + (Object)((Object)EnumChatFormatting.WHITE) + string;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef((float)d, (float)d2 + 1.4f, (float)d3);
            GL11.glRotatef(-this.mc.func_175598_ae().field_78735_i, 0.0f, 2.0f, 0.0f);
            GL11.glRotatef(this.mc.func_175598_ae().field_78732_j, 2.0f, 0.0f, 0.0f);
            GL11.glScalef(-f2, -f2, f2);
            GLUtil.setGLCap(2929, false);
            GLUtil.setGLCap(3042, true);
            float f3 = (float)(-this.mc.field_71466_p.func_78256_a(lol) / 2) - 4.6f;
            float f4 = f3 - 2.0f * f3;
            float f5 = entityPlayer.func_110143_aJ();
            float f6 = f5 / entityPlayer.func_110138_aP();
            f6 = (float)MathHelper.func_151237_a((double)f6, (double)0.0, (double)1.0);
            float f7 = f3 - 2.0f * f3 * f6;
            if (this.animationStopwatch.hasReached(5.0)) {
                this.renderHpWidth = (float)AnimationUtil.animate(f7, this.renderHpWidth, 0.053f);
                this.animationStopwatch.reset();
            }
            float f8 = -17.0f;
            RenderUtil.drawFastRoundedRect(f3, f8, f4, -0.1f, 1.0f, new Color(25, 25, 25, 101).getRGB());
            RenderUtil.drawFastRoundedRect(f3, -2.0f, f4, -0.1f, 0.0f, new Color(152, 171, 195).getRGB());
            RenderUtil.drawFastRoundedRect(f3, -2.0f, this.renderHpWidth, -0.1f, 0.0f, new Color(123, 104, 238).getRGB());
            this.mc.field_71466_p.func_78276_b(lol, (int)(f3 + 4.0f), -13, -1);
            this.renderHead(entityPlayer, -19.0f, -55.0f);
            GLUtil.revertAllCaps();
            GL11.glPopMatrix();
            GlStateManager.func_179117_G();
        }
    }

    private void renderHead(EntityPlayer entityPlayer, float f, float f2) {
        ResourceLocation resourceLocation = ((AbstractClientPlayer)entityPlayer).func_110306_p();
        this.mc.func_110434_K().func_110577_a(resourceLocation);
        Gui.func_152125_a((int)((int)(f + 3.0f)), (int)((int)((double)f2 + 3.5)), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)32, (int)32, (float)64.0f, (float)64.0f);
    }
}

