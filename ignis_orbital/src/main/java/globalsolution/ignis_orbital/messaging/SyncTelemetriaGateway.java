package globalsolution.ignis_orbital.messaging;

import globalsolution.ignis_orbital.service.TelemetriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "ignis.messaging.enabled", havingValue = "false")
public class SyncTelemetriaGateway implements TelemetriaProcessamentoGateway {

    private final TelemetriaService telemetriaService;

    public SyncTelemetriaGateway(@Lazy TelemetriaService telemetriaService) {
        this.telemetriaService = telemetriaService;
    }

    @Override
    public void solicitarProcessamento(Long payloadId) {
        log.info("Processamento sincrono de telemetria para payload {}", payloadId);
        telemetriaService.processarPayload(payloadId);
    }
}
