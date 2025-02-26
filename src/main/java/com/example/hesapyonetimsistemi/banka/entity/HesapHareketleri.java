package com.example.hesapyonetimsistemi.banka.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.example.hesapyonetimsistemi.banka.enums.HareketTuru;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="hesap_hareketleri")
@Data // getter setter, equals gibi metodları generate ediyo
@AllArgsConstructor //Tüm alanları içeren constructor generate ediyo
@NoArgsConstructor // argümansız constructor sağlıyo
@Builder
public class HesapHareketleri {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "hesap_id", nullable = false, columnDefinition = "BINARY(16)")
    private Hesap hesap;

    @Column(name = "islem_tarihi", nullable = false)
    private LocalDateTime islemTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "hareket_turu", nullable = false, length = 10)
    private HareketTuru hareketTuru;

    @Column(name = "miktar", nullable = false, precision = 9, scale = 2)
    private BigDecimal miktar;
}
