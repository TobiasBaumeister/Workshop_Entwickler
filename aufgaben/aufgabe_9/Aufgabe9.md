# Aufgabe 9

## Zielsetzung

Erstelle eine **ausführliche Dokumentation** für die bestehenden Controller:

- [CategoryController](../../ProductApiApplication/src/main/java/org/example/rest/CategoryController.java)
- [ProductController](../../ProductApiApplication/src/main/java/org/example/rest/ProductController.java)

Die Dokumentation soll für die spätere Projektdokumentation in **Confluence** verwendet werden.

## Anforderungen

- Dokumentiere die vorhandenen **REST-Endpunkte** der Controller vollständig.
- Beschreibe für jeden Endpunkt:
    - HTTP-Methode (z. B. `GET`, `POST`, `PUT`, `DELETE`)
    - Pfad (z. B. `/api/products/{id}`)
    - Zweck und Funktionalität des Endpunkts
    - Erwartete Eingaben (Request-Parameter, Request-Body)
    - Mögliche Antworten (Response-Body, HTTP-Statuscodes)
    - Eventuelle Validierungsregeln oder Besonderheiten
- Füge zu jedem Endpunkt **ein konkretes Beispiel** hinzu (Request und Response), idealerweise als JSON-Snippet.

## Erfolgskriterium

- Die Dokumentation ist vollständig und strukturiert.
- Alle Endpunkte in [`CategoryController.java`](../../ProductApiApplication/src/main/java/org/example/rest/CategoryController.java)
  und [`ProductController.java`](../../ProductApiApplication/src/main/java/org/example/rest/ProductController.java) sind beschrieben.
- Die Inhalte eignen sich für eine direkte Übernahme in eine Confluence-Seite.
- Einheitliche Struktur und klare Formulierungen werden verwendet.

---

### Hinweise

- Orientiere dich an einem typischen API-Dokumentationsstil.
- Nutze für die Beispiele realistische Produkt- und Kategoriedaten.
- Achte auf vollständige Angaben, insbesondere bei Response-Objekten und möglichen Fehlermeldungen (z. B. `404 Not Found`, `400 Bad Request`).

---
