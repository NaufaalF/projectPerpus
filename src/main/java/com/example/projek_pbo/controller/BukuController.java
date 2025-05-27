package com.example.projek_pbo.controller;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;
import com.example.projek_pbo.model.Buku;
import com.example.projek_pbo.repository.BukuRepository;


@Controller
public class BukuController {

    private final BukuRepository bookRepository;

    public BukuController(BukuRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Buku> bukuList = bookRepository.findAll();
        model.addAttribute("buku", bukuList != null ? bukuList : List.of());
        return "user/homepage";
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "admin/uploadBuku"; 
    }

    @PostMapping("/upload")
    public String uploadBuku(@RequestParam("title") String title,
                            @RequestParam("author") String author,
                            @RequestParam("publisher") String publisher,
                            @RequestParam("year") int year,
                            @RequestParam("category") String category,
                            @RequestParam("description") String description,
                            @RequestParam("lokasi_rak") String lokasi_rak,
                            @RequestParam("cover") MultipartFile coverFile) throws IOException {
        Buku buku = new Buku();
        buku.setTitle(title);
        buku.setAuthor(author);
        buku.setPublisher(publisher);
        buku.setYear(year);
        buku.setCategory(category);
        buku.setDescription(description);
        buku.setLokasi_rak(lokasi_rak);
        buku.setCover(coverFile.getBytes()); // simpan file ke BLOB

        bookRepository.save(buku);
        return "redirect:/home";
    }

    @GetMapping("/cover/{id}")
    public ResponseEntity<byte[]> getCover(@PathVariable Long id) {
        Buku buku = bookRepository.findById(id).orElseThrow();
        byte[] image = buku.getCover();

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // Atur sesuai jenis gambar
            .body(image);
    }

    @GetMapping("/home/buku/{id}")
    public String detailBuku(@PathVariable Long id, Model model) {
        Buku buku = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
        model.addAttribute("buku", buku);
        return "user/detailBuku"; // Ganti dengan nama file HTML detail kamu
    }

}

