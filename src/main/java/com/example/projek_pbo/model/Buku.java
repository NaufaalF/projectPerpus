package com.example.projek_pbo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Buku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String category;
    @Lob
    private String description;
    private String lokasi_rak;
    private boolean available = true;
    @Lob
    private byte[] cover;
}