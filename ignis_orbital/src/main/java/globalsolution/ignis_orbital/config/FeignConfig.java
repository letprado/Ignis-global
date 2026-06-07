package globalsolution.ignis_orbital.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "globalsolution.ignis_orbital.client")
public class FeignConfig {
}
