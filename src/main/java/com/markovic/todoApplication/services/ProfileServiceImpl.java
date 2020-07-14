package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Profile;
import com.markovic.todoApplication.repositories.ProfilesRepository;
import com.markovic.todoApplication.v1.model.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfilesRepository profilesRepository;

    @Override
    public Profile getById(Long id) {
        try {
            Optional<Profile> optionalProfile = profilesRepository.findById(id);
            return optionalProfile.orElse(null);
        }catch (Exception e){
            throw e;
        }
    }

    // TODO: 7/14/2020 Implement the rest of the methods
    @Override
    public Profile addProfile(ProfileDTO profileDTO) {
        return null;
    }

    @Override
    public boolean deleteProfile(Long id) {
        return false;
    }

    @Override
    public boolean patchProfile(Long id, ProfileDTO profileDTO) {
        return false;
    }

    @Override
    public Page<Profile> getAllByPaging(Integer page) {
        return null;
    }

}
