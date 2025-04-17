package org.example.services;

import org.example.database.model.Category;

public interface ProductExportService {

  /**
   * Erstellt einen CSV-String mit allen Produkten einer gegebenen Kategorie. Der CSV-String enthält Spalten: ID, Name, Preis.
   *
   * @param category Die Kategorie, deren Produkte exportiert werden sollen.
   * @return Einen String im CSV-Format oder eine Fehlermeldung bei Problemen.
   */
  String exportProductsByCategoryToCsv(Category category);
}
