package globalsolution.ignis_orbital.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ignis.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMqConfig {

    @Value("${ignis.messaging.telemetria-queue}")
    private String telemetriaQueue;

    @Bean
    Queue telemetriaQueue() {
        return new Queue(telemetriaQueue, true);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
