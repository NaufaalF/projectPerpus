package com.example.projek_pbo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Anggota;

public interface AnggotaRepository extends JpaRepository<Anggota, Long> {
    // Additional query methods can be defined here if needed
    
}
