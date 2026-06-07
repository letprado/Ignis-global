package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.dto.AlertaResponse;
import globalsolution.ignis_orbital.dto.AtualizarStatusAlertaRequest;
import globalsolution.ignis_orbital.entity.*;
import globalsolution.ignis_orbital.exception.RecursoNaoEncontradoException;
import globalsolution.ignis_orbital.repository.AlertaRepository;
import globalsolution.ignis_orbital.repository.HistoricoLogRepository;
import globalsolution.ignis_orbital.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final HistoricoLogRepository historicoLogRepository;

    @Cacheable("alertas")
    public List<AlertaResponse> listarTodos() {
        return alertaRepository.findAllComRelacionamentos().stream()
                .map(this::toResponse)
                .toList();
    }

    public AlertaResponse buscarPorId(Long id) {
        Alerta alerta = buscarEntidade(id);
        return toResponse(alerta);
    }

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public AlertaResponse atualizarStatus(Long id, AtualizarStatusAlertaRequest request) {
        Alerta alerta = buscarEntidade(id);
        StatusAlerta statusAnterior = alerta.getDsStatus();
        alerta.setDsStatus(request.novoStatus());
        alertaRepository.save(alerta);

        UsuarioAutenticado usuario = (UsuarioAutenticado) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        HistoricoLog log = HistoricoLog.builder()
                .alerta(alerta)
                .usuario(Usuario.builder().id(usuario.getId()).build())
                .dsAcao("Status alterado de " + statusAnterior + " para " + request.novoStatus())
                .dtRegistro(LocalDateTime.now())
                .build();
        historicoLogRepository.save(log);

        return toResponse(alerta);
    }

    public Alerta buscarEntidade(Long id) {
        return alertaRepository.findByIdComRelacionamentos(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta nao encontrado: " + id));
    }

    public NivelRisco classificarRisco(double temperatura) {
        if (temperatura >= 45) {
            return NivelRisco.ALTO;
        }
        if (temperatura >= 35) {
            return NivelRisco.MEDIO;
        }
        return NivelRisco.BAIXO;
    }

    private AlertaResponse toResponse(Alerta alerta) {
        return new AlertaResponse(
                alerta.getId(),
                alerta.getRegiao().getNmRegiao(),
                alerta.getVlTemperatura(),
                alerta.getDsRisco(),
                alerta.getDsStatus(),
                alerta.getDtCaptura(),
                new AlertaResponse.Coordenadas(alerta.getNrLatitude(), alerta.getNrLongitude()),
                alerta.getSatelite().getNmSatelite()
        );
    }
}
