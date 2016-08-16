package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.RewardCommand;
import com.theironyard.entities.*;
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
     * Endpoint that will return a collection of rewards in the childs wishlist for that child's account
     * @param childToken child's token to be authorized for that account signed in
     * @return all rewards in the child's wishlist
     */
    @RequestMapping(path = "/child/wishlist",method = RequestMethod.GET)
    public Collection<Reward> showWishlist(@RequestHeader (value = "Authorization") String childToken){
        Auth auth = new Auth();
        Child child = auth.getChildFromAuth(childToken);
        return child.getRewardCollection();
    }

    /**
     * Endpoint that is going to return a collection of chores for that child's account
     * @param childToken child's token to be authorized for the account signed in
     * @return all chores for that child logged in
     */
    @RequestMapping(path = "/child/chores",method = RequestMethod.GET)
    public Collection<Chore> showAllChores(@RequestHeader (value = "Authorization") String childToken){
        Auth auth = new Auth();
        Child child = auth.getChildFromAuth(childToken);
        return child.getChoreCollection();
    }

    /**
     * Endpoint that gets and returns a child's points
     * @param childToken child's token to be authorized
     * @return a child's points
     */
    @RequestMapping(path = "/child/points", method = RequestMethod.GET)
    public Point getPoints(@RequestHeader (value = "Authorization") String childToken){
        Auth auth = new Auth();
        Child child =  auth.getChildFromAuth(childToken);
        return child.getChildPoint();
    }

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
     * Endpoint for when a child is logging out of the account
     * @param childToken child's token to be authorized for that account signed in
     * @param session current session of the child's account to be invalidated
     */
    @RequestMapping(path = "/child/logout",method = RequestMethod.POST)
    public void childLogout(@RequestHeader (value = "Authorization") String childToken, HttpSession session){
        Auth auth = new Auth();
        auth.getChildFromAuth(childToken);
        session.invalidate();
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

    /**
     *
     * @param childToken child's token to be authorized for that child's account signed in
     * @param rewardCommand grab the reward's info the child types in since it is needed to create a new reward
     * @return the new collection of the child's wishlist with the new reward that was added to the collection
     */
    @RequestMapping(path = "/child/wishlist",method = RequestMethod.POST)
    public Collection<Reward> createWishlistItem(@RequestHeader (value = "Authorization") String childToken, RewardCommand rewardCommand){
        Auth auth = new Auth();
        Child child = auth.getChildFromAuth(childToken);

        Reward reward = new Reward(rewardCommand.getDescription(),rewardCommand.getUrl() ,rewardCommand.getPointvalue());
        child.addReward(reward);
        return child.getRewardCollection();
    }

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
