package br.com.gspadilha.gestaodevagas.module.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutCompanyResponseDTO {
    private String access_token;
    private Long expires_in;
}
