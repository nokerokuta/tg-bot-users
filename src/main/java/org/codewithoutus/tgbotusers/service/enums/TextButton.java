package org.codewithoutus.tgbotusers.service.enums;

import lombok.Getter;

@Getter
public enum TextButton {

    CONGRATULATE ("Поздравить \uD83E\uDD73"),
    DECLINE ("Отклонить 🚫");
    
    private final String solution;
    
    TextButton(String solution) {
        this.solution = solution;
    }
}
