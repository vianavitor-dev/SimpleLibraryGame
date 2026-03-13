package com.vianavitor.simplelibrarygame.service.utils;

import com.vianavitor.simplelibrarygame.model.utils.classes.User;

public interface ManageableUser {

    Long login(String username, String password);

    void deactivate(Long id);

    void activate(Long id);
}
