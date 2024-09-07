package com.viordache.store.bootstrap;

import com.viordache.store.entities.Role;
import com.viordache.store.entities.RoleEnum;
import com.viordache.store.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
    }

    private void loadRoles() {

        // TODO: read these from a properties file.
        RoleEnum[] roles = RoleEnum.values();
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.SUPER_ADMIN, "Super Administrator role"
        );

        Arrays.stream(roles).forEach(role -> {
            Optional<Role> roleOptional = roleRepository.findByName(role);

            if(roleOptional.isEmpty()) {
                Role roleToCreate = new Role();

                roleToCreate.setName(role);
                roleToCreate.setDescription(roleDescriptionMap.get(role));

                roleRepository.save(roleToCreate);
            }
        });
    }
}
