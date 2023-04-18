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

    public MicrosoftAlt(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
        super(string, AccountEnum.MICROSOFT);
        this.refreshToken = string2;
        this.msToken = string3;
        this.xblToken = string4;
        this.xstsToken_f = string5;
        this.xstsToken_s = string6;
        this.accessToken = string7;
        this.uuid = string8;
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

