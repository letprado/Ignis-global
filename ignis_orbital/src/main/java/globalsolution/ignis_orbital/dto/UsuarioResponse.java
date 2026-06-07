package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.PerfilUsuario;

public record UsuarioResponse(
        Long id,
        String nome,
        PerfilUsuario perfil
) {
}
