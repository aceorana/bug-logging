package com.assignment.bugLogging.controller;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import com.assignment.bugLogging.service.BugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping
    public ResponseEntity<Bug> createBug(@RequestBody Bug bug) {
        Bug saved = bugService.createBug(bug);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Bug>> getBugs(@RequestParam(value = "severity", required = false) String severity) {

        if (severity == null || severity.isBlank()) {
            List<Bug> bugs = bugService.getAllBugs();
            return ResponseEntity.ok(bugs);
        }

        Severity sevEnum = Severity.valueOf(severity.toUpperCase());
        List<Bug> bugs = bugService.getAllBugsBySeverity(sevEnum);
        return ResponseEntity.ok(bugs);
    }
}
