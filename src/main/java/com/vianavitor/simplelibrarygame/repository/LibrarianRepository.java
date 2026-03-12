package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Librarian;
import com.vianavitor.simplelibrarygame.repository.aux.LoggableUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LibrarianRepository extends CrudRepository<Librarian, Long>, LoggableUser {
}
