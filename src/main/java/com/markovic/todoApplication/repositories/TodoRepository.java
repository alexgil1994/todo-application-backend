package com.markovic.todoApplication.repositories;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

    Optional<Todo> findById(Long id);

    Optional<Todo> findByUuid(String uuid);

    @Query
    Set<Todo> getTodoListByUserIs(User user);

}
