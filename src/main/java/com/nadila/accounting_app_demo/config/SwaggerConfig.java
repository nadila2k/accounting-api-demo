package com.nadila.accounting_app_demo.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()

                // ── API metadata shown at the top of Swagger UI ───────────────
                .info(new Info()
                        .title("Accounting App API")
                        .description("""
                                REST API for the Accounting Application.
                                
                                **How to authenticate:**
                                1. Call `POST /api/v1/auth/login` with your credentials.
                                2. Copy the `token` from the response.
                                3. Click the **Authorize 🔒** button above and paste:  `Bearer <your_token>`
                                4. Click **Authorize** → now all endpoints include your JWT automatically.
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Nadila")
                                .email("nadila@example.com"))
                        .license(new License()
                                .name("MIT License")))

                // ── Tell Swagger how JWT auth works ───────────────────────────
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))          // apply globally to all endpoints

                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")     // just a label in the UI
                                        .description("Paste your JWT token here (without 'Bearer ' prefix)")));
    }
}

