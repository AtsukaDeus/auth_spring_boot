package com.base.authproject.user;

public class UserMapper {

    public UserResponse toResponse(_User user){

        return UserResponse.builder().username(user.getUsername()).build();                        
    }
}
