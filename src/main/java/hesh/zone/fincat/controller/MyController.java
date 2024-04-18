package hesh.zone.fincat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        String message = "Hello, World!";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        String errorMessage = "An error occurred!";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @GetMapping("/custom")
    public ResponseEntity<String> customResponse() {
        String customMessage = "This is a custom response!";
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(customMessage);
    }

    // Your other endpoint mappings and methods here
    @PostMapping("/upload")
    public ResponseEntity<String> respondWithAllCharges() {
        String customMessage = "This is a custom response!";
        return ResponseEntity.ok(customMessage);
    }
}
