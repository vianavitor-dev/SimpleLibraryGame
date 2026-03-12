package com.vianavitor.simplelibrarygame.repository.aux;

public interface LoggableUser {
    // used to get the user's hashed password based on the unique username
    String getPasswordByUsername(String username);

    boolean getActiveById(Long id);
}
