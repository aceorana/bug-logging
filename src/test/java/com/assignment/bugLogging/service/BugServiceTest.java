package com.assignment.bugLogging.service;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import com.assignment.bugLogging.repository.BugRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @InjectMocks
    private BugServiceImpl bugService;

    private Bug sampleBug;

    @BeforeEach
    void setUp() {
        sampleBug = new Bug();
        sampleBug.setTitle("Sample bug "+sampleBug.getId());
        sampleBug.setDescription("Something is broken");
        sampleBug.setSeverity(Severity.HIGH);
    }

    @Test
    void createBug_shouldSaveViaRepository() {
        //given
        when(bugRepository.save(sampleBug)).thenReturn(sampleBug);

        //when
        Bug createdBug = bugService.createBug(sampleBug);

        //then
        verify(bugRepository, times(1)).save(sampleBug);
        assertThat(createdBug).isNotNull();
        assertThat(createdBug.getTitle()).isEqualTo("Sample bug "+sampleBug.getId());
    }

    @Test
    void getAllBugs_shouldReturnAllViaRepository() {

        // given
        Bug bug2 = new Bug();
        bug2.setTitle("Another bug "+bug2.getId());
        bug2.setSeverity(Severity.LOW);

        List<Bug> bugs = Arrays.asList(sampleBug, bug2);
        when(bugRepository.findAll()).thenReturn(bugs);

        // when
        List<Bug> bugList = bugService.getAllBugs();

        // then
        verify(bugRepository, times(1)).findAll();
        assertThat(bugList).hasSize(2);
    }

    @Test
    void getBugsBySeverity_shouldReturnBugsBySeverityViaRepository() {
        //given
        Bug bug3 = new Bug();
        bug3.setTitle("Another bug "+bug3.getId());
        bug3.setSeverity(Severity.HIGH);

        List<Bug> bugs = Arrays.asList(sampleBug, bug3);
        when(bugRepository.findBySeverity(Severity.HIGH)).thenReturn(bugs);

        //when
        List<Bug> highBugs = bugService.getAllBugsBySeverity(Severity.HIGH);

        //then
        verify(bugRepository, times(1)).findBySeverity(Severity.HIGH);
        assertThat(highBugs).hasSize(2);
    }
}
