package com.praktikum.whitebox.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test untuk class Kategori")
public class KategoriTest {

    @Test
    @DisplayName("Test konstruktor tanpa parameter dan setter/getter")
    void testDefaultConstructorAndSetterGetter() {
        Kategori kategori = new Kategori();
        kategori.setKode("K001");
        kategori.setNama("Elektronik");
        kategori.setDeskripsi("Kategori barang elektronik");
        kategori.setAktif(true);

        assertEquals("K001", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Kategori barang elektronik", kategori.getDeskripsi());
        assertTrue(kategori.isAktif());
    }

    @Test
    @DisplayName("Test konstruktor dengan parameter")
    void testConstructorWithParameters() {
        Kategori kategori = new Kategori("K002", "Pakaian", "Kategori pakaian");
        assertEquals("K002", kategori.getKode());
        assertEquals("Pakaian", kategori.getNama());
        assertEquals("Kategori pakaian", kategori.getDeskripsi());
        assertTrue(kategori.isAktif()); // default aktif = true
    }

    @Test
    @DisplayName("Test equals() - objek sama")
    void testEqualsSameObject() {
        Kategori kategori = new Kategori("K001", "Elektronik", "Deskripsi");
        assertTrue(kategori.equals(kategori)); // objek sama
    }

    @Test
    @DisplayName("Test equals() - objek null")
    void testEqualsNullObject() {
        Kategori kategori = new Kategori("K001", "Elektronik", "Deskripsi");
        assertFalse(kategori.equals(null));
    }

    @Test
    @DisplayName("Test equals() - kelas berbeda")
    void testEqualsDifferentClass() {
        Kategori kategori = new Kategori("K001", "Elektronik", "Deskripsi");
        assertFalse(kategori.equals("StringBiasa"));
    }

    @Test
    @DisplayName("Test equals() - kode sama harus true")
    void testEqualsKodeSama() {
        Kategori k1 = new Kategori("K001", "Elektronik", "Deskripsi");
        Kategori k2 = new Kategori("K001", "Barang", "Deskripsi lain");
        assertTrue(k1.equals(k2));
    }

    @Test
    @DisplayName("Test equals() - kode berbeda harus false")
    void testEqualsKodeBerbeda() {
        Kategori k1 = new Kategori("K001", "Elektronik", "Deskripsi");
        Kategori k2 = new Kategori("K002", "Barang", "Deskripsi lain");
        assertFalse(k1.equals(k2));
    }

    @Test
    @DisplayName("Test hashCode() berdasarkan kode")
    void testHashCode() {
        Kategori k1 = new Kategori("K001", "Elektronik", "Deskripsi");
        Kategori k2 = new Kategori("K001", "Barang", "Deskripsi lain");
        assertEquals(k1.hashCode(), k2.hashCode());
    }

    @Test
    @DisplayName("Test toString() menghasilkan string berisi semua field")
    void testToString() {
        Kategori kategori = new Kategori("K003", "Mainan", "Kategori mainan anak");
        String hasil = kategori.toString();

        assertTrue(hasil.contains("K003"));
        assertTrue(hasil.contains("Mainan"));
        assertTrue(hasil.contains("Kategori mainan anak"));
        assertTrue(hasil.contains("aktif=true"));
    }
}
