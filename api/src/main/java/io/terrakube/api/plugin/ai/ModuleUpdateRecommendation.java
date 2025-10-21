package io.terrakube.api.plugin.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a module update recommendation from AI analysis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleUpdateRecommendation {
    private String currentVersion;
    private String recommendedVersion;
    private String updateType; // MAJOR, MINOR, PATCH
    private boolean breakingChanges;
    private String changeDescription;
    private double compatibilityScore;
    private String migrationNotes;
}
