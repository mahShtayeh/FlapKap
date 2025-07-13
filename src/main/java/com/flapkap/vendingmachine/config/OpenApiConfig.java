package com.flapkap.vendingmachine.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.NoArgsConstructor;

/**
 * OpenAPI Specification (OAS) Version 3.1.1 configuration
 *
 * @author Mahmoud Shtayeh
 */
@OpenAPIDefinition(
        info = @Info(
                title = "FlapKap - Vending Machine API",
                version = "1.0.0",
                description = "Manage FlapKap vending machine.",
                termsOfService = "https://flapkap.com/services/terms",
                contact = @Contact(
                        name = "Mahmoud A. M. Shtayeh",
                        url = "https://www.linkedin.com/in/mahmoud-shtayeh-436126144",
                        email = "mahmoud.shetia227@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "JWT Authorization header using the Bearer scheme. Example: 'Bearer {token}'",
        in = SecuritySchemeIn.HEADER
)
@NoArgsConstructor
public class OpenApiConfig {
}