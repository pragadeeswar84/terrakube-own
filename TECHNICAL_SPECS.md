# Terrakube Technical Specifications

## Backend Technology Stack

### Core Framework
- **Spring Boot**: 3.5.6
- **Spring Framework**: 6.2.11 (managed by Spring Boot)
- **Java**: 17 (Eclipse Temurin)
- **Build Tool**: Apache Maven 3.9.11

### Architecture
Terrakube backend consists of three main Spring Boot modules:

#### 1. API Module (`api`)
- **Main Class**: `io.terrakube.api.ServerApplication`
- **Purpose**: Core API server with REST and GraphQL endpoints
- **Key Features**:
  - Spring Data JPA with Hibernate 6.6.29.Final
  - Spring Security with OAuth2 Resource Server
  - Elide Framework for JSON:API and GraphQL (v7.1.15)
  - Spring Boot Actuator for monitoring
  - Liquibase for database migrations
  - Quartz Scheduler for job scheduling
  - Spring Cache with Caffeine
  - Redis integration for distributed caching

#### 2. Registry Module (`registry`)
- **Main Class**: `io.terrakube.registry.OpenRegistryApplication`
- **Purpose**: Terraform/OpenTofu module and provider registry
- **Key Features**:
  - Spring Boot Web
  - OAuth2 Resource Server
  - Spring Boot Actuator
  - Integration with cloud storage providers

#### 3. Executor Module (`executor`)
- **Main Class**: `io.terrakube.executor.ExecutorApplication`
- **Purpose**: Terraform/OpenTofu job execution engine
- **Key Features**:
  - Spring Boot Web with WebFlux
  - Redis for job queue management
  - Groovy scripting support (v5.0.2)
  - Spring Boot Actuator

### Database Support
- **Primary Database**: PostgreSQL 42.7.8
- **ORM**: Hibernate 6.6.29.Final via Spring Data JPA
- **Migration Tool**: Liquibase (managed by Spring Boot)
- **H2 Database**: Available for development/testing

### Security
- **Framework**: Spring Security 6.x (managed by Spring Boot)
- **Authentication**: OAuth2 Resource Server with JWT
- **Identity Provider**: Dex (external integration)
- **Token Management**: JJWT 0.13.0 for PAT (Personal Access Tokens)

### Cloud Provider Integrations

#### Amazon Web Services (AWS)
- **SDK Version**: 2.35.10
- **Services**: S3, STS, RDS
- **Managed via**: Spring Cloud AWS dependencies

#### Microsoft Azure
- **SDK Version**: 6.0.0 (Spring Cloud Azure)
- **Services**: Azure Storage Blob, Azure Identity
- **Dependencies**: 
  - azure-storage-blob (managed by BOM)
  - azure-identity

#### Google Cloud Platform (GCP)
- **SDK Version**: 26.70.0 (libraries-bom)
- **Services**: Google Cloud Storage
- **Dependencies**: google-cloud-storage

### Additional Key Dependencies

#### Development & Testing
- **Lombok**: 1.18.42 (code generation)
- **JUnit**: 5.x (managed by Spring Boot)
- **Mockito**: (managed by Spring Boot)
- **WireMock**: 3.10.6 (HTTP mocking)
- **Rest Assured**: 5.5.6 (API testing)
- **H2 Database**: (managed by Spring Boot) for testing

#### Version Control Integration
- **JGit**: 7.4.0.202509020913-r
- **BouncyCastle**: 1.82 (cryptography support)

#### Utilities
- **Apache Commons Text**: 1.14.0
- **Apache Commons IO**: 2.20.0
- **Apache Commons Lang3**: 3.19.0
- **Apache Commons Compress**: 1.28.0
- **SnakeYAML**: 2.5
- **Jackson**: 2.19.2 (managed by Spring Boot)

#### Container & Kubernetes
- **Kubernetes Client**: 7.4.0 (Fabric8)
- **Container Runtime**: Docker with Cloud Native Buildpacks
- **Base Image**: Paketo Buildpacks (Jammy Base 0.4.442)

### Build Configuration

#### Maven Plugins
- **Spring Boot Maven Plugin**: 3.5.6
  - Configured for building container images via Buildpacks
  - Java 17 runtime
  - OpenTelemetry enabled
- **JaCoCo**: 0.8.14 (code coverage)
- **SonarQube**: 5.2.0.4988 (code quality)

#### Multi-Module Structure
```
terrakube-platform (parent)
├── api (API Server)
├── registry (Terraform Registry)
├── executor (Job Executor)
└── coverage (Aggregated Coverage Reports)
```

### Runtime Requirements
- **Java Runtime**: OpenJDK 17 or higher
- **Database**: PostgreSQL 12+ (recommended)
- **Cache**: Redis 6+ (optional but recommended for production)
- **Container Runtime**: Docker or Kubernetes

### Production Code Statistics
- **Total Java Production Code**: ~22,672 lines
- **API Module**: Largest module with complex business logic
- **Test Coverage**: Comprehensive test suite with JUnit 5 and integration tests

### Configuration
- **Profile Support**: Multiple Spring profiles (test, demo, production)
- **Configuration Format**: application.properties
- **External Configuration**: Environment variables and ConfigMaps (Kubernetes)

### Observability
- **Actuator Endpoints**: Enabled on all modules
- **Metrics**: Spring Boot Actuator metrics
- **Telemetry**: OpenTelemetry integration via Buildpacks
- **Logging**: SLF4J with Logback (Spring Boot default)

### API Specifications
- **JSON:API**: Via Elide framework at `/api/v1`
- **GraphQL**: Via Elide framework at `/graphql/api/v1`
- **OpenAPI 3.0**: API documentation at `/doc`
- **Terraform Protocols**: 
  - Remote Backend Protocol
  - Module Registry Protocol
  - Provider Registry Protocol
  - Login Protocol

### Compatibility
- **Terraform**: All versions with remote backend support
- **OpenTofu**: Full support
- **Terraform BSL License**: Confirmed compatible
- **Kubernetes**: 1.20+ recommended
- **Container Orchestration**: Docker Compose, Kubernetes, Helm

### Development Environment
- **IDE Support**: VS Code DevContainers, Gitpod
- **Hot Reload**: Spring Boot DevTools (available but not included by default)
- **API Testing**: Thunder Client collections included

## Upgrade Path from Previous Versions

The project is currently on Spring Boot 3.5.6, which requires:
- **Minimum Java Version**: 17
- **Migration from Spring Boot 2.x**: Would require Jakarta EE namespace changes
- **Current Benefits**:
  - Native compilation support (GraalVM)
  - Virtual threads support (Java 21+)
  - Improved startup time and memory footprint
  - Enhanced security features
  - Latest dependency updates

## Future Considerations
- **Java 21 LTS**: Consider upgrading from Java 17 to Java 21 for:
  - Virtual threads support
  - Pattern matching enhancements
  - Record patterns
  - Performance improvements
- **Spring Boot 3.6.x**: Monitor for next minor version updates
- **GraalVM Native Image**: Evaluate for faster startup times

---
*Last Updated: October 21, 2025*
*Spring Boot Version: 3.5.6*
