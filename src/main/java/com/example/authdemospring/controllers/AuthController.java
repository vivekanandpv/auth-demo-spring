package com.example.authdemospring.controllers;

import com.example.authdemospring.services.AuthService;
import com.example.authdemospring.services.IAuthService;
import com.example.authdemospring.viewmodels.UserLoginViewModel;
import com.example.authdemospring.viewmodels.UserRegisterViewModel;
import com.example.authdemospring.viewmodels.UserTokenViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenViewModel> login(@RequestBody UserLoginViewModel loginViewModel) {
        return ResponseEntity.ok(this.authService.login(loginViewModel));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterViewModel viewModel) {
        this.authService.createUser(viewModel);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-SCB-Username") String username,
                                 @RequestHeader("X-SCB-Token") String token) {
        UserTokenViewModel tokenViewModel = new UserTokenViewModel();
        tokenViewModel.setUsername(username);
        tokenViewModel.setToken(token);
        this.authService.logout(tokenViewModel);

        return ResponseEntity.ok().build();
    }

    //  Custom exceptions can be used to fine-tune the response here
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGeneralExceptions() {
        return ResponseEntity.status(401).build();
    }
}
