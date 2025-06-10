package com.example.projek_pbo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Peminjaman;

import java.util.List;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
    
    List<Peminjaman> findByAnggotaUsername(String username);

    
}
