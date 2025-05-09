package org.example.services.impl;

import java.util.List;
import java.util.Optional;
import org.example.database.model.Category;
import org.example.database.repository.CategoryRepository;
import org.example.dto.CategoryDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.rest.mapper.CategoryMapper;
import org.example.services.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  @Override
  public List<CategoryDTO> getAllCategories() {
    return categoryMapper.toDtoList(categoryRepository.findAll());
  }

  @Override
  public Optional<CategoryDTO> getCategoryById(Long id) {
    return categoryRepository.findById(id).map(categoryMapper::toDto);
  }

  @Override
  public CategoryDTO createCategory(CategoryDTO dto) {
    Category category = categoryMapper.toEntity(dto);
    Category saved = categoryRepository.save(category);
    return categoryMapper.toDto(saved);
  }

  @Override
  public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
    Category existingCategory = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit der ID: " + id));
    existingCategory.setName(dto.getName());
    Category updatedCategory = categoryRepository.save(existingCategory);
    return categoryMapper.toDto(updatedCategory);
  }

  @Override
  public void deleteCategory(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit der ID: " + id));
    categoryRepository.deleteById(category.getId());
  }
}