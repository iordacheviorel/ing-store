package com.viordache.store.services;


import com.viordache.store.dtos.RegisterUserDTO;
import com.viordache.store.entities.Role;
import com.viordache.store.entities.RoleEnum;
import com.viordache.store.entities.User;
import com.viordache.store.repositories.RoleRepository;
import com.viordache.store.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        LOGGER.info("Retrieving all users");

        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User createAdmin(RegisterUserDTO registerUserDto) {
        LOGGER.info("Creating admin user {}", registerUserDto.email());
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        // TODO throw error?
        if (optionalRole.isEmpty()) {
            LOGGER.warn("Admin role not found");
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
            LOGGER.error("User already exists");
            throw new DuplicateKeyException(e.getMessage());
        }
    }
}
