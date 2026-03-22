package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Classroom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends CrudRepository<Classroom, Long> {
    Optional<Classroom> findByPublicCode(String publicCode);

    Optional<Classroom> findByName(String name);
}
