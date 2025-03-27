package org.example.services;

import java.util.List;
import java.util.Optional;
import org.example.database.model.Product;

public interface ProductService {

  List<Product> getAllProducts();

  Optional<Product> getProductById(Long id);

  Product createProduct(Product product);

  Product updateProduct(Long id, Product productDetails);

  void deleteProduct(Long id);
}
