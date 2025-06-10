package com.example.projek_pbo.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.BukuRepository;

@Controller
public class HomeController {

    private final BukuRepository bookRepository;

    public HomeController(BukuRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Buku> bukuList = bookRepository.findAll();
        model.addAttribute("buku", bukuList != null ? bukuList : List.of());

        // Mengambil informasi pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
        }
        return "user/homepage";
    }

    // Menampilkan cover buku
    @GetMapping("/cover/{id}")
    public ResponseEntity<byte[]> getCover(@PathVariable Long id) {
        Buku buku = bookRepository.findById(id).orElseThrow();
        byte[] image = buku.getCover();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    // Menampilkan detail buku sesuai ID
    @GetMapping("/home/buku/{id}")
    public String detailBuku(@PathVariable Long id, Model model) {
        Buku buku = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
        model.addAttribute("buku", buku);
        return "user/detailBuku";
    }
}