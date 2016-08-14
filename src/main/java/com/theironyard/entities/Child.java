package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.LocalDateTimeConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

/**
 * Created by Nigel on 8/13/16.
 */
@Entity
@Table(name = "children")
public class Child {

    public static final int TOKEN_EXPIRATION = 1;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    @JsonIgnore
    private String name;

    @Column
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    @JsonIgnore
    private int age;

    @Column
    @JsonIgnore
    private String childPhone;

    @Column
    @JsonIgnore
    private String email;

    @Column
    @JsonIgnore
    private String location;

    @Column
    @JsonIgnore
    private File childPicture;

    @OneToMany
    @JsonIgnore
    private Parent parent;

    @ManyToMany
    private Collection<Chore> choreCollection;

    @ManyToMany
    private Collection<Reward> rewardCollection;

    @Column(nullable = false, unique = true)
    @ColumnDefault("'abcabcabcabcabc'")
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @ColumnDefault("'1992-01-01'")
    private LocalDateTime expiration;

    public Child() {
        setTokenAndExpiration();
    }

    public Child(String name, String username,String password ,int age, Parent parent) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.age = age;
        this.parent = parent;
        setTokenAndExpiration();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getChildPhone() {
        return childPhone;
    }

    public void setChildPhone(String childPhone) {
        this.childPhone = childPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public File getChildPicture() {
        return childPicture;
    }

    public void setChildPicture(File childPicture) {
        this.childPicture = childPicture;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Collection<Chore> getChoreCollection() {
        return choreCollection;
    }

    public void setChoreCollection(Collection<Chore> choreCollection) {
        this.choreCollection = choreCollection;
    }

    public Collection<Reward> getRewardCollection() {
        return rewardCollection;
    }

    public void setRewardCollection(Collection<Reward> rewardCollection) {
        this.rewardCollection = rewardCollection;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public void setTokenAndExpiration() {
        token = generateToken();
        expiration = LocalDateTime.now().plus(TOKEN_EXPIRATION, ChronoUnit.YEARS);
    }

    public String regenerate(){
        setTokenAndExpiration();
        return token;
    }

}