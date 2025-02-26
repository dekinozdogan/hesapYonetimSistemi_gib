package com.example.hesapyonetimsistemi.banka.service;

import java.math.BigDecimal;
import java.util.List;
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
        return null;
    }

    public Hesap hesapEkle(YeniHesapDto yeniHesapDto) {

         // boolean hesapVarMi = hesapRepository.isHesapVarMi(yeniHesapDto.getHesapSahipTcNo(), yeniHesapDto.getHesapturu());

         return null;
      }

    public Hesap hesapGuncelle(UUID hesapId, HesapDto dto) {


        return null;
    }
    // Entity dönmemizin sebebi client update olan bilgileri görmek isteyebilir 
    

    public void hesapSil(UUID hesapId) {
        if (!hesapRepository.existsById(hesapId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hesap bulunamadı.");
        }
        hesapRepository.deleteById(hesapId);
    }


    public Hesap paraYatir(UUID hesapId, BigDecimal miktar) {

      return null;
  }


  public Hesap paraCek(UUID hesapId, BigDecimal miktar) {


    return null;
}
 



}
    
