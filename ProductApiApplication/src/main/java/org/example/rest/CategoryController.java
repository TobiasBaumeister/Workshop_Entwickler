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
import org.example.dto.CategoryDTO;
import org.example.services.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "API zur Verwaltung von Kategorien")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(summary = "Alle Kategorien abrufen", description = "Gibt eine Liste aller vorhandenen Kategorien zurück.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Liste der Kategorien erfolgreich abgerufen",
          content = @Content(mediaType = "application/json",
              schema = @Schema(type = "array", implementation = CategoryDTO.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @Operation(summary = "Kategorie anhand der ID abrufen", description = "Gibt eine einzelne Kategorie anhand ihrer eindeutigen ID zurück.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Kategorie erfolgreich gefunden und zurückgegeben",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryDTO.class))),
      @ApiResponse(responseCode = "404", description = "Kategorie mit der angegebenen ID nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(
      @Parameter(description = "ID der abzurufenden Kategorie", required = true) @PathVariable Long id) {
    return categoryService.getCategoryById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Neue Kategorie erstellen", description = "Erstellt eine neue Kategorie basierend auf den übergebenen Daten.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Kategorie erfolgreich erstellt",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryDTO.class))),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten (Validierungsfehler)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PostMapping
  public ResponseEntity<CategoryDTO> createCategory(
      @Parameter(description = "Kategorieinformationen für die neue Kategorie", required = true,
          content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
      @Valid @RequestBody CategoryDTO dto) {
    CategoryDTO created = categoryService.createCategory(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @Operation(summary = "Bestehende Kategorie aktualisieren", description = "Aktualisiert eine bestehende Kategorie anhand ihrer ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Kategorie erfolgreich aktualisiert",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryDTO.class))),
      @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten (Validierungsfehler)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "404", description = "Kategorie mit der angegebenen ID nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @Parameter(description = "ID der zu aktualisierenden Kategorie", required = true) @PathVariable Long id,
      @Parameter(description = "Aktualisierte Kategorieinformationen", required = true,
          content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
      @Valid @RequestBody CategoryDTO dto) {
    CategoryDTO updatedCategory = categoryService.updateCategory(id, dto);
    return ResponseEntity.ok(updatedCategory);
  }

  @Operation(summary = "Kategorie löschen", description = "Löscht eine Kategorie anhand ihrer ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Kategorie erfolgreich gelöscht (kein Inhalt)"),
      @ApiResponse(responseCode = "404", description = "Kategorie mit der angegebenen ID nicht gefunden",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorMessage.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "ID der zu löschenden Kategorie", required = true) @PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}

