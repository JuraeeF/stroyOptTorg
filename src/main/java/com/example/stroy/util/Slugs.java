package com.example.stroy.util;

import java.util.Locale;

/** Nomdan URL uchun qulay slug yasaydi (kirill harflari lotinga o'giriladi). */
public final class Slugs {

    private static final String CYRILLIC = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final String[] LATIN = {
            "a", "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t",
            "u", "f", "kh", "ts", "ch", "sh", "shch", "", "y", "", "e", "yu", "ya"
    };

    private Slugs() {
    }

    public static String slugify(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toLowerCase(Locale.ROOT).toCharArray()) {
            int idx = CYRILLIC.indexOf(c);
            if (idx >= 0) {
                sb.append(LATIN[idx]);
            } else if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                sb.append(c);
            } else {
                sb.append('-');
            }
        }
        String s = sb.toString().replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
        return s.isEmpty() ? "item" : s;
    }
}
