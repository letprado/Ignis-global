package globalsolution.ignis_orbital.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "ignis.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitTelemetriaGateway implements TelemetriaProcessamentoGateway {

    private final RabbitTemplate rabbitTemplate;

    @Value("${ignis.messaging.telemetria-queue}")
    private String telemetriaQueue;

    @Override
    public void solicitarProcessamento(Long payloadId) {
        rabbitTemplate.convertAndSend(telemetriaQueue, new TelemetriaProcessamentoMessage(payloadId));
        log.info("Mensagem RabbitMQ publicada para payload {}", payloadId);
    }
}
