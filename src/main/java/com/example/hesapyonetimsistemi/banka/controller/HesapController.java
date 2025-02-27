package com.example.hesapyonetimsistemi.banka.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.hesapyonetimsistemi.banka.dto.HesapDto;
import com.example.hesapyonetimsistemi.banka.dto.YeniHesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;
import com.example.hesapyonetimsistemi.banka.mappers.HesapMapper;
import com.example.hesapyonetimsistemi.banka.service.HesapService;

@RestController
@RequestMapping("/hesap")
public class HesapController {

    private final HesapService hesapService;
    @Autowired
    public HesapController(HesapService hesapService) {
        this.hesapService = hesapService;
    }

    @GetMapping("/{tcKimlikNo}/{hesapTuru}")
    public ResponseEntity<HesapDto> getHesapByTcVeTuru(@PathVariable Long tcKimlikNo, @PathVariable HesapTuru hesapTuru) {
        try {
            if (isValidTCKN(tcKimlikNo)) {
                List<Hesap> hesapListesi = hesapService.getHesapListesiByTcTur(tcKimlikNo, hesapTuru);

                if (hesapListesi.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bu TC Kimlik No ve hesap türüne ait kayıt bulunamadı");
                } else if (hesapListesi.size() > 1) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Bu TC Kimlik No ve hesap türüne ait birden fazla hesap mevcut!");
                }

                Hesap hesap = hesapListesi.stream().findFirst().orElse(null);
                return ResponseEntity.ok(HesapMapper.toDTO(hesap));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TC Kimlik No 11 haneli olmalı ve sadece rakam içermeli");
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Beklenmeyen bir hata oluştu", e);
        }
    }
    @PostMapping("/hesapEkle")
    public ResponseEntity<?> hesapEkle(@RequestBody YeniHesapDto yeniHesapDto) {
        try {
            Hesap kaydedilenHesap = hesapService.hesapEkle(yeniHesapDto);
            return ResponseEntity.ok(HesapMapper.toDTO(kaydedilenHesap));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }
    private boolean isValidTCKN(Long tcKimlikNo) {
        String tcknStr = String.valueOf(tcKimlikNo);

        if (tcknStr.length() != 11) {
            return false;
        }
        int totalOdd = 0, totalEven = 0, total = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(tcknStr.charAt(i));
            if (i % 2 == 0) totalOdd += digit;
            else totalEven += digit;
            total += digit;
        }

        int tenthDigit = ((totalOdd * 7) - totalEven) % 10;
        int eleventhDigit = (total + tenthDigit) % 10;

        return tenthDigit == Character.getNumericValue(tcknStr.charAt(9)) &&
                eleventhDigit == Character.getNumericValue(tcknStr.charAt(10));
    }

    @PutMapping("/hesapGuncelle/{hesapId}")
    public ResponseEntity<?> hesapGuncelle(@PathVariable UUID hesapId, @RequestBody HesapDto dto) {
        try {
            Hesap hesap = hesapService.hesapGuncelle(hesapId, dto);
            return ResponseEntity.ok(HesapMapper.toDTO(hesap));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Hesap Güncellenemedi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hesap güncelleme işlemi başarısız oldu.");
        }
    }
    @DeleteMapping("/hesapSil/{hesapId}")
    public ResponseEntity<Map<String, String>> hesapSil(@PathVariable UUID hesapId) {
        hesapService.hesapSil(hesapId);
        return ResponseEntity.ok(Map.of("message", "Hesap başarıyla silindi"));
    }

    @PutMapping("/paraYatir/{hesapId}/{miktar}")
    public ResponseEntity<?> paraYatir(@PathVariable UUID hesapId, @PathVariable BigDecimal miktar) {
        try {
            Hesap paraYatir = hesapService.paraYatir(hesapId, miktar);
            return ResponseEntity.ok(HesapMapper.toDTO(paraYatir));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            if(e instanceof NoSuchElementException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hata: " + e.getMessage());
            }
        }
    }
    @PutMapping("/paraCek/{hesapId}/{miktar}")
    public ResponseEntity<?> paraCek(@PathVariable UUID hesapId, @PathVariable BigDecimal miktar) {
        try {
            Hesap paraCek = hesapService.paraCek(hesapId, miktar);
            return ResponseEntity.ok(HesapMapper.toDTO(paraCek));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            if(e instanceof NoSuchElementException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hata: " + e.getMessage());
            }
        }
    }
}



    




