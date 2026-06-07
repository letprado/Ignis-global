package globalsolution.ignis_orbital.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import globalsolution.ignis_orbital.dto.TelemetriaResponse;
import globalsolution.ignis_orbital.entity.*;
import globalsolution.ignis_orbital.exception.RecursoNaoEncontradoException;
import globalsolution.ignis_orbital.exception.RegraNegocioException;
import globalsolution.ignis_orbital.messaging.TelemetriaProcessamentoGateway;
import globalsolution.ignis_orbital.repository.AlertaRepository;
import globalsolution.ignis_orbital.repository.HistoricoLogRepository;
import globalsolution.ignis_orbital.repository.RegiaoRepository;
import globalsolution.ignis_orbital.repository.TelemetriaRawRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetriaService {

    private final TelemetriaRawRepository telemetriaRepository;
    private final AlertaRepository alertaRepository;
    private final RegiaoRepository regiaoRepository;
    private final HistoricoLogRepository historicoLogRepository;
    private final AlertaService alertaService;
    private final TelemetriaProcessamentoGateway processamentoGateway;
    private final ObjectMapper objectMapper;

    public List<TelemetriaResponse> listarPendentes() {
        return telemetriaRepository.findByDsStatusProc(StatusProcessamento.PENDENTE).stream()
                .map(t -> toResponse(t, null))
                .toList();
    }

    public void solicitarProcessamentoAssincrono(Long payloadId) {
        buscarEntidade(payloadId);
        processamentoGateway.solicitarProcessamento(payloadId);
    }

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public TelemetriaResponse processarPayload(Long payloadId) {
        TelemetriaRaw telemetria = buscarEntidade(payloadId);

        if (telemetria.getDsStatusProc() == StatusProcessamento.PROCESSADO) {
            throw new RegraNegocioException("Payload ja processado: " + payloadId);
        }

        try {
            JsonNode root = objectMapper.readTree(telemetria.getJsonData());
            JsonNode coords = root.path("coordenadas");

            double lat = coords.path("lat").asDouble();
            double lon = coords.path("lon").asDouble();
            double temperatura = root.path("temperatura").asDouble();

            Regiao regiao = regiaoRepository.findAll().stream()
                    .min(Comparator.comparingDouble(r -> distanciaAproximada(
                            lat, lon,
                            coordenadaReferencia(r.getSgUf()).lat(),
                            coordenadaReferencia(r.getSgUf()).lon()
                    )))
                    .orElseThrow(() -> new RegraNegocioException("Nenhuma regiao cadastrada para vincular telemetria"));

            Alerta alerta = Alerta.builder()
                    .regiao(regiao)
                    .satelite(telemetria.getSatelite())
                    .nrLatitude(BigDecimal.valueOf(lat))
                    .nrLongitude(BigDecimal.valueOf(lon))
                    .vlTemperatura(BigDecimal.valueOf(temperatura))
                    .dsRisco(alertaService.classificarRisco(temperatura))
                    .dsStatus(StatusAlerta.DETECTADO)
                    .dtCaptura(LocalDateTime.now())
                    .build();

            alerta = alertaRepository.save(alerta);

            telemetria.setDsStatusProc(StatusProcessamento.PROCESSADO);
            telemetriaRepository.save(telemetria);

            historicoLogRepository.save(HistoricoLog.builder()
                    .alerta(alerta)
                    .dsAcao("Alerta criado via processamento automatico de telemetria #" + payloadId)
                    .dtRegistro(LocalDateTime.now())
                    .build());

            log.info("Telemetria {} processada. Alerta {} gerado para regiao {}", payloadId, alerta.getId(), regiao.getNmRegiao());
            return toResponse(telemetria, alerta.getId());

        } catch (RegraNegocioException ex) {
            throw ex;
        } catch (Exception ex) {
            telemetria.setDsStatusProc(StatusProcessamento.ERRO);
            telemetriaRepository.save(telemetria);
            throw new RegraNegocioException("Falha ao processar telemetria: " + ex.getMessage());
        }
    }

    public TelemetriaRaw buscarEntidade(Long id) {
        return telemetriaRepository.findByIdComSatelite(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Payload de telemetria nao encontrado: " + id));
    }

    private TelemetriaResponse toResponse(TelemetriaRaw telemetria, Long alertaId) {
        return new TelemetriaResponse(
                telemetria.getId(),
                telemetria.getSatelite().getId(),
                telemetria.getSatelite().getNmSatelite(),
                telemetria.getJsonData(),
                telemetria.getDtRecebimento(),
                telemetria.getDsStatusProc().name(),
                alertaId
        );
    }

    private double distanciaAproximada(double lat1, double lon1, double lat2, double lon2) {
        return Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2);
    }

    private Coordenada coordenadaReferencia(String uf) {
        return switch (uf) {
            case "AM" -> new Coordenada(-3.1, -60.0);
            case "PA" -> new Coordenada(-2.0, -54.0);
            case "TO" -> new Coordenada(-9.0, -48.0);
            case "GO" -> new Coordenada(-15.9, -47.9);
            case "MT" -> new Coordenada(-16.0, -56.0);
            case "MS" -> new Coordenada(-20.0, -57.0);
            case "SP" -> new Coordenada(-23.5, -46.6);
            case "RJ" -> new Coordenada(-22.9, -43.2);
            case "PI" -> new Coordenada(-7.0, -41.0);
            case "CE" -> new Coordenada(-5.0, -39.0);
            case "RS" -> new Coordenada(-30.0, -51.0);
            case "RO" -> new Coordenada(-10.0, -63.0);
            default -> new Coordenada(-15.0, -47.0);
        };
    }

    private record Coordenada(double lat, double lon) {
    }
}
