package com.example.libreria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    @Id
    @Column(name = "external_id", unique = true)
    private Long externalId;
    
    @Column(nullable = false)
    private String title;
    
    @ElementCollection
    @CollectionTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author_name")
    private List<String> authorName;
    
    @Column(name = "first_publish_year")
    private Integer firstPublishYear;
    
    @Column(name = "edition_count")
    private Integer editionCount;
    
    @Column(name = "has_fulltext")
    private Boolean hasFulltext;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    // Campos adicionales para inventario
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;
    
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity = 0;
}

