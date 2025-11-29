package com.assignment.bugLogging.service;

import com.assignment.bugLogging.domain.Bug;
import com.assignment.bugLogging.domain.Severity;
import com.assignment.bugLogging.repository.BugRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;

    public BugServiceImpl(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    @Override
    public Bug createBug(Bug bug) {
        //TODO add validation
        return bugRepository.save(bug);
    }

    @Override
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @Override
    public List<Bug> getAllBugsBySeverity(Severity severity) {
        return bugRepository.findBySeverity(severity);
    }
}
