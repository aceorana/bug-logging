package com.assignment.bugLogging.repository;

import com.assignment.bugLogging.entity.Bug;
import com.assignment.bugLogging.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findBySeverity(Severity severity);
}
