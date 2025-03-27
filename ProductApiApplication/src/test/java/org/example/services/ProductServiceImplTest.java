package org.example.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.ProductDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.rest.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductServiceImpl productService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllProducts() {
    List<Product> products = List.of(
        new Product(1L, "Laptop", 999.99, new Category(1L, "Laptops")),
        new Product(2L, "Phone", 499.99, new Category(2L, "Phones"))
    );

    List<ProductDTO> productDTOs = List.of(
        createProductDTO(1L, "Laptop", 999.99, 1L, "Laptops"),
        createProductDTO(2L, "Phone", 499.99, 2L, "Phones")
    );

    when(productRepository.findAll()).thenReturn(products);
    when(productMapper.toDtoList(products)).thenReturn(productDTOs);

    List<ProductDTO> result = productService.getAllProducts();

    assertEquals(2, result.size());
    assertEquals("Laptop", result.get(0).getName());
  }

  @Test
  void testGetProductById_Found() {
    Product product = new Product(1L, "Laptop", 999.99, new Category(1L, "Laptops"));
    ProductDTO dto = createProductDTO(1L, "Laptop", 999.99, 1L, "Laptops");

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toDto(product)).thenReturn(dto);

    ProductDTO result = productService.getProductById(1L);
    assertEquals("Laptop", result.getName());
  }

  @Test
  void testGetProductById_NotFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
  }

  @Test
  void testCreateProduct() {
    ProductDTO inputDto = createProductDTO(null, "Tablet", 299.99, 1L, null);
    Category category = new Category(1L, "Tablets");
    Product product = new Product(null, "Tablet", 299.99, category);
    Product saved = new Product(3L, "Tablet", 299.99, category);
    ProductDTO resultDto = createProductDTO(3L, "Tablet", 299.99, 1L, "Tablets");

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productMapper.toEntity(inputDto)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(saved);
    when(productMapper.toDto(saved)).thenReturn(resultDto);

    ProductDTO result = productService.createProduct(inputDto);
    assertEquals(3L, result.getId());
    assertEquals("Tablet", result.getName());
    assertEquals("Tablets", result.getCategoryName());
  }

  @Test
  void testCreateProduct_CategoryNotFound() {
    ProductDTO inputDto = createProductDTO(null, "Tablet", 299.99, 99L, null);

    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(inputDto));
  }

  @Test
  void testUpdateProduct() {
    ProductDTO inputDto = createProductDTO(null, "Updated Laptop", 1599.99, 1L, null);
    Category category = new Category(1L, "Laptops");
    Product existing = new Product(1L, "Old Laptop", 1099.99, category);
    Product updated = new Product(1L, "Updated Laptop", 1599.99, category);
    ProductDTO resultDto = createProductDTO(1L, "Updated Laptop", 1599.99, 1L, "Laptops");

    when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productRepository.save(existing)).thenReturn(updated);
    when(productMapper.toDto(updated)).thenReturn(resultDto);

    ProductDTO result = productService.updateProduct(1L, inputDto);
    assertEquals("Updated Laptop", result.getName());
    assertEquals(1599.99, result.getPrice());
  }

  @Test
  void testUpdateProduct_ProductNotFound() {
    ProductDTO dto = createProductDTO(null, "Whatever", 10.0, 1L, null);
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, dto));
  }

  @Test
  void testUpdateProduct_CategoryNotFound() {
    ProductDTO dto = createProductDTO(null, "Whatever", 10.0, 99L, null);
    Product existing = new Product(1L, "Something", 100.0, new Category(1L, "Old"));

    when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, dto));
  }

  @Test
  void testDeleteProduct() {
    Product existing = new Product(1L, "Laptop", 999.99, new Category(1L, "Laptops"));
    when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
    doNothing().when(productRepository).delete(existing);

    assertDoesNotThrow(() -> productService.deleteProduct(1L));
  }

  @Test
  void testDeleteProduct_NotFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
  }

  private ProductDTO createProductDTO(Long id, String name, Double price, Long categoryId, String categoryName) {
    ProductDTO dto = new ProductDTO();
    dto.setId(id);
    dto.setName(name);
    dto.setPrice(price);
    dto.setCategoryId(categoryId);
    dto.setCategoryName(categoryName);
    return dto;
  }
}
