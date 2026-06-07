package globalsolution.ignis_orbital.dto;

import java.time.LocalDateTime;

public record TelemetriaResponse(
        Long id,
        Long idSatelite,
        String nomeSatelite,
        String jsonData,
        LocalDateTime dtRecebimento,
        String statusProcessamento,
        Long alertaGeradoId
) {
}
