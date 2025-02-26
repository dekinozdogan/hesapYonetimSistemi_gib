package com.example.hesapyonetimsistemi.banka.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;



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

    public HesapController(HesapService hesapService) {
        this.hesapService = hesapService;
    }

    @GetMapping("/hesap/{tcKimlikNo}/{hesapTuru}")
    public ResponseEntity<HesapDto> getHesapByTcVeTuru( @PathVariable Long tcKimlikNo, @PathVariable HesapTuru hesapTuru) {

    if(isValidTCKN(tcKimlikNo)) {
        List<Hesap> hesapListesi = hesapService.getHesapListesiByTcTur(tcKimlikNo, hesapTuru);

        if (hesapListesi.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bu TC Kimlik No ve hesap türüne ait kayıt bulunamadı");
        } else if (hesapListesi.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bu TC Kimlik No ve hesap türüne ait birden fazla hesap mevcut!");
        }
    
        Hesap hesap = hesapListesi.stream().findFirst().orElse(null);
        return ResponseEntity.ok(HesapMapper.toDTO(hesap));
    } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geçersiz TC Kimlik Numarası!");
    }

    }
    @PostMapping("/hesapEkle")
    public ResponseEntity<HesapDto> hesapEkle( @RequestBody YeniHesapDto yeniHesapDto) {

    Long tcKimlikNo = yeniHesapDto.getHesapSahipTcNo();

    if (isValidTCKN(tcKimlikNo)) {
        Hesap kaydedilenHesap = hesapService.hesapEkle(yeniHesapDto);
        if (kaydedilenHesap == null) {
            return ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok(HesapMapper.toDTO(kaydedilenHesap));
        }  
    } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geçersiz TC Kimlik Numarası.");
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
    public ResponseEntity<HesapDto> hesapGuncelle(@PathVariable UUID hesapId,  @RequestBody HesapDto dto) {
    
    Hesap hesap = hesapService.hesapGuncelle(hesapId, dto);

    if (hesap == null) {
        return ResponseEntity.badRequest().build();
    }else {
        return ResponseEntity.ok(HesapMapper.toDTO(hesap));
        }
    }

    @DeleteMapping("/hesapSil/{hesapId}")
    public ResponseEntity<Map<String, String>> hesapSil(@PathVariable UUID hesapId) {
        hesapService.hesapSil(hesapId);
        return ResponseEntity.ok(Map.of("message", "Hesap başarıyla silindi"));
    }

    @PostMapping("/paraYatir/{hesapId}/{miktar}")
    public ResponseEntity<HesapDto> paraYatir(@PathVariable UUID hesapId, @PathVariable BigDecimal miktar) {
        Hesap paraYatir = hesapService.paraYatir(hesapId, miktar);
        if (paraYatir == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(HesapMapper.toDTO(paraYatir));
            }      
        }

    @PutMapping("/paraCek/{hesapId}/{miktar}")
    public ResponseEntity<HesapDto> paraCek(@PathVariable UUID hesapId, @PathVariable BigDecimal miktar) {
        Hesap paraCek = hesapService.paraCek(hesapId, miktar);
        if (paraCek == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(HesapMapper.toDTO(paraCek));
            }      
        }
    }


    




