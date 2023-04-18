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

    private AccountEnum(String string2) {
        this.writeName = string2;
    }

    public String getWriteName() {
        return this.writeName;
    }

    public static AccountEnum parse(String string) {
        for (AccountEnum accountEnum : AccountEnum.values()) {
            if (!accountEnum.writeName.equals(string)) continue;
            return accountEnum;
        }
        return null;
    }
}

