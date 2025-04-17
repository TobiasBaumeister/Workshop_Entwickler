package org.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductExportServiceImplTest {

  @Autowired
  private ProductExportService productExportService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private Category electronicsCategory;
  private Category booksCategory;
  private Product product1;
  private Product product2;
  private Product product3;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();

    electronicsCategory = categoryRepository.save(new Category(null, "Elektronik"));
    booksCategory = categoryRepository.save(new Category(null, "Bücher"));

    product1 = new Product(null, "Laptop Pro", 1200.00, electronicsCategory);
    product2 = new Product(null, "Wireless Maus", 25.50, electronicsCategory);
    product3 = new Product(null, "Java Grundlagen", 49.99, booksCategory);

    productRepository.saveAll(Arrays.asList(product1, product2, product3));

    assertNotNull(electronicsCategory.getId());
    assertNotNull(booksCategory.getId());
    assertNotNull(product1.getId());
    assertNotNull(product2.getId());
    assertNotNull(product3.getId());
  }

  /**
   * TODO: Dieser Test schlägt aktuell fehl. Im Rahmen von Aufgabe 8 soll das korrigiert werden.
   */
  @Test
  void exportProductsToCsv_ok() {
    String expectedCsv = String.format(
        "ID,Name,Preis,KategorieName\n" +
            "%d,%s,%.2f,%s\n" +
            "%d,%s,%.2f,%s\n",
        product1.getId(), product1.getName(), product1.getPrice(), electronicsCategory.getName(),
        product2.getId(), product2.getName(), product2.getPrice(), electronicsCategory.getName()
    );

    String actualCsv = productExportService.exportProductsByCategoryToCsv(electronicsCategory);

    String normalizedExpectedCsv = expectedCsv.replaceAll("\\r\\n", "\n");
    String normalizedActualCsv = actualCsv.replaceAll("\\r\\n", "\n");

    assertEquals(normalizedExpectedCsv, normalizedActualCsv);
  }

  @Test
  void exportProductsToCsv_EmptyCategory() {
    Category emptyCategory = categoryRepository.save(new Category(null, "Leere Kategorie"));
    String expectedCsv = "ID,Name,Preis,KategorieName\n";

    String actualCsv = productExportService.exportProductsByCategoryToCsv(emptyCategory);

    String normalizedExpectedCsv = expectedCsv.replaceAll("\\r\\n", "\n");
    String normalizedActualCsv = actualCsv.replaceAll("\\r\\n", "\n");

    assertEquals(normalizedExpectedCsv, normalizedActualCsv);
  }

  /**
   * TODO: Dieser Test schlägt aktuell fehl. Im Rahmen von Aufgabe 8 soll das korrigiert werden.
   */
  @Test
  void exportProductsToCsv_null() {
    String expectedResult = "FEHLER: Ungültige Kategorie angegeben.";

    String actualResult = productExportService.exportProductsByCategoryToCsv(null);

    assertEquals(expectedResult, actualResult);
  }

}
