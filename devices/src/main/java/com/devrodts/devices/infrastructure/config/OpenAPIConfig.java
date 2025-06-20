package com.devrodts.devices.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI deviceManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Device Manager API")
                        .description("REST API for managing devices")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Devices Team")
                                .email("support@devices.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
