package com.placeholders.mindquest.journals;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {
    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public List<Journal> findAll() {
        return journalRepository.findAll();
    }

    public List<Journal> getJournalPagedLatest(int pageNumber, int pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Journal> page = journalRepository.findByUser(user, pageable);
        return page.getContent();
    }

    public long getTotalPageCount(int pageSize, User user) {
        long totalCount = journalRepository.countByUser(user);
        return (totalCount + pageSize - 1) / pageSize;
    }
}
