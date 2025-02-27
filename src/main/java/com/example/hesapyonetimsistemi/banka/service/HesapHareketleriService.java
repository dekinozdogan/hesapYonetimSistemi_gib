package com.example.hesapyonetimsistemi.banka.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.entity.HesapHareketleri;
import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;
import com.example.hesapyonetimsistemi.banka.repository.HesapHareketleriRepository;

@Service
public class HesapHareketleriService {
    private final HesapHareketleriRepository hesapHareketleriRepository;

    @Autowired
    public HesapHareketleriService(HesapHareketleriRepository hesapHareketleriRepository) {
        this.hesapHareketleriRepository = hesapHareketleriRepository;
    }
    public List<HesapHareketleri> findByHesapIdAndHareketTuru(UUID id, HareketTuru hareketTuru) {
        return hesapHareketleriRepository.findByHesapIdAndHareketTuru(id, hareketTuru);
    }
    public void hesapHareketiEkle(Hesap hesap, BigDecimal miktar, HareketTuru hareketTuru) {
        HesapHareketleri hareket = HesapHareketleri.builder()
                .hesap(hesap)
                .islemTarihi(LocalDateTime.now())
                .hareketTuru(hareketTuru)
                .miktar(miktar)
                .build();

        hesapHareketleriRepository.save(hareket);
    }
}
