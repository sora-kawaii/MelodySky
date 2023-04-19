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

    public static String formatDouble(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String stripColor(String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static boolean isZeroLength(String input) {
        return input.length() == 0 || REPEATED_COLOR_PATTERN.matcher(input).matches();
    }

    public static String keepScoreboardCharacters(String text) {
        return SCOREBOARD_CHARACTERS.matcher(text).replaceAll("");
    }

    public static String keepFloatCharactersOnly(String text) {
        return FLOAT_CHARACTERS.matcher(text).replaceAll("");
    }

    public static String keepIntegerCharactersOnly(String text) {
        return INTEGER_CHARACTERS.matcher(text).replaceAll("");
    }

    public static String getNumbersOnly(String text) {
        return NUMBERS_SLASHES.matcher(text).replaceAll("");
    }

    public static String convertMagnitudes(String text) throws ParseException {
        Matcher matcher = MAGNITUDE_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String magnitude;
            double parsedDouble = NUMBER_FORMAT.parse(matcher.group(1)).doubleValue();
            switch (magnitude = matcher.group(2).toLowerCase(Locale.ROOT)) {
                case "k": {
                    parsedDouble *= 1000.0;
                    break;
                }
                case "m": {
                    parsedDouble *= 1000000.0;
                    break;
                }
                case "b": {
                    parsedDouble *= 1.0E9;
                    break;
                }
                case "t": {
                    parsedDouble *= 1.0E12;
                }
            }
            matcher.appendReplacement(sb, NUMBER_FORMAT.format(parsedDouble));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String removeDuplicateSpaces(String text) {
        return text.replaceAll("\\s+", " ");
    }

    public static String reverseText(String originalText) {
        StringBuilder newString = new StringBuilder();
        String[] parts = originalText.split(" ");
        for (int i = parts.length; i > 0; --i) {
            String textPart = parts[i - 1];
            boolean foundCharacter = false;
            for (char letter : textPart.toCharArray()) {
                if (letter <= '\u00bf') continue;
                foundCharacter = true;
                newString.append((CharSequence)new StringBuilder(textPart).reverse());
                break;
            }
            newString.append(" ");
            if (!foundCharacter) {
                newString.insert(0, textPart);
            }
            newString.insert(0, " ");
        }
        return TextUtils.removeDuplicateSpaces(newString.toString().trim());
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

    public static String abbreviate(int number) {
        if (number < 0) {
            return "-" + TextUtils.abbreviate(-number);
        }
        if (number < 1000) {
            return Long.toString(number);
        }
        Map.Entry<Integer, String> entry = suffixes.floorEntry(number);
        Integer divideBy = entry.getKey();
        String suffix = entry.getValue();
        int truncated = number / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (double)truncated / 10.0 != (double)(truncated / 10);
        return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10 + suffix;
    }

    public static String trimWhitespaceAndResets(String input) {
        return TRIM_WHITESPACE_RESETS.matcher(input).replaceAll("");
    }

    public static boolean isUsername(String input) {
        return USERNAME_PATTERN.matcher(input).matches();
    }

    public static String stripResets(String input) {
        return RESET_CODE_PATTERN.matcher(input).replaceAll("");
    }

    public static String toProperCase(String inputString) {
        StringBuffer sb = new StringBuffer();
        Matcher match = Pattern.compile("([a-z])([a-z]*)", 2).matcher(inputString);
        while (match.find()) {
            match.appendReplacement(sb, match.group(1).toUpperCase() + match.group(2).toLowerCase());
        }
        String ret = match.appendTail(sb).toString();
        return ret;
    }

    public static String getFormattedString(String formatted, String unformattedSubstring) {
        if (unformattedSubstring.length() == 0) {
            return "";
        }
        String styles = "kKlLmMnNoO";
        StringBuilder preEnchantFormat = new StringBuilder();
        StringBuilder formattedEnchant = new StringBuilder();
        int i = -2;
        int len = formatted.length();
        int unformattedEnchantIdx = 0;
        int k = 0;
        while (true) {
            if ((i = formatted.indexOf(167, i + 2)) == -1) {
                while (k < len) {
                    if (formatted.charAt(k) == unformattedSubstring.charAt(unformattedEnchantIdx)) {
                        formattedEnchant.append(formatted.charAt(k));
                        if (++unformattedEnchantIdx == unformattedSubstring.length()) {
                            return preEnchantFormat.append((CharSequence)formattedEnchant).toString();
                        }
                    } else {
                        unformattedEnchantIdx = 0;
                        preEnchantFormat = new StringBuilder(TextUtils.mergeFormats(preEnchantFormat.toString(), formattedEnchant.toString()));
                        formattedEnchant = new StringBuilder();
                    }
                    ++k;
                }
                return null;
            }
            while (k < i) {
                if (formatted.charAt(k) == unformattedSubstring.charAt(unformattedEnchantIdx)) {
                    formattedEnchant.append(formatted.charAt(k));
                    if (++unformattedEnchantIdx == unformattedSubstring.length()) {
                        return preEnchantFormat.append((CharSequence)formattedEnchant).toString();
                    }
                } else {
                    unformattedEnchantIdx = 0;
                    preEnchantFormat = new StringBuilder(TextUtils.mergeFormats(preEnchantFormat.toString(), formattedEnchant.toString()));
                    formattedEnchant = new StringBuilder();
                }
                ++k;
            }
            if (i + 1 >= len) continue;
            char formatChar = formatted.charAt(i + 1);
            if (unformattedEnchantIdx == 0) {
                if (styles.indexOf(formatChar) == -1) {
                    preEnchantFormat = new StringBuilder();
                }
                preEnchantFormat.append("\u00a7").append(formatChar);
            } else {
                formattedEnchant.append("\u00a7").append(formatChar);
            }
            k = i + 2;
        }
    }

    private static String mergeFormats(String firstFormat, String secondFormat) {
        if (secondFormat == null || secondFormat.length() == 0) {
            return firstFormat;
        }
        String styles = "kKlLmMnNoO";
        StringBuilder builder = new StringBuilder(firstFormat);
        int i = -2;
        while ((i = secondFormat.indexOf(167, i + 2)) != -1) {
            if (i + 1 >= secondFormat.length()) continue;
            char c = secondFormat.charAt(i + 1);
            if (styles.indexOf(c) == -1) {
                builder = new StringBuilder();
            }
            builder.append("\u00a7").append(c);
        }
        return builder.toString();
    }

    public static long reverseFormat(String str2) {
        String str = str2.toLowerCase();
        String integerPart = str.substring(0, str.length() - 1);
        long multiplier = 1L;
        if (str.endsWith("k")) {
            multiplier = 1000L;
        } else if (str.endsWith("m")) {
            multiplier = 1000000L;
        } else if (str.endsWith("b")) {
            multiplier = 1000000000L;
        } else {
            integerPart = str;
        }
        return (long)(Double.parseDouble(integerPart) * (double)multiplier);
    }

    static {
        suffixes.put(1000, "k");
        suffixes.put(1000000, "M");
        suffixes.put(1000000000, "B");
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }
}

