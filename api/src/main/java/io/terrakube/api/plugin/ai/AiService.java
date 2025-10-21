package io.terrakube.api.plugin.ai;

import io.terrakube.api.rs.module.Module;

import java.util.List;

/**
 * AI Service interface for intelligent Terraform module management
 */
public interface AiService {
    
    /**
     * Get AI-powered module recommendations based on current workspace configuration
     * @param organizationId The organization ID
     * @param workspaceId The workspace ID
     * @return List of recommended modules
     */
    List<ModuleRecommendation> getModuleRecommendations(String organizationId, String workspaceId);
    
    /**
     * Check for available updates for a module using AI analysis
     * @param module The module to check
     * @return Update recommendation if available
     */
    ModuleUpdateRecommendation checkModuleUpdate(Module module);
    
    /**
     * Auto-update module to latest compatible version
     * @param moduleId The module ID to update
     * @return true if update was successful
     */
    boolean autoUpdateModule(String moduleId);
    
    /**
     * Analyze module usage patterns and suggest optimizations
     * @param organizationId The organization ID
     * @return List of optimization suggestions
     */
    List<OptimizationSuggestion> analyzeModuleUsage(String organizationId);
}
