package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Chore;
import com.theironyard.entities.Parent;
import com.theironyard.entities.Reward;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.Auth;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ChoreRepository;
import com.theironyard.services.ParentRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;
import java.util.Collection;
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

    @Autowired
    ChoreRepository choreRepository;

    /***************************
        Read/Get Endpoints
     **************************/

    /**
     * Gets info on one child's account but requires parent linked to the child's account to be authorized
     * @param parentToken parent's token to authorized for that account signed in
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
     * Endpoint for when a child is logging out of the account
     * @param childToken child's token to be authorized for that account signed in
     * @param session current session of the child's account to be invalidated
     */
    @RequestMapping(path = "/child/logout",method = RequestMethod.GET)
    public void childLogout(@RequestHeader (value = "Authorization") String childToken, HttpSession session){
        Auth auth = new Auth();
        auth.getChildFromAuth(childToken);
        session.invalidate();
    }

    /**
     * Endpoint that will return a list for a child's account
     * @param childToken child's token to be authorized for that account signed in
     * @return a child's wishlist
     */
//    @RequestMapping(path = "/child/wishlist",method = RequestMethod.GET)
//    public Collection<Reward> showWishlist(@RequestHeader (value = "Auhorization") String childToken){
//        Auth auth = new Auth();
//        Child child = auth.getChildFromAuth(childToken);
//
//        return child.getWishlist();
//    }

    /***************************
        Create/Post Endpoints
     ***************************/

    /**
     * Endpoint for when a child is logging into the system
     * @param childCommand holds the child's info to be checked for login
     * @return a child's account that has logged in
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
     * A child must be authorized to complete a chore/task and await for the parent to approve chore/task before
     * points are rewarded
     * @param childToken child's token to be authorized for that account signed in
     * @param id the id of a certain chore that the child is currently marking for completion
     * @return the chore that was completed or marked for completion is returned
     */
    @RequestMapping(path = "/child/chore/{id}",method = RequestMethod.POST)
    public Chore completeChore(@RequestHeader (value = "Authorization") String childToken, @PathVariable int id){
        Auth auth = new Auth();
        Child child =  auth.getChildFromAuth(childToken);

        Chore chore = choreRepository.findOne(id);
        chore.setCompleted(true);
        choreRepository.save(chore);
        return chore;
    }

//    @RequestMapping(path = "/child/wishlist",method = RequestMethod.POST)
//    public Collection<Reward> createWishlistItem(@RequestHeader (value = "Authorization") String childToken){
//        Auth auth = new Auth();
//        Child child = auth.getChildFromAuth(childToken);
//    }

    /***************************
        Token Endpoints
     /**************************/

    /**
     * Gets the token of the child that is logged in
     * @param childCommand holds the child's info needed to check if they are currently logged in
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

    /**
     * Allows current child's account to regenerate a new token if token is currently expired
     * @param childCommand holds the child's info needed to check if they are logged in
     * @return a new token for the child's account
     * @throws Exception
     */
    @RequestMapping(path = "/child/token/regenerate", method = RequestMethod.PUT)
    public String regenerateChildToken(@RequestBody ChildCommand childCommand) throws Exception {
        Auth auth = new Auth();
        Child child = auth.checkChildLogin(childCommand);
        return child.regenerate();
    }

}
