package com.placeholders.mindquest.timestamp;

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
}