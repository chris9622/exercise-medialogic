package com.medialogic.data_extractor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Risposta di errore")
public class ErrorResponse {
    
    @Schema(description = "Timestamp dell'errore")
    private LocalDateTime timestamp;
    
    @Schema(description = "Codice di stato HTTP", example = "400")
    private int status;
    
    @Schema(description = "Messaggio di errore", example = "Codice fiscale non valido")
    private String message;
    
    @Schema(description = "Percorso della richiesta", example = "/api/v1/fiscal-code/RSSMRA90E15H501X")
    private String path;
}
