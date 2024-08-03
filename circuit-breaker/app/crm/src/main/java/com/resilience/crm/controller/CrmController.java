package com.resilience.crm.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crm")
public class CrmController {

    @GetMapping
    @CircuitBreaker(name = "crm", fallbackMethod = "ciruitBreakerFallback")
    public String crm(){

        RestTemplate restTemplate = new RestTemplate();

        List<Map<String, Object>> clients = restTemplate.getForObject("http://localhost:8081/client", List.class);

        String crmMessage = clients.stream()
                .map(client -> "Proposta enviada ao cliente " + client.get("nome") + " no email " + client.get("email"))
                .collect(Collectors.joining("\n"));

        return crmMessage;
    }

    public String ciruitBreakerFallback(Exception e){

        return "Fallback: Serviço de Clientes está fora do ar";
    }
}
