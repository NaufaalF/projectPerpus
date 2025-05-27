package com.example.projek_pbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;
import com.example.projek_pbo.model.User;
import com.example.projek_pbo.repository.UserRepository;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/tabel-user")
    public String tampilkanTabelUser(Model model) {
        // Di sini Anda bisa mengambil daftar user dari repository jika diperlukan
        List<User> daftarUser = userRepository.findAll();
        model.addAttribute("daftarUser", daftarUser);
        return "admin/user"; // Ganti dengan nama file HTML yang sesuai
    }
    
}
