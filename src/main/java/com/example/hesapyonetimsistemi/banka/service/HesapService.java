package com.example.hesapyonetimsistemi.banka.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final HesapHareketleriService hesapHareketleriService;

    @Autowired
    public HesapService(HesapRepository hesapRepository,
                        HesapHareketleriService hesapHareketleriService) {
        this.hesapRepository = hesapRepository;
        this.hesapHareketleriService = hesapHareketleriService;
    }

    public List<Hesap> getHesapListesiByTcTur(Long hesapSahipKimlikNo, HesapTuru hesapTuru) {
        return hesapRepository.getHesapListesiByTcTur(hesapSahipKimlikNo, hesapTuru);
    }
    @Transactional
    public Hesap hesapEkle(YeniHesapDto yeniHesapDto) {
        List<Hesap> mevcutHesapListesi = hesapRepository.getHesapListesiByTcTur(
                yeniHesapDto.getHesapSahipTcNo(), yeniHesapDto.getHesapTuru());

        if (mevcutHesapListesi.isEmpty()) {
            Hesap hesap = Hesap.builder()
                    .hesapSahipKimlikNo(yeniHesapDto.getHesapSahipTcNo())
                    .hesapSahipAd(yeniHesapDto.getHesapSahipAd())
                    .hesapSahipSoyad(yeniHesapDto.getHesapSahipSoyad())
                    .hesapTuru(yeniHesapDto.getHesapTuru())
                    .bakiye(BigDecimal.ZERO)
                    .build();

            return hesapRepository.save(hesap);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bu kimlik numarası ve hesap türüyle zaten bir hesap mevcut.");
        }
    }
    @Transactional
    public Hesap hesapGuncelle(UUID hesapId, HesapDto dto) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hesap bulunamadı"));
        if(dto.getHesapSahipAd() != null && !dto.getHesapSahipAd().isEmpty()) {
            hesap.setHesapSahipAd(dto.getHesapSahipAd());
        }
        if(dto.getHesapSahipSoyad() != null && !dto.getHesapSahipSoyad().isEmpty()) {
            hesap.setHesapSahipSoyad(dto.getHesapSahipSoyad());
        }
        if(dto.getBakiye() != null) {
            hesap.setBakiye(dto.getBakiye());
        }
        return hesapRepository.save(hesap);
    }

    // Entity dönmemizin sebebi client update olan bilgileri görmek isteyebilir
    @Transactional
    public void hesapSil(UUID hesapId) {
        if (!hesapRepository.existsById(hesapId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hesap bulunamadı.");
        }
        hesapRepository.deleteById(hesapId);
    }
    @Transactional
    public Hesap paraYatir(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new NoSuchElementException("Hesap bulunamadı"));

        BigDecimal yeniBakiye = hesap.getBakiye().add(miktar);

        if (yeniBakiye.compareTo(new BigDecimal("9999999")) > 0) {
            throw new RuntimeException("Bakiye en fazla 9.999.999 olabilir.");
        }

        hesap.setBakiye(yeniBakiye);
        hesapRepository.updateBakiye(hesap.getId(), yeniBakiye);
        hesapHareketleriService.hesapHareketiEkle(hesap, miktar, HareketTuru.YATIRMA);
        return hesap;
    }
    @Transactional
    public Hesap paraCek(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new NoSuchElementException("Hesap bulunamadı"));

        BigDecimal yeniBakiye = hesap.getBakiye().subtract(miktar);

        if (yeniBakiye.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Bakiye 0'ın altına düşemez.");
        }

        hesap.setBakiye(yeniBakiye);
        hesapRepository.updateBakiye(hesap.getId(), yeniBakiye);
        hesapHareketleriService.hesapHareketiEkle(hesap, miktar, HareketTuru.CEKME);
        return hesap;
    }

}
    
