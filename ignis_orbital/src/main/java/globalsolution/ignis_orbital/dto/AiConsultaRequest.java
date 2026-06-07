package globalsolution.ignis_orbital.dto;

import jakarta.validation.constraints.NotBlank;

public record AiConsultaRequest(
        @NotBlank String pergunta
) {
}
