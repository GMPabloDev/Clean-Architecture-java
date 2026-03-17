package io.gianmarco.cleanArchitecture.application.services;

import java.util.Map;

public interface TemplateRenderer {
    String render(String templateName, Map<String, String> vars);
}
