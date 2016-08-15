package com.theironyard.services;

import com.theironyard.entities.Chore;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by EddyJ on 8/15/16.
 */
public interface ChoreRepository extends JpaRepository<Chore, Integer> {
}
