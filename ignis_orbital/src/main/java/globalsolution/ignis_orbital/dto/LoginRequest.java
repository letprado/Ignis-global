package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}
