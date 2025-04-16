package org.example.services.impl.pricing;

import org.example.database.model.Product;
import org.example.services.pricing.TaxService;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaxServiceImpl implements TaxService {

  private static final double STANDARD_TAX_RATE = 0.19;

  @Override
  public double getTaxRate(Product product) {
    return STANDARD_TAX_RATE;
  }
}
