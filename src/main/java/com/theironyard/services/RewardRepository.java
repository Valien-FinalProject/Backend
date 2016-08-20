package com.theironyard.services;

import com.theironyard.entities.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nigel on 8/15/16.
 */

public interface RewardRepository extends JpaRepository<Reward, Integer>{
}
