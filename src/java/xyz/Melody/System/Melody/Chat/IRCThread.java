/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Chat;

import xyz.Melody.Client;

public final class IRCThread
extends Thread {
    boolean connect = false;

    @Override
    public void run() {
        Client.instance.irc.connect(25565, this.connect);
        while (!(Client.instance.ircExeption || Client.stopping || Client.instance.irc.shouldThreadStop)) {
            Client.instance.irc.handleInput();
        }
    }

    IRCThread(boolean bl) {
        this.connect = bl;
    }
}

