package com.example.hesapyonetimsistemi.banka.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;

@Repository
public interface HesapRepository extends JpaRepository<Hesap, UUID> {

    //boolean isHesapVarMi(Long hesapSahipKimlikNo, HesapTuru hesapTuru);

    @Query("SELECT h FROM Hesap h WHERE h.hesapSahipKimlikNo = :hesapSahipKimlikNo AND h.hesapTuru = :hesapTuru")
    List<Hesap> getHesapListesiByTcTur(@Param("hesapSahipKimlikNo") Long tcKimlikNo, @Param("hesapTuru") HesapTuru hesapTuru);

    @Modifying
    @Transactional
    @Query("UPDATE Hesap h SET h.bakiye = :bakiye WHERE h.id = :id")
    void updateBakiye(@Param("id") UUID id, @Param("bakiye") BigDecimal bakiye);




}

