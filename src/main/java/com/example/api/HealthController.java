package com.example.api;

// import java.util.Map;
// import java.util.UUID;

// import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String healthCheck() {
        // HttpHeaders responseHeaders = new HttpHeaders();
        // responseHeaders.add("X-Report-ID", UUID.randomUUID().toString());
        return "Application is running.";
    }
}