package com.example.hesapyonetimsistemi.repository;

import com.example.hesapyonetimsistemi.banka.entity.HesapHareketleri;
import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;
import com.example.hesapyonetimsistemi.banka.repository.HesapHareketleriRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HesapHareketleriRepositoryTest {

    @Autowired
    private HesapHareketleriRepository repository;

    @Test
    public void testFindByHesapIdAndHareketTuru() {
        UUID hesapId = UUID.randomUUID();
        HareketTuru hareketTuru = HareketTuru.CEKME;

        HesapHareketleri hareket = new HesapHareketleri();
        hareket.setId(UUID.randomUUID());
        hareket.setId(hesapId);
        hareket.setHareketTuru(hareketTuru);
        repository.save(hareket);

        List<HesapHareketleri> found = repository.findByHesapIdAndHareketTuru(hesapId, hareketTuru);

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getId()).isEqualTo(hesapId);
        assertThat(found.get(0).getHareketTuru()).isEqualTo(hareketTuru);
    }
}
