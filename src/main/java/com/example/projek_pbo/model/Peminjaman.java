package com.example.projek_pbo.model;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity 
public class Peminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "anggota_id", referencedColumnName = "id")
    private Anggota anggota;

    @ManyToOne
    @JoinColumn(name = "buku_id", referencedColumnName = "id")
    private Buku buku;

    private LocalDate tanggal_pinjam;
    private LocalDate tanggal_kembali;

    public enum Status { // Ubah nama enum menjadi Status
        MENUNGGU,
        DIPINJAM,
        SELESAI
    }

    @Enumerated(EnumType.STRING)
    private Status status_peminjaman; // Ubah tipe menjadi Status
}

