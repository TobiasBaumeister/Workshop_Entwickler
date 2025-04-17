package org.example.services.impl;

import java.util.Collections;
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

  private static final String CSV_DELIMITER = ";";

  @Autowired
  public ProductExportServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public String exportProductsByCategoryToCsv(Category category) {

    Long categoryId = category.getId();
    log.info("Starte Export für Kategorie ID: {}", categoryId);

    List<Product> products = fetchProductsForCategory(categoryId);

    if (products.isEmpty()) {
      log.info("Keine Produkte für Kategorie ID {} gefunden. Gebe nur Header zurück.", categoryId);
      return "ID,Name,Preis,KategorieName\n";
    }

    try {
      return buildCsvContent(products);
    } catch (Exception e) {
      log.error("Unerwarteter Fehler beim Erstellen des CSV für Kategorie ID {}: {}", categoryId, e.getMessage(), e);
      throw new RuntimeException("Fehler beim CSV-Export: " + e.getMessage(), e);
    }
  }

  @Transactional(readOnly = true)
  public List<Product> fetchProductsForCategory(Long categoryId) {
    if (categoryId == null) {
      return Collections.emptyList();
    }
    log.debug("Lade Produkte für Kategorie ID: {}", categoryId);
    List<Product> products = productRepository.findByCategoryId(categoryId);
    log.info("{} Produkte für Kategorie ID {} gefunden.", products.size(), categoryId);
    return products;
  }

  private String buildCsvContent(List<Product> products) {
    StringBuilder csvBuilder = new StringBuilder("ID,Name,Preis,KategorieName\n");

    for (Product product : products) {
      csvBuilder.append(formatToCsvLine(product));
    }

    log.info("CSV-Inhalt erstellt (möglicherweise mit Formatierungsfehlern).");
    return csvBuilder.toString();
  }

  private String formatToCsvLine(Product product) {
    String categoryName = product.getCategory().getName();

    return String.format("%d%s%s%s%.2f%s%s\n",
        product.getId(),
        CSV_DELIMITER,
        product.getName(),
        CSV_DELIMITER,
        product.getPrice(),
        CSV_DELIMITER,
        categoryName
    );
  }

}
