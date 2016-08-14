package com.theironyard.services;

import com.theironyard.entities.Point;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by EddyJ on 8/13/16.
 */
public interface PointRepository extends JpaRepository<Point, Integer> {
}
