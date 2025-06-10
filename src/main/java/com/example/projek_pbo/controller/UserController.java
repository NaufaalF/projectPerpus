package com.example.projek_pbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;

import com.example.projek_pbo.model.User;
import com.example.projek_pbo.repository.UserRepository;

import com.example.projek_pbo.model.Admin;
import com.example.projek_pbo.repository.AdminRepository;

import com.example.projek_pbo.model.Anggota;
import com.example.projek_pbo.repository.AnggotaRepository;

@Controller
public class UserController {
    private final UserRepository userRepository;
    private final AnggotaRepository anggotaRepository;
    private final AdminRepository adminRepository;

    public UserController(
        UserRepository userRepository,
        AdminRepository adminRepository, 
        AnggotaRepository anggotaRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.anggotaRepository = anggotaRepository;
    }

    @GetMapping("/tabel-user")
    public String tampilkanTabelUser(Model model) {
        List<User> daftarUser = userRepository.findAll();
        model.addAttribute("daftarUser", daftarUser);
        return "admin/tabel user/user";
    }

    @GetMapping("/tabel-anggota")
    public String tampilkanTabelAnggota(Model model) {
        List<Anggota> daftarAnggota = anggotaRepository.findAll();
        model.addAttribute("daftarAnggota", daftarAnggota);
        return "admin/tabel anggota/anggota";
    }

    @GetMapping("/tabel-admin")
    public String tampilkanTabelAdmin(Model model) {
        List<Admin> daftarAdmin = adminRepository.findAll();
        model.addAttribute("daftarAdmin", daftarAdmin);
        return "admin/tabel admin/admin";
    }
}