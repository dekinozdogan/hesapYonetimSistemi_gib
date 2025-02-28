package com.example.hesapyonetimsistemi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.hesapyonetimsistemi.banka.service.HesapHareketleriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.entity.HesapHareketleri;
import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;
import com.example.hesapyonetimsistemi.banka.repository.HesapHareketleriRepository;

class HesapHareketleriServiceTest {

    @Mock
    private HesapHareketleriRepository hesapHareketleriRepository;

    @InjectMocks
    private HesapHareketleriService hesapHareketleriService;

    private UUID hesapId;
    private Hesap hesap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hesapId = UUID.randomUUID();
        hesap = new Hesap();
        hesap.setId(hesapId);
    }

    @Test
    void testFindByHesapIdAndHareketTuru() {
        HesapHareketleri hareket1 = HesapHareketleri.builder()
                .id(UUID.randomUUID())
                .hesap(hesap)
                .islemTarihi(LocalDateTime.now())
                .hareketTuru(HareketTuru.YATIRMA)
                .miktar(new BigDecimal("500.00"))
                .build();

        HesapHareketleri hareket2 = HesapHareketleri.builder()
                .id(UUID.randomUUID())
                .hesap(hesap)
                .islemTarihi(LocalDateTime.now())
                .hareketTuru(HareketTuru.YATIRMA)
                .miktar(new BigDecimal("300.00"))
                .build();

        when(hesapHareketleriRepository.findByHesapIdAndHareketTuru(hesapId, HareketTuru.YATIRMA))
                .thenReturn(List.of(hareket1, hareket2));

        // Servis metodunu çağır ve sonucu doğrula
        List<HesapHareketleri> result = hesapHareketleriService.findByHesapIdAndHareketTuru(hesapId, HareketTuru.YATIRMA);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(HareketTuru.YATIRMA, result.get(0).getHareketTuru());
        assertEquals(new BigDecimal("500.00"), result.get(0).getMiktar());
    }

    @Test
    void testHesapHareketiEkle() {

        hesapHareketleriService.hesapHareketiEkle(hesap, new BigDecimal("150.00"), HareketTuru.YATIRMA);
        verify(hesapHareketleriRepository, times(1)).save(any(HesapHareketleri.class));
    }

}
