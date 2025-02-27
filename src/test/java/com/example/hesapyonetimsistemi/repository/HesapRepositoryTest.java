package com.example.hesapyonetimsistemi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;

import com.example.hesapyonetimsistemi.banka.repository.HesapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class HesapRepositoryTest {

    @Autowired
    private HesapRepository hesapRepository;

    private Hesap testHesap;

    @BeforeEach
    void setUp() {

        testHesap = Hesap.builder()
                .id(UUID.randomUUID())
                .hesapSahipKimlikNo(12345678901L)
                .hesapSahipAd("Ekin")
                .hesapSahipSoyad("Deren")
                .hesapTuru(HesapTuru.TL)
                .bakiye(new BigDecimal("1000.00"))
                .build();
        hesapRepository.save(testHesap);
    }

    @Test
    void testGetHesapListesiByTcTur() {

        List<Hesap> hesapListesi = hesapRepository.getHesapListesiByTcTur(12345678901L, HesapTuru.TL);
        assertThat(hesapListesi).isNotEmpty();
        assertThat(hesapListesi.get(0).getHesapSahipKimlikNo()).isEqualTo(12345678901L);
        assertThat(hesapListesi.get(0).getHesapTuru()).isEqualTo(HesapTuru.TL);
        assertThat(hesapListesi.get(0).getHesapSahipAd()).isEqualTo("Ekin");
        assertThat(hesapListesi.get(0).getHesapSahipSoyad()).isEqualTo("Deren");
    }

    @Test
    @Transactional
    void testUpdateBakiye() {
        BigDecimal yeniBakiye = new BigDecimal("5000.00");
        hesapRepository.updateBakiye(testHesap.getId(), yeniBakiye);
        Optional<Hesap> guncellenmisHesap = hesapRepository.findById(testHesap.getId());
        assertThat(guncellenmisHesap).isPresent();
        assertThat(guncellenmisHesap.get().getBakiye()).isEqualByComparingTo(yeniBakiye);
    }
}

