/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Managers.Client.ModuleManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public final class AuthMe
extends Command {
    public AuthMe() {
        super("authme", new String[]{"verifyme", "auth"}, "", "sketit");
    }

    @Override
    public String execute(String[] stringArray) {
        if (Client.instance.authManager.verified) {
            Helper.sendMessage("[AUTHENTICATION] Already Verified.");
            return null;
        }
        if (Client.instance.authManager.authMe(this.mc.getSession().getProfile().getId().toString(), this.mc.getSession().getUsername())) {
            Helper.sendMessage("[AUTHENTICATION] Success.");
            for (Module module : ModuleManager.getModules()) {
                if (!module.enabledOnStartup) continue;
                module.setEnabled(true);
            }
        } else {
            Helper.sendMessage("[AUTHENTICATION] Failed.");
        }
        return null;
    }
}

