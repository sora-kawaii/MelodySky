/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.fakemc;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.EnumPacketDirection;
import xyz.Melody.Utils.fakemc.FakeNetworkManager;

public final class FakeNetHandlerPlayClient
extends NetHandlerPlayClient {
    private NetworkPlayerInfo playerInfo;

    public FakeNetHandlerPlayClient(Minecraft mcIn) {
        super(mcIn, mcIn.currentScreen, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), mcIn.getSession().getProfile());
        this.playerInfo = new NetworkPlayerInfo(mcIn.getSession().getProfile());
    }

    @Override
    public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        return this.playerInfo;
    }

    @Override
    public NetworkPlayerInfo getPlayerInfo(String name) {
        return this.playerInfo;
    }
}

