package com.example.libreria.repository;

import com.example.libreria.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByExternalId(Long externalId);
    
    boolean existsByExternalId(Long externalId);
}

