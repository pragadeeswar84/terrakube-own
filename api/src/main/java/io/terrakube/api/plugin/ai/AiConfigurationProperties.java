package io.terrakube.api.plugin.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for AI service integration
 */
@Configuration
@ConfigurationProperties(prefix = "terrakube.ai")
@Getter
@Setter
public class AiConfigurationProperties {
    
    /**
     * Enable or disable AI features
     */
    private boolean enabled = true;
    
    /**
     * AI provider (e.g., "openai", "anthropic", "local")
     */
    private String provider = "local";
    
    /**
     * API key for AI service provider
     */
    private String apiKey;
    
    /**
     * AI model to use (e.g., "gpt-4", "claude-3")
     */
    private String model = "gpt-4";
    
    /**
     * Enable auto-update feature
     */
    private boolean autoUpdateEnabled = false;
    
    /**
     * Minimum compatibility score for auto-updates (0.0 - 1.0)
     */
    private double autoUpdateMinScore = 0.8;
    
    /**
     * Enable module recommendations
     */
    private boolean recommendationsEnabled = true;
    
    /**
     * Enable usage optimization suggestions
     */
    private boolean optimizationsEnabled = true;
}
