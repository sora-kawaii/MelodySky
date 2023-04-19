/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account;

public enum AccountEnum {
    OFFLINE("OFFLINE"),
    XBLTOKEN("XBOXLIVE"),
    MICROSOFT("MICROSOFT"),
    ORIGINAL("ORIGINAL");

    private final String writeName;

    private AccountEnum(String name) {
        this.writeName = name;
    }

    public String getWriteName() {
        return this.writeName;
    }

    public static AccountEnum parse(String str) {
        for (AccountEnum value : AccountEnum.values()) {
            if (!value.writeName.equals(str)) continue;
            return value;
        }
        return null;
    }
}

