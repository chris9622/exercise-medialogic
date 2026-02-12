package com.medialogic.data_extractor.service;

import com.medialogic.data_extractor.dto.FiscalCodeResponse;
import com.medialogic.data_extractor.exception.InvalidFiscalCodeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FiscalCodeServiceTest {

    @Autowired
    private FiscalCodeService fiscalCodeService;

    @Test
    void testValidMaleFiscalCode() {
        FiscalCodeResponse response = fiscalCodeService.extractDataFromFiscalCode("RSSMRA90E15H501X");

        assertNotNull(response);
        assertEquals(LocalDate.of(1990, 5, 15), response.getBirthDate());
        assertTrue(response.getAge() >= 35);
    }

    @Test
    void testValidFemaleFiscalCode() {
        FiscalCodeResponse response = fiscalCodeService.extractDataFromFiscalCode("RSSLRA85T55F205Z");

        assertNotNull(response);
        assertEquals(LocalDate.of(1985, 12, 15), response.getBirthDate());
        assertTrue(response.getAge() >= 40);
    }

    @Test
    void testFiscalCodeWithLeapYear() {
        FiscalCodeResponse response = fiscalCodeService.extractDataFromFiscalCode("RSSMRA16B29H501X");

        assertNotNull(response);
        assertEquals(LocalDate.of(2016, 2, 29), response.getBirthDate());
    }

    @Test
    void testNullFiscalCode() {
        InvalidFiscalCodeException exception = assertThrows(
                InvalidFiscalCodeException.class,
                () -> fiscalCodeService.extractDataFromFiscalCode(null)
        );

        assertTrue(exception.getMessage().contains("non può essere vuoto"));
    }

    @Test
    void testEmptyFiscalCode() {
        InvalidFiscalCodeException exception = assertThrows(
                InvalidFiscalCodeException.class,
                () -> fiscalCodeService.extractDataFromFiscalCode("   ")
        );

        assertTrue(exception.getMessage().contains("non può essere vuoto"));
    }

    @Test
    void testInvalidFormatTooShort() {
        InvalidFiscalCodeException exception = assertThrows(
                InvalidFiscalCodeException.class,
                () -> fiscalCodeService.extractDataFromFiscalCode("RSSMRA90E15")
        );

        assertTrue(exception.getMessage().contains("Il codice fiscale deve essere lungo 16 caratteri"));
    }

    @Test
    void testInvalidFormatWrongCharacters() {
        InvalidFiscalCodeException exception = assertThrows(
                InvalidFiscalCodeException.class,
                () -> fiscalCodeService.extractDataFromFiscalCode("1234567890123456")
        );

        assertTrue(exception.getMessage().contains("Cognome e nome deve contenere solo lettere maiuscole"));
    }

    @Test
    void testFiscalCodeCaseInsensitive() {
        FiscalCodeResponse response1 = fiscalCodeService.extractDataFromFiscalCode("RSSMRA90E15H501X");
        FiscalCodeResponse response2 = fiscalCodeService.extractDataFromFiscalCode("rssmra90e15h501x");

        assertEquals(response1.getBirthDate(), response2.getBirthDate());
    }

    @Test
    void testAllMonths() {
        String[] fiscalCodes = {
                "RSSMRA90A15H501X",
                "RSSMRA90B15H501X",
                "RSSMRA90C15H501X",
                "RSSMRA90D15H501X",
                "RSSMRA90E15H501X",
                "RSSMRA90H15H501X",
                "RSSMRA90L15H501X",
                "RSSMRA90M15H501X",
                "RSSMRA90P15H501X",
                "RSSMRA90R15H501X",
                "RSSMRA90S15H501X",
                "RSSMRA90T15H501X"
        };

        for (int i = 0; i < fiscalCodes.length; i++) {
            FiscalCodeResponse response = fiscalCodeService.extractDataFromFiscalCode(fiscalCodes[i]);
            assertEquals(i + 1, response.getBirthDate().getMonthValue());
        }
    }
}
