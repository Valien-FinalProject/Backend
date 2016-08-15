package com.theironyard.services;

/**
 * Created by Nigel on 8/13/16.
 */
<<<<<<< Updated upstream
public interface ParentRepository {
=======
public interface ParentRepository extends JpaRepository<Parent, Integer> {

    Parent findFirstByToken(String token);

    Parent findFirstByUsername(String name);


>>>>>>> Stashed changes
}
