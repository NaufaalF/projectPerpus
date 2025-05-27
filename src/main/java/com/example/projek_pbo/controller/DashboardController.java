package com.example.projek_pbo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.BukuRepository;

import java.util.List;

@Controller
public class DashboardController {

    private final BukuRepository bookRepository;

    @Autowired
    public DashboardController(BukuRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        // Mengambil daftar buku
        List<Buku> bukuList = bookRepository.findAll();
        model.addAttribute("buku", bukuList != null ? bukuList : List.of());

        // Mengambil informasi pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Mendapatkan nama pengguna
            model.addAttribute("username", username); // Menambahkan nama pengguna ke model
        }

        return "admin/dashboard"; // Mengembalikan nama view
    }
}