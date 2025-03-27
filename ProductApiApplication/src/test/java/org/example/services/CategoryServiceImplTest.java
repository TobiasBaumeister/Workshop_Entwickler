package org.example.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.database.model.Category;
import org.example.database.repository.CategoryRepository;
import org.example.dto.CategoryDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.rest.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryServiceImplTest {

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllCategories() {
    List<Category> categories = List.of(
        new Category(1L, "Laptops"),
        new Category(2L, "Phones")
    );

    List<CategoryDTO> categoryDTOs = List.of(
        createCategoryDTO(1L, "Laptops"),
        createCategoryDTO(2L, "Phones")
    );

    when(categoryRepository.findAll()).thenReturn(categories);
    when(categoryMapper.toDtoList(categories)).thenReturn(categoryDTOs);

    List<CategoryDTO> result = categoryService.getAllCategories();
    assertEquals(2, result.size());
    assertEquals("Phones", result.get(1).getName());
  }

  @Test
  void testGetCategoryById_Found() {
    Category category = new Category(1L, "Laptops");
    CategoryDTO dto = createCategoryDTO(1L, "Laptops");

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(categoryMapper.toDto(category)).thenReturn(dto);

    Optional<CategoryDTO> result = categoryService.getCategoryById(1L);

    assertTrue(result.isPresent());
    assertEquals("Laptops", result.get().getName());
  }

  @Test
  void testGetCategoryById_NotFound() {
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    Optional<CategoryDTO> result = categoryService.getCategoryById(99L);
    assertTrue(result.isEmpty());
  }

  @Test
  void testCreateCategory() {
    CategoryDTO request = createCategoryDTO(null, "Tablets");
    Category entity = new Category(null, "Tablets");
    Category saved = new Category(3L, "Tablets");
    CategoryDTO response = createCategoryDTO(3L, "Tablets");

    when(categoryMapper.toEntity(request)).thenReturn(entity);
    when(categoryRepository.save(entity)).thenReturn(saved);
    when(categoryMapper.toDto(saved)).thenReturn(response);

    CategoryDTO result = categoryService.createCategory(request);
    assertEquals(3L, result.getId());
    assertEquals("Tablets", result.getName());
  }

  @Test
  void testDeleteCategory_Success() {
    Category existing = new Category(1L, "Laptops");
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
    doNothing().when(categoryRepository).delete(existing);

    assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
  }

  @Test
  void testDeleteCategory_NotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
  }

  private CategoryDTO createCategoryDTO(Long id, String name) {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(id);
    dto.setName(name);
    return dto;
  }
}
