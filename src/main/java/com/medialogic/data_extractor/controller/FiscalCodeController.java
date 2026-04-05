package com.medialogic.data_extractor.controller;

import com.medialogic.data_extractor.dto.ErrorResponse;
import com.medialogic.data_extractor.dto.FiscalCodeResponse;
import com.medialogic.data_extractor.exception.InvalidFiscalCodeException;
import com.medialogic.data_extractor.service.FiscalCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fiscal-code")
@Tag(name = "Codice Fiscale", description = "API per l'estrazione di dati dal codice fiscale italiano")
public class FiscalCodeController {

    private final FiscalCodeService fiscalCodeService;
    
    @Autowired
    public FiscalCodeController(FiscalCodeService fiscalCodeService) {
        this.fiscalCodeService = fiscalCodeService;
    }

    @Operation(
            summary = "Estrae data di nascita ed età dal codice fiscale",
            description = "Dato un codice fiscale italiano valido, restituisce la data di nascita, " +
                         "l'età calcolata e il sesso della persona. " +
                         "Il codice fiscale deve essere di 16 caratteri nel formato standard italiano."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dati estratti con successo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FiscalCodeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Codice fiscale non valido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{fiscalCode}")
    public ResponseEntity<FiscalCodeResponse> extractData(
            @Parameter(
                    description = "Codice fiscale italiano (16 caratteri)",
                    example = "RSSMRA90E15H501X",
                    required = true
            )
            @PathVariable String fiscalCode) throws InvalidFiscalCodeException {
        
        FiscalCodeResponse response = fiscalCodeService.extractDataFromFiscalCode(fiscalCode);
        return ResponseEntity.ok(response);
    }
}
