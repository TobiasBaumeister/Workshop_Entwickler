# Aufgabe 8

## Zielsetzung

Für den [ProductExportServiceImpl](../../ProductApiApplication/src/main/java/org/example/services/impl/ProductExportServiceImpl.java) wurde eine
zugehörige Testklasse [ProductExportServiceImplTest](../../ProductApiApplication/src/test/java/org/example/services/ProductExportServiceImplTest.java)
erstellt.

Der Test `exportProductsToCsv_ok` schlägt derzeit fehl.  
Deine Aufgabe ist es, den **Grund** für den Fehler herauszufinden und den **Code zu korrigieren**, sodass:

- Der Test erfolgreich (grün) wird.
- Die Lösung nicht nur im Testszenario, sondern auch in der **normalen Anwendungslogik** funktioniert.

## Anforderungen

- Analysiere die Ursache für den Testfehler.
- Überarbeite die Implementierung
  in [`ProductExportServiceImpl.java`](../../ProductApiApplication/src/main/java/org/example/services/impl/ProductExportServiceImpl.java)
  entsprechend.
- Stelle sicher, dass alle Tests
  in [`ProductExportServiceImplTest.java`](../../ProductApiApplication/src/test/java/org/example/services/ProductExportServiceImplTest.java) danach
  grün sind.

## Erfolgskriterien

- Der Test `exportProductsToCsv_ok` läuft erfolgreich.
- Alle anderen vorhandenen Tests (`exportProductsToCsv_EmptyCategory`, `exportProductsToCsv_null`) bleiben grün.
- Die Umsetzung funktioniert **auch in der echten Anwendung** zuverlässig.

---
