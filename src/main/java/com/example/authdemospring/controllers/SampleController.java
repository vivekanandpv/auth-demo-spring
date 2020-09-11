package com.example.authdemospring.controllers;

import com.example.authdemospring.services.AuthService;
import com.example.authdemospring.viewmodels.UserTokenViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/sample")
public class SampleController {
    private final AuthService authService;

    public SampleController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admin-only/{id}")
    public ResponseEntity adminOnly(@RequestHeader("X-SCB-Username") String username,
                                    @RequestHeader("X-SCB-Token") String token, @PathVariable int id) {
        UserTokenViewModel tokenViewModel = new UserTokenViewModel(username, token);
        if (this.authService.authorize(tokenViewModel, "admin")) {
                // Business logic
            return new ResponseEntity("Admin path matched for id: " + id, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/authenticated-only/{id}")
    public ResponseEntity authenticatedOnly(@RequestHeader("X-SCB-Username") String username,
                            @RequestHeader("X-SCB-Token") String token, @PathVariable int id) {
        UserTokenViewModel tokenViewModel = new UserTokenViewModel(username, token);
        if (this.authService.authenticate(tokenViewModel)) {
            //Business logic
            return new ResponseEntity("Authenticated path matched for id: " + id, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
