package io.terrakube.api.plugin.ai;

import io.terrakube.api.repository.ModuleRepository;
import io.terrakube.api.repository.ModuleVersionRepository;
import io.terrakube.api.rs.Organization;
import io.terrakube.api.rs.module.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for AI Service Implementation
 */
@ExtendWith(MockitoExtension.class)
class AiServiceImplTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModuleVersionRepository moduleVersionRepository;

    @Mock
    private AiConfigurationProperties aiConfig;

    private AiServiceImpl aiService;

    @BeforeEach
    void setUp() {
        lenient().when(aiConfig.isEnabled()).thenReturn(true);
        lenient().when(aiConfig.isRecommendationsEnabled()).thenReturn(true);
        lenient().when(aiConfig.isAutoUpdateEnabled()).thenReturn(true);
        lenient().when(aiConfig.isOptimizationsEnabled()).thenReturn(true);
        lenient().when(aiConfig.getAutoUpdateMinScore()).thenReturn(0.8);
        
        aiService = new AiServiceImpl(moduleRepository, moduleVersionRepository, aiConfig);
    }

    @Test
    void testGetModuleRecommendations() {
        // Given
        String organizationId = "test-org-id";
        String workspaceId = "test-workspace-id";

        // When
        List<ModuleRecommendation> recommendations = 
                aiService.getModuleRecommendations(organizationId, workspaceId);

        // Then
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        assertTrue(recommendations.size() > 0);
        
        ModuleRecommendation firstRecommendation = recommendations.get(0);
        assertNotNull(firstRecommendation.getModuleName());
        assertNotNull(firstRecommendation.getProvider());
        assertNotNull(firstRecommendation.getVersion());
        assertNotNull(firstRecommendation.getDescription());
        assertTrue(firstRecommendation.getConfidenceScore() > 0.0);
        assertTrue(firstRecommendation.getConfidenceScore() <= 1.0);
    }

    @Test
    void testCheckModuleUpdate() {
        // Given
        Module module = new Module();
        module.setId(UUID.randomUUID());
        module.setName("test-module");
        module.setProvider("aws");
        module.setLatestVersion("1.0.0");
        
        Organization org = new Organization();
        org.setName("test-org");
        module.setOrganization(org);

        // When
        ModuleUpdateRecommendation recommendation = aiService.checkModuleUpdate(module);

        // Then
        assertNotNull(recommendation);
        assertEquals("1.0.0", recommendation.getCurrentVersion());
        assertNotNull(recommendation.getRecommendedVersion());
        assertNotNull(recommendation.getUpdateType());
        assertFalse(recommendation.isBreakingChanges());
        assertTrue(recommendation.getCompatibilityScore() > 0.0);
    }

    @Test
    void testCheckModuleUpdateWithNoVersion() {
        // Given
        Module module = new Module();
        module.setId(UUID.randomUUID());
        module.setName("test-module");
        module.setLatestVersion(null);

        // When
        ModuleUpdateRecommendation recommendation = aiService.checkModuleUpdate(module);

        // Then
        assertNull(recommendation);
    }

    @Test
    void testAutoUpdateModule_Success() {
        // Given
        UUID moduleId = UUID.randomUUID();
        Module module = new Module();
        module.setId(moduleId);
        module.setName("test-module");
        module.setLatestVersion("1.0.0");
        
        Organization org = new Organization();
        org.setName("test-org");
        module.setOrganization(org);

        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        // When
        boolean result = aiService.autoUpdateModule(moduleId.toString());

        // Then
        assertTrue(result);
        verify(moduleRepository).save(any(Module.class));
    }

    @Test
    void testAutoUpdateModule_ModuleNotFound() {
        // Given
        UUID moduleId = UUID.randomUUID();
        when(moduleRepository.findById(moduleId)).thenReturn(Optional.empty());

        // When
        boolean result = aiService.autoUpdateModule(moduleId.toString());

        // Then
        assertFalse(result);
        verify(moduleRepository, never()).save(any(Module.class));
    }

    @Test
    void testAnalyzeModuleUsage() {
        // Given
        String organizationId = "test-org-id";

        // When
        List<OptimizationSuggestion> suggestions = aiService.analyzeModuleUsage(organizationId);

        // Then
        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
        
        OptimizationSuggestion firstSuggestion = suggestions.get(0);
        assertNotNull(firstSuggestion.getType());
        assertNotNull(firstSuggestion.getTitle());
        assertNotNull(firstSuggestion.getDescription());
        assertNotNull(firstSuggestion.getImpact());
        assertNotNull(firstSuggestion.getRecommendation());
    }

    @Test
    void testOptimizationSuggestionTypes() {
        // Given
        String organizationId = "test-org-id";

        // When
        List<OptimizationSuggestion> suggestions = aiService.analyzeModuleUsage(organizationId);

        // Then
        boolean hasCostSuggestion = suggestions.stream()
                .anyMatch(s -> "COST".equals(s.getType()));
        boolean hasSecuritySuggestion = suggestions.stream()
                .anyMatch(s -> "SECURITY".equals(s.getType()));
        boolean hasPerformanceSuggestion = suggestions.stream()
                .anyMatch(s -> "PERFORMANCE".equals(s.getType()));
        
        assertTrue(hasCostSuggestion, "Should have cost optimization suggestion");
        assertTrue(hasSecuritySuggestion, "Should have security optimization suggestion");
        assertTrue(hasPerformanceSuggestion, "Should have performance optimization suggestion");
    }
}
