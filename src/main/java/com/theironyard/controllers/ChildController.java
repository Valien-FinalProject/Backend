package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nigel on 8/13/16.
 */
@RestController
@CrossOrigin
public class ChildController {

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

    @RequestMapping(path = "/children",method = RequestMethod.GET)
    public List<Child> getAllChildren(@RequestHeader(value = "Authorization") String parentToken){
        getParentFromAuth(parentToken);
        return childRepository.findAll();
    }

    @RequestMapping(path = "/child/{id}",method = RequestMethod.GET)
    public Child getOneChild(@RequestHeader(value = "Authorization") String parentToken, @PathVariable int id){
        getParentFromAuth(parentToken);
        return childRepository.findOne(id);
    }

    @RequestMapping(path = "/login/child",method = RequestMethod.POST)
    public Child childLogin(@RequestBody ChildCommand childCommand) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        Child child = childRepository.findByUsername(childCommand.getUsername());
        if(child == null){
            throw new UserNotFoundException();
        }
        if(!PasswordStorage.verifyPassword(childCommand.getPassword(), child.getPassword())){
            throw new LoginFailedException();
        }
        return child;
    }

    @RequestMapping(path = "/child",method = RequestMethod.POST)
    public Child createChild(@RequestHeader(value = "Authorization") String parentToken, @RequestBody ChildCommand childCommand) throws PasswordStorage.CannotPerformOperationException {
        Parent parent = getParentFromAuth(parentToken);
        return new Child(childCommand.getName(), childCommand.getUsername(), PasswordStorage.createHash(childCommand.getPassword()), childCommand.getAge(), parent);
    }

    public Parent getParentFromAuth(String parentToken){
        Parent parent = parentRepository.findFirstByToken(parentToken.split(" ")[1]);
        if(!parent.isTokenValid()){
            throw new TokenExpiredException();
        }
        return parent;
    }
}
