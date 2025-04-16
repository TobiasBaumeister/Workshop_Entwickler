package org.example.services.impl.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.example.database.model.Product;
import org.example.database.repository.ProductRepository;
import org.example.dto.PriceDetailsDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.DiscountInfo;
import org.example.model.DiscountType;
import org.example.services.pricing.DiscountService;
import org.example.services.pricing.PriceCalculationService;
import org.example.services.pricing.TaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PriceCalculationServiceImpl implements PriceCalculationService {

  private static final Logger log = LoggerFactory.getLogger(PriceCalculationServiceImpl.class);
  private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

  private final ProductRepository productRepository;
  private final DiscountService discountService;
  private final TaxService taxService;

  public PriceCalculationServiceImpl(ProductRepository productRepository,
      DiscountService discountService,
      TaxService taxService) {
    this.productRepository = productRepository;
    this.discountService = discountService;
    this.taxService = taxService;
  }

  @Override
  public PriceDetailsDTO calculatePriceDetails(Long productId) {
    log.info("Calculating price details for product ID: {}", productId);

    // 1. Produkt laden
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + productId));
    log.debug("Found product: {}", product.getName());

    BigDecimal basePrice = BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP);
    if (basePrice.compareTo(ZERO) < 0) {
      log.error("Product ID {} has negative price: {}", productId, basePrice);
      throw new IllegalArgumentException("Produkt (ID: " + productId + ") hat einen negativen Preis: " + basePrice);
    }
    log.debug("Base price for product ID {}: {}", productId, basePrice);

    // 2. Rabatt ermitteln
    DiscountInfo discountInfo = discountService.getDiscount(product);
    log.debug("Discount info for product ID {}: Type={}, Value={}", productId, discountInfo.type(), discountInfo.value());

    // 3. Rabatt anwenden
    BigDecimal discountAmount = ZERO;
    BigDecimal priceAfterDiscount = basePrice;

    switch (discountInfo.type()) {
      case PERCENTAGE:
        if (discountInfo.value() > 0 && discountInfo.value() <= 1.0) {
          discountAmount = basePrice.multiply(BigDecimal.valueOf(discountInfo.value()))
              .setScale(2, RoundingMode.HALF_UP);
          priceAfterDiscount = basePrice.subtract(discountAmount);
          log.debug("Applied percentage discount: Amount={}, PriceAfter={}", discountAmount, priceAfterDiscount);
        } else {
          log.warn("Invalid percentage discount value {} for product ID {}, ignoring.", discountInfo.value(), productId);
          // Kein gültiger Rabatt -> discountInfo auf NONE setzen für DTO
          discountInfo = new DiscountInfo(DiscountType.NONE, 0);
        }
        break;
      case FIXED_AMOUNT:
        BigDecimal fixedDiscount = BigDecimal.valueOf(discountInfo.value()).setScale(2, RoundingMode.HALF_UP);
        if (fixedDiscount.compareTo(ZERO) > 0) {
          discountAmount = fixedDiscount;
          priceAfterDiscount = basePrice.subtract(discountAmount);
          if (priceAfterDiscount.compareTo(ZERO) < 0) {
            log.warn("Fixed discount {} is higher than base price {} for product ID {}. Setting price after discount to 0.",
                discountAmount, basePrice, productId);
            priceAfterDiscount = ZERO;
            discountAmount = basePrice;
          }
          log.debug("Applied fixed discount: Amount={}, PriceAfter={}", discountAmount, priceAfterDiscount);
        } else {
          log.warn("Invalid fixed discount value {} for product ID {}, ignoring.", discountInfo.value(), productId);
          discountInfo = new DiscountInfo(DiscountType.NONE, 0);
        }
        break;
      case NONE:
        log.debug("No discount applied.");
        break;
    }

    // 4. Steuersatz ermitteln
    double taxRate = taxService.getTaxRate(product);
    log.debug("Tax rate for product ID {}: {}", productId, taxRate);
    if (taxRate < 0) {
      log.error("Tax service returned negative tax rate {} for product ID {}. Using 0.", taxRate, productId);
      taxRate = 0;
    }

    // 5. Steuer berechnen (auf den Preis NACH Rabatt)
    BigDecimal taxAmount = ZERO;
    if (taxRate > 0) {
      taxAmount = priceAfterDiscount.multiply(BigDecimal.valueOf(taxRate))
          .setScale(2, RoundingMode.HALF_UP);
      log.debug("Calculated tax amount: {}", taxAmount);
    } else {
      log.debug("No tax applied (tax rate is 0 or negative).");
    }

    // 6. Endpreis berechnen
    BigDecimal finalPrice = priceAfterDiscount.add(taxAmount);
    log.info("Final calculated price for product ID {}: {}", productId, finalPrice);

    // 7. Ergebnis-DTO erstellen und zurückgeben
    return new PriceDetailsDTO(
        productId,
        basePrice,
        discountInfo,
        discountAmount,
        priceAfterDiscount,
        taxRate,
        taxAmount,
        finalPrice
    );
  }

}
