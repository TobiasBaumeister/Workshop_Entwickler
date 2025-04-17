# Aufgabe 6

## Bearbeitungshinweis

Beim Bearbeiten dieser Aufgabe gilt folgende Vorgabe:

- Es darf **kein eigener Code** manuell geschrieben werden.
- Alle Codeabschnitte müssen **ausschließlich** durch den Chatbot generiert und von dort übernommen werden.
- Auch Korrekturen und Anpassungen dürfen nur vom Chatbot vorgenommen werden.

Ziel dieser Regel ist es, den gesamten Lösungsweg vollständig durch KI-Unterstützung abzubilden.

## Zielsetzung

Erstelle eine neue
Testklasse [PriceCalculationServiceImplTest](../../ProductApiApplication/src/test/java/org/example/services/pricing/PriceCalculationServiceImplTest.java)
für den [PriceCalculationServiceImpl](../../ProductApiApplication/src/main/java/org/example/services/impl/pricing/PriceCalculationServiceImpl.java).

Ziel ist es, mit dieser Testklasse eine **100 % Testabdeckung** für die Methoden des `PriceCalculationServiceImpl` zu erreichen.

## Anforderungen

- Decke alle möglichen Ausführungszweige ab, inklusive:
    - Erfolgreicher Preisberechnung mit verschiedenen Rabattarten (`PERCENTAGE`, `FIXED_AMOUNT`, `NONE`).
    - Fehlerfälle wie:
        - Produkt nicht gefunden (`ResourceNotFoundException`).
        - Produktpreis ist negativ (`IllegalArgumentException`).
        - Ungültiger Rabattwert.
        - Negativer Steuersatz.
- Nutze für das Testen Frameworks wie JUnit 5 und Mockito.

## Erfolgskriterien

- Nach dem Ausführen der Tests mit der Funktion **[Run with Coverage](https://www.jetbrains.com/help/idea/code-coverage.html)** in IntelliJ IDEA soll
  eine **100 % Testabdeckung** erreicht werden.
- Alle Tests müssen grün (erfolgreich) sein.

---

### Hinweise

- Mocke Abhängigkeiten sinnvoll, um verschiedene Eingabeszenarien zu simulieren.
- Teste auch Randfälle wie:
    - Rabatte mit ungültigen Prozentwerten (größer als 1 oder kleiner als 0).
    - Rabatte mit fixem Betrag größer als der Produktpreis.
    - Steuerberechnungen mit negativem oder Nullsteuersatz.
- Achte auf eine klare Benennung der Testmethoden, z. B. `shouldCalculatePriceWithPercentageDiscount` oder `shouldThrowExceptionWhenProductNotFound`.
