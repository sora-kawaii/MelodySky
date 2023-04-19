/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.altimpl;

import xyz.Melody.System.Melody.Account.AccountEnum;
import xyz.Melody.System.Melody.Account.Alt;

public final class MicrosoftAlt
extends Alt {
    private String refreshToken;
    private String msToken;
    private String xblToken;
    private String xstsToken_f;
    private String xstsToken_s;
    private String accessToken;
    private String uuid;
    private String type;

    public MicrosoftAlt(String userName, String refreshToken, String msToken, String xblToken, String xsts1, String uhs, String accessToken, String uuid) {
        super(userName, AccountEnum.MICROSOFT);
        this.refreshToken = refreshToken;
        this.msToken = msToken;
        this.xblToken = xblToken;
        this.xstsToken_f = xsts1;
        this.xstsToken_s = uhs;
        this.accessToken = accessToken;
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getMsToken() {
        return this.msToken;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getXblToken() {
        return this.xblToken;
    }

    public String getXstsToken_f() {
        return this.xstsToken_f;
    }

    public String getXstsToken_s() {
        return this.xstsToken_s;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}

