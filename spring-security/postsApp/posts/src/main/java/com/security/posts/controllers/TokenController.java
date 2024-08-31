package com.security.posts.controllers;

import com.security.posts.controllers.dtos.LoginRequest;
import com.security.posts.controllers.dtos.LoginResponse;
import com.security.posts.repositories.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ClientRepository clientRepository;

    public TokenController(JwtEncoder jwtEncoder, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest){

        var client = this.clientRepository.findByName(loginRequest.userName());

        if (client.isEmpty() || !client.get().isLoginCorrect(loginRequest, this.passwordEncoder)){
            throw new BadCredentialsException("client name or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("PostsApp")
                .subject(client.get().getClientId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
