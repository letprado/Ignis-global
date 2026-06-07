package globalsolution.ignis_orbital.dto;

public record LoginResponse(
        String token,
        UsuarioResponse usuario
) {
}
