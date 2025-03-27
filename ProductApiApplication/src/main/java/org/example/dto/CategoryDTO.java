package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

  private Long id;

  @NotBlank(message = "Kategoriename ist erforderlich")
  private String name;
}

