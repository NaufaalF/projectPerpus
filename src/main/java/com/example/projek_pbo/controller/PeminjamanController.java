package com.example.projek_pbo.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.security.Principal;
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

    public PeminjamanController(PeminjamanRepository peminjamanRepository, AnggotaRepository anggotaRepository,
            BukuRepository bukuRepository) {
        this.peminjamanRepository = peminjamanRepository;
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
    }

    @GetMapping("/tabel-peminjaman")
    public String tampilkanTabelPeminjaman(Model model) {
        List<Peminjaman> daftarPeminjaman = peminjamanRepository.findAll();
        model.addAttribute("daftarPeminjaman", daftarPeminjaman);
        return "admin/tabel peminjaman/peminjaman";
    }

    @GetMapping("/upload-peminjaman")
    public String showUploadForm(Model model) {
        List<Anggota> daftarAnggota = anggotaRepository.findAll();
        List<Buku> daftarBuku = bukuRepository.findAll();

        model.addAttribute("daftarAnggota", daftarAnggota);
        model.addAttribute("daftarBuku", daftarBuku);

        return "admin/tabel peminjaman/uploadPeminjaman";
    }

    @PostMapping("/upload-peminjaman")
    public String uploadPeminjaman(@RequestParam("anggota_id") Long anggotaId,
            @RequestParam("buku_id") Long bukuId,
            @RequestParam("tanggal_pinjam") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalPinjam,
            @RequestParam("tanggal_kembali") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalKembali,
            @RequestParam("status_peminjaman") String statusPeminjaman,
            RedirectAttributes redirectAttributes) {

        Anggota anggota = anggotaRepository.findById(anggotaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid anggota ID: " + anggotaId));

        Buku buku = bukuRepository.findById(bukuId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid buku ID: " + bukuId));

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
        peminjaman.setTanggal_pinjam(tanggalPinjam);
        peminjaman.setTanggal_kembali(tanggalKembali);
        peminjaman.setStatus_peminjaman(Peminjaman.Status.valueOf(statusPeminjaman));

        peminjamanRepository.save(peminjaman);

        redirectAttributes.addFlashAttribute("successMessage", "Peminjaman berhasil disimpan.");
        return "redirect:/tabel-peminjaman";
    }

    @GetMapping("/edit-peminjaman/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid peminjaman ID: " + id));

        List<Anggota> daftarAnggota = anggotaRepository.findAll();
        List<Buku> daftarBuku = bukuRepository.findAll();

        model.addAttribute("peminjaman", peminjaman);
        model.addAttribute("daftarAnggota", daftarAnggota);
        model.addAttribute("daftarBuku", daftarBuku);
        return "admin/tabel peminjaman/editPeminjaman";
    }

    @PostMapping("/edit-peminjaman/{id}")
    public String updatePeminjaman(
            @PathVariable("id") Long id,
            @RequestParam("anggota_id") Long anggotaId,
            @RequestParam("buku_id") Long bukuId,
            @RequestParam("tanggal_pinjam") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalPinjam,
            @RequestParam("tanggal_kembali") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalKembali,
            @RequestParam("status_peminjaman") String statusPeminjaman,
            RedirectAttributes redirectAttributes) {

        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid peminjaman ID: " + id));

        Anggota anggota = anggotaRepository.findById(anggotaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid anggota ID: " + anggotaId));

        Buku bukuLama = peminjaman.getBuku();
        Buku bukuBaru = bukuRepository.findById(bukuId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid buku ID: " + bukuId));

        // Jika buku diganti, ubah status buku lama & baru
        if (!bukuLama.getId().equals(bukuBaru.getId())) {
            bukuLama.setAvailable(true);
            bukuRepository.save(bukuLama);

            if (!bukuBaru.isAvailable()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Buku baru tidak tersedia.");
                return "redirect:/edit-peminjaman/" + id;
            }

            bukuBaru.setAvailable(false); // buku baru jadi tidak tersedia
            bukuRepository.save(bukuBaru);

            peminjaman.setBuku(bukuBaru);
        }

        peminjaman.setAnggota(anggota);
        peminjaman.setTanggal_pinjam(tanggalPinjam);
        peminjaman.setTanggal_kembali(tanggalKembali);
        peminjaman.setStatus_peminjaman(Peminjaman.Status.valueOf(statusPeminjaman));

        peminjamanRepository.save(peminjaman);
        redirectAttributes.addFlashAttribute("successMessage", "Peminjaman berhasil diperbarui.");
        return "redirect:/tabel-peminjaman";
    }

    @GetMapping("/delete-peminjaman/{id}")
    public String deleteBuku(@PathVariable("id") Long id) {
        peminjamanRepository.deleteById(id);
        return "redirect:/tabel-peminjaman";
    }

    @PostMapping("/peminjaman/konfirmasi/{id}")
    public String konfirmasi(@PathVariable Long id,
            @RequestParam("tanggal_kembali") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggalKembali) {
        Peminjaman p = peminjamanRepository.findById(id).orElseThrow();
        p.setStatus_peminjaman(Peminjaman.Status.DIPINJAM);
        p.setTanggal_pinjam(LocalDate.now());
        p.setTanggal_kembali(tanggalKembali);
        peminjamanRepository.save(p);
        return "redirect:/tabel-peminjaman";
    }

    @PostMapping("/peminjaman/selesai/{id}")
    public String selesai(@PathVariable Long id) {
        Peminjaman p = peminjamanRepository.findById(id).orElseThrow();

        // Set status peminjaman selesai dan tanggal kembali
        p.setStatus_peminjaman(Peminjaman.Status.SELESAI);
        peminjamanRepository.save(p);

        // Set buku menjadi available kembali
        Buku buku = p.getBuku();
        if (buku != null) {
            buku.setAvailable(true);
            bukuRepository.save(buku);
        }

        return "redirect:/tabel-peminjaman";
    }

    // ============================== For User ==============================

    @PostMapping("/tambah-peminjaman-user")
    public String tambahPeminjaman(@RequestParam("buku_id") Long bukuId,
            @RequestParam("anggota_username") String anggotaUsername,
            @RequestParam("status_peminjaman") String statusPeminjaman,
            RedirectAttributes redirectAttributes) {

        Anggota anggota = anggotaRepository.findByUsername(anggotaUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid anggota username: " + anggotaUsername));
        Buku buku = bukuRepository.findById(bukuId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid buku ID: " + bukuId));

        // Cek status buku
        if (!buku.isAvailable()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Buku tidak tersedia untuk dipinjam.");
            return "redirect:/detail-buku/" + bukuId; // Kembali ke detail buku jika tidak tersedia
        }

        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setBuku(buku);
        peminjaman.setAnggota(anggota);
        peminjaman.setStatus_peminjaman(Peminjaman.Status.valueOf(statusPeminjaman));

        Peminjaman saved = peminjamanRepository.save(peminjaman);

        // Update status buku jadi tidak tersedia
        buku.setAvailable(false);
        bukuRepository.save(buku);

        redirectAttributes.addFlashAttribute("idPeminjaman", saved.getId());
        return "redirect:/home/buku/" + bukuId;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/home/peminjaman")
    public String tampilkanRiwayatPinjam(Model model, Principal principal) {
        String username = principal.getName();
        List<Peminjaman> daftarPeminjaman = peminjamanRepository.findByAnggotaUsername(username);
        model.addAttribute("daftarPeminjaman", daftarPeminjaman);
        return "user/peminjamanUser";
    }
}
