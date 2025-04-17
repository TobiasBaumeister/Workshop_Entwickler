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
    
    Product product = loadProduct(productId);
    BigDecimal basePrice = validateAndGetBasePrice(product);
    DiscountInfo discountInfo = discountService.getDiscount(product);
    
    PriceCalculationResult calculationResult = applyDiscount(basePrice, discountInfo, productId);
    
    double taxRate = getValidatedTaxRate(product);
    BigDecimal taxAmount = calculateTaxAmount(calculationResult.priceAfterDiscount(), taxRate);
    BigDecimal finalPrice = calculationResult.priceAfterDiscount().add(taxAmount);
    
    log.info("Final calculated price for product ID {}: {}", productId, finalPrice);
    
    return new PriceDetailsDTO(
        productId,
        basePrice,
        calculationResult.discountInfo(),
        calculationResult.discountAmount(),
        calculationResult.priceAfterDiscount(),
        taxRate,
        taxAmount,
        finalPrice
    );
  }

  private Product loadProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + productId));
  }

  private BigDecimal validateAndGetBasePrice(Product product) {
    BigDecimal basePrice = BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP);
    if (basePrice.compareTo(ZERO) < 0) {
      log.error("Product ID {} has negative price: {}", product.getId(), basePrice);
      throw new IllegalArgumentException("Produkt (ID: " + product.getId() + ") hat einen negativen Preis: " + basePrice);
    }
    log.debug("Base price for product ID {}: {}", product.getId(), basePrice);
    return basePrice;
  }

  private double getValidatedTaxRate(Product product) {
    double taxRate = taxService.getTaxRate(product);
    log.debug("Tax rate for product ID {}: {}", product.getId(), taxRate);
    if (taxRate < 0) {
      log.error("Tax service returned negative tax rate {} for product ID {}. Using 0.", taxRate, product.getId());
      taxRate = 0;
    }
    return taxRate;
  }

  private BigDecimal calculateTaxAmount(BigDecimal priceAfterDiscount, double taxRate) {
    if (taxRate <= 0) {
      log.debug("No tax applied (tax rate is 0 or negative).");
      return ZERO;
    }
    BigDecimal taxAmount = priceAfterDiscount.multiply(BigDecimal.valueOf(taxRate))
        .setScale(2, RoundingMode.HALF_UP);
    log.debug("Calculated tax amount: {}", taxAmount);
    return taxAmount;
  }

  private PriceCalculationResult applyDiscount(BigDecimal basePrice, DiscountInfo discountInfo, Long productId) {
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

    return new PriceCalculationResult(discountInfo, discountAmount, priceAfterDiscount);
  }

  private record PriceCalculationResult(
      DiscountInfo discountInfo,
      BigDecimal discountAmount,
      BigDecimal priceAfterDiscount
  ) {}
}
