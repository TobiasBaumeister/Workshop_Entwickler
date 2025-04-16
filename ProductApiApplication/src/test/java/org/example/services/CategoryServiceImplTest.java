package org.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.example.database.model.Category;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.CategoryDTO;
import org.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class CategoryServiceImplTest {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;

  private Category cat1;
  private Category cat2;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();

    cat1 = categoryRepository.save(new Category(null, "Electronics"));
    cat2 = categoryRepository.save(new Category(null, "Books"));
  }

  @Test
  @DisplayName("getAllCategories should return all categories from DB")
  void testGetAllCategories() {
    List<CategoryDTO> result = categoryService.getAllCategories();

    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result).extracting(CategoryDTO::getName)
        .containsExactlyInAnyOrder("Electronics", "Books");
    assertThat(result).extracting(CategoryDTO::getId)
        .containsExactlyInAnyOrder(cat1.getId(), cat2.getId());
  }

  @Test
  @DisplayName("getCategoryById should return category when found")
  void testGetCategoryById_Found() {
    Long idToFind = cat1.getId();

    Optional<CategoryDTO> resultOpt = categoryService.getCategoryById(idToFind);

    assertThat(resultOpt).isPresent();
    assertThat(resultOpt.get().getId()).isEqualTo(idToFind);
    assertThat(resultOpt.get().getName()).isEqualTo("Electronics");
  }

  @Test
  @DisplayName("getCategoryById should return empty Optional when not found")
  void testGetCategoryById_NotFound() {
    Long nonExistentId = 9999L;

    Optional<CategoryDTO> resultOpt = categoryService.getCategoryById(nonExistentId);

    assertThat(resultOpt).isNotPresent();
  }

  @Test
  @DisplayName("createCategory should save and return new category")
  void testCreateCategory() {
    CategoryDTO newCategoryDto = new CategoryDTO();
    newCategoryDto.setName("Software");

    CategoryDTO createdDto = categoryService.createCategory(newCategoryDto);

    assertThat(createdDto).isNotNull();
    assertThat(createdDto.getId()).isNotNull();
    assertThat(createdDto.getName()).isEqualTo("Software");

    Optional<Category> savedCategoryOpt = categoryRepository.findById(createdDto.getId());
    assertThat(savedCategoryOpt).isPresent();
    assertThat(savedCategoryOpt.get().getName()).isEqualTo("Software");
  }

  @Test
  @DisplayName("updateCategory should modify existing category")
  void testUpdateCategory_Success() {
    Long idToUpdate = cat1.getId();
    CategoryDTO updateDto = new CategoryDTO();
    updateDto.setName("Updated Electronics");

    CategoryDTO updatedDto = categoryService.updateCategory(idToUpdate, updateDto);

    assertThat(updatedDto).isNotNull();
    assertThat(updatedDto.getId()).isEqualTo(idToUpdate);
    assertThat(updatedDto.getName()).isEqualTo("Updated Electronics");

    Optional<Category> updatedCategoryOpt = categoryRepository.findById(idToUpdate);
    assertThat(updatedCategoryOpt).isPresent();
    assertThat(updatedCategoryOpt.get().getName()).isEqualTo("Updated Electronics");
    Optional<Category> unchangedCategoryOpt = categoryRepository.findById(cat2.getId());
    assertThat(unchangedCategoryOpt).isPresent();
    assertThat(unchangedCategoryOpt.get().getName()).isEqualTo("Books");
  }

  @Test
  @DisplayName("updateCategory should throw ResourceNotFoundException for non-existent ID")
  void testUpdateCategory_NotFound() {
    Long nonExistentId = 9999L;
    CategoryDTO updateDto = new CategoryDTO();
    updateDto.setName("Does not matter");

    assertThatThrownBy(() -> categoryService.updateCategory(nonExistentId, updateDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Kategorie nicht gefunden mit der ID: " + nonExistentId);
  }


  @Test
  @DisplayName("deleteCategory should remove category from DB")
  void testDeleteCategory_Success() {
    Long idToDelete = cat1.getId();
    assertThat(categoryRepository.existsById(idToDelete)).isTrue();

    categoryService.deleteCategory(idToDelete);

    assertThat(categoryRepository.existsById(idToDelete)).isFalse();
    assertThat(categoryRepository.existsById(cat2.getId())).isTrue();
  }

  @Test
  @DisplayName("deleteCategory should throw ResourceNotFoundException for non-existent ID")
  void testDeleteCategory_NotFound() {
    Long nonExistentId = 9999L;

    assertThatThrownBy(() -> categoryService.deleteCategory(nonExistentId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Kategorie nicht gefunden mit der ID: " + nonExistentId);
  }
}