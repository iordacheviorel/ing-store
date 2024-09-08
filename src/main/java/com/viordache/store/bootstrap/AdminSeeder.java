package com.viordache.store.bootstrap;

import com.viordache.store.dtos.RegisterUserDto;
import com.viordache.store.entities.Role;
import com.viordache.store.entities.RoleEnum;
import com.viordache.store.entities.User;
import com.viordache.store.repositories.RoleRepository;
import com.viordache.store.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder  implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto(
                "Super Admin",
                "super.admin@mail.com",
                "123456");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setFullName(userDto.fullName());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
