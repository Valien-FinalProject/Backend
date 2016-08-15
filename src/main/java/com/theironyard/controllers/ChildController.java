package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.Auth;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Get a list of all children
     * @param parentToken parent's token to auth
     * @return a list of children
     */
    @RequestMapping(path = "/children",method = RequestMethod.GET)
    public List<Child> getAllChildren(@RequestHeader(value = "Authorization") String parentToken){
        Auth auth = new Auth();
        auth.getParentFromAuth(parentToken);
        return childRepository.findAll();
    }

    /**
     * Get info on one child
     * @param parentToken parent's token to auth
     * @param id child's id is passed to find certain child
     * @return a child's info
     */
    @RequestMapping(path = "/child/{id}",method = RequestMethod.GET)
    public Child getOneChild(@RequestHeader(value = "Authorization") String parentToken, @PathVariable int id){
        Auth auth = new Auth();
        auth.getParentFromAuth(parentToken);
        return childRepository.findOne(id);
    }

    /**
     * When a child is logging into the system
     * @param childCommand holds the child's info to be checked for login
     * @return a child's account that logs in
     * @throws PasswordStorage.InvalidHashException
     * @throws PasswordStorage.CannotPerformOperationException
     */
    @RequestMapping(path = "/child/login",method = RequestMethod.POST)
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

    /**
     * Gets the token of the child that is logged in
     * @param childCommand holds the child's info
     * @return the token for the child's account that is logged in
     * @throws Exception
     */
    @RequestMapping(path = "/child/token",method = RequestMethod.GET)
    public Map getChildToken(@RequestBody ChildCommand childCommand) throws Exception{
        Auth auth = new Auth();
        Child child = auth.checkChildLogin(childCommand);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", child.getToken());
        return tokenMap;
    }

}
