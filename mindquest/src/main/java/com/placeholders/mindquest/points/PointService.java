package com.placeholders.mindquest.points;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public List<Point> findAllByUser(User user) {
        return pointRepository.findPointsByUser(user);
    }

    public void savePoint(Point point) {
        pointRepository.save(point);
    }
}
