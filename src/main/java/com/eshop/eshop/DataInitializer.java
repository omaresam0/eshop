package com.eshop.eshop;

import com.eshop.eshop.model.Role;
import com.eshop.eshop.model.User;
import com.eshop.eshop.repository.RoleRepository;
import com.eshop.eshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
       Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNoExists();
        createDefaultAdminIfNoExists();
        createDefaultRoleIfNotExists(defaultRoles);

    }

    private void createDefaultUserIfNoExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i = 1; i<5; i++){
            String defaultEmail = "user" + i + "@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFname("User");
            user.setLName(""+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default users " + i + "created successfully");
        }
    }

    private void createDefaultAdminIfNoExists(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for(int i = 1; i<5; i++){
            String defaultEmail = "admin" + i + "@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFname("Admin");
            user.setLName("Admin"+i);
            user.setEmail(defaultEmail);
            user.setRoles(Set.of(adminRole));

            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
            System.out.println("Default admin user " + i + "created successfully");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role->roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }
}
