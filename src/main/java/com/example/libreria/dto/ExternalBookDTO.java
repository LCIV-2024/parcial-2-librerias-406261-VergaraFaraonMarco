package com.example.libreria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalBookDTO {
    
    private Long id;
    
    @JsonProperty("has_fulltext")
    private Boolean hasFulltext;
    
    @JsonProperty("edition_count")
    private Integer editionCount;
    
    private String title;
    
    @JsonProperty("author_name")
    private List<String> authorName;
    
    @JsonProperty("first_publish_year")
    private Integer firstPublishYear;
    
    private BigDecimal price;
}

