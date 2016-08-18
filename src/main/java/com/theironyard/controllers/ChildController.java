package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.RewardCommand;
import com.theironyard.entities.*;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.*;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nigel on 8/13/16.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/child")
public class ChildController {

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

    @Autowired
    ChoreRepository choreRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    AuthService authService;

    /***************************
        Read/Get Endpoints
     **************************/

    /**
     * Gets info on one child's account but requires parent linked to the child's account to be authorized
     * @param childToken parent's token to authorized for that account signed in
     * @param id child's id is passed to find certain child
     * @return a child's info
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Child getOneChild(@RequestHeader(value = "Authorization") String childToken, @PathVariable int id){

        Child child = authService.getChildFromAuth(childToken);
        return childRepository.findOne(id);
    }

    /**
     * Endpoint that will return a collection of rewards in the childs wishlist for that child's account
     * @param childToken child's token to be authorized for that account signed in
     * @return all rewards in the child's wishlist
     */
    @RequestMapping(path = "/wishlist", method = RequestMethod.GET)
    public Collection<Reward> showWishlist(@RequestHeader (value = "Authorization") String childToken){

        Child child = authService.getChildFromAuth(childToken);
        return child.getWishlistCollection();
    }

    /**
     * Endpoint that is going to return a collection of chores for that child's account
     * @param childToken child's token to be authorized for the account signed in
     * @return all chores for that child logged in
     */
    @RequestMapping(path = "/chores", method = RequestMethod.GET)
    public Collection<Chore> showAllChores(@RequestHeader (value = "Authorization") String childToken){

        Child child = authService.getChildFromAuth(childToken);
        return child.getChoreCollection();
    }

    /**
     * Endpoint that gets and returns a child's points
     * @param childToken child's token to be authorized
     * @return a child's points
     */
    @RequestMapping(path = "/points", method = RequestMethod.GET)
    public int getPoints(@RequestHeader (value = "Authorization") String childToken){

        Child child =  authService.getChildFromAuth(childToken);
        return child.getChildPoint();
    }

    /**
     * Endpoint that gets all rewards for child currently logged in
     * @param childToken child's token to be authorized
     * @return a collection of the child's rewards
     */
    @RequestMapping(path = "/rewards", method = RequestMethod.GET)
    public Collection<Reward> getChildRewards(@RequestHeader (value = "Authorization") String childToken){

        Child child = authService.getChildFromAuth(childToken);
        return child.getRewardCollection();
    }

    /**
     * Gets the child's email for when the child wishes to change his/her email but requires them to be signed in
     * @param childToken holds the child's token to be authorized
     * @return the child's email for the signed in account
     */
    @RequestMapping(path = "/email", method = RequestMethod.GET)
    public String getChildEmail(@RequestHeader (value = "Authorization") String childToken){
        Child child = authService.getChildFromAuth(childToken);
        return child.getEmail();
    }

    /**
     * Gets the child's phone for when the child wishes to change his/her phone but requires them to be signed in
     * @param childToken holds the child's token to be authorized
     * @return the child's phone for the signed in account
     */
    @RequestMapping(path = "/phone", method = RequestMethod.GET)
    public String getChildPhone(@RequestHeader (value = "Authorization") String childToken){
        Child child = authService.getChildFromAuth(childToken);
        return child.getPhone();
    }

    /***************************
        Create/Post Endpoints
     ***************************/

    /**
     * Endpoint for when a child is logging out of the account
     * @param childToken child's token to be authorized for that account signed in
     * @param session current session of the child's account to be invalidated
     */
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void childLogout(@RequestHeader (value = "Authorization") String childToken, HttpSession session){

        Child child = authService.getChildFromAuth(childToken);
        session.invalidate();
    }

