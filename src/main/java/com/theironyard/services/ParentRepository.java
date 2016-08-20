package com.theironyard.services;

import com.theironyard.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nigel on 8/13/16.
 */

public interface ParentRepository extends JpaRepository<Parent, Integer> {

    Parent findFirstByToken(String parentToken);

    Parent findFirstByUsername(String username);

}
