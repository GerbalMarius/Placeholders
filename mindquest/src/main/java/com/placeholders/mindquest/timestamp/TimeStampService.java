package com.placeholders.mindquest.timestamp;

import com.placeholders.mindquest.mindfeed.Post;
import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeStampService {

    private final TimeStampRepository timeStampRepository;

    public TimeStampService(TimeStampRepository timeStampRepository) {
        this.timeStampRepository = timeStampRepository;
    }

    public List<TimeStamp> findAll() {
        return timeStampRepository.findAll();
    }

    public TimeStamp findByTimeStamp(LocalDateTime timeStamp) {
        return timeStampRepository.findTimeStampByTimestamp(timeStamp);
    }

    public TimeStamp save(TimeStamp timeStamp) {
        return timeStampRepository.save(timeStamp);
    }

    public List<TimeStamp> getPostsPagedLatest(int pageNumber, int pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<TimeStamp> page = timeStampRepository.findByUser(user, pageable);
        return page.getContent();
    }

    public long getTotalPageCount(int pageSize, User user) {
        long totalCount = timeStampRepository.countByUser(user);
        return (totalCount + pageSize - 1) / pageSize;
    }
}