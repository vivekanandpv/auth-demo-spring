package com.example.authdemospring.services;

import com.example.authdemospring.models.User;
import com.example.authdemospring.repositories.AuthRepository;
import com.example.authdemospring.viewmodels.UserLoginViewModel;
import com.example.authdemospring.viewmodels.UserRegisterViewModel;
import com.example.authdemospring.viewmodels.UserTokenViewModel;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public UserTokenViewModel login(UserLoginViewModel loginViewModel){
        User userDb=this.authRepository.getUser(loginViewModel.getUsername());
        String hashedPassword = Hashing.sha512().hashString(loginViewModel.getPassword(), StandardCharsets.UTF_8).toString();

        if (userDb.getPasswordHash().equals(hashedPassword)) {
            User loggedInUser = this.authRepository.login(loginViewModel.getUsername(), UUID.randomUUID().toString());
            UserTokenViewModel tokenViewModel = new UserTokenViewModel();
            tokenViewModel.setToken(loggedInUser.getToken());
            tokenViewModel.setUsername(loggedInUser.getUsername());

            return tokenViewModel;
        } else {
            throw new RuntimeException("Cannot process the request");
        }
    }

    public void logout(UserTokenViewModel tokenViewModel) {
        this.authRepository.logout(tokenViewModel.getUsername());
    }

    public void createUser(UserRegisterViewModel registerViewModel) {
        User user = new User();
        String roles = Arrays.stream(registerViewModel.getRoles())
                .collect(Collectors.joining(";"));
        String hashedPassword = Hashing.sha512().hashString(registerViewModel.getPassword(), StandardCharsets.UTF_8).toString();

        user.setRoles(roles);
        user.setPasswordHash(hashedPassword);
        user.setUsername(registerViewModel.getUsername());
        user.setDisplayName(registerViewModel.getDisplayName());

        this.authRepository.register(user);
    }

    public boolean authenticate(UserTokenViewModel tokenViewModel) {
        return this.authRepository.getLoginStatus(tokenViewModel.getUsername(), tokenViewModel.getToken());
    }

    public boolean authorize(UserTokenViewModel tokenViewModel, String role) {
        User userDb=this.authRepository.getUser(tokenViewModel.getUsername());

        return Arrays.stream(userDb.getRoles()
                .split(";"))
                .anyMatch(r -> r == role);
    }
}
