/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.Melody.GUI.Circle;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.Scissor;

public final class ClientButton
extends GuiButton {
    private ResourceLocation image;
    private ResourceLocation hoveredImage;
    private float imgScale;
    private float imgXShift;
    private float imgYShift;
    private int r1;
    private int g1;
    private int b1;
    private int alpha;
    private int alpha1;
    private int whiteAlpha = 0;
    private float buttonZ = 0.0f;
    private boolean clicked = false;
    private ArrayList<Circle> circles = new ArrayList();

    public ClientButton(int n, int n2, int n3, int n4, int n5, String string, Color color) {
        super(n, n2, n3, 10, 12, string);
        this.width = n4;
        this.height = n5;
        this.alpha = color.getAlpha();
        this.image = null;
        this.hoveredImage = null;
        this.r1 = color.getRed();
        this.g1 = color.getGreen();
        this.b1 = color.getBlue();
        this.alpha1 = color.getAlpha();
        this.imgScale = (float)this.height / 2.6f;
    }

    public ClientButton(int n, int n2, int n3, int n4, int n5, String string, ResourceLocation resourceLocation, Color color) {
        super(n, n2, n3, 10, 12, string);
        this.width = n4;
        this.height = n5;
        this.alpha = color.getAlpha();
        this.image = resourceLocation;
        this.hoveredImage = null;
        this.r1 = color.getRed();
        this.g1 = color.getGreen();
        this.b1 = color.getBlue();
        this.alpha1 = color.getAlpha();
        this.imgScale = (float)this.height / 2.6f;
        this.imgXShift = 0.0f;
        this.imgYShift = 0.0f;
    }

    public ClientButton(int n, int n2, int n3, int n4, int n5, String string, ResourceLocation resourceLocation, float f, float f2, float f3, Color color) {
        super(n, n2, n3, 10, 12, string);
        this.width = n4;
        this.height = n5;
        this.alpha = color.getAlpha();
        this.image = resourceLocation;
        this.hoveredImage = null;
        this.r1 = color.getRed();
        this.g1 = color.getGreen();
        this.b1 = color.getBlue();
        this.alpha1 = color.getAlpha();
        this.imgScale = f3;
        this.imgXShift = f;
        this.imgYShift = f2;
    }

    public ClientButton(int n, int n2, int n3, int n4, int n5, String string, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, float f, float f2, float f3, Color color) {
        super(n, n2, n3, 10, 12, string);
        this.width = n4;
        this.height = n5;
        this.alpha = color.getAlpha();
        this.image = resourceLocation;
        this.hoveredImage = resourceLocation2;
        this.r1 = color.getRed();
        this.g1 = color.getGreen();
        this.b1 = color.getBlue();
        this.alpha1 = color.getAlpha();
        this.imgScale = f3;
        this.imgXShift = f;
        this.imgYShift = f2;
    }

    @Override
    public void drawButton(Minecraft minecraft, int n, int n2) {
        CFontRenderer cFontRenderer = FontLoaders.CNMD18;
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        int n3 = new ScaledResolution(minecraft).getScaleFactor();
        Scissor.start(((float)this.xPosition - this.buttonZ) * (float)n3, ((float)this.yPosition - this.buttonZ * 0.8f) * (float)n3, ((float)(this.xPosition + this.width) + this.buttonZ) * (float)n3, ((float)(this.yPosition + this.height) + this.buttonZ * 0.8f) * (float)n3);
        if (!this.enabled) {
            this.whiteAlpha = 0;
            cFontRenderer.drawCenteredString(this.displayString, this.xPosition + this.width / 2, (float)(this.yPosition + this.height / 2) - 2.7f, -1);
            RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8f, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8f, 4.0f, new Color(20, 20, 20, 130).getRGB());
            return;
        }
        this.hovered = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        if (this.hovered) {
            if (this.alpha1 > 15) {
                this.alpha1 -= 15;
            }
            if (this.whiteAlpha < 130) {
                this.whiteAlpha += 15;
            }
            if (this.buttonZ < 1.0f) {
                this.buttonZ += 0.2f;
            }
        }
        if (!this.hovered) {
            if (this.alpha1 <= this.alpha) {
                this.alpha1 += 15;
            }
            if (this.whiteAlpha > 0) {
                this.whiteAlpha -= 15;
            }
            if (this.buttonZ > 0.0f) {
                this.buttonZ -= 0.2f;
            }
        }
        if (this.clicked && !Mouse.isButtonDown(0)) {
            this.clicked = false;
        }
        if (this.hovered && Mouse.isButtonDown(0)) {
            RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8f, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8f, 4.0f, new Color(this.r1, this.g1, this.b1, this.alpha1).getRGB());
            RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8f, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8f, 4.0f, new Color(140, 140, 140, this.whiteAlpha).getRGB());
            if (!this.clicked) {
                this.circles.add(new Circle(n, n2, this.width));
                this.clicked = true;
            }
        } else {
            if (this.alpha != 0) {
                RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8f, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8f, 4.0f, new Color(this.r1, this.g1, this.b1, this.alpha1).getRGB());
            }
            RenderUtil.drawFastRoundedRect((float)this.xPosition - this.buttonZ, (float)this.yPosition - this.buttonZ * 0.8f, (float)(this.xPosition + this.width) + this.buttonZ, (float)(this.yPosition + this.height) + this.buttonZ * 0.8f, 4.0f, new Color(222, 222, 222, this.whiteAlpha).getRGB());
        }
        for (Circle circle2 : this.circles) {
            circle2.draw();
        }
        this.circles.removeIf(circle -> circle.op.getOpacity() == (float)(this.width + 10));
        if (this.image != null) {
            RenderUtil.drawImage(this.image, (float)this.xPosition + (float)this.height / 3.0f + this.imgXShift, (float)this.yPosition + (float)this.height / 3.0f + this.imgYShift, this.imgScale, this.imgScale);
        }
        if (this.hoveredImage != null && this.hovered) {
            RenderUtil.drawImage(this.hoveredImage, (float)this.xPosition + (float)this.height / 3.0f + this.imgXShift, (float)this.yPosition + (float)this.height / 3.0f + this.imgYShift, this.imgScale, this.imgScale);
        }
        GL11.glColor3f(2.55f, 2.55f, 2.55f);
        this.mouseDragged(minecraft, n, n2);
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glScaled(1.0, 1.0, 1.0);
        cFontRenderer.drawCenteredString(this.displayString, this.xPosition + this.width / 2, (float)(this.yPosition + this.height / 2) - 2.7f, this.whiteAlpha > 60 ? Color.DARK_GRAY.getRGB() : -1);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        Scissor.end();
    }
}

