package de.workshop.entwickler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FehlerhafteKlasseTest {

    private final FehlerhafteKlasse fehlerhafteKlasse = new FehlerhafteKlasse();

    @Test
    void verarbeiteDatenKomplex_Success() {
        List<String> daten = Arrays.asList("10", "20", "besonders", "40");
        String erwartet = String.format("Element '%s' (besonders=%b) geteilt durch %d ergibt: %.2f",
                "20", false, 5, 4.00);
        // Dieser Test sollte erfolgreich sein, wenn alle privaten Methoden korrekt funktionieren
        // und keine Exceptions für gültige Eingaben werfen.
        assertEquals(erwartet, fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 1, 5));
    }

    @Test
    void verarbeiteDatenKomplex_IndexOutOfBounds() {
        List<String> daten = Arrays.asList("10", "20");
        // Erwartet, dass die Methode eine geeignete Fehlerbehandlung (z.B. Exception oder Fehlermeldung) hat,
        // anstatt einer rohen IndexOutOfBoundsException.
        // Hier prüfen wir auf eine spezifische Exception als Beispiel für korrekte Fehlerbehandlung.
        assertThrows(IllegalArgumentException.class, () -> {
             fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 2, 5);
        }, "Sollte eine Exception werfen, wenn der Index ungültig ist.");
    }

    @Test
    void verarbeiteDatenKomplex_NumberFormat() {
        List<String> daten = Arrays.asList("10", "abc", "30");
        // Erwartet korrekte Fehlerbehandlung statt NumberFormatException.
        assertThrows(IllegalArgumentException.class, () -> {
             fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 1, 5);
        }, "Sollte eine Exception werfen, wenn das Element keine Zahl ist.");
    }

    @Test
    void verarbeiteDatenKomplex_DivisionByZero() {
        List<String> daten = Arrays.asList("10", "20", "30");
        // Erwartet korrekte Fehlerbehandlung statt ArithmeticException.
        assertThrows(IllegalArgumentException.class, () -> {
             fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 1, 0);
        }, "Sollte eine Exception werfen, wenn der Divisor 0 ist.");
    }

     @Test
     void verarbeiteDatenKomplex_IstBesondersLogic() {
         List<String> daten = Arrays.asList("", "20"); // Leerer String am Index 0
         // Erwartet, dass ein leerer String NICHT als besonders gilt.
         // Der aktuelle Code würde `besonders=true` liefern.
         String erwartet = String.format("Element '%s' (besonders=%b) geteilt durch %d ergibt: %.2f",
                 "", false, 5, 0.00); // Annahme: Leerer String wird zu 0 konvertiert (oder Fehler behandelt)

         // Hängt auch von der Behebung von Fehler 8 (NumberFormat) ab, wenn leerer String nicht konvertiert werden kann.
         // Dieser Test ist komplexer und prüft die korrekte Logik von istBesonders im Kontext.
         assertDoesNotThrow(() -> {
             String ergebnis = fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 0, 5);
             // Hier müsste man genauer prüfen, was das *korrekte* Ergebnis sein soll,
             // nachdem alle Fehler behoben sind. Z.B. ob leere Strings erlaubt sind.
             // Fürs Erste prüfen wir nur, ob der String nicht als "besonders" markiert wird.
             assertFalse(ergebnis.contains("besonders=true"), "Leerer String sollte nicht als besonders gelten.");
         }, "Sollte keine Exception werfen und leeren String nicht als besonders behandeln.");

     }

    @Test
    void verarbeiteDatenKomplex_NullInput() {
        // Erwartet, dass die Methode eine Exception wirft, statt nur einen String zurückzugeben.
        assertThrows(IllegalArgumentException.class, () -> {
            fehlerhafteKlasse.verarbeiteDatenKomplex(null, 0, 5);
        }, "Sollte eine Exception bei null-Eingabe werfen.");
    }

    // Indirekter Test für Fehler 6 (extrahiereElement - IndexOutOfBounds)
    @Test
    void check_extrahiereElement_ValidIndex() {
        List<String> daten = Arrays.asList("10", "20");
        // Dieser Aufruf sollte NICHT fehlschlagen, wenn der Index gültig ist.
        // Wir provozieren hier keinen Fehler, sondern prüfen den Erfolgsfall,
        // der aber von der Korrektur von Fehler 8 und 9 abhängt.
        assertDoesNotThrow(() -> {
            fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 0, 5); // Gültiger Index 0
        }, "Sollte bei gültigem Index nicht fehlschlagen (wenn andere Fehler behoben sind).");
    }

    // Indirekter Test für Fehler 7 (istBesonders - Logik)
    @Test
    void check_istBesonders_CorrectLogic() {
        List<String> datenNormal = Arrays.asList("normal", "20");
        List<String> datenBesonders = Arrays.asList("besonders", "30");
        List<String> datenLeer = Arrays.asList("", "40");
        List<String> datenNull = Arrays.asList(null, "50"); // Testet auch Null-Verhalten von istBesonders

        // Annahmen nach Korrektur: "normal" -> false, "besonders" -> true, "" -> false, null -> false
        assertDoesNotThrow(() -> {
            String resultNormal = fehlerhafteKlasse.verarbeiteDatenKomplex(datenNormal, 0, 5);
            assertTrue(resultNormal.contains("besonders=false"), "'normal' sollte nicht besonders sein.");

            String resultBesonders = fehlerhafteKlasse.verarbeiteDatenKomplex(datenBesonders, 0, 5);
            assertTrue(resultBesonders.contains("besonders=true"), "'besonders' sollte besonders sein.");

            // Dieser Teil hängt stark von der Korrektur von Fehler 8 (parseInt) ab
            // String resultLeer = fehlerhafteKlasse.verarbeiteDatenKomplex(datenLeer, 0, 5);
            // assertTrue(resultLeer.contains("besonders=false"), "Leerer String sollte nicht besonders sein.");

            // Dieser Teil hängt stark von der Korrektur von Fehler 7 (Null Check) und 8 ab
            // assertThrows(NullPointerException oder IllegalArgumentException, () -> {
            //    fehlerhafteKlasse.verarbeiteDatenKomplex(datenNull, 0, 5);
            // }, "Null-String sollte nicht verarbeitet werden oder Fehler werfen.");
        }, "Tests für istBesonders Logik (abhängig von anderen Korrekturen).");
    }

    // Indirekter Test für Fehler 8 (konvertiereUndTeile - NumberFormat)
    @Test
    void check_konvertiereUndTeile_ValidNumber() {
        List<String> daten = Arrays.asList("123", "20");
        // Sollte eine gültige Zahl korrekt verarbeiten können.
        assertDoesNotThrow(() -> {
            String result = fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 0, 3);
            assertTrue(result.contains("ergibt: 41.00"), "Sollte '123' / 3 korrekt berechnen.");
        }, "Sollte gültige Zahlen ohne NumberFormatException verarbeiten.");
    }

    // Indirekter Test für Fehler 9 (konvertiereUndTeile - Arithmetic)
    @Test
    void check_konvertiereUndTeile_ValidDivision() {
        List<String> daten = Arrays.asList("100", "20");
        // Sollte eine gültige Division durchführen können.
        assertDoesNotThrow(() -> {
             String result = fehlerhafteKlasse.verarbeiteDatenKomplex(daten, 0, 4);
             assertTrue(result.contains("ergibt: 25.00"), "Sollte 100 / 4 korrekt berechnen.");
        }, "Sollte gültige Division ohne ArithmeticException durchführen.");
    }
} 