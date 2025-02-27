package com.example.hesapyonetimsistemi.banka.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="hesap")
@Data // getter setter, equals gibi metodları generate ediyo
@AllArgsConstructor //Tüm alanları içeren constructor generate ediyo
@NoArgsConstructor // argümansız constructor sağlıyo
@Builder
public class Hesap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "hesap_sahip_kimlik_no", nullable = false)
    private Long hesapSahipKimlikNo;

    @Column(name = "hesap_sahip_ad", length = 50, nullable = false)
    private String hesapSahipAd;

    @Column(name = "hesap_sahip_soyad", length = 50, nullable = false)
    private String hesapSahipSoyad;

    @Enumerated(EnumType.STRING)
    @Column(name = "hesapturu", nullable = false, length = 10)
    private HesapTuru hesapTuru;

    @Column(name = "bakiye", nullable = false, precision = 9, scale = 2)
    private BigDecimal bakiye;

    @OneToMany(mappedBy = "hesap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HesapHareketleri> hesapHareketleri;
}
