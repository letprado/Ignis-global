package globalsolution.ignis_orbital.messaging;

import globalsolution.ignis_orbital.service.TelemetriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "ignis.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class TelemetriaProcessamentoListener {

    private final TelemetriaService telemetriaService;

    @RabbitListener(queues = "${ignis.messaging.telemetria-queue}")
    public void processar(TelemetriaProcessamentoMessage message) {
        log.info("Consumindo mensagem de telemetria para payload {}", message.payloadId());
        telemetriaService.processarPayload(message.payloadId());
    }
}
