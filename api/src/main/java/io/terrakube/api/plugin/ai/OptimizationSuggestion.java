package io.terrakube.api.plugin.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an AI-generated optimization suggestion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationSuggestion {
    private String type; // COST, PERFORMANCE, SECURITY, COMPLIANCE
    private String title;
    private String description;
    private String impact; // HIGH, MEDIUM, LOW
    private String recommendation;
    private double estimatedSavings;
}
