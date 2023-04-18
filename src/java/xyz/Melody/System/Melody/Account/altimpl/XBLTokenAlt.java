/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.altimpl;

import xyz.Melody.System.Melody.Account.AccountEnum;
import xyz.Melody.System.Melody.Account.Alt;

public final class XBLTokenAlt
extends Alt {
    private String xblToken;
    private String xstsToken_f;
    private String xstsToken_s;
    private String accessToken;
    private String uuid;
    private String type;

    public XBLTokenAlt(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        super(string, AccountEnum.XBLTOKEN);
        this.xblToken = string2;
        this.xstsToken_f = string3;
        this.xstsToken_s = string4;
        this.accessToken = string5;
        this.uuid = string6;
        this.type = string7;
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

