package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.StigmaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(StigmaController.BASE_URL)
public class StigmaController {

    public static final String BASE_URL = "v1/stigma";

    @Autowired
    private StigmaServiceImpl stigmaService;


    @DeleteMapping("/deleteStigmaByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    public void delete(Authentication authentication, @RequestParam Long id){
        stigmaService.deleteStigma(id, authentication.getName());
    }

    // TODO: 8/8/2020 Could only be changed to not show all the user information that are attached to the Stigma's User
    @GetMapping("/getAllStigmasByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Stigma> getStigmasByUsername(Authentication authentication){
        return stigmaService.getStigmaListByUsername(authentication.getName());
    }

}
