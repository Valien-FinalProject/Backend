//package com.theironyard.controllers;
//
//import com.theironyard.entities.Reward;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
///**
// * Created by EddyJ on 8/19/16.
// */
//@RestController
//@CrossOrigin
//public class WalmartSearchAPI {
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @Value("${walmart.api.key}")
//    private String walmartKey;
//
//    protected String searchTerm = null;
//
//    private String BASE_URL = "http://api.walmartlabs.com/v1/search?format=json&apiKey=" + walmartKey + "query=";
//
//    public String searchUrl(Reward reward){
//        searchTerm = reward.getDescription();
//        return BASE_URL + searchTerm;
//    }
//
//    @RequestMapping(path = "{url}&numItems=1", method = RequestMethod.GET)
//    public String getSearchItem(@PathVariable String url){
//        Map product = restTemplate.getForObject()
//
//    }
//}
