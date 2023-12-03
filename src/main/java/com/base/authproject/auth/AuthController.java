package com.base.authproject.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.authproject.user.UserRegister;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController{

        private final AuthService authService;


        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody UserRegister request){

            if(!authService.usernameAlreadyExists(request)){

                AuthResponse authResponse = authService.register(request);
                if(authResponse != null){
                    return new ResponseEntity<>("User created!", HttpStatus.CREATED);
                }
                else{
                    return new ResponseEntity<>("user not created...", HttpStatus.BAD_REQUEST);
                }

            } else{

                return new ResponseEntity<>("Error! the username already exists.", HttpStatus.BAD_REQUEST);
            }


        }
        
        
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthRequest request){

            AuthResponse authResponse = authService.login(request);
            if(authResponse == null){
                return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
            }
    
            return ResponseEntity.ok(authService.login(request));
        }
}
