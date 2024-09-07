package com.viordache.store.services;


import com.viordache.store.dtos.LoginUserDto;
import com.viordache.store.dtos.RegisterUserDto;
import com.viordache.store.entities.User;
import com.viordache.store.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(RegisterUserDto registerUserDto) {

        var user = new User();
        user.setFullName(registerUserDto.getFullName());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto loginUserDto) {

        authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        return  userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
    }
}
