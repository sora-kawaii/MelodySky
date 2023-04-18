/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class ShaderUtils {
    private static final Minecraft mc = Minecraft.func_71410_x();
    private final int programID;

    public ShaderUtils(String string, String string2) {
        int n;
        int n2 = GL20.glCreateProgram();
        try {
            n = this.createShader(mc.func_110442_L().func_110536_a(new ResourceLocation(string)).func_110527_b(), 35632);
            GL20.glAttachShader(n2, n);
            int n3 = this.createShader(mc.func_110442_L().func_110536_a(new ResourceLocation(string2)).func_110527_b(), 35633);
            GL20.glAttachShader(n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        GL20.glLinkProgram(n2);
        n = GL20.glGetProgrami(n2, 35714);
        if (n == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = n2;
    }

    public ShaderUtils(String string) {
        this(string, "Melody/GLSL/Shaders/vertex.vsh");
    }

    public void init() {
        GL20.glUseProgram(this.programID);
    }

    public void unload() {
        GL20.glUseProgram(0);
    }

    public int getUniform(String string) {
        return GL20.glGetUniformLocation(this.programID, string);
    }

    public void setUniformf(String string, float ... fArray) {
        int n = GL20.glGetUniformLocation(this.programID, string);
        switch (fArray.length) {
            case 1: {
                GL20.glUniform1f(n, fArray[0]);
                break;
            }
            case 2: {
                GL20.glUniform2f(n, fArray[0], fArray[1]);
                break;
            }
            case 3: {
                GL20.glUniform3f(n, fArray[0], fArray[1], fArray[2]);
                break;
            }
            case 4: {
                GL20.glUniform4f(n, fArray[0], fArray[1], fArray[2], fArray[3]);
            }
        }
    }

    public void setUniformi(String string, int ... nArray) {
        int n = GL20.glGetUniformLocation(this.programID, string);
        if (nArray.length > 1) {
            GL20.glUniform2i(n, nArray[0], nArray[1]);
        } else {
            GL20.glUniform1i(n, nArray[0]);
        }
    }

    public static void drawQuads() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        float f = (float)scaledResolution.func_78327_c();
        float f2 = (float)scaledResolution.func_78324_d();
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, f2);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(f, f2);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(f, 0.0f);
        GL11.glEnd();
    }

    private int createShader(InputStream inputStream, int n) {
        int n2 = GL20.glCreateShader(n);
        GL20.glShaderSource(n2, this.readInputStream(inputStream));
        GL20.glCompileShader(n2);
        if (GL20.glGetShaderi(n2, 35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog(n2, 4096));
            throw new IllegalStateException(String.format("Shader failed to compile!", n));
        }
        return n2;
    }

    public String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string).append('\n');
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

