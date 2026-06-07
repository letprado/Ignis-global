package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.dto.UsuarioDetalheResponse;
import globalsolution.ignis_orbital.dto.UsuarioRequest;
import globalsolution.ignis_orbital.entity.Usuario;
import globalsolution.ignis_orbital.exception.RecursoNaoEncontradoException;
import globalsolution.ignis_orbital.exception.RegraNegocioException;
import globalsolution.ignis_orbital.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDetalheResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioDetalheResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public UsuarioDetalheResponse criar(UsuarioRequest request) {
        String email = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Ja existe usuario cadastrado com este email");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome().trim())
                .email(email)
                .senhaHash(passwordEncoder.encode(request.senha()))
                .perfil(request.perfil())
                .dtCadastro(LocalDateTime.now())
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado: " + id));
    }

    private UsuarioDetalheResponse toResponse(Usuario usuario) {
        return new UsuarioDetalheResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getDtCadastro()
        );
    }
}
