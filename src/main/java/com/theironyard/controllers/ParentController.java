package com.theironyard.controllers;

import com.theironyard.command.RewardCommand;
import com.theironyard.entities.*;
import com.theironyard.services.*;
import com.twilio.sdk.TwilioRestException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.ChoreCommand;
import com.theironyard.command.ParentCommand;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


/**
 * Created by Nigel on 8/13/16.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/parent")
public class ParentController {

    @Autowired
    ParentRepository parents;

    @Autowired
    ChildRepository children;

    @Autowired
    ChoreRepository chores;

    @Autowired
    RewardRepository rewards;

    @Autowired
    AuthService authService;

    @Autowired
    EmailService emailService;

    @Autowired
    TwilioNotifications twilioNotifications;


    /*==========================================================
    ***************** 'LOGIN & LOGOUT' ENDPOINTS ***************
    ============================================================*/

    /**
     * Allows the parent to register.
     *
     * @param command - allows parent info to be passed without making a new parent object.
     * @return newly saved parent object.
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Parent registerParent(@RequestBody ParentCommand command) throws PasswordStorage.CannotPerformOperationException, IOException, TwilioRestException {

        //Create the new Parent.
        Parent parent = new Parent(command.getName(), command.getUsername(), PasswordStorage.createHash(command.getPassword()));

        parent.setEmail(command.getEmail());
        parent.setPhone(command.getPhone());
        parent.setEmailOptIn(command.isEmailOptIn());
        parent.setPhoneOptIn(command.isPhoneOptIn());

        //Save parent to the 'parents' repository.
        parents.save(parent);

        //Send new parent object
        if(parent.isPhoneOptIn() && parent.getPhone() != null) {
            twilioNotifications.parentRegister(parent);
        }

        //If email Opt-in is true, send an email:
//        String body = "Hello " + parent.getName() + ". Thank you for registering with us. We hope you enjoy our app.";
//        if (parent.isEmailOptIn() && parent.getEmail() != null) emailService.sendEmail(parent.getEmail(), body);

        return parent;

    }

    /**
     * Allows the parent to logout
     *
     * @param parentToken a token for the parent's account that is currently signed in
     * @param session     allows the session to be ended and the parent to be logged out
     */
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void parentLogout(@RequestHeader(value = "Authorization") String parentToken, HttpSession session) {

        //Parent is logged in
        Parent parent = authService.getParentFromAuth(parentToken);

        //kill session
        session.invalidate();

    }


    /*==================================================
    ***************** 'CREATE' ENDPOINTS ***************
    ===================================================*/


    /**
     * Parent can register their child.
     *
     * @param command holds info for the child object
     * @param auth auth token of the parent
     * @return
     */
    @RequestMapping(path = "/child", method = RequestMethod.POST)
    public Child createChild(@RequestBody ChildCommand command,@RequestHeader(value = "Authorization") String auth) throws PasswordStorage.CannotPerformOperationException {

        //Find parent via token
        Parent parent = authService.getParentFromAuth(auth);

        //Create new Child object.

        Child child = new Child(command.getName(), command.getUsername(), PasswordStorage.createHash(command.getPassword()));
        child.setParent(parent);
        child.setPhone(command.getPhone());
        child.setEmail(command.getEmail());
        //Add child to Parent's child Collection & Save the child to 'children' repository.
        parent.addChild(child);
        child.setParent(parent);
        children.save(child);
        parents.save(parent);
        //return saved object
        return child;
    }

    /**
     * Allows a parent to assign a chore to a child.
     * @param id - the child's id.
     * @param auth - the parent's token.
     * @return
     */
    @RequestMapping(path = "/child/{id}/chore", method = RequestMethod.POST)
    public Chore assignAndCreateChore(@PathVariable int id, @RequestBody ChoreCommand command, @RequestHeader(value = "Authorization") String auth){

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Find child via id
        Child child = children.findOne(id);

        //Create chore
        Chore chore = new Chore(command.getName(), command.getDescription(), command.getValue());
        chore.setStartDate(command.getStartDate());
        chore.setEndDate(command.getEndDate());
        chore.setCreator(parent);

        //Assign chore to child
        if (!child.getChoreCollection().contains(chore)) {
            chore.setChildAssigned(child);

            //Save chore to 'chores' repository
            chores.save(chore);

            //Add chore to the child's chore Collection
            child.addChore(chore);

            //Update the child
            children.save(child);
        }

        //Send the assigned chore object
        parent.addChore(chore);
        parents.save(parent);
        return chore;
    }

    @RequestMapping(path = "/child/{childId}/chore/{choreId}", method = RequestMethod.POST)
    public Chore assignChore(@PathVariable int childId, @PathVariable int choreId, @RequestHeader(value = "Authorization") String auth){

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Find child via id
        Child child = children.findOne(childId);

        //Find chore via id
        Chore chore = chores.findOne(choreId);

        //Assign chore to child if the chore is not already assigned.
        if (chore.getChildAssigned() == null) {
            chore.setChildAssigned(child);

            //Save chore to 'chores' repository
            chores.save(chore);

            //Add chore to the child's chore Collection
            child.addChore(chore);

            //Update the child
            children.save(child);
        }

        //Update Parent and parents repository
        parent.addChore(chore);
        parents.save(parent);

        //Send the assigned chore object
        return chore;
    }

    /**
     * Allows a parent to create a new chore.
     * @param command - hold the info for the chore command.
     * @param auth    - the parent's token.
     * @return the new chore created
     */
    @RequestMapping(path = "/chore", method = RequestMethod.POST)
    public Chore createChore(@RequestHeader(value = "Authorization") String auth, @RequestBody ChoreCommand command) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Make new chore object
        Chore chore = new Chore(command.getName(), command.getDescription(), command.getValue());

        //Take Long and Make it a date & save those dates into the chore object.
        chore.setStartDate(command.getStartDate());
        chore.setEndDate(command.getEndDate());
        chore.setCreator(parent);

        //Save the chore object to the Parent Collection & Repository
        chores.save(chore);
        parent.addChore(chore);
        parents.save(parent);

        //Send the chore object.
        return chore;
    }

    /**
     * Allows a parent to create a new reward.
     *
     * @param parentToken   the parent's token to be authorized
     * @param command holds info for the reward that is going to be created
     * @return the new reward created
     */
    @RequestMapping(path = "/reward", method = RequestMethod.POST)
    public Reward createReward(@RequestHeader(value = "Authorization") String parentToken, @RequestBody RewardCommand command) {

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //Create a new Reward
        Reward reward = new Reward(command.getName());
        reward.setDescription(command.getDescription());
        reward.setUrl(command.getUrl());
        reward.setPoints(command.getPoints());

        //Save Reward to the Collections in Parent & Child. Also to the 'rewards' repository.
        rewards.save(reward);
        parent.addReward(reward);
        parents.save(parent);

        return reward;
    }

    /**
     * Child has completed a chore and the parent will approve. We need to add those point to the child and remove the chore from the child's list.
     *
     * @param childId id of the child
     * @param choreId id of the chore to be approved
     * @return a string stating that we have removed the chore and added points to the child.
     */
    @RequestMapping(path = "/child/{childId}/approve/{choreId}", method = RequestMethod.POST)
    public Chore approveChore(@PathVariable int childId, @PathVariable int choreId, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the chore to be approved and the child that the chore belongs to
        Chore choreToApprove = chores.findOne(choreId);
        Child child = children.findOne(childId);
        Collection<Chore> childChores = child.getChoreCollection();
        Collection<Chore> parentChores = parent.getChoreCollection();

        //Add point value of the chore to the child's points.
        child.setChildPoint(child.getChildPoint() + choreToApprove.getValue());

        //Mark chore as complete
        choreToApprove.setComplete(true);
        choreToApprove.setPending(false);

        //Remove the chore from the child's chore Collection
        childChores.remove(choreToApprove);
        chores.save(choreToApprove);
        children.save(child);


        return choreToApprove;
    }



    /*==================================================
    ***************** 'READ' ENDPOINTS ***************
    ===================================================*/


    /**
     * Get a Collection of chores that have not been approved, are not pending and are assigned to a given child.
     * @param id child's unique id
     * @param token parent's token
     * @return Collection of chores
     */
    @RequestMapping(path = "/child/{id}/current", method = RequestMethod.GET)
    public Collection<Chore> getCurrentChore(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        //Get the parent from and the child from id.
        Parent parent = authService.getParentFromAuth(token);
        Child child = children.findOne(id);

        //Get the parent chore Collection & Create List to return
        Collection<Chore> parentCollection = parent.getChoreCollection();
        Collection<Chore> currentChoreList = new ArrayList<>();

        //Stream through Collection to find chore that are not pending, are not complete, and are assigned to child.
        parentCollection.stream().filter(chore -> !chore.isPending() && !chore.isComplete() && chore.getChildAssigned() == child).forEach(currentChoreList::add);

        //Give List of Current Chores
        return currentChoreList;
    }

    /**
     * Get a list of chores that are pending and assigned to a given child.
     * @param id - child's id
     * @param token -parent's token
     * @return List of the chores that are pending
     */
    @RequestMapping(path = "child/{id}/pending", method = RequestMethod.GET)
    public Collection<Chore> getPendingChores(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        //Get the parent from and the child from id.
        Parent parent = authService.getParentFromAuth(token);
        Child child = children.findOne(id);

        //Get the parent chore Collection & Create List to return
        Collection<Chore> parentCollection = parent.getChoreCollection();
        Collection<Chore> pendingChoreList = new ArrayList<>();

        //Stream through Collection to find chores that are pending and assigned to child.
        parentCollection.stream().filter(chore -> chore.isPending() && chore.getChildAssigned() == child).forEach(pendingChoreList::add);

        //Give List of Current Chores
        return pendingChoreList;
    }

    /**
     * Get a list of chores that are complete and assigned to a given child.
     * @param id child's id
     * @param token parent's token
     * @return list of chores
     */
    @RequestMapping(path = "/child/{id}/complete", method = RequestMethod.GET)
    public Collection<Chore> getCompleteChores(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        //Get the parent from and the child from id.
        Parent parent = authService.getParentFromAuth(token);
        Child child = children.findOne(id);

        //Get the parent chore Collection & Create List to return
        Collection<Chore> parentCollection = parent.getChoreCollection();
        Collection<Chore> completeChoreList = new ArrayList<>();

        //Stream through collection to find chores that are complete and assigned to child.
        parentCollection.stream().filter(chore -> chore.isComplete() && chore.getChildAssigned() == child).forEach(completeChoreList::add);

        //Give List of Current Chores
        return completeChoreList;
    }

    /**
     * Gets a parent's info.
     *
     * @param id - the parent's id is passed.
     * @return returns a parent.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Parent getParent(@PathVariable int id) {
        //Find the parent by their id.
        Parent parent = parents.findOne(id);

        //Return the parent object.
        return parent;
    }

    /**
     * Returns all children that belong to the parent.
     *
     * @param auth - verifies the parent's token
     * @return returns the collection of children.
     */
    @RequestMapping(path = "/children", method = RequestMethod.GET)
    public Collection<Child> getChildren(@RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Return the list of children that parent has created
        return parent.getChildCollection();
    }

    /**
     * Gets the collection of chores assigned to a child's account by their id.
     *
     * @param parentToken verifies the parent's token
     * @param id returns a collection of chores from a child's account
     * @return
     */
    @RequestMapping(path = "/child/{id}/chores", method = RequestMethod.GET)
    public Collection<Chore> getAChildsChores(@RequestHeader (value = "Authorization") String parentToken,  @PathVariable int id){

        //Find parent via token
        authService.getParentFromAuth(parentToken);

        Parent parent = authService.getParentFromAuth(parentToken);

        //Get child via id
        Child child = children.findOne(id);

        //give the Child's Collection of chores.
        return child.getChoreCollection();
    }

    /**
     * Find all chores created by the parent.
     * @param parentToken parent's unique token.
     * @return a Collection of Chore.
     */
    @RequestMapping(path = "/chores", method = RequestMethod.GET)
    public Collection<Chore> getAllChores(@RequestHeader(value = "Authorization") String parentToken){

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //give the chore collection
        return parent.getChoreCollection();
    }

    /**
     * Get all chores that are not assigned to a child.
     * @param parentToken - token of the parent that is signed in
     * @return - collection of chores that are not assigned to a child.
     */
    @RequestMapping(path = "/chores/pool", method = RequestMethod.GET)
    public Collection<Chore> getChoresPool(@RequestHeader(value = "Authorization") String parentToken){

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //Create a Collection of Chores that are unassigned. --that's right..it uses a stream! boom!
        Collection<Chore> parentCollection = parent.getChoreCollection();
        Collection<Chore> unassignedChoreList = new ArrayList<>();
        parentCollection.stream().filter(c -> c.getChildAssigned() == null).forEach(unassignedChoreList::add);

        //Send Collection of chores that are unassigned.
        return unassignedChoreList;
    }

    /**
     * Finds all chores that are pending in the parent's repository
     * @param parentToken parent's token that is need to authorize parent
     * @return a list of all pending chores
     */
    @RequestMapping(path = "/chores/pending", method = RequestMethod.GET)
    public List<Chore> getPendingChores(@RequestHeader(value = "Authorization") String parentToken) {

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //give the chores that are pending
        return chores.findAllByCreatorAndPendingTrue(parent);
    }

    /**
     * Sends all rewards created by the parent, that's logged in.
     * @param parentToken parent's token needed to be authorized
     * @return Collection of Reward Collection.
     */
    @RequestMapping(path = "/rewards", method = RequestMethod.GET)
    public Collection<Reward> getParentRewards(@RequestHeader(value = "Authorization") String parentToken){

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //Send all rewards created by parent
        return parent.getRewardCollection();
    }

    /**
     * Get reward from repository via Id.
     * @param id the reward id
     * @param parentToken parent's unique token
     * @return
     */
    @RequestMapping(path = "/reward/{id}", method = RequestMethod.GET)
    public Reward getReward(@PathVariable int id, @RequestHeader(value = "Authorization") String parentToken) {

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //Give the reward
        return rewards.findOne(id);
    }

    /**
     *
     * @param id
     * @param parentToken
     * @return
     */
    @RequestMapping(path = "/child/{id}/wishlist", method = RequestMethod.GET)
    public Collection<Reward> getWishlist(@PathVariable int id, @RequestHeader(value = "Authorization") String parentToken) {

        //Find parent via token
        Parent parent = authService.getParentFromAuth(parentToken);

        //Find child via id
        Child child = children.findOne(id);

        //Get wish list from child
        //Send wishlist
        return child.getWishlistCollection();
    }

    /**
     * Gets the parent's email for when the parent wishes to change his/her email but requires them to be signed in
     * @param parentToken holds the parent's token to be authorized
     * @return the parent's email for the signed in account
     */
    @RequestMapping(path = "/email", method = RequestMethod.GET)
    public String getParentEmail(@RequestHeader (value = "Authorization") String parentToken){
        Parent parent = authService.getParentFromAuth(parentToken);
        return parent.getEmail();
    }

    /**
     * Gets the parent's phone for when the parent wishes to change his/her phone but requires them to be signed in
     * @param parentToken holds the parent's token to be authorized
     * @return the parent's phone for the signed in account
     */
    @RequestMapping(path = "/phone", method = RequestMethod.GET)
    public String getParentPhone(@RequestHeader (value = "Authorization") String parentToken){
        Parent parent = authService.getParentFromAuth(parentToken);
        return parent.getPhone();
    }

    /**
     * Gets the token of the Parent that is currently logged in.
     * @param command
     * @return token of the current parent
     * @throws Exception
     */
    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getToken(@RequestBody ParentCommand command) throws Exception {
        Parent parent = authService.checkParentLogin(command);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id", String.valueOf(parent.getId()));
        tokenMap.put("token", parent.getToken());
        tokenMap.put("username", command.getUsername());
        tokenMap.put("id", String.valueOf(parent.getId()));
        return tokenMap;
    }

    /**
     * Endpoint that puts each child in a map as the key and each of their total points as the value
     * @param parentToken parent's token to be authorized
     * @return a map of each child's name and the total points they have
     */
    @RequestMapping(path = "/child/points", method = RequestMethod.GET)
    public Map getAllPoints(@RequestHeader (value = "Authorization") String parentToken){
        Parent parent = authService.getParentFromAuth(parentToken);

        Map<String, Integer> pointsMap = new HashMap<>();
        Collection<Child> children = parent.getChildCollection();

        children.stream().forEach(child -> pointsMap.put(child.getName(), child.getChildPoint()));
        return pointsMap;
    }

    /**
     * Endpoint that gets a child's points by their id as long as their parent is authorized by their token
     * @param parentToken parent's token that is required to author the request
     * @param id id of the child that the endpoint will use to get their points
     * @return points of one child
     */
    @RequestMapping(path = "/child/{id}/points", method = RequestMethod.GET)
    public int getOneChildsPoitns(@RequestHeader (value = "Authorization") String parentToken, @PathVariable int id){
        authService.getParentFromAuth(parentToken);
        Child child = children.getOne(id);

        return child.getChildPoint();
    }

    /**
     * Allows the parent to see all the rewards that each child has used their points to cash in for
     * @param parentToken parent's token currently logged in that is needed to be authorized
     * @param id id of the child that is trying to be accessed
     * @return a collection of all the rewards the child has cashed their points in for
     */
    @RequestMapping(path = "/child/{id}/rewards", method = RequestMethod.GET)
    public Collection<Reward> getChildCashedInRewards(@RequestHeader (value = "Authorization") String parentToken, @PathVariable int id){
        authService.getParentFromAuth(parentToken);
        Child child = children.getOne(id);
        return child.getCashedInRewards();
    }


    /*==================================================
    ***************** 'UPDATE' ENDPOINTS ***************
    ===================================================*/

    /**
     * Allows parent to change their name, emial, and if they optin for sms and email notifications.
     *
     * @param command placeholder for parent data
     * @param auth    parent token
     * @return the modified parent
     */
    @RequestMapping(path = "/profile", method = RequestMethod.PUT)
    public Parent modifyParent(@RequestBody ParentCommand command, @RequestHeader(value = "Authorization") String auth) throws PasswordStorage.CannotPerformOperationException, TwilioRestException, PasswordStorage.InvalidHashException {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Modify the parent;
        if(command.getName() != null){parent.setName(command.getName());}
        if(command.getEmail() != null ){parent.setEmail(command.getEmail());}
        if(command.getPhone() != null){ parent.setPhone(command.getPhone());}
        if(command.getPassword() != null && PasswordStorage.verifyPassword(command.getPassword(), parent.getPassword())) {
            parent.setPassword(PasswordStorage.createHash(command.getPassword()));
        }
        parent.setEmailOptIn(command.isEmailOptIn());
        parent.setPhoneOptIn(command.isPhoneOptIn());
        parents.save(parent);

        return parent;
    }

    /**
     * Subtract point from the child
     *
     * @param id   the child's id
     * @param auth the parent's token
     * @return the child's points
     */
    @RequestMapping(path = "/deduct/{newPoint}/child/{id}", method = RequestMethod.PUT)
    public int deductPoints(@PathVariable int id, @PathVariable int newPoint, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the child
        Child child = children.findOne(id);

        //Add points to child's point
        int modifiedPointValue = child.getChildPoint() - newPoint;
        child.setChildPoint(modifiedPointValue);
        children.save(child);

        //Send the
        return child.getChildPoint();
    }

    /**
     * Add points to the child
     *
     * @param id       child id
     * @param newPoint point to add to the child
     * @param auth     parent token
     * @return new point value
     */
    @RequestMapping(path = "/add/{newPoint}/child/{id}", method = RequestMethod.PUT)
    public int addPoints(@PathVariable int id, @PathVariable int newPoint, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the child
        Child child = children.findOne(id);

        //Add points to child's point
        int modifiedPointValue = child.getChildPoint() + newPoint;
        child.setChildPoint(modifiedPointValue);
        children.save(child);

        //Send the
        return child.getChildPoint();
    }

    /**
     * Allows a parent to deny a chore
     *
     * @param id   the chore id
     * @param auth the parent's token
     */
    @RequestMapping(path = "/chore/{id}/deny", method = RequestMethod.PUT)
    public Chore denyChore(@PathVariable int id, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get chore and set pending to false
        Chore chore = chores.getOne(id);
        chore.setPending(false);

        //save chore
        chores.save(chore);
        return chore;
    }

    @RequestMapping(path = "/child/{id}", method = RequestMethod.PUT)
    public void modifyChild(@PathVariable int id, @RequestBody ChildCommand command, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the child via their id
        Child child = children.findOne(id);

        //Modify the child
        child.setName(command.getName());
        child.setChildPicture(command.getChildPicture());

        //save child
        children.save(child);
    }

    /**
     * Allows parent to modify a reward
     *
     * @param id      the reward id
     * @param command place holder for the new reward data
     * @param auth    parent's token
     */
    @RequestMapping(path = "/reward/{id}", method = RequestMethod.PUT)
    public void modifyReward(@PathVariable int id, @RequestBody RewardCommand command, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        AuthService getAuthService = new AuthService();

        authService.getParentFromAuth(auth);

        //Get the reward via it's id
        Reward reward = rewards.getOne(id);

        //modify the reward
        reward.setDescription(command.getDescription());
        reward.setPoints(command.getPoints());
        reward.setPoints(command.getPoints());


        //save the reward
        rewards.save(reward);
    }

    /**
     * Allows a parent to change a wish list item into a reward by instantiating a value and description
     *
     * @param childId  id of the child
     * @param rewardId id of the reward
     * @param command  data to modify the reward
     * @param auth     parent's token
     */
    @RequestMapping(path = "/child/{childId}/wishlist/{rewardId}", method = RequestMethod.PUT)
    public Reward modifyWishlist(@PathVariable int childId, @PathVariable int rewardId, @RequestBody RewardCommand command, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the child and reward
        Child child = children.getOne(childId);
        Reward reward = rewards.getOne(rewardId);

        //modify the reward
        reward.setDescription(command.getDescription());
        reward.setPoints(command.getPoints());

        //remove from child wishlist
        child.getWishlistCollection().remove(reward);

        //add to reward list
        parent.getRewardCollection().add(reward);
        children.save(child);
        rewards.save(reward);
        return reward;
    }

    /*==================================================
    ***************** 'DELETE' ENDPOINTS ***************
    ===================================================*/


    @RequestMapping(path = "/reward/{id}", method = RequestMethod.DELETE)
    public Collection<Reward> deleteReward(@PathVariable int id, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the Collection of Rewards from the parent
        Collection<Reward> parentRewardCollection = parent.getRewardCollection();

        //Find the award and delete it from the Collection and Repository.
        parentRewardCollection.remove(rewards.findOne(id));
        rewards.delete(id);

        //Return the new collection
        return parent.getRewardCollection();
    }

    @RequestMapping(path = "/chore/{id}", method = RequestMethod.DELETE)
    public Collection<Chore> deleteChore(@PathVariable int id, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the Collection of Chores from the parent
        Collection<Chore> parentChoreCollection = parent.getChoreCollection();

        //Find the chore and delete it from the Collection and Repository.
        parentChoreCollection.remove(chores.findOne(id));
        chores.delete(id);

        //Return the new Collection
        return parent.getChoreCollection();
    }

    @RequestMapping(path = "/child/{id}", method = RequestMethod.DELETE)
    public Collection<Child> deleteChild(@PathVariable int id, @RequestHeader(value = "Authorization") String auth) {

        //Find the parent via their token
        Parent parent = authService.getParentFromAuth(auth);

        //Get the Collection of Children from the parent
        Collection<Child> parentChildCollection = parent.getChildCollection();

        //Find the child and delete it from the Collection and Repository.
        Child child = children.getOne(id);
        parentChildCollection.remove(child);
        children.delete(child);


        //Return the new Collection
        return parent.getChildCollection();
    }

    /**
     * Allows the parent to deny and remove a wishlist item from a child's list
     * @param childId child's id that the parent is deleting the item from
     * @param rewardId the id of the wishlist item the parent is deleting
     * @param auth parent's token that needs to be authorized first
     */
    @RequestMapping(path = "/child/{childId}/wishlist/{rewardId}", method = RequestMethod.DELETE)
    public Child denyWishlistItem(@PathVariable int childId, @PathVariable int rewardId, @RequestHeader(value = "Authorization") String auth) {
        authService.getParentFromAuth(auth);

        Child child = children.getOne(childId);
        Reward reward = rewards.getOne(rewardId);

        child.getWishlistCollection().remove(reward);
        children.save(child);
        rewards.delete(reward);
        return child;
    }
    
}