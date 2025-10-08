package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test untuk ValidationUtils")
public class ValidationUtilsTest {

    // ============================
    // isValidKodeProduk
    // ============================
    @Test
    void testIsValidKodeProduk_NullDanKosong() {
        assertFalse(ValidationUtils.isValidKodeProduk(null));
        assertFalse(ValidationUtils.isValidKodeProduk(""));
        assertFalse(ValidationUtils.isValidKodeProduk("   "));
    }

    @Test
    void testIsValidKodeProduk_FormatSalah() {
        assertFalse(ValidationUtils.isValidKodeProduk("AB")); // kurang dari 3
        assertFalse(ValidationUtils.isValidKodeProduk("ABCDEFGHIJK")); // lebih dari 10
        assertFalse(ValidationUtils.isValidKodeProduk("ABC@")); // karakter tidak valid
    }

    @Test
    void testIsValidKodeProduk_Valid() {
        assertTrue(ValidationUtils.isValidKodeProduk("ABC123"));
    }

    // ============================
    // isValidNama
    // ============================
    @Test
    void testIsValidNama_NullDanKosong() {
        assertFalse(ValidationUtils.isValidNama(null));
        assertFalse(ValidationUtils.isValidNama(""));
        assertFalse(ValidationUtils.isValidNama("  "));
    }

    @Test
    void testIsValidNama_PanjangKurangDari3() {
        assertFalse(ValidationUtils.isValidNama("AB"));
    }

    @Test
    void testIsValidNama_PanjangLebihDari100() {
        String panjang = "A".repeat(101);
        assertFalse(ValidationUtils.isValidNama(panjang));
    }

    @Test
    void testIsValidNama_Valid() {
        assertTrue(ValidationUtils.isValidNama("Nama Produk"));
    }

    // ============================
    // isValidHarga
    // ============================
    @Test
    void testIsValidHarga() {
        assertFalse(ValidationUtils.isValidHarga(0));
        assertFalse(ValidationUtils.isValidHarga(-5));
        assertTrue(ValidationUtils.isValidHarga(10.5));
    }

    // ============================
    // isValidStok
    // ============================
    @Test
    void testIsValidStok() {
        assertTrue(ValidationUtils.isValidStok(0));
        assertTrue(ValidationUtils.isValidStok(5));
        assertFalse(ValidationUtils.isValidStok(-1));
    }

    // ============================
    // isValidStokMinimum
    // ============================
    @Test
    void testIsValidStokMinimum() {
        assertTrue(ValidationUtils.isValidStokMinimum(0));
        assertTrue(ValidationUtils.isValidStokMinimum(5));
        assertFalse(ValidationUtils.isValidStokMinimum(-2));
    }

    // ============================
    // isValidPersentase
    // ============================
    @Test
    void testIsValidPersentase() {
        assertFalse(ValidationUtils.isValidPersentase(-1));
        assertTrue(ValidationUtils.isValidPersentase(0));
        assertTrue(ValidationUtils.isValidPersentase(50));
        assertTrue(ValidationUtils.isValidPersentase(100));
        assertFalse(ValidationUtils.isValidPersentase(101));
    }

    // ============================
    // isValidKuantitas
    // ============================
    @Test
    void testIsValidKuantitas() {
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-1));
        assertTrue(ValidationUtils.isValidKuantitas(5));
    }

    // ============================
    // isValidKategori
    // ============================
    @Test
    void testIsValidKategori_Null() {
        assertFalse(ValidationUtils.isValidKategori(null));
    }

    @Test
    void testIsValidKategori_KodeAtauNamaTidakValid() {
        Kategori kategori = new Kategori("A", "Na", "Deskripsi");
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    void testIsValidKategori_DeskripsiTerlaluPanjang() {
        Kategori kategori = new Kategori("K01", "Nama", "A".repeat(501));
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    void testIsValidKategori_ValidDanDeskripsiNull() {
        Kategori kategori = new Kategori("K01", "Nama Valid", null);
        assertTrue(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    void testIsValidKategori_ValidDenganDeskripsiPendek() {
        Kategori kategori = new Kategori("K01", "Nama Valid", "Deskripsi singkat");
        assertTrue(ValidationUtils.isValidKategori(kategori));
    }

    // ============================
    // isValidProduk
    // ============================
    @Test
    void testIsValidProduk_Null() {
        assertFalse(ValidationUtils.isValidProduk(null));
    }

    @Test
    void testIsValidProduk_FieldTidakValid() {
        Produk produk = new Produk();
        produk.setKode("A"); // invalid
        produk.setNama("B"); // invalid
        produk.setKategori("C"); // invalid
        produk.setHarga(-10); // invalid
        produk.setStok(-1); // invalid
        produk.setStokMinimum(-2); // invalid
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    @Test
    void testIsValidProduk_Valid() {
        Produk produk = new Produk();
        produk.setKode("P123");
        produk.setNama("Laptop Asus");
        produk.setKategori("Elektronik");
        produk.setHarga(1500000);
        produk.setStok(10);
        produk.setStokMinimum(5);

        assertTrue(ValidationUtils.isValidProduk(produk));
    }
}
