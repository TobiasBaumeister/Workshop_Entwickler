# Aufgabe 8

## Zielsetzung

Für den [ProductExportServiceImpl](../../ProductApiApplication/src/main/java/org/example/services/impl/ProductExportServiceImpl.java) wurde eine
zugehörige Testklasse [ProductExportServiceImplTest](../../ProductApiApplication/src/test/java/org/example/services/ProductExportServiceImplTest.java)
erstellt.

Mehrere Tests schlagen derzeit fehl:

- Der Test `exportProductsToCsv_ok` schlägt aus **zwei unterschiedlichen Gründen** fehl.
- Auch der Test `exportProductsToCsv_null` schlägt aktuell fehl.

Deine Aufgabe ist es, die Fehlerquellen jeweils zu identifizieren, die Ursachen zu verstehen und die Implementierung so anzupassen, dass:

- Alle Tests wieder erfolgreich (grün) laufen.
- Die Lösung nicht nur im Testszenario funktioniert, sondern auch in der normalen Ausführung der Anwendung.

## Anforderungen

- Analysiere die Gründe für die Fehler in `exportProductsToCsv_ok` und `exportProductsToCsv_null`.
- Alle im Testlauf auftretenden Probleme müssen vollständig behoben werden.
- Die Anwendung soll auch außerhalb der Tests korrekt funktionieren.
- Achte darauf, dass deine Korrekturen nicht nur für die Tests gelten, sondern auch für die reale Laufzeitumgebung (z. B. beim echten CSV-Export).

## Erfolgskriterium

- Alle Tests
  in [`ProductExportServiceImplTest.java`](../../ProductApiApplication/src/test/java/org/example/services/ProductExportServiceImplTest.java) laufen
  erfolgreich (grün).
- Die Anwendung generiert in allen Fällen korrektes CSV-Format.
- Fehlerbehandlungen (z. B. bei ungültigen Kategorien oder fehlenden Daten) funktionieren robust und einheitlich.

---
