package com.example.hesapyonetimsistemi.banka.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum HesapTuru {
    TL,
    DOLAR,
    STERLIN;

    @JsonCreator
    public static HesapTuru fromString(String value) {
        return Arrays.stream(HesapTuru.values())
                .filter(t -> t.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz hesap türü: " + value));
    }
}

