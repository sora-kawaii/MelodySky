/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account;

import xyz.Melody.System.Melody.Account.AccountEnum;

public abstract class Alt {
    private final String userName;
    private final AccountEnum accountType;

    public Alt(String userName, AccountEnum accountType) {
        this.userName = userName;
        this.accountType = accountType;
    }

    public AccountEnum getAccountType() {
        return this.accountType;
    }

    public String getUserName() {
        return this.userName;
    }
}

