package com.example.hesapyonetimsistemi.banka.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.hesapyonetimsistemi.banka.dto.HesapDto;
import com.example.hesapyonetimsistemi.banka.dto.YeniHesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;
import com.example.hesapyonetimsistemi.banka.repository.HesapHareketleriRepository;
import com.example.hesapyonetimsistemi.banka.repository.HesapRepository;


@Service
public class HesapService {

    private final HesapRepository hesapRepository;
    private final HesapHareketleriRepository hesapHareketleriRepository;
    private final HesapHareketleriService hesapHareketleriService;

    @Autowired
    public HesapService(HesapRepository hesapRepository, HesapHareketleriRepository hesapHareketleriRepository,
                        HesapHareketleriService hesapHareketleriService) {
        this.hesapRepository = hesapRepository;
        this.hesapHareketleriRepository = hesapHareketleriRepository;
        this.hesapHareketleriService = hesapHareketleriService;
    }

    public List<Hesap> getHesapListesiByTcTur(Long hesapSahipKimlikNo, HesapTuru hesapTuru) {
        return hesapRepository.getHesapListesiByTcTur(hesapSahipKimlikNo, hesapTuru);
    }

    public Hesap hesapEkle(YeniHesapDto yeniHesapDto) {
        List<Hesap> mevcutHesap = hesapRepository.getHesapListesiByTcTur(
                yeniHesapDto.getHesapSahipTcNo(), yeniHesapDto.getHesapturu());

        if (mevcutHesap != null) {
            Hesap hesap = Hesap.builder()
                    .hesapSahipKimlikNo(yeniHesapDto.getHesapSahipTcNo())
                    .hesapSahipAd(yeniHesapDto.getHesapSahipAd())
                    .hesapTuru(yeniHesapDto.getHesapturu())
                    .bakiye(BigDecimal.ZERO)
                    .build();

            return hesapRepository.save(hesap);
        } else {
            throw new RuntimeException("Bu kimlik numarası ve hesap türüyle zaten bir hesap mevcut.");
        }
    }


    public Hesap hesapGuncelle(UUID hesapId, HesapDto dto) {
        // Hesabı veritabanından getir, yoksa hata fırlat
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hesap bulunamadı"));

        // Yeni değerleri güncelle
        hesap.setHesapSahipAd(dto.getHesapSahipAd());
        hesap.setHesapSahipSoyad(dto.getHesapSahipSoyad());
        hesap.setBakiye(dto.getBakiye());

        // Güncellenmiş hesap nesnesini kaydet ve döndür
        return hesapRepository.save(hesap);
    }

    // Entity dönmemizin sebebi client update olan bilgileri görmek isteyebilir

    public void hesapSil(UUID hesapId) {
        if (!hesapRepository.existsById(hesapId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hesap bulunamadı.");
        }
        hesapRepository.deleteById(hesapId);
    }

    public Hesap paraYatir(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));

        BigDecimal yeniBakiye = hesap.getBakiye().add(miktar);

        if (yeniBakiye.compareTo(new BigDecimal("9999999")) > 0) {
            throw new RuntimeException("Bakiye en fazla 9.999.999 olabilir.");
        }

        hesapRepository.updateBakiye(hesap.getId(), yeniBakiye);
        hesapHareketleriService.hesapHareketiEkle(hesap, miktar, HareketTuru.YATIRMA);

        return hesap;
    }

    public Hesap paraCek(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));

        BigDecimal yeniBakiye = hesap.getBakiye().subtract(miktar);

        if (yeniBakiye.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Bakiye 0'ın altına düşemez.");
        }

        hesapRepository.updateBakiye(hesap.getId(), yeniBakiye);
        hesapHareketleriService.hesapHareketiEkle(hesap, miktar, HareketTuru.CEKME);

        return hesap;
    }

}
    
