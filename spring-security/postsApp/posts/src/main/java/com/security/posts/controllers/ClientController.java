package com.security.posts.controllers;

import com.security.posts.controllers.dtos.CreateClient;
import com.security.posts.entities.Client;
import com.security.posts.entities.Role;
import com.security.posts.repositories.ClientRepository;
import com.security.posts.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientController(ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> newClient(@RequestBody CreateClient dto){

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var clientFromDb = clientRepository.findByName(dto.clientname());

        if(clientFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var client = new Client();
        client.setName(dto.clientname());
        client.setPassword(this.passwordEncoder.encode(dto.password()));
        client.setRoles(Set.of(basicRole));

        this.clientRepository.save(client);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Client>> listClients(){

        var clients = this.clientRepository.findAll();
        return ResponseEntity.ok(clients);
    }
}
