package com.markovic.todoApplication.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProfilesController.BASE_URL)
public class ProfilesController {

    public static final String BASE_URL = "v1/profiles";

    // TODO: 7/14/2020 Implement Rest requests
}
