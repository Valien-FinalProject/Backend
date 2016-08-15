package com.theironyard.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.ChoreCommand;
import com.theironyard.command.ParentCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Chore;
import com.theironyard.entities.Parent;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.services.Auth;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nigel on 8/13/16.
 */
@RestController
@CrossOrigin
public class ParentController {

    @Autowired
    ParentRepository parents;

    @Autowired
    ChildRepository children;


    /*==================================================
    ***************** 'CREATE' ENDPOINTS ***************
    ===================================================*/


    /**
     * Parent can register their child.
     * @param command holds info for the child object.
     * @param id the parent's id is passed.
     * @param auth auth token of the parent
     * @return
     */
    @RequestMapping(path = "/parent/{id}/child", method = RequestMethod.POST)
    public Child createChild(@RequestBody ChildCommand command, @PathVariable int id, @RequestHeader(value = "Authorization") String auth){

        Parent parent = parents.findOne(id);

        Child child = new Child(command.getName(), command.getUsername(), command.getPassword(), command.getAge(), parent);

        parent.addChild(child);
        children.save(child);

        return child;
    }

    /**
     * Allows the parent to login
     * @param command - get info for parent object.
     * @return return the parent that matches the credentials provided.
     * @throws PasswordStorage.InvalidHashException
     * @throws PasswordStorage.CannotPerformOperationException
     */
    @RequestMapping(path = "/parent/login", method = RequestMethod.POST)
    public Parent parentLogin(@RequestBody ParentCommand command) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        Parent parent = parents.findFirstByUsername(command.getUsername());

        if (parent == null){
            //registerParent();
        } else if (!PasswordStorage.verifyPassword(command.getPassword(), parent.getPassword())){
            throw new LoginFailedException();
        }

        return parent;
    }

    /**
     * Allows a parent to assign a chore to a child.
     * @param id - the child's id.
     * @param command - hold the info for the chore command.
     * @param auth - the parent's token.
     * @return
     */
    @RequestMapping(path = "/parent/child/{id}/chore/", method = RequestMethod.POST)
    public Chore assignChore(@PathVariable int id, @RequestBody ChoreCommand command, @RequestHeader(value = "Authorization") String auth){
        Auth getAuth = new Auth();

        Child child = children.findOne(id);
        Parent parent = getAuth.getParentFromAuth(auth);

        Chore chore = new Chore(parent, command.getDescription(), command.getValue());

        child.addChore(chore);
        children.save(child);

        return chore;
    }


    /*==================================================
    ***************** 'READ' ENDPOINTS ***************
    ===================================================*/


    /**
     * Gets a parent's info.
     * @param id - the parent's id is passed.
     * @return returns a parent.
     */
    @RequestMapping(path = "/parent/{id}", method = RequestMethod.GET)
    public Parent getParent(@PathVariable int id){

        Parent parent = parents.findOne(id);

        return parent;
    }

    /**
     * Returns all children that belong to the parent.
     * @param id - the parent's id is passed.
     * @param auth - verifies the parent's token
     * @return returns the collection of children.
     */
    @RequestMapping(path = "/parent/{id}/children", method = RequestMethod.GET)
    public Collection<Child> getChildren(@PathVariable int id, @RequestHeader(value = "Authorization") String auth){

        Auth getAuth = new Auth();

        Parent parent = getAuth.getParentFromAuth(auth);

        return parent.getChildCollection() ;
    }

    /**
     * Gets the token of the Parent that is currently logged in.
     * @param command
     * @return token of the current parent
     * @throws Exception
     */
    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getToken(@RequestBody ParentCommand command) throws Exception {
        Auth getAuth = new Auth();
        Parent parent = getAuth.checkParentLogin(command);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", parent.getToken());

        return tokenMap;
    }
}