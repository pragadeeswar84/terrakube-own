package io.terrakube.api.plugin.ai.controller;

import io.terrakube.api.plugin.ai.*;
import io.terrakube.api.repository.ModuleRepository;
import io.terrakube.api.rs.module.Module;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for AI-powered features
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
@AllArgsConstructor
public class AiController {
    
    private final AiService aiService;
    private final ModuleRepository moduleRepository;
    private final AiConfigurationProperties aiConfig;

    /**
     * Get AI-powered module recommendations
     */
    @GetMapping("/recommendations/{organizationId}")
    public ResponseEntity<List<ModuleRecommendation>> getRecommendations(
            @PathVariable String organizationId,
            @RequestParam(required = false) String workspaceId) {
        
        if (!aiConfig.isEnabled() || !aiConfig.isRecommendationsEnabled()) {
            log.warn("AI recommendations are disabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        
        log.info("Getting AI recommendations for organization: {}", organizationId);
        
        try {
            List<ModuleRecommendation> recommendations = 
                    aiService.getModuleRecommendations(organizationId, workspaceId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting AI recommendations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Check for module updates using AI
     */
    @GetMapping("/modules/{moduleId}/check-update")
    public ResponseEntity<ModuleUpdateRecommendation> checkModuleUpdate(
            @PathVariable String moduleId) {
        
        if (!aiConfig.isEnabled()) {
            log.warn("AI features are disabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        
        log.info("Checking updates for module: {}", moduleId);
        
        try {
            var moduleOpt = moduleRepository.findById(UUID.fromString(moduleId));
            if (moduleOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Module module = moduleOpt.get();
            ModuleUpdateRecommendation recommendation = aiService.checkModuleUpdate(module);
            
            if (recommendation == null) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(recommendation);
        } catch (Exception e) {
            log.error("Error checking module update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Auto-update a module to the recommended version
     */
    @PostMapping("/modules/{moduleId}/auto-update")
    public ResponseEntity<String> autoUpdateModule(@PathVariable String moduleId) {
        
        if (!aiConfig.isEnabled() || !aiConfig.isAutoUpdateEnabled()) {
            log.warn("AI auto-update is disabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Auto-update feature is disabled");
        }
        
        log.info("Auto-updating module: {}", moduleId);
        
        try {
            boolean success = aiService.autoUpdateModule(moduleId);
            
            if (success) {
                return ResponseEntity.ok("Module updated successfully");
            } else {
                return ResponseEntity.badRequest()
                        .body("Module does not meet auto-update criteria");
            }
        } catch (Exception e) {
            log.error("Error auto-updating module", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating module: " + e.getMessage());
        }
    }

    /**
     * Get optimization suggestions for an organization
     */
    @GetMapping("/optimizations/{organizationId}")
    public ResponseEntity<List<OptimizationSuggestion>> getOptimizations(
            @PathVariable String organizationId) {
        
        if (!aiConfig.isEnabled() || !aiConfig.isOptimizationsEnabled()) {
            log.warn("AI optimizations are disabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        
        log.info("Getting optimization suggestions for organization: {}", organizationId);
        
        try {
            List<OptimizationSuggestion> suggestions = 
                    aiService.analyzeModuleUsage(organizationId);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("Error getting optimization suggestions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get AI feature status
     */
    @GetMapping("/status")
    public ResponseEntity<AiStatusResponse> getStatus() {
        AiStatusResponse status = AiStatusResponse.builder()
                .enabled(aiConfig.isEnabled())
                .provider(aiConfig.getProvider())
                .recommendationsEnabled(aiConfig.isRecommendationsEnabled())
                .autoUpdateEnabled(aiConfig.isAutoUpdateEnabled())
                .optimizationsEnabled(aiConfig.isOptimizationsEnabled())
                .build();
        
        return ResponseEntity.ok(status);
    }
}
