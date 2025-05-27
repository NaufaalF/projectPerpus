package com.example.projek_pbo.model;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Anggota extends User {
    private String address;
    private String email;
    
}
