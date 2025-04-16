package org.example.rest.mapper;

import java.util.List;
import org.example.database.model.Product;
import org.example.dto.ProductDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

  @Mapping(source = "category.id", target = "categoryId")
  @Mapping(source = "category.name", target = "categoryName")
  ProductDTO toDto(Product product);

  @BeanMapping(ignoreUnmappedSourceProperties = {"categoryId", "categoryName"})
  @Mapping(target = "category", ignore = true)
  Product toEntity(ProductDTO productDTO);

  List<ProductDTO> toDtoList(List<Product> products);


}
