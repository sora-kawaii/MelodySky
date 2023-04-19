/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Authentication;

public final class UserObj {
    private String tkn;
    private String name;
    private String uuid;

    public String getToken() {
        return this.tkn;
    }

    public String getName() {
        return this.name;
    }

    public UserObj(String string, String string2, String string3) {
        this.name = string;
        this.uuid = string2;
        this.tkn = string3;
    }

    public String getUuid() {
        return this.uuid;
    }
}

