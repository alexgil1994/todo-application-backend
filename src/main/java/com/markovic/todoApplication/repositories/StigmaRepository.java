package com.markovic.todoApplication.repositories;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StigmaRepository extends CrudRepository<Stigma, Long> {

    @Query
    Set<Stigma> getStigmaListByUserIs(User user);

}