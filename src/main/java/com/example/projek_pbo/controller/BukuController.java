package com.example.projek_pbo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.BukuRepository;


@RestController
@RequestMapping("/buku")
public class BukuController {
    @Autowired
    private BukuRepository bookRepository;

    @GetMapping
    public List<Buku> getAllBooks() {
        return bookRepository.findAll();
    }
}
