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
        super(minecraft, minecraft.field_71462_r, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), minecraft.func_110432_I().func_148256_e());
        this.playerInfo = new NetworkPlayerInfo(minecraft.func_110432_I().func_148256_e());
    }

    public NetworkPlayerInfo func_175102_a(UUID uUID) {
        return this.playerInfo;
    }

    public NetworkPlayerInfo func_175104_a(String string) {
        return this.playerInfo;
    }
}

