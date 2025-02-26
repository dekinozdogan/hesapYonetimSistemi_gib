package com.example.hesapyonetimsistemi.banka.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.hesapyonetimsistemi.banka.entity.HesapHareketleri;
import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;

@Repository
public interface HesapHareketleriRepository extends JpaRepository<HesapHareketleri, UUID> {

    List<HesapHareketleri> findByHesapIdAndHareketTuru(UUID hesapId, HareketTuru hareketTuru);



}
