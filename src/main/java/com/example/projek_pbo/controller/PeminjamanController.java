package com.example.projek_pbo.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.example.projek_pbo.model.Anggota;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.model.Peminjaman;
import com.example.projek_pbo.repository.PeminjamanRepository;
import com.example.projek_pbo.repository.AnggotaRepository;
import com.example.projek_pbo.repository.BukuRepository;

@Controller
public class PeminjamanController {
    
    private final PeminjamanRepository peminjamanRepository;
    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;

    public PeminjamanController(PeminjamanRepository peminjamanRepository, AnggotaRepository anggotaRepository, BukuRepository bukuRepository) {
        this.peminjamanRepository = peminjamanRepository;
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
    }

    @GetMapping("/tabel-peminjaman")
    public String tampilkanTabelPeminjaman(Model model) {
        // Di sini Anda bisa mengambil daftar peminjaman dari repository jika diperlukan
        List<Peminjaman> daftarPeminjaman = peminjamanRepository.findAll();
        model.addAttribute("daftarPeminjaman", daftarPeminjaman);
        return "admin/tabel peminjaman/peminjaman";
    }
    
    @GetMapping("/upload-peminjaman")
    public String showUploadForm(Model model) {
        // Ambil daftar anggota dan buku untuk ditampilkan di form
        List<Anggota> daftarAnggota = anggotaRepository.findAll();
        List<Buku> daftarBuku = bukuRepository.findAll();
        
        model.addAttribute("daftarAnggota", daftarAnggota);
        model.addAttribute("daftarBuku", daftarBuku);
        
        return "admin/tabel peminjaman/uploadPeminjaman";
    }

    // POST request untuk menambahkan peminjaman
    @PostMapping("/upload-peminjaman")
public String uploadPeminjaman(@RequestParam("anggota_id") Long anggota_id,
                              @RequestParam("buku_id") Long buku_id,
                              @RequestParam("tanggal_pinjam") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal_pinjam,
                              @RequestParam("tanggal_kembali") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal_kembali,
                              RedirectAttributes redirectAttributes) {

    Anggota anggota = anggotaRepository.findById(anggota_id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid anggota ID: " + anggota_id));

    Buku buku = bukuRepository.findById(buku_id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid buku ID: " + buku_id));

    // Cek status buku
    if (!buku.isAvailable()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Buku tidak tersedia untuk dipinjam.");
        return "redirect:/upload-peminjaman"; // Kembali ke form jika tidak tersedia
    }

    // Update status buku jadi tidak tersedia
    buku.setAvailable(false);
    bukuRepository.save(buku);

    // Simpan data peminjaman
    Peminjaman peminjaman = new Peminjaman();
    peminjaman.setAnggota(anggota);
    peminjaman.setBuku(buku);
    peminjaman.setTanggal_pinjam(tanggal_pinjam);
    peminjaman.setTanggal_kembali(tanggal_kembali);

    peminjamanRepository.save(peminjaman);

    redirectAttributes.addFlashAttribute("successMessage", "Peminjaman berhasil disimpan.");
    return "redirect:/tabel-peminjaman";
}



}
