package com.assignment.bugLogging.service;

import com.assignment.bugLogging.entity.Bug;
import com.assignment.bugLogging.entity.Severity;

import java.util.List;

public interface BugService {

    Bug createBug(Bug bug);

    List<Bug> getAllBugs();

    List<Bug> getAllBugsBySeverity(Severity severity);
}
