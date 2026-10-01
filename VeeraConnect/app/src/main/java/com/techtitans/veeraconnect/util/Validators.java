package com.techtitans.veeraconnect.util;

import android.util.Patterns;

/** Client-side input validation and sanitising. Server-side rules still apply in Firebase. */
public final class Validators {

    private Validators() { }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    /** At least 8 characters, containing at least one letter and one digit. */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    /** Trims text and strips angle brackets so user text can never be treated as markup. */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>]", "");
    }
}
