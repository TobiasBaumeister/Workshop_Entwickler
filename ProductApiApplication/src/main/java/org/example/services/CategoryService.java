package org.example.services;

import java.util.List;
import java.util.Optional;
import org.example.dto.CategoryDTO;

public interface CategoryService {

  List<CategoryDTO> getAllCategories();

  Optional<CategoryDTO> getCategoryById(Long id);

  CategoryDTO createCategory(CategoryDTO categoryDTO);

  CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

  void deleteCategory(Long id);
}
