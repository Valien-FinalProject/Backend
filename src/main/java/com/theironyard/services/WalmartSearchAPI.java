package com.theironyard.services;

import com.theironyard.entities.Child;
import com.theironyard.entities.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by EddyJ on 8/19/16.
 */
@Service
public class WalmartSearchAPI {

    @Autowired
    ChildRepository childRepository;

    @Autowired
    RewardRepository rewardRepository;

    protected String searchTerm = null;

    @Value("${walmart.api.key}")
    private String walmartKey;

    private String BASE_URL = "http://api.walmartlabs.com/v1/search?format=json&apiKey=" + walmartKey + "query=";

    public void searchForItem(Reward reward){
        searchTerm = reward.getDescription();
        String searchUrl = BASE_URL + searchTerm;


    }
}
