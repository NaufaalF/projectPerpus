package com.example.projek_pbo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Anggota;
import java.util.Optional;

public interface AnggotaRepository extends JpaRepository<Anggota, Long> {
    
    Optional<Anggota> findByUsername(String username);
}