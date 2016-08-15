package com.theironyard.entities;

/**
 * Created by Nigel on 8/13/16.
 */
public class Parent {
<<<<<<< Updated upstream
=======

    public static final int TOKEN_EXPIRATION = 1;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String parentPhone;

    @OneToMany
    private Collection<Child> childCollection;

    @Column(nullable = false, unique = true)
    @ColumnDefault("'NO-TOKEN-HERE'")
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @ColumnDefault("'1992-01-01'")
    private LocalDateTime expiration;

    public Parent() {
    }

    public Parent(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        setTokenAndExpiration();
    }

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

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public Collection<Child> getChildCollection() {
        return childCollection;
    }

    public void addChild(Child child) {
        childCollection.add(child);
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
>>>>>>> Stashed changes
}
