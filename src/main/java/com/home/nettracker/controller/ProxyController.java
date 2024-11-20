package com.home.nettracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/proxy/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> proxyRequest(HttpServletRequest request, @RequestBody(required = false) String body) {
        String requestURI = request.getRequestURI();
        String targetUrl = "http://" + requestURI.substring("/proxy/".length()); // Target URL

        // Log the request details
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URL: " + targetUrl);
        System.out.println("Request Body: " + body);
        System.out.println("Request IP: " + request.getRemoteAddr());

        // Forward the request
        try {
            ResponseEntity<?> response = restTemplate.exchange(new URI(targetUrl), HttpMethod.valueOf(request.getMethod()), null, Object.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error forwarding request: " + e.getMessage());
        }
    }
}