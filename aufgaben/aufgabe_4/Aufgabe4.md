# Wichtige Hinweise vor dem Start

Bevor du mit der Bearbeitung der Aufgaben beginnst, stelle sicher, dass die Projektstruktur in deiner IDE korrekt eingerichtet ist:

- Markiere den Ordner `src/main/java` als **Sources Root**:
  - Rechtsklick auf den Ordner → **Mark Directory as** → **Sources Root**
- Markiere den Ordner `src/test/java` als **Test Sources Root**:
  - Rechtsklick auf den Ordner → **Mark Directory as** → **Test Sources Root**

Diese Einstellungen sind notwendig, damit der Code und die Tests korrekt kompiliert und ausgeführt werden können.

# Aufgabe 4

## Bearbeitungshinweis

Beim Bearbeiten dieser Aufgabe gilt folgende Vorgabe:

- Es darf **kein eigener Code** manuell geschrieben werden.
- Alle Codeabschnitte müssen **ausschließlich** durch den Chatbot generiert und von dort übernommen werden.
- Auch Korrekturen und Anpassungen dürfen nur vom Chatbot vorgenommen werden.

Ziel dieser Regel ist es, den gesamten Lösungsweg vollständig durch KI-Unterstützung abzubilden.

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

## Erfolgskriterien

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
