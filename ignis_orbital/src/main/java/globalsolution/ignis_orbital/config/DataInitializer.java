package globalsolution.ignis_orbital.config;

import globalsolution.ignis_orbital.entity.PerfilUsuario;
import globalsolution.ignis_orbital.entity.Usuario;
import globalsolution.ignis_orbital.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@ignis.com")) {
            Usuario admin = Usuario.builder()
                    .nome("Admin")
                    .email("admin@ignis.com")
                    .senhaHash(passwordEncoder.encode("123"))
                    .perfil(PerfilUsuario.ADMIN)
                    .dtCadastro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario admin@ignis.com criado com senha 123");
        }
    }
}
