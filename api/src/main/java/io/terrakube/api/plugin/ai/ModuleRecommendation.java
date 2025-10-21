package io.terrakube.api.plugin.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an AI-powered module recommendation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRecommendation {
    private String moduleName;
    private String provider;
    private String version;
    private String description;
    private double confidenceScore;
    private String reasoning;
    private String category;
}
