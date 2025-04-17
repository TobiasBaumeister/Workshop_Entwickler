package org.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.ProductDTO;
import org.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class ProductServiceImplTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private Category category1;
  private Category category2;
  private Product product1;
  private Product product2;
  private Product product3;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();

    category1 = categoryRepository.save(new Category(null, "Electronics"));
    category2 = categoryRepository.save(new Category(null, "Books"));

    product1 = new Product(null, "Laptop", 1200.00, category1);
    product2 = new Product(null, "Keyboard", 75.50, category1);
    product3 = new Product(null, "Java Guide", 45.00, category2);

    productRepository.saveAll(Arrays.asList(product1, product2, product3));
  }

  @Test
  void testGetAllProducts() {
    List<ProductDTO> result = productService.getAllProducts();

    assertThat(result).isNotNull().hasSize(3);
    assertThat(result).extracting(ProductDTO::getName)
        .containsExactlyInAnyOrder("Laptop", "Keyboard", "Java Guide");
  }

  @Test
  void testGetProductById_Found() {
    Long idToFind = product1.getId();

    ProductDTO resultDto = productService.getProductById(idToFind);

    assertThat(resultDto).isNotNull();
    assertThat(resultDto.getId()).isEqualTo(idToFind);
    assertThat(resultDto.getName()).isEqualTo("Laptop");
    assertThat(resultDto.getPrice()).isCloseTo(1200.00, within(0.01));
    assertThat(resultDto.getCategoryId()).isEqualTo(category1.getId());
  }

  @Test
  void testGetProductById_NotFound() {
    Long nonExistentId = 9999L;

    assertThatThrownBy(() -> productService.getProductById(nonExistentId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Produkt nicht gefunden mit der ID: " + nonExistentId);
  }

  @Test
  void testCreateProduct_Success() {
    ProductDTO newProductDto = new ProductDTO();
    newProductDto.setName("New Mouse");
    newProductDto.setPrice(25.99);
    newProductDto.setCategoryId(category1.getId());

    ProductDTO createdDto = productService.createProduct(newProductDto);

    assertThat(createdDto).isNotNull();
    assertThat(createdDto.getId()).isNotNull();
    assertThat(createdDto.getName()).isEqualTo("New Mouse");
    assertThat(createdDto.getPrice()).isCloseTo(25.99, within(0.01));
    assertThat(createdDto.getCategoryId()).isEqualTo(category1.getId());

    Optional<Product> savedProductOpt = productRepository.findById(createdDto.getId());
    assertThat(savedProductOpt).isPresent();
    assertThat(savedProductOpt.get().getName()).isEqualTo("New Mouse");
    assertThat(savedProductOpt.get().getCategory().getId()).isEqualTo(category1.getId());
  }

  @Test
  void testCreateProduct_CategoryNotFound() {
    Long nonExistentCategoryId = 8888L;
    ProductDTO newProductDto = new ProductDTO();
    newProductDto.setName("Product with invalid category");
    newProductDto.setPrice(10.0);
    newProductDto.setCategoryId(nonExistentCategoryId);

    assertThatThrownBy(() -> productService.createProduct(newProductDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Kategorie nicht gefunden mit der ID: " + nonExistentCategoryId);
  }

  /**
   * Test für Aufgabe 4
   */
  @Test
  void testUpdateProduct_Success() {
    Long idToUpdate = product2.getId();
    Category newCategory = category2;
    ProductDTO updateDto = new ProductDTO();
    updateDto.setName("Updated Keyboard");
    updateDto.setPrice(80.00);
    updateDto.setCategoryId(newCategory.getId());

    ProductDTO updatedDto = productService.updateProduct(idToUpdate, updateDto);

    assertThat(updatedDto).isNotNull();
    assertThat(updatedDto.getId()).isEqualTo(idToUpdate);
    assertThat(updatedDto.getName()).isEqualTo("Updated Keyboard");
    assertThat(updatedDto.getPrice()).isCloseTo(80.00, within(0.01));
    assertThat(updatedDto.getCategoryId()).isEqualTo(newCategory.getId());

    Optional<Product> updatedProductOpt = productRepository.findById(idToUpdate);
    assertThat(updatedProductOpt).isPresent();
    assertThat(updatedProductOpt.get().getName()).isEqualTo("Updated Keyboard");
    assertThat(updatedProductOpt.get().getPrice()).isCloseTo(80.00, within(0.01));
    assertThat(updatedProductOpt.get().getCategory().getId()).isEqualTo(newCategory.getId());
  }

  /**
   * Test für Aufgabe 4
   */
  @Test
  void testUpdateProduct_ProductNotFound() {
    Long nonExistentProductId = 9999L;
    ProductDTO updateDto = new ProductDTO();
    updateDto.setName("Does not matter");
    updateDto.setPrice(1.0);
    updateDto.setCategoryId(category1.getId());

    assertThatThrownBy(() -> productService.updateProduct(nonExistentProductId, updateDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Produkt nicht gefunden mit der ID: " + nonExistentProductId);
  }

  /**
   * Test für Aufgabe 4
   */
  @Test
  void testUpdateProduct_CategoryNotFound() {
    Long idToUpdate = product1.getId();
    Long nonExistentCategoryId = 8888L;
    ProductDTO updateDto = new ProductDTO();
    updateDto.setName("Laptop with invalid category");
    updateDto.setPrice(1250.0);
    updateDto.setCategoryId(nonExistentCategoryId);

    assertThatThrownBy(() -> productService.updateProduct(idToUpdate, updateDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Kategorie nicht gefunden mit der ID: " + nonExistentCategoryId);
  }

  @Test
  void testDeleteProduct_Success() {
    Long idToDelete = product3.getId();
    assertThat(productRepository.existsById(idToDelete)).isTrue();

    productService.deleteProduct(idToDelete);

    assertThat(productRepository.existsById(idToDelete)).isFalse();
    assertThat(productRepository.count()).isEqualTo(2);
  }

  @Test
  void testDeleteProduct_NotFound() {
    Long nonExistentId = 9999L;

    assertThatThrownBy(() -> productService.deleteProduct(nonExistentId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Produkt nicht gefunden mit der ID: " + nonExistentId);
  }
}