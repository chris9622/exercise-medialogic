package com.medialogic.data_extractor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Risposta contenente i dati estratti dal codice fiscale")
public class FiscalCodeResponse {
    
    @Schema(description = "Data di nascita estratta dal codice fiscale", example = "1990-05-15")
    private LocalDate birthDate;
    
    @Schema(description = "Età calcolata in anni", example = "35")
    private int age;

}
