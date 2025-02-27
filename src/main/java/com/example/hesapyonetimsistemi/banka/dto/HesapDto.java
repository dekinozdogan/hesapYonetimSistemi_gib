package com.example.hesapyonetimsistemi.banka.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HesapDto {
    private UUID id;
    private String hesapSahipAd;
    private String hesapSahipSoyad;
    private HesapTuru hesapturu;
    private BigDecimal bakiye;
}
