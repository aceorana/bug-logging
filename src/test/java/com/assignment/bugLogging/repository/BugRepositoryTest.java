package com.assignment.bugLogging.repository;

import java.util.List;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import com.assignment.bugLogging.domain.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BugRepositoryTest {

    @Autowired
    private BugRepository bugRepository;

    @AfterEach
    public void cleanUp() {
        bugRepository.deleteAll();
    }

    @Test
    void shouldSaveBug() {
        Bug bug = new Bug();
        bug.setTitle("Cannot log in");
        bug.setDescription("User gets 500 error when logging in");
        bug.setSeverity(Severity.HIGH);
        bug.setStatus(Status.OPEN);

        Bug saved = bugRepository.save(bug);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedDate()).isNotNull();
    }

    @Test
    void shouldFindAllBugs() {
        Bug bug1 = new Bug("Cannot log in", "500 on login", Status.OPEN, Severity.HIGH);
        Bug bug2 = new Bug("Typo on homepage", "Misspelled word", Status.OPEN, Severity.LOW);
        bugRepository.save(bug1);
        bugRepository.save(bug2);

        List<Bug> bugs = bugRepository.findAll();

        assertThat(bugs).hasSize(2);
    }

    @Test
    void shouldFilterBySeverity() {
        Bug highBug = new Bug("Cannot log in", "Internal Server Error", Status.OPEN, Severity.HIGH);
        Bug lowBug = new Bug("Page not found", "Incorrect URL", Status.OPEN, Severity.LOW);
        Bug medBug = new Bug("User access blocked", "Fraud account", Status.OPEN, Severity.MEDIUM);
        Bug lowBug2 = new Bug("User not found", "Mispelled username", Status.OPEN, Severity.LOW);

        bugRepository.save(highBug);
        bugRepository.save(lowBug);
        bugRepository.save(medBug);
        bugRepository.save(lowBug2);

        List<Bug> lowBugs = bugRepository.findBySeverity(Severity.LOW);

        assertThat(lowBugs).hasSize(2)
                .extracting(Bug::getSeverity)
                .containsOnly(Severity.LOW);

    }
}
