package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.PerfilUsuario;

import java.time.LocalDateTime;

public record UsuarioDetalheResponse(
        Long id,
        String nome,
        String email,
        PerfilUsuario perfil,
        LocalDateTime dtCadastro
) {
}
