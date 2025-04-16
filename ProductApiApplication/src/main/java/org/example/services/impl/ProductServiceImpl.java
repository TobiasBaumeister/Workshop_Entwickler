package org.example.services.impl;

import java.util.List;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.ProductDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.rest.mapper.ProductMapper;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.productMapper = productMapper;
  }

  @Override
  public List<ProductDTO> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return productMapper.toDtoList(products);
  }

  @Override
  public ProductDTO getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + id));
    return productMapper.toDto(product);
  }

  @Override
  public ProductDTO createProduct(ProductDTO dto) {
    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit der ID: " + dto.getCategoryId()));

    Product product = productMapper.toEntity(dto);
    product.setCategory(category);

    Product savedProduct = productRepository.save(product);
    return productMapper.toDto(savedProduct);
  }

  @Override
  public ProductDTO updateProduct(Long id, ProductDTO dto) {
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + id));

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit der ID: " + dto.getCategoryId()));

    existingProduct.setName(dto.getName());
    existingProduct.setPrice(dto.getPrice());
    existingProduct.setCategory(category);

    Product updatedProduct = productRepository.save(existingProduct);
    return productMapper.toDto(updatedProduct);
  }

  @Override
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + id));
    productRepository.delete(product);
  }
}