package com.theironyard.controllers;

import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nigel on 8/13/16.
 */
@RestController
@CrossOrigin
public class ChoreController {

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

}
