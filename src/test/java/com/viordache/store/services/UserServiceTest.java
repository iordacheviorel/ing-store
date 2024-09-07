package com.viordache.store.services;

import com.viordache.store.dtos.RegisterUserDto;
import com.viordache.store.entities.Role;
import com.viordache.store.entities.RoleEnum;
import com.viordache.store.entities.User;
import com.viordache.store.repositories.RoleRepository;
import com.viordache.store.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allUsers_shouldReturnListOfUsers() {
        // setup
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // execute
        List<User> result = userService.allUsers();

        // verify
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createAdmin_whenRoleDoesNotExist_shouldReturnNull() {
        // setup
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setFullName("Admin User");
        registerUserDto.setEmail("admin@example.com");
        registerUserDto.setPassword("password");

        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.empty());

        // execute
        User result = userService.createAdmin(registerUserDto);

        // verify
        assertNull(result);
        verify(roleRepository, times(1)).findByName(RoleEnum.ADMIN);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createAdmin_whenRoleExists_shouldCreateAndReturnAdminUser() {
        // setup
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setFullName("Admin User");
        registerUserDto.setEmail("admin@example.com");
        registerUserDto.setPassword("password");

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ADMIN);

        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1); // mock ID after saving
        savedUser.setFullName("Admin User");
        savedUser.setEmail("admin@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(adminRole);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // execute
        User result = userService.createAdmin(registerUserDto);

        // verify
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.getId(), "The user ID should be 1");
        assertEquals("Admin User", result.getFullName(), "The full name should be 'Admin User'");
        assertEquals("admin@example.com", result.getEmail(), "The email should be 'admin@example.com'");
        assertEquals("encodedPassword", result.getPassword(), "The password should be 'encodedPassword'");
        assertEquals(adminRole, result.getRole(), "The role should match the ADMIN role");

        verify(roleRepository, times(1)).findByName(RoleEnum.ADMIN);
        verify(passwordEncoder, times(1)).encode(registerUserDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}