package com.example.hesapyonetimsistemi.banka.mappers;

import com.example.hesapyonetimsistemi.banka.dto.HesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;

public class HesapMapper {

    public static HesapDto toDTO(Hesap hesap) {
        return new HesapDto(
            hesap.getId(),
            hesap.getHesapSahipAd(),
            hesap.getHesapSahipSoyad(),
            hesap.getHesapTuru(),
            hesap.getBakiye()
        );
    }

    public static Hesap toEntity(HesapDto hesapDto) {
        return new Hesap(
            hesapDto.getId(),
            null, 
            hesapDto.getHesapSahipAd(),
            hesapDto.getHesapSahipSoyad(),
            hesapDto.getHesapturu(),
            hesapDto.getBakiye(),
            null
        );
    }
}

