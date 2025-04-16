package org.example.services.pricing;

import org.example.database.model.Product;

/**
 * Service zur Ermittlung des korrekten Steuersatzes für Produkte.
 */
public interface TaxService {

  /**
   * Ermittelt den anzuwendenden Steuersatz für ein gegebenes Produkt.
   *
   * @param product Das Produkt.
   * @return Der Steuersatz als Dezimalzahl (z.B. 0.19 für 19%).
   */
  double getTaxRate(Product product);
}
