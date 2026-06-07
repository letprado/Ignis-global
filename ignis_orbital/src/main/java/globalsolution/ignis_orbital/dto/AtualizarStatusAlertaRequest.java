package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.StatusAlerta;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusAlertaRequest(
        @NotNull StatusAlerta novoStatus
) {
}
