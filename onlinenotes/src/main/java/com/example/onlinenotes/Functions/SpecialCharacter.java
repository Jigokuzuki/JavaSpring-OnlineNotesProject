package com.example.onlinenotes.Functions;

import org.springframework.stereotype.Component;

@Component
public class SpecialCharacter {
    public boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";
        for (char c : specialChars.toCharArray()) {
            if (password.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
}
