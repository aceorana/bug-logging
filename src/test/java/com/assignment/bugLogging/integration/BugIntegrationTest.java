package com.assignment.bugLogging.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc
class BugIntegrationTest {

    // Start a MySQL 8 container just for the tests
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");

    /**
     * Override Spring datasource properties so tests use the container
     * instead of your normal MySQL instance.
     */
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        // Make sure schema gets created/updated
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndListBugs() throws Exception {
        // IMPORTANT: use field names that match your Bug entity
        String json = """
            {
              "title": "Bug1",
              "description": "D",
              "severity": "LOW",
              "status": "OPEN"
            }
            """;

        // 1) Create a bug via real controller/service/repository/DB
        mockMvc.perform(post("/api/bugs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // 2) List bugs and verify the one we just created exists
        mockMvc.perform(get("/api/bugs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Bug1"))
                .andExpect(jsonPath("$[0].severity").value("LOW"));
    }

    @Test
    void createBug_missingTitle_shouldReturn400() throws Exception {
        String json = """
            {
              "title": "",
              "description": "Some desc",
              "severity": "LOW",
              "status": "OPEN"
            }
            """;

        mockMvc.perform(post("/api/bugs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
