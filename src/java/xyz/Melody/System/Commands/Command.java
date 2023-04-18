/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands;

import net.minecraft.client.Minecraft;
import xyz.Melody.Utils.Helper;

public abstract class Command {
    private String name;
    private String[] alias;
    private String syntax;
    private String help;
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String string, String[] stringArray, String string2, String string3) {
        this.name = string.toLowerCase();
        this.syntax = string2.toLowerCase();
        this.help = string3;
        this.alias = stringArray;
    }

    public abstract String execute(String[] var1);

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public String getHelp() {
        return this.help;
    }

    public void syntaxError(String string) {
        Helper.sendMessage(String.format("\u00a77Invalid command usage", string));
    }

    public void syntaxError(byte by) {
        switch (by) {
            case 0: {
                this.syntaxError("bad argument");
                break;
            }
            case 1: {
                this.syntaxError("argument gay");
            }
        }
    }
}

