package org.example.services.impl.pricing;

import org.example.database.model.Product;
import org.example.model.DiscountInfo;
import org.example.model.DiscountType;
import org.example.services.pricing.DiscountService;
import org.springframework.stereotype.Service;

@Service
public class SimpleDiscountServiceImpl implements DiscountService {


  @Override
  public DiscountInfo getDiscount(Product product) {
    if (product == null) {
      return new DiscountInfo(DiscountType.NONE, 0);
    }

    // Beispiel: 10% Rabatt auf alle Produkte der Kategorie "Electronics"
    if (product.getCategory() != null && "Electronics".equalsIgnoreCase(product.getCategory().getName())) {
      return new DiscountInfo(DiscountType.PERCENTAGE, 0.10);
    }

    // Beispiel: 5 EUR fester Rabatt auf Produkte über 100 EUR in Kategorie "Books"
    if (product.getCategory() != null && "Books".equalsIgnoreCase(product.getCategory().getName()) && product.getPrice() > 100.0) {
      return new DiscountInfo(DiscountType.FIXED_AMOUNT, 5.00);
    }

    // Standard: Kein Rabatt
    return new DiscountInfo(DiscountType.NONE, 0);
  }
}
