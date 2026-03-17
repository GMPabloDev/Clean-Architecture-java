package io.gianmarco.cleanArchitecture.infrastructure.services;

import io.gianmarco.cleanArchitecture.application.services.TemplateRenderer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class ResourceTemplateRenderer implements TemplateRenderer {

    private final ResourceLoader resourceLoader;

    public ResourceTemplateRenderer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String render(String templateName, Map<String, String> vars) {
        try {
            // Carga el archivo desde resources/templates/
            Resource resource = resourceLoader.getResource(
                "classpath:templates/" + templateName
            );
            String html = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
            );

            // Reemplaza tus {{ VAR }}
            for (Map.Entry<String, String> entry : vars.entrySet()) {
                html = html.replace(
                    "{{" + entry.getKey() + "}}",
                    entry.getValue()
                );
            }
            return html;
        } catch (IOException e) {
            throw new RuntimeException(
                "Error cargando plantilla: " + templateName,
                e
            );
        }
    }
}
