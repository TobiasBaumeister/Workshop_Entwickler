# Aufgabe 8

## Bearbeitungshinweis

Beim Bearbeiten dieser Aufgabe gilt folgende Vorgabe:

- Es darf **kein eigener Code** manuell geschrieben werden.
- Alle Codeabschnitte müssen **ausschließlich** durch den Chatbot generiert und von dort übernommen werden.
- Auch Korrekturen und Anpassungen dürfen nur vom Chatbot vorgenommen werden.

Ziel dieser Regel ist es, den gesamten Lösungsweg vollständig durch KI-Unterstützung abzubilden.

## Zielsetzung

Erstelle eine **ausführliche Dokumentation** für die bestehenden Controller:

- [CategoryController](../../ProductApiApplication/src/main/java/org/example/rest/CategoryController.java)
- [ProductController](../../ProductApiApplication/src/main/java/org/example/rest/ProductController.java)

Die Dokumentation soll für die spätere Projektdokumentation in **Confluence** verwendet werden.

Die Dokumentation soll auf folgender Confluence-Seite erstellt werden:  
**[KI Workshop für Entwickler](https://jira.s-und-n.de/confluence/pages/viewpage.action?pageId=280428589)**

Für jeden Controller wurde dort bereits eine eigene Seite unter eurem Namen vorbereitet.

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
- Beide Controller sind auf einer eigenen Seite im Confluence dokumentiert.
- Die Seiten sind **ansprechend formatiert** (klare Überschriften, Abschnitte, Beispiele) und gut lesbar aufgebaut.
- **Wichtig:** Die Formatierung wird ebenfalls **ausschließlich vom Chatbot** vorgegeben.  
  Eigene manuelle Formatierungen sind nicht erlaubt.  
  Falls Anpassungen an der Formatierung notwendig sind, muss die entsprechende Prompt-Formulierung an den Chatbot erfolgen.

---

### Hinweise

- Orientiere dich an einem typischen API-Dokumentationsstil.
- Nutze für die Beispiele realistische Produkt- und Kategoriedaten.
- Achte auf vollständige Angaben, insbesondere bei Response-Objekten und möglichen Fehlermeldungen (z. B. `404 Not Found`, `400 Bad Request`).
- Halte eine einheitliche und klare Strukturierung über beide Dokumentationsseiten hinweg ein.

---