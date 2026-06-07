package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(min = 3, max = 72) String senha,
        @NotNull PerfilUsuario perfil
) {
}
