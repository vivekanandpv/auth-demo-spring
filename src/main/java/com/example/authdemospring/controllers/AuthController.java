package com.example.authdemospring.controllers;

import com.example.authdemospring.services.AuthService;
import com.example.authdemospring.viewmodels.UserLoginViewModel;
import com.example.authdemospring.viewmodels.UserRegisterViewModel;
import com.example.authdemospring.viewmodels.UserTokenViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenViewModel> login(@RequestBody UserLoginViewModel loginViewModel) {
        return new ResponseEntity(this.authService.login(loginViewModel), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterViewModel viewModel) {
        this.authService.createUser(viewModel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("logout")
    public ResponseEntity logout(@RequestHeader("X-SCB-Username") String username,
                                 @RequestHeader("X-SCB-Token") String token) {
        UserTokenViewModel tokenViewModel = new UserTokenViewModel();
        tokenViewModel.setUsername(username);
        tokenViewModel.setToken(token);
        this.authService.logout(tokenViewModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    //  Custom exceptions can be used to fine-tune the response here
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleGeneralExceptions() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
