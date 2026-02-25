package com.autoflex.inventory.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.*;

class WebConfigTest {

    @Test
    void addCorsMappings_ShouldConfigureCors() {
        // Deve configurar corretamente o CORS permitindo todas as origens, métodos e cabeçalhos
        WebConfig webConfig = new WebConfig();
        CorsRegistry registry = mock(CorsRegistry.class);
        org.springframework.web.servlet.config.annotation.CorsRegistration registration = mock(org.springframework.web.servlet.config.annotation.CorsRegistration.class);

        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins("*")).thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);

        webConfig.addCorsMappings(registry);

        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("*");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
    }
}
