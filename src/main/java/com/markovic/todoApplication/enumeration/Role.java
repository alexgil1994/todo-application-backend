package com.markovic.todoApplication.enumeration;

import static com.markovic.todoApplication.constant.Authorities.*;

// Defining our business logic Role authorities and setting up basic methods to get them
// We are using the Roles Authorities from constant -> Authorities
// TODO: 9/6/2020 Changed to only have user and admin auth. The already saved users have the older authorities inside, even though i wont use them
public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);
    // Not using them
//    ROLE_HR(HR_AUTHORITIES),
//    ROLE_MANAGER(MANAGER_AUTHORITIES),
//    ROLE_SUPER_USER(SUPER_USER_AUTHORITIES);

    private final String[] authorities;

    // It gets -any number- (...) of authorities
    Role(String... authorities) {
        this.authorities = authorities;
    }

    // Return the authorities
    public String[] getAuthorities(){
        return authorities;
    }
}
