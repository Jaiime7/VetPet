package com.insitutosanjuandelacruz.vetpet.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEmailPassword {
    public ValidateEmailPassword() {
    }

    public boolean validateEmail(String email) {
        String patronEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(patronEmail);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        // La contraseña debe tener al menos 8 caracteres.
        if (password.length() < 8) {
            return false;
        }
        // La contraseña debe contener al menos una letra minúscula.
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // La contraseña debe contener al menos una letra mayúscula.
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // La contraseña debe contener al menos un número.
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        // Si la contraseña ha pasado todas las comprobaciones, es válida.
        return true;
    }
}
