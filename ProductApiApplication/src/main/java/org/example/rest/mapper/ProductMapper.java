package org.example.rest.mapper;

import java.util.List;
import org.example.database.model.Product;
import org.example.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

  ProductDTO toDto(Product product);

  Product toEntity(ProductDTO dto);

  List<ProductDTO> toDtoList(List<Product> products);


}
