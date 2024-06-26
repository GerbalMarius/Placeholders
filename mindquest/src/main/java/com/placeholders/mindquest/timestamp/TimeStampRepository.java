package com.placeholders.mindquest.timestamp;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long> {
    /**
     * Timestamp in this case is an instance of localDate time
     * @param timeStamp datetime value
     * @return timeStamp Db entry.
     */
    TimeStamp findTimeStampByTimestamp(LocalDateTime timeStamp);
    Page<TimeStamp> findByUser(User user, Pageable pageable);
    long countByUser(User user);
}
