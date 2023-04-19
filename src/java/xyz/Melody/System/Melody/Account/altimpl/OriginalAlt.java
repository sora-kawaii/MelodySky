/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.altimpl;

import xyz.Melody.System.Melody.Account.AccountEnum;
import xyz.Melody.System.Melody.Account.Alt;

public final class OriginalAlt
extends Alt {
    private final String accessToken;
    private final String uuid;
    private final String type;

    public OriginalAlt(String userName, String accessToken, String uuid, String type) {
        super(userName, AccountEnum.ORIGINAL);
        this.accessToken = accessToken;
        this.uuid = uuid;
        this.type = type;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getType() {
        return this.type;
    }
}

