package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {

  private Long id;

  @NotBlank(message = "Produktname ist erforderlich")
  private String name;

  @NotNull(message = "Preis ist erforderlich")
  private Double price;

  @NotNull(message = "Kategorie-ID ist erforderlich")
  private Long categoryId;

  private String categoryName;
}
