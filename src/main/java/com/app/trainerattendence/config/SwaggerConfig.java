package com.app.trainerattendence.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI trainerAttendanceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trainer Attendance Management API")
                        .description("API documentation for Trainer Attendance system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Barathkumaran K S")
                                .email("barathkumaran2003@gmail.com")
                                .url("https://trainerattendence-backed.onrender.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/BarathkumaranKS/TrainerAttendance"));
    }
}
