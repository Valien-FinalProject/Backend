package com.theironyard.services;

import com.theironyard.command.ChildCommand;
import com.theironyard.command.ParentCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by chocodonis on 8/14/16.
 */

@Service
public class AuthService {

    @Autowired
    ParentRepository parents;

    @Autowired
    ChildRepository children;

    @PostConstruct
    public void temp(){
        String foo = "bar";

        System.out.println("FOO");
    }

    public Parent getParentFromAuth(String auth) {
        Parent parent = parents.findFirstByToken(auth.split(" ")[1]);
        if (!parent.isTokenValid()) {
            throw new TokenExpiredException();
        }
        return parent;
    }

    public Child getChildFromAuth(String auth) {
        Child child = children.findFirstByToken(auth.split(" ")[1]);
        if (!child.isTokenValid()) {
            throw new TokenExpiredException();
        }
        return child;
    }

    /**
     * Checks if parent is logged in
     * @param command
     * @return user, found by username
     * @throws Exception
     */
    public Parent checkParentLogin(ParentCommand command) throws Exception{
        Parent parent = parents.findFirstByUsername(command.getUsername());
        if(parent == null){
            throw new UserNotFoundException();
        }

        if(!PasswordStorage.verifyPassword(command.getPassword(), parent.getPassword())){
            throw new LoginFailedException();
        }

        if(!parent.isTokenValid()){
            throw new TokenExpiredException();
        }

        return parent;
    }

    /**
     * Checks if child is logged in
     * @param childCommand holds child's info to be checked
     * @return a child
     * @throws Exception
     */
    public Child checkChildLogin(ChildCommand childCommand) throws Exception{
        Child child  = children.findByUsername(childCommand.getUsername());
        if (child == null){
            throw new UserNotFoundException();
        }
        if (!PasswordStorage.verifyPassword(childCommand.getPassword(), child.getPassword())){
            throw new LoginFailedException();
        }
        if (!child.isTokenValid()){
            throw new TokenExpiredException();
        }
        return child;
    }
}