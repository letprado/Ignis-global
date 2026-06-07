package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.Bioma;
import jakarta.validation.constraints.*;

public record RegiaoRequest(
        @NotBlank @Size(max = 100) String nmRegiao,
        @NotNull Bioma dsBioma,
        @NotNull @Min(1) @Max(10) Integer nrCriticidadeBase,
        @NotBlank @Size(min = 2, max = 2) String sgUf
) {
}
