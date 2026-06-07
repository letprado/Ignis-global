package globalsolution.ignis_orbital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import globalsolution.ignis_orbital.entity.NivelRisco;
import globalsolution.ignis_orbital.entity.StatusAlerta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AlertaResponse(
        @JsonProperty("id_alerta")
        Long idAlerta,
        @JsonProperty("regiao_nome")
        String regiaoNome,
        BigDecimal temperatura,
        NivelRisco risco,
        StatusAlerta status,
        @JsonProperty("data_captura")
        LocalDateTime dataCaptura,
        Coordenadas coordenadas,
        String satelite
) {
    public record Coordenadas(
            BigDecimal latitude,
            BigDecimal longitude
    ) {
    }
}
