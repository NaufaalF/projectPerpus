package com.example.projek_pbo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Buku {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private String author;

    private boolean available = true;
}
