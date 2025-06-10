package com.example.projek_pbo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

import com.example.projek_pbo.model.User;
import com.example.projek_pbo.repository.UserRepository;

import com.example.projek_pbo.model.Admin;
import com.example.projek_pbo.repository.AdminRepository;

import com.example.projek_pbo.model.Anggota;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.AnggotaRepository;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // ============================== CRUD Anggota ==============================

    @GetMapping("/tambah-anggota")
    public String showTambahAnggotaForm() {
        return "admin/tabel anggota/tambahAnggota";
    }
    
    @PostMapping("/tambah-anggota")
    public String tambahAnggota(@RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam("email") String email) throws IOException {
        Anggota anggota = new Anggota();

        if (anggota.getRole() == null) {
            anggota.setRole(User.Role.ANGGOTA);
        }

        anggota.setName(name);
        anggota.setUsername(username);
        anggota.setPassword(passwordEncoder.encode(password));
        anggota.setAddress(address);
        anggota.setEmail(email);

        anggotaRepository.save(anggota);
        return "redirect:/tabel-anggota";
    }

    @GetMapping("/edit-anggota/{id}")
    public String showEditAnggotaForm(@PathVariable("id") Long id, Model model) {
        Anggota anggota = anggotaRepository.findById(id).orElse(null);
        if (anggota == null) {
            return "redirect:/tabel-anggota";
        }
        model.addAttribute("anggota", anggota);
        return "admin/tabel anggota/editAnggota";
    }

    @PostMapping("/edit-anggota/{id}")
    public String updateAnggota(@PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("address") String address,
            @RequestParam("email") String email) throws IOException {
        Anggota anggota = anggotaRepository.findById(id).orElse(null);
        if (anggota == null) {
            return "redirect:/tabel-anggota";
        }

        anggota.setName(name);
        anggota.setUsername(username);
        anggota.setAddress(address);
        anggota.setEmail(email);

        anggotaRepository.save(anggota);
        return "redirect:/tabel-anggota";
    }

    @GetMapping("/delete-anggota/{id}")
    public String deleteAnggota(@PathVariable("id") Long id) {
        anggotaRepository.deleteById(id);
        return "redirect:/tabel-anggota";
    }

    // ============================== CRUD Admin ==============================

    @GetMapping("/tambah-admin")
    public String showTambahAdminForm() {
        return "admin/tabel admin/tambahAdmin";
    }
    
    @PostMapping("/tambah-admin")
    public String tambahAdmin(@RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("department") String department) throws IOException {
        Admin admin = new Admin();

        if (admin.getRole() == null) {
            admin.setRole(User.Role.ADMIN);
        }

        admin.setName(name);
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setDepartment(department);

        adminRepository.save(admin);
        return "redirect:/tabel-admin";
    }

    @GetMapping("/edit-admin/{id}")
    public String showEditAdminForm(@PathVariable("id") Long id, Model model) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) {
            return "redirect:/tabel-admin";
        }
        model.addAttribute("admin", admin);
        return "admin/tabel admin/editAdmin";
    }

    @PostMapping("/edit-admin/{id}")
    public String updateAdmin(@PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("department") String department) throws IOException {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) {
            return "redirect:/tabel-admin";
        }

        admin.setName(name);
        admin.setUsername(username);
        admin.setDepartment(department);

        adminRepository.save(admin);
        return "redirect:/tabel-admin";
    }

    @GetMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable("id") Long id) {
        adminRepository.deleteById(id);
        return "redirect:/tabel-admin";
    }

}