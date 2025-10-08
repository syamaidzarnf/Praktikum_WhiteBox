package com.praktikum.whitebox.service;
import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("Test Service Inventaris dengan Mocking")
public class ServiceInventarisTest {
    @Mock
    private RepositoryProduk mockRepositoryProduk;
    private ServiceInventaris serviceInventaris;
    private Produk produkTest;
    @BeforeEach
    void setUp() {
        serviceInventaris = new ServiceInventaris(mockRepositoryProduk);
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik",
                15000000, 10, 5);
    }
    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);
// Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);
// Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }
    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
// Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);
// Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
    }
    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001",
                5)).thenReturn(true);
// Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);
// Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }
    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
// Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 15);
// Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(),
                anyInt());
    }
    @Test
    @DisplayName("Hitung total nilai inventaris")
    void testHitungTotalNilaiInventaris() {
// Arrange
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik",
                10000000, 2, 1);
        Produk produk2 = new Produk("PROD002", "Mouse", "Elektronik",
                500000, 5, 2);
        Produk produkNonAktif = new Produk("PROD003", "Keyboard",
                "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);
        List<Produk> semuaProduk = Arrays.asList(produk1, produk2,
                produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

// Act
        double totalNilai =
                serviceInventaris.hitungTotalNilaiInventaris();
// Assert
        double expected = (10000000 * 2) + (500000 * 5); // hanya produkaktif
        assertEquals(expected, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }
    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
// Arrange
        Produk produkStokAman = new Produk("PROD001", "Laptop",
                "Elektronik", 10000000, 10, 5);
        Produk produkStokMenipis = new Produk("PROD002", "Mouse",
                "Elektronik", 500000, 3, 5);
        List<Produk> produkMenipis =
                Collections.singletonList(produkStokMenipis);
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);
// Act
        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();
// Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }
    @Test
    @DisplayName("Tambah produk gagal - produk tidak valid")
    void testTambahProdukGagalProdukTidakValid() {
        // Produk invalid (misal null)
        Produk produkInvalid = null;
        boolean hasil = serviceInventaris.tambahProduk(produkInvalid);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - kode produk tidak valid")
    void testHapusProdukKodeTidakValid() {
        boolean hasil = serviceInventaris.hapusProduk("");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - produk tidak ditemukan")
    void testHapusProdukTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        boolean hasil = serviceInventaris.hapusProduk("PROD001");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - stok masih ada")
    void testHapusProdukGagalMasihAdaStok() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        boolean hasil = serviceInventaris.hapusProduk("PROD001");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk berhasil - stok 0")
    void testHapusProdukBerhasil() {
        produkTest.setStok(0);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.hapus("PROD001")).thenReturn(true);
        boolean hasil = serviceInventaris.hapusProduk("PROD001");
        assertTrue(hasil);
    }

    @Test
    @DisplayName("Update stok gagal - kode tidak valid")
    void testUpdateStokKodeTidakValid() {
        boolean hasil = serviceInventaris.updateStok("", 10);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Update stok gagal - stok baru negatif")
    void testUpdateStokNegatif() {
        boolean hasil = serviceInventaris.updateStok("PROD001", -5);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Update stok gagal - produk tidak ditemukan")
    void testUpdateStokProdukTidakAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        boolean hasil = serviceInventaris.updateStok("PROD001", 5);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Update stok berhasil")
    void testUpdateStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);
        boolean hasil = serviceInventaris.updateStok("PROD001", 5);
        assertTrue(hasil);
    }

    @Test
    @DisplayName("Masuk stok gagal - kode tidak valid")
    void testMasukStokKodeTidakValid() {
        boolean hasil = serviceInventaris.masukStok("", 10);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Masuk stok gagal - jumlah <= 0")
    void testMasukStokJumlahTidakValid() {
        boolean hasil = serviceInventaris.masukStok("PROD001", 0);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Masuk stok gagal - produk tidak aktif atau tidak ditemukan")
    void testMasukStokProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        boolean hasil = serviceInventaris.masukStok("PROD001", 5);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Masuk stok berhasil")
    void testMasukStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 15)).thenReturn(true);
        boolean hasil = serviceInventaris.masukStok("PROD001", 5);
        assertTrue(hasil);
    }

    @Test
    @DisplayName("Cari produk by kode gagal - kode tidak valid")
    void testCariProdukByKodeInvalid() {
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("");
        assertFalse(hasil.isPresent());
    }

    @Test
    @DisplayName("Cari produk by kode berhasil")
    void testCariProdukByKodeBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");
        assertTrue(hasil.isPresent());
    }

    @Test
    @DisplayName("Cari produk by nama dan kategori")
    void testCariProdukByNamaDanKategori() {
        when(mockRepositoryProduk.cariByNama("Laptop")).thenReturn(List.of(produkTest));
        when(mockRepositoryProduk.cariByKategori("Elektronik")).thenReturn(List.of(produkTest));

        assertEquals(1, serviceInventaris.cariProdukByNama("Laptop").size());
        assertEquals(1, serviceInventaris.cariProdukByKategori("Elektronik").size());
    }

    @Test
    @DisplayName("Get produk stok habis")
    void testGetProdukStokHabis() {
        List<Produk> produkHabis = List.of(new Produk("PROD002", "Mouse", "Elektronik", 50000, 0, 2));
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(produkHabis);
        List<Produk> hasil = serviceInventaris.getProdukStokHabis();
        assertEquals(1, hasil.size());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }

    @Test
    @DisplayName("Hitung total stok hanya produk aktif")
    void testHitungTotalStok() {
        Produk aktif1 = new Produk("P1", "A", "K1", 1000, 10, 1);
        Produk aktif2 = new Produk("P2", "B", "K1", 2000, 5, 2);
        Produk nonAktif = new Produk("P3", "C", "K2", 3000, 20, 1);
        nonAktif.setAktif(false);
        when(mockRepositoryProduk.cariSemua()).thenReturn(List.of(aktif1, aktif2, nonAktif));
        int total = serviceInventaris.hitungTotalStok();
        assertEquals(15, total);
        verify(mockRepositoryProduk).cariSemua();
    }
}