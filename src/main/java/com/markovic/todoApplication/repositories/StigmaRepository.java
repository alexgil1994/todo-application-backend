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

    // TODO: 8/16/2020 Check if it works
    @Query(value = "SELECT id FROM Stigma WHERE ip = ?1 AND user_id = ?2")
    Long getStigmaByIpAndUserIsOrderByIdDesc(String ip, User existingUser);
}
