package org.example.services.pricing;

import org.example.dto.PriceDetailsDTO;

/**
 * Service zur Berechnung des Endpreises eines Produkts unter Berücksichtigung von Rabatten und Steuern.
 */
public interface PriceCalculationService {

  /**
   * Berechnet die detaillierten Preisinformationen für ein Produkt.
   *
   * @param productId Die ID des Produkts.
   * @return PriceDetailsDTO mit allen Preisbestandteilen.
   * @throws org.example.exception.ResourceNotFoundException wenn das Produkt nicht gefunden wird.
   * @throws IllegalArgumentException                        wenn das Produkt einen ungültigen Basispreis hat (z.B. null oder negativ).
   */
  PriceDetailsDTO calculatePriceDetails(Long productId);
}
