package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.LocalDateTimeConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

/**
 * Created by Nigel on 8/15/16.
 */
@MappedSuperclass
public abstract class User {

    public static final int TOKEN_EXPIRATION = 1;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    protected String name;

    @Column
    @JsonIgnore
    protected String email;

    @Column
    @JsonIgnore

    protected  String phone;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false)
    @JsonIgnore
    protected String password;

    @ManyToMany
    @JsonIgnore
    protected Collection<Chore> choreCollection;

    @ManyToMany
    @JsonIgnore
    protected Collection<Reward> rewardCollection;

    @ManyToMany
    @JsonIgnore
    protected Collection<Reward> wishlistCollection;


    public Collection<Chore> getChoreCollection() {
        return choreCollection;
    }

    public void setChoreCollection(Collection<Chore> choreCollection) {
        this.choreCollection = choreCollection;
    }

    public void addChore(Chore chore){
        this.choreCollection.add(chore);
    }

    public Collection<Reward> getRewardCollection() {
        return rewardCollection;
    }

    public void setRewardCollection(Collection<Reward> rewardCollection) {
        this.rewardCollection = rewardCollection;
    }

    public void addReward(Reward reward){
        this.rewardCollection.add(reward);
    }

    @Column(nullable = false, unique = true)
    @ColumnDefault("'NO-TOKEN-HERE'")
    @JsonIgnore
    protected String token;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @ColumnDefault("'1992-01-01'")
    @JsonIgnore
    protected LocalDateTime expiration;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTokenValid(){
        return expiration.isAfter(LocalDateTime.now());
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public Collection<Reward> getWishlistCollection() {
        return wishlistCollection;
    }

    public void setWishlistCollection(Collection<Reward> wishlistCollection) {
        this.wishlistCollection = wishlistCollection;
    }

    public void addWishlistItem(Reward reward){
        this.wishlistCollection.add(reward);
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
