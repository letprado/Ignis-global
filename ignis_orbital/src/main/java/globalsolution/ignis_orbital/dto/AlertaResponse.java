package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.NivelRisco;
import globalsolution.ignis_orbital.entity.StatusAlerta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AlertaResponse(
        Long id,
        String regiao,
        BigDecimal temperatura,
        NivelRisco risco,
        StatusAlerta status,
        LocalDateTime dataCaptura,
        BigDecimal latitude,
        BigDecimal longitude,
        String satelite
) {
}
