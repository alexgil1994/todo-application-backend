package com.markovic.todoApplication.constant;

public class Authorities {

    // TODO: 8/9/2020 IpUser is mine check if it works
//    public static final String[] IP_USER_AUTHORITIES = { "user:guest" };
    public static final String[] USER_AUTHORITIES = { "user:read" };

    public static final String[] HR_AUTHORITIES = { "user:read", "user:update" };
    public static final String[] MANAGER_AUTHORITIES = { "user:read", "user:update" };
    public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update" };
    public static final String[] SUPER_USER_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete" };
}
