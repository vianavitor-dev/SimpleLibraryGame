package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import com.vianavitor.simplelibrarygame.repository.aux.LoggableUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, LoggableUser {
}
