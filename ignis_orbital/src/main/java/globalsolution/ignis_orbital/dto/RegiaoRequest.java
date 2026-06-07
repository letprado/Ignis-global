package globalsolution.ignis_orbital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import globalsolution.ignis_orbital.entity.Bioma;
import jakarta.validation.constraints.*;

public record RegiaoRequest(
        @JsonProperty("nm_regiao")
        @NotBlank @Size(max = 100) String nmRegiao,
        @JsonProperty("ds_bioma")
        @NotNull Bioma dsBioma,
        @JsonProperty("nr_criticidade_base")
        @NotNull @Min(1) @Max(10) Integer nrCriticidadeBase,
        @JsonProperty("sg_uf")
        @Size(min = 2, max = 2) String sgUf
) {
}
