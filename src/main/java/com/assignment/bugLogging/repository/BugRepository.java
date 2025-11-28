package com.assignment.bugLogging.repository;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findBySeverity(Severity severity);
}
