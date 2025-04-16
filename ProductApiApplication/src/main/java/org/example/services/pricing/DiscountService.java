package org.example.services.pricing;

import org.example.database.model.Product;
import org.example.model.DiscountInfo;

/**
 * Service zur Ermittlung von anwendbaren Rabatten für Produkte.
 */
public interface DiscountService {

  /**
   * Ermittelt den gültigen Rabatt für ein gegebenes Produkt.
   *
   * @param product Das Produkt, für das der Rabatt ermittelt werden soll.
   * @return DiscountInfo Objekt, das den Rabatt beschreibt (Typ und Wert).
   */
  DiscountInfo getDiscount(Product product);
}
