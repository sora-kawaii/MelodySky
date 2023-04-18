/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int SIXBIT = 6;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet;
    private static final char[] lookUpBase64Alphabet;

    protected static boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    protected static boolean isPad(char c) {
        return c == '=';
    }

    protected static boolean isData(char c) {
        return c < '\u0080' && base64Alphabet[c] != -1;
    }

    protected static boolean isBase64(char c) {
        return Base64.isWhiteSpace(c) || Base64.isPad(c) || Base64.isData(c);
    }

    public static String encode(byte[] byArray) {
        byte by;
        int n;
        if (byArray == null) {
            return null;
        }
        int n2 = byArray.length * 8;
        if (n2 == 0) {
            return "";
        }
        int n3 = n2 % 24;
        int n4 = n2 / 24;
        int n5 = n3 != 0 ? n4 + 1 : n4;
        char[] cArray = null;
        cArray = new char[n5 * 4];
        byte by2 = 0;
        byte by3 = 0;
        byte by4 = 0;
        byte by5 = 0;
        byte by6 = 0;
        int n6 = 0;
        int n7 = 0;
        for (n = 0; n < n4; ++n) {
            by4 = byArray[n7++];
            by5 = byArray[n7++];
            by6 = byArray[n7++];
            by3 = (byte)(by5 & 0xF);
            by2 = (byte)(by4 & 3);
            by = (by4 & 0xFFFFFF80) == 0 ? (byte)(by4 >> 2) : (byte)(by4 >> 2 ^ 0xC0);
            byte by7 = (by5 & 0xFFFFFF80) == 0 ? (byte)(by5 >> 4) : (byte)(by5 >> 4 ^ 0xF0);
            byte by8 = (by6 & 0xFFFFFF80) == 0 ? (byte)(by6 >> 6) : (byte)(by6 >> 6 ^ 0xFC);
            cArray[n6++] = lookUpBase64Alphabet[by];
            cArray[n6++] = lookUpBase64Alphabet[by7 | by2 << 4];
            cArray[n6++] = lookUpBase64Alphabet[by3 << 2 | by8];
            cArray[n6++] = lookUpBase64Alphabet[by6 & 0x3F];
        }
        if (n3 == 8) {
            by4 = byArray[n7];
            by2 = (byte)(by4 & 3);
            n = (by4 & 0xFFFFFF80) == 0 ? (byte)(by4 >> 2) : (byte)(by4 >> 2 ^ 0xC0);
            cArray[n6++] = lookUpBase64Alphabet[n];
            cArray[n6++] = lookUpBase64Alphabet[by2 << 4];
            cArray[n6++] = 61;
            cArray[n6++] = 61;
        } else if (n3 == 16) {
            by4 = byArray[n7];
            by5 = byArray[n7 + 1];
            by3 = (byte)(by5 & 0xF);
            by2 = (byte)(by4 & 3);
            n = (by4 & 0xFFFFFF80) == 0 ? (byte)(by4 >> 2) : (byte)(by4 >> 2 ^ 0xC0);
            by = (by5 & 0xFFFFFF80) == 0 ? (byte)(by5 >> 4) : (byte)(by5 >> 4 ^ 0xF0);
            cArray[n6++] = lookUpBase64Alphabet[n];
            cArray[n6++] = lookUpBase64Alphabet[by | by2 << 4];
            cArray[n6++] = lookUpBase64Alphabet[by3 << 2];
            cArray[n6++] = 61;
        }
        return new String(cArray);
    }

    public static byte[] decode(String string) {
        int n;
        if (string == null) {
            return null;
        }
        char[] cArray = string.toCharArray();
        int n2 = Base64.removeWhiteSpace(cArray);
        if (n2 % 4 != 0) {
            return null;
        }
        int n3 = n2 / 4;
        if (n3 == 0) {
            return new byte[0];
        }
        byte[] byArray = null;
        byte by = 0;
        byte by2 = 0;
        byte by3 = 0;
        byte by4 = 0;
        char c = '\u0000';
        char c2 = '\u0000';
        char c3 = '\u0000';
        char c4 = '\u0000';
        int n4 = 0;
        int n5 = 0;
        byArray = new byte[n3 * 3];
        for (n = 0; n < n3 - 1; ++n) {
            if (!(Base64.isData(c = cArray[n5++]) && Base64.isData(c2 = cArray[n5++]) && Base64.isData(c3 = cArray[n5++]) && Base64.isData(c4 = cArray[n5++]))) {
                return null;
            }
            by = base64Alphabet[c];
            by2 = base64Alphabet[c2];
            by3 = base64Alphabet[c3];
            by4 = base64Alphabet[c4];
            byArray[n4++] = (byte)(by << 2 | by2 >> 4);
            byArray[n4++] = (byte)((by2 & 0xF) << 4 | by3 >> 2 & 0xF);
            byArray[n4++] = (byte)(by3 << 6 | by4);
        }
        if (!Base64.isData(c = cArray[n5++]) || !Base64.isData(c2 = cArray[n5++])) {
            return null;
        }
        by = base64Alphabet[c];
        by2 = base64Alphabet[c2];
        c3 = cArray[n5++];
        c4 = cArray[n5++];
        if (!Base64.isData(c3) || !Base64.isData(c4)) {
            if (Base64.isPad(c3) && Base64.isPad(c4)) {
                if ((by2 & 0xF) != 0) {
                    return null;
                }
                byte[] byArray2 = new byte[n * 3 + 1];
                System.arraycopy(byArray, 0, byArray2, 0, n * 3);
                byArray2[n4] = (byte)(by << 2 | by2 >> 4);
                return byArray2;
            }
            if (!Base64.isPad(c3) && Base64.isPad(c4)) {
                by3 = base64Alphabet[c3];
                if ((by3 & 3) != 0) {
                    return null;
                }
                byte[] byArray3 = new byte[n * 3 + 2];
                System.arraycopy(byArray, 0, byArray3, 0, n * 3);
                byArray3[n4++] = (byte)(by << 2 | by2 >> 4);
                byArray3[n4] = (byte)((by2 & 0xF) << 4 | by3 >> 2 & 0xF);
                return byArray3;
            }
            return null;
        }
        by3 = base64Alphabet[c3];
        by4 = base64Alphabet[c4];
        byArray[n4++] = (byte)(by << 2 | by2 >> 4);
        byArray[n4++] = (byte)((by2 & 0xF) << 4 | by3 >> 2 & 0xF);
        byArray[n4++] = (byte)(by3 << 6 | by4);
        return byArray;
    }

    protected static int removeWhiteSpace(char[] cArray) {
        if (cArray == null) {
            return 0;
        }
        int n = 0;
        int n2 = cArray.length;
        for (int i = 0; i < n2; ++i) {
            if (Base64.isWhiteSpace(cArray[i])) continue;
            cArray[n++] = cArray[i];
        }
        return n;
    }

    static {
        int n;
        base64Alphabet = new byte[128];
        lookUpBase64Alphabet = new char[64];
        for (n = 0; n < 128; ++n) {
            Base64.base64Alphabet[n] = -1;
        }
        for (n = 90; n >= 65; --n) {
            Base64.base64Alphabet[n] = (byte)(n - 65);
        }
        for (n = 122; n >= 97; --n) {
            Base64.base64Alphabet[n] = (byte)(n - 97 + 26);
        }
        for (n = 57; n >= 48; --n) {
            Base64.base64Alphabet[n] = (byte)(n - 48 + 52);
        }
        Base64.base64Alphabet[43] = 62;
        Base64.base64Alphabet[47] = 63;
        for (n = 0; n <= 25; ++n) {
            Base64.lookUpBase64Alphabet[n] = (char)(65 + n);
        }
        n = 26;
        int n2 = 0;
        while (n <= 51) {
            Base64.lookUpBase64Alphabet[n] = (char)(97 + n2);
            ++n;
            ++n2;
        }
        n = 52;
        n2 = 0;
        while (n <= 61) {
            Base64.lookUpBase64Alphabet[n] = (char)(48 + n2);
            ++n;
            ++n2;
        }
        Base64.lookUpBase64Alphabet[62] = 43;
        Base64.lookUpBase64Alphabet[63] = 47;
    }
}

