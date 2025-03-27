package org.example.rest.mapper;

import java.util.List;
import org.example.database.model.Category;
import org.example.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

  CategoryDTO toDto(Category category);

  Category toEntity(CategoryDTO dto);

  List<CategoryDTO> toDtoList(List<Category> categories);
}
