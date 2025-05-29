package com.example.projek_pbo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Peminjaman;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
    
    // You can add custom query methods here if needed
    // For example:
    // List<Peminjaman> findByAnggotaId(Long anggotaId);
    // List<Peminjaman> findByBukuId(Long bukuId);
    
}
