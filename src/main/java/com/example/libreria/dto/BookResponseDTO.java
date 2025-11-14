package com.example.libreria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    
    private Long externalId;
    private String title;
    private List<String> authorName;
    private Integer firstPublishYear;
    private Integer editionCount;
    private Boolean hasFulltext;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer availableQuantity;
}

