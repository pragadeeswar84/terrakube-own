package io.terrakube.api.plugin.ai.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for AI status endpoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiStatusResponse {
    private boolean enabled;
    private String provider;
    private boolean recommendationsEnabled;
    private boolean autoUpdateEnabled;
    private boolean optimizationsEnabled;
}
