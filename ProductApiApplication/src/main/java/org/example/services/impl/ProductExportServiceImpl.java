package org.example.services.impl;

import java.util.List;
import org.example.database.model.Category;
import org.example.database.model.Product;
import org.example.database.repository.ProductRepository;
import org.example.services.ProductExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductExportServiceImpl implements ProductExportService {

  private static final Logger log = LoggerFactory.getLogger(ProductExportServiceImpl.class);
  private final ProductRepository productRepository;

  @Autowired
  public ProductExportServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }


  @Override
  public String exportProductsByCategoryToCsv(Category category) {
    if (category == null || category.getId() == null) {
      log.error("Kategorie ist null oder hat keine ID.");
      return "FEHLER: Ungültige Kategorie angegeben.";
    }
    log.info("Starte Export für Kategorie ID: {}", category.getId());

    List<Product> products = fetchProductsForCategory(category.getId());

    try {
      return buildCsvContent(products);
    } catch (Exception e) {
      log.error("Technischer Fehler beim Erstellen des CSV-Inhalts für Kategorie ID {}: {}", category.getId(), e.getMessage());
      throw new RuntimeException("Fehler beim CSV-Export: " + e.getMessage(), e);
    }
  }


  @Transactional(readOnly = true)
  public List<Product> fetchProductsForCategory(Long categoryId) {
    log.debug("Lade Produkte für Kategorie ID: {}", categoryId);
    List<Product> products = productRepository.findByCategoryId(categoryId);
    log.info("{} Produkte für Kategorie ID {} gefunden.", products.size(), categoryId);
    return products;
  }

  private String buildCsvContent(List<Product> products) {
    StringBuilder csvBuilder = new StringBuilder("ID,Name,Preis,KategorieName\n"); // Header

    for (Product product : products) {
      csvBuilder.append(formatToCsvLine(product));
    }

    log.info("CSV-Inhalt erfolgreich erstellt.");
    return csvBuilder.toString();
  }

  private String formatToCsvLine(Product product) {
    String categoryName = product.getCategory().getName();

    return String.format("%d,%s,%.2f,%s\n",
        product.getId(),
        product.getName(),
        product.getPrice(),
        categoryName
    );
  }

}
