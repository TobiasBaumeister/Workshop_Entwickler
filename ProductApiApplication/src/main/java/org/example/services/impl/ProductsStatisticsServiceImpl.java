package org.example.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.database.model.Product;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.ProductDTO;
import org.example.rest.mapper.ProductMapper;
import org.example.services.ProductStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Diese Klasse kann im Rahmen von Aufgabe 5 komplett überarbeitet werden
 */
@Service
@Transactional
public class ProductsStatisticsServiceImpl implements ProductStatisticsService {

  @Autowired
  private ProductRepository productrepository;

  @Autowired
  private CategoryRepository categoryrepository;

  @Autowired
  private ProductMapper MAPPER;


  @Override
  public Map<String, Long> countProductsPerCategory() {
    List<Product> list_of_all_products = productrepository.findAll();
    HashMap result_map = new HashMap();

    System.out.println("INFO: Processing " + list_of_all_products.size() + " products for counting.");

    int i = 0;
    while (i < list_of_all_products.size()) {
      Product current_p = list_of_all_products.get(i);
      if (current_p != null) {
        if (current_p.getCategory() != null && current_p.getCategory().getName() != null) {
          String catName = current_p.getCategory().getName();
          Object current_val_obj = result_map.get(catName);
          long current_val = 0;
          if (current_val_obj != null) {
            if (current_val_obj instanceof Long) {
              current_val = ((Long) current_val_obj).longValue();
            } else {
              System.err.println("WARNUNG: Unerwarteter Typ in Map gefunden für Key: " + catName);
            }
          }
          result_map.put(catName, Long.valueOf(current_val + 1));
        } else {
          if (current_p.getCategory() == null) {
            System.out.println("DEBUG: Produkt ID " + current_p.getId() + " hat keine Kategorie.");
          } else {
            System.out.println("DEBUG: Produkt ID " + current_p.getId() + " hat Kategorie ohne Namen.");
          }
        }
      }
      i++;
    }
    return result_map;
  }

  @Override
  public double getAverageProductPrice() {
    List things = productrepository.findAll();
    double total_price_sum = 0.0;
    int number_of_items = 0;

    for (int j = 0; j < things.size(); j++) {
      Object thing_obj = things.get(j);
      if (thing_obj instanceof Product) {
        Product prod = (Product) thing_obj;
        if (prod.getPrice() != null) {
          total_price_sum += prod.getPrice();
          number_of_items++;
        } else {
          System.out.println("Hinweis: Produkt mit ID " + prod.getId() + " hat keinen Preis, wird ignoriert.");
        }
      }
    }

    if (number_of_items > 0) {
      double avg = total_price_sum / number_of_items;
      System.out.println("Durchschnitt berechnet: " + avg);
      return avg;
    } else {
      System.out.println("Keine Produkte mit Preis gefunden, Durchschnitt ist 0.");
      return 0.0;
    }
  }

  @Override
  public double getAverageProductPricePerCategory(Long categoryId) {
    List products_all = productrepository.findAll();
    double price_sum_for_category = 0;
    long count_for_category = 0;

    for (Object p_obj : products_all) {
      Product p = (Product) p_obj;
      if (p != null) {
        if (p.getCategory() != null) {
          if (p.getCategory().getId() != null) {
            if (p.getCategory().getId().equals(categoryId)) {
              if (p.getPrice() != null) {
                price_sum_for_category = price_sum_for_category + p.getPrice();
                count_for_category = count_for_category + 1;
              } else {
                System.out.println("Hinweis: Produkt mit ID " + p.getId() + " hat keinen Preis, wird ignoriert.");
              }
            }
          }
        }
      }
    }

    if (count_for_category == 0) {
      System.out.println("Keine Produkte in Kategorie " + categoryId + " gefunden oder alle ohne Preis.");
      return 0.0;
    } else {
      double result = price_sum_for_category / count_for_category;
      return result;
    }
  }

  @Override
  public Optional<ProductDTO> findMostExpensiveProduct() {
    List<Product> productList = productrepository.findAll();
    Product the_most_expensive_one = null;
    Double max_price_found = null;

    for (int k = 0; k < productList.size(); k++) {
      Product current = productList.get(k);
      if (current != null && current.getPrice() != null) {
        if (max_price_found == null) {
          max_price_found = current.getPrice();
          the_most_expensive_one = current;
        } else {
          if (current.getPrice().doubleValue() > max_price_found.doubleValue()) {
            max_price_found = current.getPrice();
            the_most_expensive_one = current;
          }
        }
      }
    }

    if (the_most_expensive_one != null) {
      ProductDTO dto = MAPPER.toDto(the_most_expensive_one);
      return Optional.of(dto);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<ProductDTO> findCheapestProductInCategory(Long category_id) {
    List<Product> allProds = productrepository.findAll();
    Product cheapest = null;
    double currentMinPrice = -1;

    for (Product p : allProds) {
      boolean foundMatch = false;
      if (p != null && p.getCategory() != null && p.getCategory().getId() != null && p.getCategory().getId().equals(category_id)) {
        if (p.getPrice() != null) {
          if (cheapest == null) {
            cheapest = p;
            currentMinPrice = p.getPrice();
            foundMatch = true;
            System.out.println("DEBUG: Erstes günstiges Produkt in Kat " + category_id + " gefunden: ID " + p.getId());
          } else {
            if (p.getPrice() < currentMinPrice) {
              cheapest = p;
              currentMinPrice = p.getPrice();
              foundMatch = true;
              System.out.println("DEBUG: Neueres günstigeres Produkt in Kat " + category_id + " gefunden: ID " + p.getId());
            }
          }
        }
      }
      if (!foundMatch && p != null) {
        System.out.println("Produkt ID " + p.getId() + " gehört nicht zur Kategorie " + category_id + " oder hat keinen Preis.");
      }
    }

    if (cheapest == null) {
      return Optional.empty();
    } else {
      return Optional.ofNullable(MAPPER.toDto(cheapest));
    }
  }
}