/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.other;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtils {
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-ORZ]");
    private static final Pattern REPEATED_COLOR_PATTERN = Pattern.compile("(?i)(\u00a7[0-9A-FK-ORZ])+");
    private static final Pattern NUMBERS_SLASHES = Pattern.compile("[^0-9 /]");
    private static final Pattern SCOREBOARD_CHARACTERS = Pattern.compile("[^a-z A-Z:0-9_/'.!\u00a7\\[\\]\u2764]");
    private static final Pattern FLOAT_CHARACTERS = Pattern.compile("[^.0-9\\-]");
    private static final Pattern INTEGER_CHARACTERS = Pattern.compile("[^0-9]");
    private static final Pattern TRIM_WHITESPACE_RESETS = Pattern.compile("^(?:\\s|\u00a7r)*|(?:\\s|\u00a7r)*$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]+");
    private static final Pattern RESET_CODE_PATTERN = Pattern.compile("(?i)\u00a7R");
    private static final Pattern MAGNITUDE_PATTERN = Pattern.compile("(\\d[\\d,.]*\\d*)+([kKmMbBtT])");
    private static final NavigableMap<Integer, String> suffixes = new TreeMap<Integer, String>();

    public static String formatDouble(double d) {
        return NUMBER_FORMAT.format(d);
    }

    public static String stripColor(String string) {
        return STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public static boolean isZeroLength(String string) {
        return string.length() == 0 || REPEATED_COLOR_PATTERN.matcher(string).matches();
    }

    public static String keepScoreboardCharacters(String string) {
        return SCOREBOARD_CHARACTERS.matcher(string).replaceAll("");
    }

    public static String keepFloatCharactersOnly(String string) {
        return FLOAT_CHARACTERS.matcher(string).replaceAll("");
    }

    public static String keepIntegerCharactersOnly(String string) {
        return INTEGER_CHARACTERS.matcher(string).replaceAll("");
    }

    public static String getNumbersOnly(String string) {
        return NUMBERS_SLASHES.matcher(string).replaceAll("");
    }

    public static String convertMagnitudes(String string) throws ParseException {
        Matcher matcher = MAGNITUDE_PATTERN.matcher(string);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String string2;
            double d = NUMBER_FORMAT.parse(matcher.group(1)).doubleValue();
            switch (string2 = matcher.group(2).toLowerCase(Locale.ROOT)) {
                case "k": {
                    d *= 1000.0;
                    break;
                }
                case "m": {
                    d *= 1000000.0;
                    break;
                }
                case "b": {
                    d *= 1.0E9;
                    break;
                }
                case "t": {
                    d *= 1.0E12;
                }
            }
            matcher.appendReplacement(stringBuffer, NUMBER_FORMAT.format(d));
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static String removeDuplicateSpaces(String string) {
        return string.replaceAll("\\s+", " ");
    }

    public static String reverseText(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] stringArray = string.split(" ");
        for (int i = stringArray.length; i > 0; --i) {
            String string2 = stringArray[i - 1];
            boolean bl = false;
            for (char c : string2.toCharArray()) {
                if (c <= '\u00bf') continue;
                bl = true;
                stringBuilder.append((CharSequence)new StringBuilder(string2).reverse());
                break;
            }
            stringBuilder.append(" ");
            if (!bl) {
                stringBuilder.insert(0, string2);
            }
            stringBuilder.insert(0, " ");
        }
        return TextUtils.removeDuplicateSpaces(stringBuilder.toString().trim());
    }

    public static String getOrdinalSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1: {
                return "st";
            }
            case 2: {
                return "nd";
            }
            case 3: {
                return "rd";
            }
        }
        return "th";
    }

    public static String abbreviate(int n) {
        if (n < 0) {
            return "-" + TextUtils.abbreviate(-n);
        }
        if (n < 1000) {
            return Long.toString(n);
        }
        Map.Entry<Integer, String> entry = suffixes.floorEntry(n);
        Integer n2 = entry.getKey();
        String string = entry.getValue();
        int n3 = n / (n2 / 10);
        boolean bl = n3 < 100 && (double)n3 / 10.0 != (double)(n3 / 10);
        return bl ? (double)n3 / 10.0 + string : n3 / 10 + string;
    }

    public static String trimWhitespaceAndResets(String string) {
        return TRIM_WHITESPACE_RESETS.matcher(string).replaceAll("");
    }

    public static boolean isUsername(String string) {
        return USERNAME_PATTERN.matcher(string).matches();
    }

    public static String stripResets(String string) {
        return RESET_CODE_PATTERN.matcher(string).replaceAll("");
    }

    public static String toProperCase(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = Pattern.compile("([a-z])([a-z]*)", 2).matcher(string);
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase() + matcher.group(2).toLowerCase());
        }
        String string2 = matcher.appendTail(stringBuffer).toString();
        return string2;
    }

    public static String getFormattedString(String string, String string2) {
        if (string2.length() == 0) {
            return "";
        }
        String string3 = "kKlLmMnNoO";
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        int n = -2;
        int n2 = string.length();
        int n3 = 0;
        int n4 = 0;
        while (true) {
            if ((n = string.indexOf(167, n + 2)) == -1) {
                while (n4 < n2) {
                    if (string.charAt(n4) == string2.charAt(n3)) {
                        stringBuilder2.append(string.charAt(n4));
                        if (++n3 == string2.length()) {
                            return stringBuilder.append((CharSequence)stringBuilder2).toString();
                        }
                    } else {
                        n3 = 0;
                        stringBuilder = new StringBuilder(TextUtils.mergeFormats(stringBuilder.toString(), stringBuilder2.toString()));
                        stringBuilder2 = new StringBuilder();
                    }
                    ++n4;
                }
                return null;
            }
            while (n4 < n) {
                if (string.charAt(n4) == string2.charAt(n3)) {
                    stringBuilder2.append(string.charAt(n4));
                    if (++n3 == string2.length()) {
                        return stringBuilder.append((CharSequence)stringBuilder2).toString();
                    }
                } else {
                    n3 = 0;
                    stringBuilder = new StringBuilder(TextUtils.mergeFormats(stringBuilder.toString(), stringBuilder2.toString()));
                    stringBuilder2 = new StringBuilder();
                }
                ++n4;
            }
            if (n + 1 >= n2) continue;
            char c = string.charAt(n + 1);
            if (n3 == 0) {
                if (string3.indexOf(c) == -1) {
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append("\u00a7").append(c);
            } else {
                stringBuilder2.append("\u00a7").append(c);
            }
            n4 = n + 2;
        }
    }

    private static String mergeFormats(String string, String string2) {
        if (string2 == null || string2.length() == 0) {
            return string;
        }
        String string3 = "kKlLmMnNoO";
        StringBuilder stringBuilder = new StringBuilder(string);
        int n = -2;
        while ((n = string2.indexOf(167, n + 2)) != -1) {
            if (n + 1 >= string2.length()) continue;
            char c = string2.charAt(n + 1);
            if (string3.indexOf(c) == -1) {
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append("\u00a7").append(c);
        }
        return stringBuilder.toString();
    }

    public static long reverseFormat(String string) {
        String string2 = string.toLowerCase();
        String string3 = string2.substring(0, string2.length() - 1);
        long l2 = 1L;
        if (string2.endsWith("k")) {
            l2 = 1000L;
        } else if (string2.endsWith("m")) {
            l2 = 1000000L;
        } else if (string2.endsWith("b")) {
            l2 = 1000000000L;
        } else {
            string3 = string2;
        }
        return (long)(Double.parseDouble(string3) * (double)l2);
    }

    static {
        suffixes.put(1000, "k");
        suffixes.put(1000000, "M");
        suffixes.put(1000000000, "B");
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }
}

