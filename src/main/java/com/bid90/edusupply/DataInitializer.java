package com.bid90.edusupply;

import com.bid90.edusupply.model.Group;
import com.bid90.edusupply.model.Role;
import com.bid90.edusupply.model.User;
import com.bid90.edusupply.repository.GroupRepository;
import com.bid90.edusupply.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer  implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer (UserRepository userRepository,
                            GroupRepository groupRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        var allGroup = groupRepository.findByName("ALL")
                .orElseGet(() -> {
                    var group = new Group();
                    group.setName("ALL");
                    group.setDescription("ALL is group that includes all other groups. This group cannot be deleted.");
                    group.setImmutable(true);
                    return groupRepository.save(group);
                });

        userRepository.findByEmail("admin@admin")
                .ifPresentOrElse(user -> {
                    if(!user.getRole().equals(Role.ADMIN)){
                        user.setRole(Role.ADMIN);
                        userRepository.save(user);
                    }
                },() -> {
                    User admin = new User();
                    admin.setName("Admin");
                    admin.setEmail("admin@admin");
                    admin.setPassword(passwordEncoder.encode("Admin123@")); // Schimbă parola default
                    admin.setEnabled(true);
                    admin.setRole(Role.ADMIN);
                    admin.getGroups().add(allGroup);
                    userRepository.save(admin);
                    System.out.println("Admin user created: admin@admin / Admin123@");
                });
    }
}