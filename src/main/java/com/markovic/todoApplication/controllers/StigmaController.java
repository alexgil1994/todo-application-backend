package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.StigmaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(StigmaController.BASE_URL)
public class StigmaController {

    public static final String BASE_URL = "v1/stigma";

    @Autowired
    private StigmaServiceImpl stigmaService;

    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @DeleteMapping("/deleteStigmaByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam Long id, @RequestParam String username){
        stigmaService.deleteStigma(id, username);
    }

    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @GetMapping("/getAllStigmasByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Stigma> getStigmasByUsername(@RequestParam String username){
        return stigmaService.getStigmaListByUsername(username);
    }

}
