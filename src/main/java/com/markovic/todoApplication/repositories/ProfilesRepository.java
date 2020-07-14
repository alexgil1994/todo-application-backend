package com.markovic.todoApplication.repositories;

import com.markovic.todoApplication.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends CrudRepository<Profile, Long> {

    Page<Profile> findAll(Pageable pageable);

}
