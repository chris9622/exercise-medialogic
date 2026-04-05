package com.medialogic.data_extractor.service;

import com.medialogic.data_extractor.dto.FiscalCodeResponse;
import com.medialogic.data_extractor.exception.InvalidFiscalCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Service
public class FiscalCodeService {

    private static final Map<Character, Integer> MONTH_MAP = Map.ofEntries(
            Map.entry('A', 1),
            Map.entry('B', 2),
            Map.entry('C', 3),
            Map.entry('D', 4),
            Map.entry('E', 5),
            Map.entry('H', 6),
            Map.entry('L', 7),
            Map.entry('M', 8),
            Map.entry('P', 9),
            Map.entry('R', 10),
            Map.entry('S', 11),
            Map.entry('T', 12)
    );

    public FiscalCodeResponse extractDataFromFiscalCode(String fiscalCode) throws InvalidFiscalCodeException {

        if (fiscalCode == null || fiscalCode.isBlank()) {
            throw new InvalidFiscalCodeException("Il codice fiscale non può essere vuoto");
        }

        fiscalCode = fiscalCode.trim().toUpperCase();

        validateFiscalCode(fiscalCode);

        try {
            int year = extractYear(fiscalCode);
            int month = extractMonth(fiscalCode);
            int day = extractDay(fiscalCode);

            LocalDate birthDate = LocalDate.of(year, month, day);
            int age = calculateAge(birthDate);



            return FiscalCodeResponse.builder()
                    .birthDate(birthDate)
                    .age(age)
                    .build();

        } catch (Exception e) {
            throw new InvalidFiscalCodeException(
                    "Errore nell'estrazione dei dati dal codice fiscale: " + e.getMessage(),
                    e
            );
        }
    }


    private void validateFiscalCode(String cf) throws InvalidFiscalCodeException {

        if (cf.length() != 16) {
            throw new InvalidFiscalCodeException("Il codice fiscale deve essere lungo 16 caratteri");
        }

        checkLetters(cf, 0, 6, "Cognome e nome");
        checkDigits(cf, 6, 8, "Anno di nascita");
        checkMonth(cf.charAt(8));
        checkDigits(cf, 9, 11, "Giorno di nascita");
        checkDay(Integer.parseInt(cf.substring(9, 11)));
        checkLetter(cf.charAt(11), "Codice catastale");
        checkDigits(cf, 12, 15, "Codice catastale");
        checkLetter(cf.charAt(15), "Carattere di controllo");
    }

    private void checkLetters(String cf, int start, int end, String field) throws InvalidFiscalCodeException {
        for (int i = start; i < end; i++) {
            if (!Character.isUpperCase(cf.charAt(i))) {
                throw new InvalidFiscalCodeException(
                        field + " deve contenere solo lettere maiuscole"
                );
            }
        }
    }

    private void checkLetter(char c, String field) throws InvalidFiscalCodeException {
        if (!Character.isUpperCase(c)) {
            throw new InvalidFiscalCodeException(field + " deve essere una lettera maiuscola");
        }
    }

    private void checkDigits(String cf, int start, int end, String field) throws InvalidFiscalCodeException {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(cf.charAt(i))) {
                throw new InvalidFiscalCodeException(field + " deve contenere solo numeri");
            }
        }
    }

    private void checkMonth(char monthChar) throws InvalidFiscalCodeException {
        if (!MONTH_MAP.containsKey(monthChar)) {
            throw new InvalidFiscalCodeException(
                    "Carattere mese non valido nel codice fiscale: " + monthChar
            );
        }
    }

    private void checkDay(int day) throws InvalidFiscalCodeException {
        if (day < 1 || day > 71) {
            throw new InvalidFiscalCodeException(
                    "Giorno non valido nel codice fiscale: " + day
            );
        }

        int realDay = day > 40 ? day - 40 : day;
        if (realDay > 31) {
            throw new InvalidFiscalCodeException(
                    "Giorno reale non valido nel codice fiscale: " + realDay
            );
        }
    }


    private int extractYear(String fiscalCode) {
        int yearTwoDigits = Integer.parseInt(fiscalCode.substring(6, 8));
        int currentYear = LocalDate.now().getYear();
        int currentCentury = (currentYear / 100) * 100;
        int previousCentury = currentCentury - 100;

        int yearWithCurrentCentury = currentCentury + yearTwoDigits;
        return yearWithCurrentCentury > currentYear
                ? previousCentury + yearTwoDigits
                : yearWithCurrentCentury;
    }

    private int extractMonth(String fiscalCode) {
        return MONTH_MAP.get(fiscalCode.charAt(8));
    }

    private int extractDay(String fiscalCode) {
        int day = Integer.parseInt(fiscalCode.substring(9, 11));
        return day > 40 ? day - 40 : day;
    }



    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
