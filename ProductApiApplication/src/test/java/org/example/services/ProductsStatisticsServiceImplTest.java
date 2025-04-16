package org.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductsStatisticsServiceImplTest {

  @Autowired
  private ProductStatisticsService statisticsService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private Category electronicsCategory;
  private Category booksCategory;
  private Category emptyCategory;
  private Product laptop;
  private Product mouse;
  private Product javaBook;
  private Product springBook;
  private Product orphanProduct;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();

    electronicsCategory = categoryRepository.save(new Category(null, "Electronics"));
    booksCategory = categoryRepository.save(new Category(null, "Books"));
    emptyCategory = categoryRepository.save(new Category(null, "EmptyCategory"));

    laptop = new Product(null, "Laptop", 1200.00, electronicsCategory);
    mouse = new Product(null, "Mouse", 25.50, electronicsCategory);
    javaBook = new Product(null, "Java Basics", 50.00, booksCategory);
    springBook = new Product(null, "Spring Guide", 75.00, booksCategory);
    orphanProduct = new Product(null, "Orphan Widget", 10.00, null);

    productRepository.saveAll(Arrays.asList(laptop, mouse, javaBook, springBook, orphanProduct));
  }


  @Test
  @DisplayName("countProductsPerCategory should return correct counts for existing categories")
  void testCountProductsPerCategory_CorrectCounts() {
    Map<String, Long> counts = statisticsService.countProductsPerCategory();
    assertThat(counts)
        .isNotNull()
        .hasSize(2)
        .containsEntry("Electronics", 2L)
        .containsEntry("Books", 2L)
        .doesNotContainKey("EmptyCategory");
  }

  @Test
  @DisplayName("getAverageProductPrice should return the correct average of all products")
  void testGetAverageProductPrice_CorrectAverage() {
    double expectedAverage = (1200.00 + 25.50 + 50.00 + 75.00 + 10.00) / 5.0; // 272.10
    double actualAverage = statisticsService.getAverageProductPrice();
    assertThat(actualAverage).isCloseTo(expectedAverage, within(0.01));
  }

  @Test
  @DisplayName("findMostExpensiveProduct should return the correct product (Laptop)")
  void testFindMostExpensiveProduct_CorrectProduct() {
    Optional<ProductDTO> resultOpt = statisticsService.findMostExpensiveProduct();
    assertThat(resultOpt).isPresent();
    ProductDTO mostExpensive = resultOpt.get();
    assertThat(mostExpensive.getId()).isEqualTo(laptop.getId());
    assertThat(mostExpensive.getName()).isEqualTo("Laptop");
    assertThat(mostExpensive.getPrice()).isCloseTo(1200.00, within(0.01));
    assertThat(mostExpensive.getCategoryId()).isEqualTo(electronicsCategory.getId());
  }


  static Stream<Arguments> averagePricePerCategoryProvider() {
    return Stream.of(
        arguments("Electronics", 612.75), // (1200 + 25.50) / 2
        arguments("Books", 62.50),     // (50 + 75) / 2
        arguments("EmptyCategory", 0.0)
    );
  }

  @DisplayName("getAverageProductPricePerCategory should return correct average")
  @ParameterizedTest(name = "[{index}] Category ''{0}'' -> Expected Average: {1}")
  @MethodSource("averagePricePerCategoryProvider")
  void testGetAverageProductPricePerCategory_Parameterized(String categoryName, double expectedAverage) {
    Category category = categoryRepository.findByName(categoryName)
        .orElseThrow(() -> new IllegalStateException("Test setup error: Category not found: " + categoryName));
    Long categoryId = category.getId();

    double actualAverage = statisticsService.getAverageProductPricePerCategory(categoryId);

    assertThat(actualAverage).isCloseTo(expectedAverage, within(0.01));
  }


  static Stream<Arguments> cheapestProductInCategoryProvider() {
    return Stream.of(
        arguments("Electronics", "Mouse", 25.50),
        arguments("Books", "Java Basics", 50.00)
    );
  }

  @DisplayName("findCheapestProductInCategory should return the correct product")
  @ParameterizedTest(name = "[{index}] Category ''{0}'' -> Expected Cheapest: ''{1}'' ({2} EUR)")
  @MethodSource("cheapestProductInCategoryProvider")
  void testFindCheapestProductInCategory_Parameterized(String categoryName, String expectedProductName, double expectedProductPrice) {
    Category category = categoryRepository.findByName(categoryName)
        .orElseThrow(() -> new IllegalStateException("Test setup error: Category not found: " + categoryName));
    Long categoryId = category.getId();

    Optional<ProductDTO> resultOpt = statisticsService.findCheapestProductInCategory(categoryId);

    assertThat(resultOpt).isPresent();
    ProductDTO cheapest = resultOpt.get();
    assertThat(cheapest.getName()).isEqualTo(expectedProductName);
    assertThat(cheapest.getPrice()).isCloseTo(expectedProductPrice, within(0.01));
    assertThat(cheapest.getCategoryId()).isEqualTo(categoryId); // Wichtiger Check!
  }

  @Test
  @DisplayName("findCheapestProductInCategory should return empty Optional for category with no products")
  void testFindCheapestProductInCategory_EmptyCategory() {
    Long emptyCategoryId = emptyCategory.getId();

    Optional<ProductDTO> resultOpt = statisticsService.findCheapestProductInCategory(emptyCategoryId);

    assertThat(resultOpt).isNotPresent();
  }
}