package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.dto.LoginRequest;
import globalsolution.ignis_orbital.dto.LoginResponse;
import globalsolution.ignis_orbital.dto.UsuarioResponse;
import globalsolution.ignis_orbital.security.JwtService;
import globalsolution.ignis_orbital.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        UsuarioAutenticado usuario = (UsuarioAutenticado) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getPerfil()
        );

        return new LoginResponse(token, usuarioResponse);
    }
}
