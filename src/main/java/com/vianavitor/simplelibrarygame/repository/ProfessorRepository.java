package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.repository.aux.LoggableUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Long>, LoggableUser {
}
