/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import java.util.Arrays;

public final class StringUtils {
    public static String toCompleteString(String[] stringArray, int n) {
        if (stringArray.length <= n) {
            return "";
        }
        return String.join((CharSequence)" ", Arrays.copyOfRange(stringArray, n, stringArray.length));
    }

    public static String replace(String string, String string2, String string3) {
        if (string.isEmpty() || string2.isEmpty() || string2.equals(string3)) {
            return string;
        }
        if (string3 == null) {
            string3 = "";
        }
        int n = string.length();
        int n2 = string2.length();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < n; ++i) {
            int n3 = stringBuilder.indexOf(string2, i);
            if (n3 == -1) {
                if (i == 0) {
                    return string;
                }
                return stringBuilder.toString();
            }
            stringBuilder.replace(n3, n3 + n2, string3);
        }
        return stringBuilder.toString();
    }
}

