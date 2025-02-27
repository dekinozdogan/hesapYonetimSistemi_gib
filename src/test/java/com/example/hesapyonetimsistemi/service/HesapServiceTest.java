package com.example.hesapyonetimsistemi.service;

import com.example.hesapyonetimsistemi.banka.dto.HesapDto;
import com.example.hesapyonetimsistemi.banka.dto.YeniHesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;
import com.example.hesapyonetimsistemi.banka.repository.HesapRepository;
import com.example.hesapyonetimsistemi.banka.service.HesapHareketleriService;
import com.example.hesapyonetimsistemi.banka.service.HesapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class HesapServiceTest {

    @Mock
    private HesapRepository hesapRepository;

    @Mock
    private HesapHareketleriService hesapHareketleriService;

    @InjectMocks
    private HesapService hesapService;

    private Hesap testHesap;
    private UUID hesapId;
    private Long tcKimlikNo;

    @BeforeEach
    void setUp() {
        hesapId = UUID.randomUUID();
        tcKimlikNo = 12345678901L;

        testHesap = Hesap.builder()
                .id(hesapId)
                .hesapSahipKimlikNo(tcKimlikNo)
                .hesapSahipAd("Ekin")
                .hesapSahipSoyad("Deren")
                .hesapTuru(HesapTuru.TL)
                .bakiye(BigDecimal.ZERO)
                .build();
    }

    @Test
    void testGetHesapListesiByTcTur_HesapVar() {
        when(hesapRepository.getHesapListesiByTcTur(tcKimlikNo, HesapTuru.DOLAR))
                .thenReturn(List.of(testHesap));

        List<Hesap> hesapList = hesapService.getHesapListesiByTcTur(tcKimlikNo, HesapTuru.DOLAR);

        assertFalse(hesapList.isEmpty());
        assertEquals(1, hesapList.size());
        assertEquals(testHesap.getHesapSahipKimlikNo(), hesapList.get(0).getHesapSahipKimlikNo());
    }

    @Test
    void testHesapEkle_HesapZatenVar() {
        YeniHesapDto yeniHesapDto = new YeniHesapDto(tcKimlikNo, "Ekin", "Deren", HesapTuru.TL, BigDecimal.ZERO);

        when(hesapRepository.getHesapListesiByTcTur(tcKimlikNo, HesapTuru.TL))
                .thenReturn(List.of(testHesap));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                hesapService.hesapEkle(yeniHesapDto)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void testHesapGuncelle_HesapBulunamazsa() {
        HesapDto hesapDto = new HesapDto();
        hesapDto.setHesapSahipAd("Ekin");
        hesapDto.setHesapSahipAd("Deren");
        hesapDto.setBakiye(new BigDecimal("500"));

        when(hesapRepository.findById(hesapId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                hesapService.hesapGuncelle(hesapId, hesapDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testHesapSil_HesapBulunamazsa() {
        when(hesapRepository.existsById(hesapId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                hesapService.hesapSil(hesapId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testParaYatir_BakiyeSiniriAsilamaz() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal fazlaMiktar = new BigDecimal("10000000");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                hesapService.paraYatir(hesapId, fazlaMiktar)
        );

        assertEquals("Bakiye en fazla 9.999.999 olabilir.", exception.getMessage());
    }

    @Test
    void testParaCek_BakiyeSifirinAltinaDusmemeli() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal cekilecekMiktar = new BigDecimal("100");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                hesapService.paraCek(hesapId, cekilecekMiktar)
        );

        assertEquals("Bakiye 0'ın altına düşemez.", exception.getMessage());
    }
}
