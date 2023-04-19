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

    public XBLTokenAlt(String userName, String xblToken, String xsts1, String xsts2, String accessToken, String uuid, String type) {
        super(userName, AccountEnum.XBLTOKEN);
        this.xblToken = xblToken;
        this.xstsToken_f = xsts1;
        this.xstsToken_s = xsts2;
        this.accessToken = accessToken;
        this.uuid = uuid;
        this.type = type;
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

