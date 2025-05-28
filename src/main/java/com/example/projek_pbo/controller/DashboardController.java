package com.example.projek_pbo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.projek_pbo.model.User;
import com.example.projek_pbo.repository.UserRepository;
import com.example.projek_pbo.model.Admin;
import com.example.projek_pbo.repository.AdminRepository;
import com.example.projek_pbo.model.Anggota;
import com.example.projek_pbo.repository.AnggotaRepository;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.BukuRepository;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bookRepository;

    public DashboardController( 
        UserRepository userRepository,
        AdminRepository adminRepository,
        AnggotaRepository anggotaRepository,
        BukuRepository bookRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.anggotaRepository = anggotaRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        // Menampilkan total 
        long totalUser = userRepository.count();
        model.addAttribute("totalUser", totalUser);
        List<User> userList = userRepository.findAll();
        model.addAttribute("user", userList != null ? userList : List.of());

        long totalAdmin = adminRepository.count();
        model.addAttribute("totalAdmin", totalAdmin);
        List<Admin> adminList = adminRepository.findAll();
        model.addAttribute("admin", adminList != null ? adminList : List.of());

        long totalAnggota = anggotaRepository.count();
        model.addAttribute("totalAnggota", totalAnggota);
        List<Anggota> anggotaList = anggotaRepository.findAll();
        model.addAttribute("anggota", anggotaList != null ? anggotaList : List.of());

        long totalBuku = bookRepository.count();
        model.addAttribute("totalBuku", totalBuku);
        List<Buku> bukuList = bookRepository.findAll();
        model.addAttribute("buku", bukuList != null ? bukuList : List.of());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
        }

        return "admin/dashboard";
    }
}