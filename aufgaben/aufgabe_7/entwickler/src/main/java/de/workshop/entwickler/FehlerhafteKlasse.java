package de.workshop.entwickler;

import java.util.List;

public class FehlerhafteKlasse {

    /**
     * Verarbeitet eine Liste von Strings komplex.
     * Ruft interne private Methoden auf, die fehleranfällig sind.
     * @param daten Liste von Strings (könnte Zahlen enthalten).
     * @param suchIndex Index, dessen Element speziell behandelt wird.
     * @param divisor Divisor für eine interne Berechnung.
     * @return Ein Ergebnisstring oder eine Fehlermeldung (potenziell aber Exception).
     */
    public String verarbeiteDatenKomplex(List<String> daten, int suchIndex, int divisor) {
        if (daten == null) {
            return "Fehler: Eingabeliste ist null";
        }

        String element = extrahiereElement(daten, suchIndex);
        boolean besonders = istBesonders(element);
        double ergebnis = konvertiereUndTeile(element, divisor);

        return String.format("Element '%s' (besonders=%b) geteilt durch %d ergibt: %.2f",
                element, besonders, divisor, ergebnis);
    }

    /**
     * Extrahiert ein Element an einem gegebenen Index.
     * Fehler: Wirft IndexOutOfBoundsException bei ungültigem Index.
     */
    private String extrahiereElement(List<String> liste, int index) {
        return liste.get(index);
    }

    /**
     * Prüft, ob ein String "besonders" ist.
     * Fehler: Logikfehler, betrachtet leere Strings fälschlicherweise als besonders.
     */
    private boolean istBesonders(String s) {
        return s.equalsIgnoreCase("besonders") || s.isEmpty();
    }

    /**
     * Versucht, einen String in eine Zahl zu konvertieren und durch einen Divisor zu teilen.
     * Fehler: Wirft NumberFormatException oder ArithmeticException.
     */
    private double konvertiereUndTeile(String wertStr, int divisor) {
        int wert = Integer.parseInt(wertStr);
        return (double) wert / divisor;
    }
} 