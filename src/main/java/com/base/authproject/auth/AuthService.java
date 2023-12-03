package com.base.authproject.auth;

import org.springframework.stereotype.Service;

import com.base.authproject.configurations.Sha256;
import com.base.authproject.jwt.JwtService;
import com.base.authproject.role.Role;
import com.base.authproject.user.UserMapper;
import com.base.authproject.user.UserRegister;
import com.base.authproject.user.UserRepository;
import com.base.authproject.user.UserResponse;
import com.base.authproject.user._User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public AuthResponse register(UserRegister userRegisterData){

        _User user = _User.builder()
            .username(userRegisterData.getUsername())
            .password(userRegisterData.getPassword())
            .role(Role.USER) // USER or ADMIN
            .locked(false)
            .build();
        
        user.setPassword(Sha256.hashString(user.getPassword()));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }



    public AuthResponse login(AuthRequest authRequest){

        var user = userRepository.findByUsername(authRequest.getUsername())
                        .orElseThrow(null);
        
        if(
            user == null ||
            !(   
                user.getUsername().equals(authRequest.getUsername()) &&
                user.getPassword().equals(
                    Sha256.hashString(authRequest.getPassword())
                )
            )
        ) return null;

        if(user.isAccountNonLocked()){
            var jwtToken = jwtService.generateToken(user);

            UserResponse userResponse = new UserMapper().toResponse(user);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .userResponse(userResponse)
                    .build();
        
        } else{
            return null;
        }

    }


    public boolean usernameAlreadyExists(UserRegister userRegisterData){

        var userFinded = userRepository.findByUsername(userRegisterData.getUsername())
                        .orElse(null); 

        return userFinded != null;
    }

}
