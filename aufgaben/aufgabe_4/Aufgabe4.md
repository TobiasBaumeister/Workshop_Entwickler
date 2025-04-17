# Aufgabe 4

## Zielsetzung

Erstelle einen neuen **Endpunkt** zum Aktualisieren eines Produkts
im [ProductController](../../ProductApiApplication/src/main/java/org/example/rest/ProductController.java).  
Zusätzlich wird eine entsprechende Methode
im [ProductServiceImpl](../../ProductApiApplication/src/main/java/org/example/services/impl/ProductServiceImpl.java)
benötigt, um die Aktualisierungslogik im Service-Layer
umzusetzen.

## Anforderungen

- Implementiere im [ProductController](../../ProductApiApplication/src/main/java/org/example/rest/ProductController.java) einen
  neuen HTTP-Endpunkt, der es ermöglicht, ein bestehendes Produkt zu aktualisieren.
- Ergänze die Logik zur Produktaktualisierung in
  der [ProductServiceImpl](../../ProductApiApplication/src/main/java/org/example/services/impl/ProductServiceImpl.java).
- Der Endpunkt soll sowohl das eigentliche Produkt als auch die zugehörige Kategorie berücksichtigen und entsprechend validieren.

## Erfolgskriterium

Am Ende sollen die folgenden bestehenden Tests erfolgreich (grün) durchlaufen:

### Tests im [ProductServiceImplTest](../../ProductApiApplication/src/test/java/org/example/services/ProductServiceImplTest.java)

- `testUpdateProduct_Success`
- `testUpdateProduct_ProductNotFound`
- `testUpdateProduct_CategoryNotFound`

### Test im [ProductControllerTest](../../ProductApiApplication/src/test/java/org/example/rest/ProductControllerTest.java)

- `testUpdateProduct`

---

### Hinweise

- Achte darauf, saubere Fehlerbehandlungen einzubauen (z. B. falls Produkt oder Kategorie nicht gefunden werden).
- Nutze bestehende Mapper, um DTOs und Entitäten zu konvertieren.
- Halte dich an die bestehende Struktur und Konventionen im Projekt (z. B. Verwendung von ResponseEntity im Controller).
