package org.example.services;

import java.util.List;
import org.example.dto.ProductDTO;

public interface ProductService {

  List<ProductDTO> getAllProducts();

  ProductDTO getProductById(Long id);

  ProductDTO createProduct(ProductDTO productDTO);

  ProductDTO updateProduct(Long id, ProductDTO productDTO);

  void deleteProduct(Long id);
}
