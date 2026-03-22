package com.vianavitor.simplelibrarygame.repository.aux;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.utils.classes.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface LoggableUser {
    // used to get the user's hashed password based on the unique username
    Optional<UserInfo> getIdAndPasswordByUsername(String username);

//    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

//    Optional<Boolean> getActiveById(Long id);
}
