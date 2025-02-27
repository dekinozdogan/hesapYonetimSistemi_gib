package com.example.hesapyonetimsistemi.controller;

import com.example.hesapyonetimsistemi.banka.controller.HesapController;
import com.example.hesapyonetimsistemi.banka.dto.HesapDto;
import com.example.hesapyonetimsistemi.banka.dto.YeniHesapDto;
import com.example.hesapyonetimsistemi.banka.entity.Hesap;
import com.example.hesapyonetimsistemi.banka.enums.HesapTuru;
import com.example.hesapyonetimsistemi.banka.mappers.YeniHesapMapper;
import com.example.hesapyonetimsistemi.banka.service.HesapService;
import com.example.hesapyonetimsistemi.banka.mappers.HesapMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
@SpringBootTest
public class HesapControllerTest {

    @Autowired
    private HesapController hesapController;

    private MockMvc mockMvc;
    private HesapService hesapService;

    @BeforeEach
    void setUp() {
        hesapService = mock(HesapService.class);
        hesapController = new HesapController(hesapService);
        mockMvc = MockMvcBuilders.standaloneSetup(hesapController).build();
    }

    @Test
    void testGetHesapByTcVeTuru_HesapBulundu() throws Exception {
        Long tcKimlikNo = 35489326112L;
        Hesap hesap = new Hesap();
        hesap.setId(UUID.randomUUID());
        hesap.setHesapSahipAd("Hakan");
        hesap.setHesapSahipSoyad("Serdar");
        hesap.setHesapTuru(HesapTuru.TL);
        hesap.setHesapSahipKimlikNo(tcKimlikNo);
        hesap.setBakiye(BigDecimal.ZERO);

        HesapDto hesapDto = HesapMapper.toDTO(hesap);

        when(hesapService.getHesapListesiByTcTur(tcKimlikNo, HesapTuru.TL)).thenReturn(List.of(hesap));

        mockMvc.perform(get("/hesap/{tcKimlikNo}/{hesapTuru}", tcKimlikNo, HesapTuru.TL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hesapSahipAd").value(hesapDto.getHesapSahipAd()))
                .andExpect(jsonPath("$.hesapSahipSoyad").value(hesapDto.getHesapSahipSoyad()));

        verify(hesapService).getHesapListesiByTcTur(tcKimlikNo, HesapTuru.TL);
    }


    @Test
    void testGetHesapByTcVeTuru_HesapBulunamadi() throws Exception {
        Long tcKimlikNo = 12345678901L;

        when(hesapService.getHesapListesiByTcTur(tcKimlikNo, HesapTuru.TL)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hesap/{tcKimlikNo}/{hesapTuru}", tcKimlikNo, HesapTuru.TL))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHesapEkle() throws Exception {

        Long tcKimlikNo = 35489326112L;
        Hesap hesap = new Hesap();
        hesap.setId(UUID.randomUUID());
        hesap.setHesapSahipAd("Hakan");
        hesap.setHesapSahipSoyad("Serdar");
        hesap.setHesapTuru(HesapTuru.TL);
        hesap.setHesapSahipKimlikNo(tcKimlikNo);
        hesap.setBakiye(BigDecimal.ZERO);

        YeniHesapDto yeniHesapDto = new YeniHesapDto();
        yeniHesapDto.setHesapSahipAd("Hakan");
        yeniHesapDto.setHesapSahipSoyad("Serdar");
        yeniHesapDto.setHesapTuru(HesapTuru.TL);
        yeniHesapDto.setBakiye(BigDecimal.ZERO);
        yeniHesapDto.setHesapSahipTcNo(tcKimlikNo);
        when(hesapService.hesapEkle(any())).thenReturn(hesap);

        mockMvc.perform(post("/hesap/hesapEkle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hesapSahipAd\":\"Hakan\",\"hesapSahipSoyad\":\"Serdar\",\"hesapTuru\":\"TL\",\"hesapSahipTcNo\":\"35489326112\",\"bakiye\":\"0\"}"))
                .andExpect(status().isOk());

        verify(hesapService).hesapEkle(yeniHesapDto);
    }

    @Test
    void testHesapGuncelle() throws Exception {
        UUID hesapId = UUID.randomUUID();
        Long tcKimlikNo = 35489326112L;

        Hesap hesap = new Hesap();
        hesap.setId(hesapId);
        hesap.setHesapSahipAd("Hakan");
        hesap.setHesapSahipSoyad("Serdar");
        hesap.setHesapTuru(HesapTuru.TL);
        hesap.setHesapSahipKimlikNo(tcKimlikNo);
        hesap.setBakiye(BigDecimal.ZERO);

        HesapDto hesapDto = new HesapDto();
        hesapDto.setHesapSahipAd("Hakan");
        hesapDto.setHesapSahipSoyad("Serdar");
        hesapDto.setHesapturu(HesapTuru.TL);
        hesapDto.setBakiye(BigDecimal.ZERO);

        when(hesapService.hesapGuncelle(hesapId, hesapDto)).thenReturn(hesap);

        mockMvc.perform(put("/hesap/hesapGuncelle/{hesapId}", hesapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hesapSahipAd\":\"Hakan\",\"hesapSahipSoyad\":\"Serdar\",\"hesapTuru\":\"TL\",\"hesapSahipTcNo\":\"35489326112\",\"bakiye\":\"0\"}"))
                .andExpect(status().isOk());

        verify(hesapService).hesapGuncelle(hesapId, hesapDto);
    }


    @Test
    void testHesapSil() throws Exception {
        UUID hesapId = UUID.randomUUID();

        mockMvc.perform(delete("/hesap/hesapSil/{hesapId}", hesapId))
                .andExpect(status().isOk());

        verify(hesapService).hesapSil(hesapId);
    }

    @Test
    void testParaYatir() throws Exception {
        UUID hesapId = UUID.randomUUID();
        BigDecimal miktar = BigDecimal.valueOf(1000);
        Hesap hesap = new Hesap();
        hesap.setId(hesapId);
        hesap.setBakiye(BigDecimal.valueOf(1000));

        when(hesapService.paraYatir(hesapId, miktar)).thenReturn(hesap);

        mockMvc.perform(put("/hesap/paraYatir/{hesapId}/{miktar}", hesapId, miktar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hesapId.toString()))
                .andExpect(jsonPath("$.bakiye").value(1000));

        verify(hesapService).paraYatir(hesapId, miktar);
    }


    @Test
    void testParaCek() throws Exception {
        UUID hesapId = UUID.randomUUID();
        BigDecimal miktar = BigDecimal.valueOf(500);
        Hesap hesap = new Hesap();
        hesap.setId(hesapId);
        hesap.setBakiye(BigDecimal.valueOf(1000));

        when(hesapService.paraCek(hesapId, miktar)).thenReturn(hesap);

        mockMvc.perform(put("/hesap/paraCek/{hesapId}/{miktar}", hesapId, miktar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hesapId.toString()))
                .andExpect(jsonPath("$.bakiye").value(1000));

        verify(hesapService).paraCek(hesapId, miktar);
    }

}
