package com.example.libreria.controller;

import com.example.libreria.dto.BookResponseDTO;
import com.example.libreria.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    
    @PostMapping("/sync")
    public ResponseEntity<String> syncBooks() {
        bookService.syncBooksFromExternalApi();
        return ResponseEntity.ok("Libros sincronizados exitosamente desde la API externa");
    }
    
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{externalId}")
    public ResponseEntity<BookResponseDTO> getBookByExternalId(@PathVariable Long externalId) {
        BookResponseDTO book = bookService.getBookByExternalId(externalId);
        return ResponseEntity.ok(book);
    }
    
    @PutMapping("/{externalId}/stock")
    public ResponseEntity<BookResponseDTO> updateStock(
            @PathVariable Long externalId,
            @RequestParam Integer stockQuantity) {
        BookResponseDTO book = bookService.updateStock(externalId, stockQuantity);
        return ResponseEntity.ok(book);
    }
}

