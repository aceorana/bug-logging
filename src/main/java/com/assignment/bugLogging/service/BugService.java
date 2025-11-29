package com.assignment.bugLogging.service;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;

import java.util.List;

public interface BugService {

    Bug createBug(Bug bug);

    List<Bug> getAllBugs();

    List<Bug> getAllBugsBySeverity(Severity severity);
}
