package org.example.services;

import org.example.database.model.Product;
import org.example.database.repository.ProductRepository;
import org.example.database.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllProducts() {
    List<Product> products = Arrays.asList(
        new Product(1L, "Laptop", 999.99),
        new Product(2L, "Phone", 499.99)
    );
    when(productRepository.findAll()).thenReturn(products);

    List<Product> result = productService.getAllProducts();

    assertEquals(2, result.size());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  void testGetProductByIdExists() {
    Product product = new Product(1L, "Laptop", 999.99);
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    Optional<Product> result = productService.getProductById(1L);
    assertTrue(result.isPresent());
    assertEquals("Laptop", result.get().getName());
  }

  @Test
  void testGetProductByIdNotExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    Optional<Product> result = productService.getProductById(1L);
    assertFalse(result.isPresent());
  }

  @Test
  void testCreateProduct() {
    Product product = new Product(null, "Tablet", 299.99);
    Product savedProduct = new Product(3L, "Tablet", 299.99);
    when(productRepository.save(product)).thenReturn(savedProduct);

    Product result = productService.createProduct(product);
    assertNotNull(result.getId());
    assertEquals("Tablet", result.getName());
  }

  @Test
  void testUpdateProductExists() {
    Product existing = new Product(1L, "Laptop", 999.99);
    Product updatedDetails = new Product(null, "Gaming Laptop", 1299.99);
    when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(productRepository.save(existing)).thenReturn(existing);

    Product result = productService.updateProduct(1L, updatedDetails);
    assertEquals("Gaming Laptop", result.getName());
    assertEquals(1299.99, result.getPrice());
    verify(productRepository).findById(1L);
    verify(productRepository).save(existing);
  }

  @Test
  void testUpdateProductNotExists() {
    Product updatedDetails = new Product(null, "Gaming Laptop", 1299.99);
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      productService.updateProduct(1L, updatedDetails);
    });
  }

  @Test
  void testDeleteProductExists() {
    Product existing = new Product(1L, "Laptop", 999.99);
    when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
    doNothing().when(productRepository).delete(existing);

    productService.deleteProduct(1L);
    verify(productRepository).delete(existing);
  }

  @Test
  void testDeleteProductNotExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
  }
}

