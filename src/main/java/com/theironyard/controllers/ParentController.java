package com.theironyard.controllers;

import com.theironyard.entities.Point;
import com.theironyard.services.ChoreRepository;
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

    @Autowired
    ChoreRepository chores;


    /*==================================================
    ***************** 'LOGIN & LOGOUT' ENDPOINTS ***************
    ===================================================*/

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

    //PARENT CAN CREATE A CHORE WITH NO ASSIGNMENT

    //CREATE A REWARD



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
        //Find the parent by their id.
        Parent parent = parents.findOne(id);

        //Return the parent object.
        return parent;
    }

    /**
     * Returns all children that belong to the parent.
     * @param auth - verifies the parent's token
     * @return returns the collection of children.
     */
    @RequestMapping(path = "/parent/children", method = RequestMethod.GET)
    public Collection<Child> getChildren(@RequestHeader(value = "Authorization") String auth){

        //Find the parent via their token
        Auth getAuth = new Auth();
        Parent parent = getAuth.getParentFromAuth(auth);

        //Return the list of children that parent has created
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


    /*==================================================
    ***************** 'UPDATE' ENDPOINTS ***************
    ===================================================*/

    /**
     * Child has completed a chore and the parent will approve. We need to add those point to the child and remove the chore from the child's list.
     * @param childId id of the child
     * @param choreId id of the chore to be approved
     * @return a string stating that we have removed the chore and added points to the child.
     */
    @RequestMapping(path = "/parent/child/{childId}/approve/{choreId}", method = RequestMethod.POST)
    public String approveChore(@PathVariable int childId, @PathVariable int choreId, @RequestHeader(value = "Authorization") String auth){

        //Get the chore to be approved and the child that the chore belongs to
        Chore choreToApprove = chores.findOne(choreId);
        Child child = children.findOne(childId);
        Collection<Chore> childChores = child.getChoreCollection();

        //Add point value of the chore to the child's points.
        Point point = child.getChildPoint();
        int chorePoint = choreToApprove.getValue().getValue();
        point.setValue(point.getValue() + chorePoint);

        //Remove the chore from the child's chore Collection
        childChores.remove(choreToApprove);

        //Build a string stating what we have done.
        String success = child.getName() + "'s points have been updated and " + choreToApprove.getDescription() + " has been removed from their list.";

        //Remove the chore from the database
        chores.delete(choreToApprove.getId());

        return success;
    }

    @RequestMapping(path = "/parent/deduct/child/{id}")
    public String deductPoints(@PathVariable int id, @RequestHeader(value = "Authorization") String auth){

        //Find the parent via their token
        Auth getAuth = new Auth();
        Parent parent = getAuth.getParentFromAuth(auth);

        //Get the child from the parent
        Collection<Child> parentChildren = parent.getChildCollection();
        Child child = parentChildren.getClass()
    }


}