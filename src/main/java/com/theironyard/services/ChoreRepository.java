package com.theironyard.services;

import com.theironyard.entities.Chore;
import com.theironyard.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Created by Nigel on 8/15/16.
 */
public interface ChoreRepository extends JpaRepository<Chore, Integer>{
    List<Chore> findAllByCreatorAndPendingTrue(Parent creator);
}
