package org.example.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.example.dto.ProductDTO;
import org.example.services.ProductService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "API zur Verwaltung von Produkten")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "Alle Produkte abrufen", description = "Gibt eine Liste aller vorhandenen Produkte zurück.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Liste der Produkte erfolgreich abgerufen",
          content = @Content(mediaType = "application/json",
              schema = @Schema(type = "array", implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping
  public ResponseEntity<List<ProductDTO>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @Operation(summary = "Produkt anhand der ID abrufen", description = "Gibt ein einzelnes Produkt anhand seiner eindeutigen ID zurück.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produkt erfolgreich gefunden und zurückgegeben",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "404", description = "Produkt mit der angegebenen ID nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProductById(
      @Parameter(description = "ID des abzurufenden Produkts", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @Operation(summary = "Neues Produkt erstellen", description = "Erstellt ein neues Produkt basierend auf den übergebenen Daten.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produkt erfolgreich erstellt",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten (Validierungsfehler)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "404", description = "Zugehörige Kategorie nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(
      @Parameter(description = "Produktinformationen für das neue Produkt", required = true,
          content = @Content(schema = @Schema(implementation = ProductDTO.class)))
      @Valid @RequestBody ProductDTO productDTO) {
    ProductDTO createdProduct = productService.createProduct(productDTO);
    return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
  }

  @Operation(summary = "Bestehendes Produkt aktualisieren", description = "Aktualisiert ein bestehendes Produkt anhand seiner ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produkt erfolgreich aktualisiert",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten (Validierungsfehler)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "404", description = "Produkt oder zugehörige Kategorie nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> updateProduct(
      @Parameter(description = "ID des zu aktualisierenden Produkts", required = true) @PathVariable Long id,
      @Parameter(description = "Aktualisierte Produktinformationen", required = true,
          content = @Content(schema = @Schema(implementation = ProductDTO.class)))
      @Valid @RequestBody ProductDTO productDTO) {
    ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
    return ResponseEntity.ok(updatedProduct);
  }

  @Operation(summary = "Produkt löschen", description = "Löscht ein Produkt anhand seiner ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produkt erfolgreich gelöscht (kein Inhalt)"),
      @ApiResponse(responseCode = "404", description = "Produkt mit der angegebenen ID nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID des zu löschenden Produkts", required = true) @PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
