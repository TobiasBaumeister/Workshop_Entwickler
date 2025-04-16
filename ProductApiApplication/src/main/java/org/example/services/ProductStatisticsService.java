package org.example.services;

import java.util.Map;
import java.util.Optional;
import org.example.dto.ProductDTO;

/**
 * Service zur Bereitstellung von Statistiken über Produkte und Kategorien.
 */
public interface ProductStatisticsService {

  /**
   * Zählt die Anzahl der Produkte pro Kategorie.
   *
   * @return Eine Map, bei der der Schlüssel der Kategorie-Name und der Wert die Anzahl der Produkte ist.
   */
  Map<String, Long> countProductsPerCategory();

  /**
   * Berechnet den durchschnittlichen Preis aller Produkte.
   *
   * @return Der durchschnittliche Preis oder 0.0, wenn keine Produkte vorhanden sind.
   */
  double getAverageProductPrice();

  /**
   * Berechnet den durchschnittlichen Preis der Produkte für eine bestimmte Kategorie.
   *
   * @param categoryId Die ID der Kategorie.
   * @return Der durchschnittliche Preis oder 0.0, wenn keine Produkte in der Kategorie vorhanden sind.
   * @throws org.example.exception.ResourceNotFoundException wenn die Kategorie-ID nicht existiert.
   */
  double getAverageProductPricePerCategory(Long categoryId);

  /**
   * Findet das Produkt mit dem höchsten Preis.
   *
   * @return Ein Optional, das das teuerste Produkt als DTO enthält, oder Optional.empty(), wenn keine Produkte vorhanden sind.
   */
  Optional<ProductDTO> findMostExpensiveProduct();

  /**
   * Findet das Produkt mit dem niedrigsten Preis innerhalb einer bestimmten Kategorie.
   *
   * @param categoryId Die ID der Kategorie.
   * @return Ein Optional, das das günstigste Produkt der Kategorie als DTO enthält, oder Optional.empty(), wenn keine Produkte in der Kategorie vorhanden sind.
   * @throws org.example.exception.ResourceNotFoundException wenn die Kategorie-ID nicht existiert.
   */
  Optional<ProductDTO> findCheapestProductInCategory(Long categoryId);

}
