package com.viordache.store.services;


import com.viordache.store.dtos.LoginUserDto;
import com.viordache.store.dtos.RegisterUserDto;
import com.viordache.store.entities.Role;
import com.viordache.store.entities.RoleEnum;
import com.viordache.store.entities.User;
import com.viordache.store.repositories.RoleRepository;
import com.viordache.store.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    public User signUp(RegisterUserDto registerUserDto) {

        LOGGER.info("Signing up user: {}", registerUserDto.email());
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            // TODO throw error when role is not found?
            LOGGER.info("USER Role not found");
            return null;
        }

        var user = new User();
        user.setFullName(registerUserDto.fullName());
        user.setEmail(registerUserDto.email());
        user.setPassword(passwordEncoder.encode(registerUserDto.password()));
        user.setRole(optionalRole.get());

        try {
            return userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Integrity violation while saving user: {}", registerUserDto.email());
            throw new DuplicateKeyException(e.getMessage());
        }
    }

    public User authenticate(LoginUserDto loginUserDto) {

        LOGGER.info("Authenticating user: {}", loginUserDto.email());
        authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
                loginUserDto.email(), loginUserDto.password())
        );

        return  userRepository.findByEmail(loginUserDto.email()).orElseThrow();
    }
}
