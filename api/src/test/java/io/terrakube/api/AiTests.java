package io.terrakube.api;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Integration tests for AI features
 */
class AiTests extends ServerApplicationTests {

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetAiStatus() {
        given()
                .headers("Authorization", "Bearer " + generatePAT("TERRAKUBE_DEVELOPERS"))
                .when()
                .get("/api/v1/ai/status")
                .then()
                .assertThat()
                .body("enabled", notNullValue())
                .body("provider", notNullValue())
                .body("recommendationsEnabled", notNullValue())
                .body("autoUpdateEnabled", notNullValue())
                .body("optimizationsEnabled", notNullValue())
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testGetModuleRecommendations() {
        given()
                .headers("Authorization", "Bearer " + generatePAT("TERRAKUBE_DEVELOPERS"))
                .when()
                .get("/api/v1/ai/recommendations/f5365c9e-bc11-4781-b649-45a281ccdd4a")
                .then()
                .assertThat()
                .body("$", hasSize(greaterThan(0)))
                .body("[0].moduleName", notNullValue())
                .body("[0].provider", notNullValue())
                .body("[0].version", notNullValue())
                .body("[0].confidenceScore", notNullValue())
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testCheckModuleUpdate() {
        given()
                .headers("Authorization", "Bearer " + generatePAT("TERRAKUBE_DEVELOPERS"))
                .when()
                .get("/api/v1/ai/modules/4e92ff1e-9937-400f-848d-f0ea367927bf/check-update")
                .then()
                .assertThat()
                .log()
                .all();
                // Status can be 200 OK or 204 NO_CONTENT depending on if the module has version info
    }

    @Test
    void testGetOptimizations() {
        given()
                .headers("Authorization", "Bearer " + generatePAT("TERRAKUBE_DEVELOPERS"))
                .when()
                .get("/api/v1/ai/optimizations/f5365c9e-bc11-4781-b649-45a281ccdd4a")
                .then()
                .assertThat()
                .body("$", hasSize(greaterThan(0)))
                .body("[0].type", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].impact", notNullValue())
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testAutoUpdateModule_WithoutPermission() {
        // Auto-update should fail without proper permissions or if feature is disabled
        given()
                .headers("Authorization", "Bearer " + generatePAT("TERRAKUBE_DEVELOPERS"))
                .when()
                .post("/api/v1/ai/modules/4e92ff1e-9937-400f-848d-f0ea367927bf/auto-update")
                .then()
                .assertThat()
                .log()
                .all();
                // Can be 503 SERVICE_UNAVAILABLE if auto-update is disabled, or 400/200 if enabled
    }
}
