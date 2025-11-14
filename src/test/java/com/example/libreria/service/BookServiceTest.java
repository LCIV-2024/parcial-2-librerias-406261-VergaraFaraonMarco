package com.example.libreria.service;

import com.example.libreria.dto.BookResponseDTO;
import com.example.libreria.dto.ExternalBookDTO;
import com.example.libreria.model.Book;
import com.example.libreria.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private ExternalBookService externalBookService;
    
    @InjectMocks
    private BookService bookService;
    
    private Book testBook;
    private ExternalBookDTO externalBookDTO;
    
    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setExternalId(258027L);
        testBook.setTitle("The Lord of the Rings");
        testBook.setPrice(new BigDecimal("15.99"));
        testBook.setStockQuantity(10);
        testBook.setAvailableQuantity(5);
        
        externalBookDTO = new ExternalBookDTO();
        externalBookDTO.setId(258027L);
        externalBookDTO.setTitle("The Lord of the Rings");
        externalBookDTO.setPrice(new BigDecimal("15.99"));
    }
    
    @Test
    void testSyncBooksFromExternalApi_NewBook() {
        when(externalBookService.fetchAllBooks()).thenReturn(Arrays.asList(externalBookDTO));
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        bookService.syncBooksFromExternalApi();
        
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    void testSyncBooksFromExternalApi_ExistingBook() {
        when(externalBookService.fetchAllBooks()).thenReturn(Arrays.asList(externalBookDTO));
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        bookService.syncBooksFromExternalApi();
        
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    void testGetAllBooks() {
        Book book2 = new Book();
        book2.setExternalId(140081L);
        book2.setTitle("The Hitchhiker's Guide to the Galaxy");
        
        when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook, book2));
        
        List<BookResponseDTO> result = bookService.getAllBooks();
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test
    void testGetBookByExternalId_Success() {
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        
        BookResponseDTO result = bookService.getBookByExternalId(258027L);
        
        assertNotNull(result);
        assertEquals(testBook.getExternalId(), result.getExternalId());
        assertEquals(testBook.getTitle(), result.getTitle());
    }
    
    @Test
    void testGetBookByExternalId_NotFound() {
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            bookService.getBookByExternalId(258027L);
        });
    }
    
    @Test
    void testUpdateStock_Success() {
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        BookResponseDTO result = bookService.updateStock(258027L, 20);
        
        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    void testUpdateStock_InvalidStock() {
        testBook.setStockQuantity(10);
        testBook.setAvailableQuantity(5); // 5 reservados
        
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        
        assertThrows(RuntimeException.class, () -> {
            bookService.updateStock(258027L, 3); // Menos que los reservados
        });
    }
    
    @Test
    void testDecreaseAvailableQuantity_Success() {
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        bookService.decreaseAvailableQuantity(258027L);
        
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    void testDecreaseAvailableQuantity_NoStock() {
        testBook.setAvailableQuantity(0);
        
        when(bookRepository.findByExternalId(258027L)).thenReturn(Optional.of(testBook));
        
        assertThrows(RuntimeException.class, () -> {
            bookService.decreaseAvailableQuantity(258027L);
        });
    }
}

