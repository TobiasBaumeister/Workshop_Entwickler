package org.example.database.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Schema(description = "Produkt-Entity, das ein Produkt im System repräsentiert")
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  @Schema(description = "Eindeutige Kennung des Produkts", example = "1", required = true)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Schema(description = "Name des Produkts", example = "Laptop", required = true)
  @NotBlank(message = "Name ist erforderlich")
  private String name;

  @Schema(description = "Preis des Produkts", example = "999.99", required = true)
  @NotNull("Preis ist erforderlich")
  private Double price;

}