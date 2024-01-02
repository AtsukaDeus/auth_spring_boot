package com.base.authproject.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.authproject.jwt.JwtService;
import com.base.authproject.user.Role;
import com.base.authproject.user.UserMapper;
import com.base.authproject.user.UserRegister;
import com.base.authproject.user.UserRepository;
import com.base.authproject.user.UserResponse;
import com.base.authproject.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthResponse register(UserRegister userRegisterData){

        User user = User.builder()
            .username(userRegisterData.getUsername())
            .password(userRegisterData.getPassword())
            .role(Role.ROLE_USER) // USER or ADMIN
            .locked(false)
            .build();
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }



    public AuthResponse login(AuthRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        var user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        UserResponse userResponse = new UserMapper().toResponse(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .userResponse(userResponse)
                .build();

    }


    public boolean usernameAlreadyExists(UserRegister userRegisterData){

        var userFinded = userRepository.findByUsername(userRegisterData.getUsername())
                        .orElse(null); 

        return userFinded != null;
    }

}