    /**
     * A child must be authorized to complete a chore/task and await for the parent to approve chore/task before
     * points are rewarded
     * @param childToken child's token to be authorized for that account signed in
     * @param id the id of a certain chore that the child is currently marking for completion
     * @return the chore that was completed or marked for completion is returned
     */
    @RequestMapping(path = "/chore/{id}", method = RequestMethod.POST)
    public Chore completeChore(@RequestHeader (value = "Authorization") String childToken, @PathVariable int id){

        Child child = authService.getChildFromAuth(childToken);

        Chore chore = choreRepository.findOne(id);
        chore.setComplete(true);
        choreRepository.save(chore);
        return chore;
    }

    /**
     *
     * @param childToken child's token to be authorized for that child's account signed in
     * @param rewardCommand grab the reward's info the child types in since it is needed to create a new reward
     * @return the new collection of the child's wishlist with the new reward that was added to the collection
     */
    @RequestMapping(path = "/wishlist", method = RequestMethod.POST)
    public Collection<Reward> createWishlistItem(@RequestHeader (value = "Authorization") String childToken, @RequestBody RewardCommand rewardCommand){

        Child child = authService.getChildFromAuth(childToken);

        Reward reward = new Reward(rewardCommand.getDescription(),rewardCommand.getUrl() ,rewardCommand.getPoints());
        rewardRepository.save(reward);
        child.addWishlistItem(reward);
        childRepository.save(child);
        return child.getWishlistCollection();
    }

    /***************************
        Update/PUT Endpoints
     ***************************/

    @RequestMapping(path = "/profile", method = RequestMethod.PUT)
    public Child updateProfile(@RequestHeader (value = "Authorization") String childToken, @RequestBody ChildCommand childCommand){

        Child child = authService.getChildFromAuth(childToken);

        child.setEmail(childCommand.getEmail());
        child.setPhone(childCommand.getPhone());
        childRepository.save(child);

        return child;
    }

    /**
     * Endpoint that allows child to set a chore to a pending status so the parent may review the chore and judge
     * it the chore is approved and completed and denied and returned back to the chores to do
     * @param childToken child's token that is required for authorization
     * @param choreId id of the chore the child is changing the pending status of
     * @return the chore that is now pending to be completed
     */
    @RequestMapping(path = "/chore/{id}/pending", method = RequestMethod.PUT)
    public Chore setPendingChore(@RequestHeader (value = "Authorization") String childToken, @PathVariable int choreId){

        Child child = authService.getChildFromAuth(childToken);

        Collection<Chore> childChores = child.getChoreCollection();

        Chore pendingChore = choreRepository.findOne(choreId);
        pendingChore.setPending(true);
        choreRepository.save(pendingChore);
        childChores.add(pendingChore);
        return pendingChore;
    }

    /***************************
        Delete Endpoints
     ***************************/

    /**
     * Delete endpoint that allows a child to delete an item in their wishlist
     * @param childToken child's token to be authoized for that child's account signed in
     * @param id id of the item/reward that the child is going to delete from their wishlist
     * @return a new collection of the child's updated wishlist
     */
    @RequestMapping(path = "/delete/wishlist/{id}", method = RequestMethod.DELETE)
    public Collection<Reward> deleteWishlistItem (@RequestHeader (value = "Authorization") String childToken, @PathVariable int id){

        Child child = authService.getChildFromAuth(childToken);
        Reward reward = rewardRepository.findOne(id);
        Collection<Reward> wishlist = child.getWishlistCollection();
        wishlist.remove(reward);
        return wishlist;
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
    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getChildToken(@RequestBody ChildCommand childCommand) throws Exception{

        Child child = authService.checkChildLogin(childCommand);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", child.getToken());
        tokenMap.put("username", childCommand.getUsername());
        return tokenMap;
    }

    /**
     * Allows current child's account to regenerate a new token if token is currently expired
     * @param childCommand holds the child's info needed to check if they are logged in
     * @return a new token for the child's account
     * @throws Exception
     */
    @RequestMapping(path = "/token/regenerate", method = RequestMethod.PUT)
    public String regenerateChildToken(@RequestBody ChildCommand childCommand) throws Exception {

        Child child = authService.checkChildLogin(childCommand);
        return child.regenerate();
    }
}
