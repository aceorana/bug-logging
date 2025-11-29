package com.assignment.bugLogging.controller;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import com.assignment.bugLogging.service.BugService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Starts a light web application context with BugController class only
@WebMvcTest(BugController.class)
public class BugControllerTest {

    @Autowired
    private MockMvc mockMvc; //simulates HTTP requests to the controller

    @MockBean //creates mock for Spring context
    private BugService bugService;

    @Autowired
    private ObjectMapper objectMapper; //converts objects to JSON strings and vice

    @Test
    void createBug_shouldReturn201AndSavedBug() throws Exception {
        Bug inputBug = new Bug();
        inputBug.setTitle("Sample bug");
        inputBug.setDescription("Something is broken");
        inputBug.setSeverity(Severity.HIGH);

        Bug savedBug = new Bug();
        savedBug.setTitle("Sample bug");
        savedBug.setDescription("Something is broken");
        savedBug.setSeverity(Severity.HIGH);

        given(bugService.createBug(any(Bug.class))).willReturn(savedBug);

        mockMvc.perform(post("/api/bugs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputBug)))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample bug"))
                .andExpect(jsonPath("$.severity").value("HIGH"));
    }

    @Test
    void getAllBugs_shouldReturnList() throws Exception {
        Bug bug1 = new Bug();
        bug1.setTitle("Bug 1");
        bug1.setSeverity(Severity.LOW);

        Bug bug2 = new Bug();
        bug2.setTitle("Bug 2");
        bug2.setSeverity(Severity.HIGH);

        given(bugService.getAllBugs()).willReturn(List.of(bug1, bug2));

        mockMvc.perform(get("/api/bugs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Bug 1")))
                .andExpect(jsonPath("$[1].title", is("Bug 2")));
    }

    @Test
    void getBugsBySeverity_shouldFilterProperly() throws Exception {
        Bug bug = new Bug();
        bug.setTitle("Severe bug");
        bug.setSeverity(Severity.HIGH);

        given(bugService.getAllBugsBySeverity(Severity.HIGH))
                .willReturn(List.of(bug));

        mockMvc.perform(get("/api/bugs")
                        .param("severity", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Severe bug")))
                .andExpect(jsonPath("$[0].severity", is("HIGH")));
    }

}
