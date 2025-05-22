package com.example.projek_pbo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Buku;

public interface BukuRepository extends JpaRepository<Buku, Long> {
    
}