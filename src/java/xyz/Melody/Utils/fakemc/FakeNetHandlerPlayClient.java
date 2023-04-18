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

    public FakeNetHandlerPlayClient(Minecraft minecraft) {
        super(minecraft, minecraft.currentScreen, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), minecraft.getSession().getProfile());
        this.playerInfo = new NetworkPlayerInfo(minecraft.getSession().getProfile());
    }

    @Override
    public NetworkPlayerInfo getPlayerInfo(UUID uUID) {
        return this.playerInfo;
    }

    @Override
    public NetworkPlayerInfo getPlayerInfo(String string) {
        return this.playerInfo;
    }
}

