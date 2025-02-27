package com.example.hesapyonetimsistemi.banka.mappers;

import java.math.BigDecimal;

import com.example.hesapyonetimsistemi.banka.dto.YeniHesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;

public class YeniHesapMapper {

    public static Hesap toEntity(YeniHesapDto yeniHesapDto) {
        return Hesap.builder()
                .hesapSahipKimlikNo(yeniHesapDto.getHesapSahipTcNo())
                .hesapSahipAd(yeniHesapDto.getHesapSahipAd())
                .hesapSahipSoyad(yeniHesapDto.getHesapSahipSoyad())
                .hesapTuru(yeniHesapDto.getHesapTuru())
                .bakiye(BigDecimal.ZERO) 
                .build();
    }
}

