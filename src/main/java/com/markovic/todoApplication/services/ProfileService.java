package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Profile;
import com.markovic.todoApplication.v1.model.ProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {

    Profile getById(Long id);

    Profile addProfile(ProfileDTO profileDTO);

    boolean deleteProfile(Long id);

    boolean patchProfile(Long id, ProfileDTO profileDTO);

    Page<Profile> getAllByPaging(Integer page);

}
