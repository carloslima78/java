package com.security.posts.config;

import com.security.posts.entities.Client;
import com.security.posts.entities.Role;
import com.security.posts.repositories.ClientRepository;
import com.security.posts.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminClientConfig implements CommandLineRunner {

    private RoleRepository roleRepository;

    private ClientRepository clientRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public AdminClientConfig(RoleRepository roleRepository, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = this.roleRepository.findByName(Role.Values.ADMIN.name());

        var clientAdmin = this.clientRepository.findByName("admin");

        clientAdmin.ifPresentOrElse(
                client -> {
                    System.out.println("admin already exists");
                },
                () -> {
                    var client = new Client();
                    client.setName("admin");
                    client.setPassword(passwordEncoder.encode("123"));
                    client.setRoles(Set.of(roleAdmin));
                    this.clientRepository.save(client);
                }
        );
    }
}
