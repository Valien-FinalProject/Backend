package com.theironyard.services;

import com.theironyard.entities.Child;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by EddyJ on 8/13/16.
 */
public interface ChildRepository extends JpaRepository<Child, Integer> {
    Child findByUsername(String username);
    Child findFirstByToken(String childToken);
}
