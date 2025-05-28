package com.example.projek_pbo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projek_pbo.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
}