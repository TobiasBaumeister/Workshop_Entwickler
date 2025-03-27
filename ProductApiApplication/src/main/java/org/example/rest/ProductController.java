package org.example.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import jakarta.validation.Valid;
import org.example.database.exception.ResourceNotFoundException;
import org.example.database.model.Product;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController( ProductService productService ){
    this.productService = productService;
  }

  @Operation(summary = "Alle Produkte abrufen")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Liste der Produkte erfolgreich abgerufen",
          content = { @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Product.class))) }),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content)
  })
  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts(){
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  @Operation(summary = "Produkt nach ID abrufen")
  @ApiResponses(value = {
      @ApiResponse(responseCode="200", description="Produkt gefunden",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = Product.class)) }),
      @ApiResponse(responseCode="404", description="Produkt nicht gefunden",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long productId){
    Product product = productService.getProductById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + productId));
    return ResponseEntity.ok().body(product);
  }

  @Operation(summary = "Neues Produkt erstellen")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produkt erfolgreich erstellt",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = Product.class)) }),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
    Product createdProduct = productService.createProduct(product);
    return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
  }

  @Operation(summary = "Existierendes Produkt aktualisieren")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produkt erfolgreich aktualisiert",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = Product.class)) }),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Produkt nicht gefunden",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(
      @PathVariable(value = "id") Long productId,
      @Valid @RequestBody Product productDetails){
    Product updatedProduct = productService.updateProduct(productId, productDetails);
    return ResponseEntity.ok(updatedProduct);
  }

  @Operation(summary = "Produkt löschen")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produkt erfolgreich gelöscht",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Produkt nicht gefunden",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") Long productId){
    productService.deleteProduct(productId);
    return ResponseEntity.noContent().build();
  }
}
