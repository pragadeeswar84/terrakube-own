# AI-Powered Features in Terrakube

## Overview

Terrakube now includes advanced AI-powered features to help you manage your Terraform modules more efficiently. These features leverage artificial intelligence to provide intelligent recommendations, automated updates, and optimization suggestions.

## Features

### 1. Module Recommendations

Get AI-powered module recommendations based on your workspace configuration and infrastructure patterns.

**API Endpoint:**
```
GET /api/v1/ai/recommendations/{organizationId}?workspaceId={workspaceId}
```

**Example Response:**
```json
[
  {
    "moduleName": "vpc",
    "provider": "aws",
    "version": "5.0.0",
    "description": "AWS VPC module for network infrastructure",
    "confidenceScore": 0.95,
    "reasoning": "High usage in similar workspaces with AWS provider",
    "category": "NETWORKING"
  }
]
```

### 2. Automated Module Updates

Check for available updates and automatically update modules to compatible versions.

**Check for Updates:**
```
GET /api/v1/ai/modules/{moduleId}/check-update
```

**Auto-Update Module:**
```
POST /api/v1/ai/modules/{moduleId}/auto-update
```

**Example Response:**
```json
{
  "currentVersion": "1.0.0",
  "recommendedVersion": "1.1.0",
  "updateType": "MINOR",
  "breakingChanges": false,
  "changeDescription": "Performance improvements and bug fixes",
  "compatibilityScore": 0.92,
  "migrationNotes": "No breaking changes detected. Safe to upgrade."
}
```

### 3. Optimization Suggestions

Get AI-generated suggestions to optimize your infrastructure for cost, performance, security, and compliance.

**API Endpoint:**
```
GET /api/v1/ai/optimizations/{organizationId}
```

**Example Response:**
```json
[
  {
    "type": "COST",
    "title": "Optimize EC2 instance types",
    "description": "Current module configuration uses oversized instances",
    "impact": "HIGH",
    "recommendation": "Consider using t3.medium instead of t3.xlarge for non-production workloads",
    "estimatedSavings": 2400.00
  }
]
```

## Configuration

Enable and configure AI features in your `application.properties` or via environment variables:

```properties
# Enable/disable AI features
terrakube.ai.enabled=true

# AI provider (options: local, openai, anthropic)
terrakube.ai.provider=local

# API key for external AI providers (optional)
terrakube.ai.api-key=your-api-key-here

# AI model to use
terrakube.ai.model=gpt-4

# Enable auto-update feature
terrakube.ai.auto-update-enabled=false

# Minimum compatibility score for auto-updates (0.0 - 1.0)
terrakube.ai.auto-update-min-score=0.8

# Enable module recommendations
terrakube.ai.recommendations-enabled=true

# Enable optimization suggestions
terrakube.ai.optimizations-enabled=true
```

### Environment Variables

You can also configure AI features using environment variables:

- `AiEnabled` - Enable/disable AI features (default: `true`)
- `AiProvider` - AI provider to use (default: `local`)
- `AiApiKey` - API key for external AI providers
- `AiModel` - AI model to use (default: `gpt-4`)
- `AiAutoUpdateEnabled` - Enable auto-update feature (default: `false`)
- `AiAutoUpdateMinScore` - Minimum score for auto-updates (default: `0.8`)
- `AiRecommendationsEnabled` - Enable recommendations (default: `true`)
- `AiOptimizationsEnabled` - Enable optimizations (default: `true`)

## API Reference

### Get AI Status

Check the current status and configuration of AI features.

```
GET /api/v1/ai/status
```

**Response:**
```json
{
  "enabled": true,
  "provider": "local",
  "recommendationsEnabled": true,
  "autoUpdateEnabled": false,
  "optimizationsEnabled": true
}
```

## Security Considerations

1. **API Keys**: Store AI provider API keys securely using environment variables or secret management tools.
2. **Auto-Update**: The auto-update feature is disabled by default for safety. Enable it only after thorough testing.
3. **Compatibility Score**: Only modules with a compatibility score above the configured threshold will be auto-updated.
4. **Breaking Changes**: Modules with detected breaking changes will not be auto-updated.

## Best Practices

1. **Start with Recommendations**: Begin by using the recommendation feature to explore suggested modules.
2. **Review Before Auto-Update**: Review update recommendations manually before enabling auto-update.
3. **Monitor Optimizations**: Regularly check optimization suggestions to improve your infrastructure.
4. **Test in Non-Production**: Test AI-recommended changes in non-production environments first.
5. **Adjust Thresholds**: Fine-tune compatibility score thresholds based on your risk tolerance.

## Roadmap

Future enhancements planned for AI features:

- Integration with OpenAI, Anthropic Claude, and other AI providers
- Natural language queries for infrastructure management
- Predictive cost analysis and forecasting
- Automated compliance checking and remediation
- Smart dependency resolution and conflict detection
- Infrastructure drift detection and correction suggestions

## Support

For questions or issues related to AI features, please:

1. Check the [documentation](https://docs.terrakube.io/)
2. Join our [Slack community](https://join.slack.com/t/terrakubeworkspace/shared_invite/zt-2cx6yn95t-2CTBGvsQhBQJ5bfbG4peFg)
3. Open an issue on [GitHub](https://github.com/terrakube-io/terrakube/issues)
