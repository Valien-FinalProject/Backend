package com.theironyard.controllers;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.RewardCommand;
import com.theironyard.entities.*;
import com.theironyard.exceptions.ChoreNotFoundException;
import com.theironyard.exceptions.NotEnoughPointsException;
import com.theironyard.services.*;
import com.theironyard.services.TwilioNotifications;
import com.twilio.sdk.TwilioRestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    TwilioNotifications twilioNotifications;

    @Autowired
    EmailService emailService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${walmart.api.key}")
    public String walmartKey;

    private String BASE_URL = "http://api.walmartlabs.com/v1/search?format=json&apiKey=" + System.getenv("walmartKey") /**walmartKey**/ + "&numItems=1&query=";

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
     * Gets all chores that are not assigned to any children and returns a pool of chores
     * @param childToken child's token to be authorized for the account signed in
     * @return a collection of chores that are not assigned to any children
     */
    @RequestMapping(path = "/chores/pool", method = RequestMethod.GET)
    public Collection<Chore> showPoolChores(@RequestHeader (value = "Authorization") String childToken){
        Child child = authService.getChildFromAuth(childToken);
        Parent parent = child.getParent();

        Collection<Chore> pool = new ArrayList<>();
        Collection<Chore> parentCollection = parent.getChoreCollection();
        parentCollection.stream().filter(c -> c.getChildAssigned() == null).forEach(c -> pool.add(c));
        return pool;
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
        Parent parent = child.getParent();
        return parent.getRewardCollection();
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

    /**
     * Get a Collection of chores that have not been approved, are not pending and are assigned to a given child.
     * @param token child's token
     * @return Collection of chores
     */
    @RequestMapping(path = "/current", method = RequestMethod.GET)
    public Collection<Chore> getCurrentChore(@RequestHeader(value = "Authorization") String token){
        Child child = authService.getChildFromAuth(token);
        Parent parent = child.getParent();

        Collection<Chore> parentChoreCollection = parent.getChoreCollection();
        Collection<Chore> currentChoreList = new ArrayList<>();

        parentChoreCollection.stream().filter(chore -> chore.isPending() == false && chore.isComplete() == false && chore.getChildAssigned() == child).forEach(chore -> currentChoreList.add(chore));

        return currentChoreList;
    }


    /**
     * Get a list of chores that are pending and assigned to a given child.
     * @param token -child's token
     * @return List of the chores that are pending
     */
    @RequestMapping(path = "/pending", method = RequestMethod.GET)
    public Collection<Chore> getPendingChores(@RequestHeader(value = "Authorization") String token){
        Child child = authService.getChildFromAuth(token);

        Collection<Chore> childChoreCollection = child.getChoreCollection();
        Collection<Chore> pendingChoreList = new ArrayList<>();

        childChoreCollection.stream().filter(chore -> chore.isPending() == true).forEach(chore -> pendingChoreList.add(chore));

        return pendingChoreList;
    }

    /**
     * Get a list of chores that are complete and assigned to a given child.
     * @param token child's token
     * @return list of chores
     */
    @RequestMapping(path = "/complete", method = RequestMethod.GET)
    public Collection<Chore> getCompleteChores(@RequestHeader(value = "Authorization") String token){
        Child child = authService.getChildFromAuth(token);
        Parent parent = child.getParent();

        Collection<Chore> parentChoreCollection = parent.getChoreCollection();
        Collection<Chore> completeChoreList = new ArrayList<>();

        parentChoreCollection.stream().filter(chore -> chore.isComplete() && chore.getChildAssigned() == child).forEach(chore -> completeChoreList.add(chore));

        return completeChoreList;
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
        authService.getChildFromAuth(childToken);
        session.invalidate();
    }

    /**
     *
     * @param childToken child's token to be authorized for that child's account signed in
     * @param rewardCommand grab the reward's info the child types in since it is needed to create a new reward
     * @return the new collection of the child's wishlist with the new reward that was added to the collection
     */
    @RequestMapping(path = "/wishlist", method = RequestMethod.POST)
    public Reward createWishlistItem(@RequestHeader (value = "Authorization") String childToken, @RequestBody RewardCommand rewardCommand) throws IllegalAccessException, InstantiationException, TwilioRestException {
        Child child = authService.getChildFromAuth(childToken);
        Parent parent = child.getParent();

        Reward reward = new Reward(rewardCommand.getName());

        Map product = restTemplate.getForObject(BASE_URL + rewardCommand.getName(), HashMap.class);
        List<Map> item = ((List<Map>) product.get("items"));
        reward.setUrl((String)item.get(0).get("productUrl"));
        reward.setImageUrl((String) item.get(0).get("mediumImage"));
        rewardRepository.save(reward);
        child.addWishlistItem(reward);
        childRepository.save(child);

        if (parent.isPhoneOptIn() && parent.getPhone() != null){
            twilioNotifications.wishlistItemAdded(parent, child);
        }

        return reward;
    }

    /***************************
        Update/PUT Endpoints
     ***************************/

    /**
     * Allows child to change its email, phone number, password, and name.
     * @param childToken
     * @param childCommand
     * @return
     */
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
     * @param id id of the chore the child is changing the pending status of
     * @return the chore that is now pending to be completed
     */
    @RequestMapping(path = "/chore/{id}/pending", method = RequestMethod.PUT)
    public Chore setPendingChore(@RequestHeader (value = "Authorization") String childToken, @PathVariable int id) throws TwilioRestException {

        Child child = authService.getChildFromAuth(childToken);
        Parent parent = child.getParent();

        Collection<Chore> childChores = child.getChoreCollection();
        Chore pendingChore = choreRepository.findOne(id);
        if (pendingChore == null){
            throw new ChoreNotFoundException();
        }
        
        pendingChore.setPending(true);
        choreRepository.save(pendingChore);
        childRepository.save(child);

        // If parent's phone Opt-in is true then send a text
        if(parent.isPhoneOptIn() && parent.getPhone() != null) {
            twilioNotifications.chorePending(child.getParent());
        }
        return pendingChore;
    }

    /**
     * Allows the child to grab a chore from the pool of chores and assign it to himself/herself
     * @param childToken child's token to be authorized
     * @param id chore's id the child is trying to get
     * @return the chore that the child assigned to himself/herself
     */
    @RequestMapping(value = "/chore/{id}", method = RequestMethod.PUT)
    public Chore assignChoreToChild(@RequestHeader (value = "Authorization") String childToken, @PathVariable int id){
        Child child = authService.getChildFromAuth(childToken);
        Chore chore = choreRepository.findOne(id);

        if(chore.getChildAssigned() == null){
            chore.setChildAssigned(child);
            child.addChore(chore);
            choreRepository.save(chore);
            childRepository.save(child);
        }
        return chore;
    }

    /**
     * Allows the child to cash in their points for a reward, once completed it will send a notification to the parent
     * if they have an email or phone number available and is has the opt-in field set to true
     * @param childToken child's token that is need to be authorized
     * @param id id of the reward the child is trying to cash in their points for
     * @return
     * @throws IOException
     * @throws TwilioRestException
     */
    @RequestMapping(value = "/reward/{id}/deduct", method = RequestMethod.PUT)
    public Child cashInPoints(@RequestHeader (value = "Authorization") String childToken, @PathVariable int id) throws IOException, TwilioRestException {
        Child child = authService.getChildFromAuth(childToken);
        Reward reward = rewardRepository.getOne(id);

        child.setChildPoint(child.getChildPoint() - reward.getPoints());
        if(child.getChildPoint() < 0){
            throw new NotEnoughPointsException();
        }

        // If email Opt-in is true, send an email:
        Parent parent = child.getParent();
        String body = "Hello, " + parent.getName() + ". We are just letting you know that, " + child.getName() + " has cashed in " + reward.getPoints() + " points.";

        if (parent.isEmailOptIn()) emailService.sendEmail(parent.getEmail(), body);
        // If phone Opt-in is true, send a text
        if (parent.isPhoneOptIn()){
            twilioNotifications.childCashedInPoints(parent, child, reward);
        }
        childRepository.save(child);
        return child;
    }

    /****************************
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
        rewardRepository.delete(reward);
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
        tokenMap.put("id", String.valueOf(child.getId()));
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
