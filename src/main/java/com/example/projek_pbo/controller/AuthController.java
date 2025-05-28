package com.example.projek_pbo.controller;

import com.example.projek_pbo.model.User;
import com.example.projek_pbo.model.Anggota;
import com.example.projek_pbo.repository.AnggotaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AnggotaRepository anggotaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerAnggota(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "ANGGOTA") String role) {
        Anggota anggota = new Anggota();
        anggota.setName(name);
        anggota.setAddress(address);
        anggota.setEmail(email);
        anggota.setUsername(username);
        anggota.setPassword(passwordEncoder.encode(password));
        anggota.setRole(User.Role.valueOf(role.toUpperCase()));

        anggotaRepository.save(anggota);
        return "redirect:/login?registered=true";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("User  " + auth.getName() + " is logging out.");
            new SecurityContextLogoutHandler().logout(request, response, auth);
            System.out.println("User  " + auth.getName() + " has logged out.");
        } else {
            System.out.println("No user is currently authenticated.");
        }
        return "redirect:/home";
    }
}