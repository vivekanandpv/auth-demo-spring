package com.example.authdemospring.services;

import com.example.authdemospring.models.User;
import com.example.authdemospring.repositories.AuthJpaRepository;
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
public class AuthService implements IAuthService {
    private final AuthJpaRepository authRepository;

    public AuthService(AuthJpaRepository authRepository) {
        this.authRepository = authRepository;
    }

    public UserTokenViewModel login(UserLoginViewModel loginViewModel){
        //  LoginFailedException
        User userDb=this.authRepository
                .findByUsername(loginViewModel.getUsername())
                .orElseThrow(() -> new RuntimeException("Login failed"));

        String hashedPassword = Hashing.sha512().hashString(loginViewModel.getPassword(), StandardCharsets.UTF_8).toString();

        if (userDb.getPasswordHash().equals(hashedPassword)) {
            userDb.setToken(UUID.randomUUID().toString());
            this.authRepository.saveAndFlush(userDb);

            UserTokenViewModel tokenViewModel = new UserTokenViewModel();
            tokenViewModel.setToken(userDb.getToken());
            tokenViewModel.setUsername(userDb.getUsername());
            tokenViewModel.setDisplayName(userDb.getDisplayName());
            tokenViewModel.setRoles(userDb.getRoles());

            return tokenViewModel;
        } else {
            throw new RuntimeException("Cannot process the request");
        }
    }

    public void logout(UserTokenViewModel tokenViewModel) {
        //  LogoutFailedException
        User userDb=this.authRepository
                .findByUsername(tokenViewModel.getUsername())
                .orElseThrow(() -> new RuntimeException("Login failed"));

        userDb.setToken(null);

        this.authRepository.saveAndFlush(userDb);
    }

    public void createUser(UserRegisterViewModel registerViewModel) {
        //  CreatePreventedException
        User user = new User();
        String roles = Arrays.stream(registerViewModel.getRoles())
                .collect(Collectors.joining(";"));
        String hashedPassword = Hashing.sha512().hashString(registerViewModel.getPassword(),
                StandardCharsets.UTF_8).toString();

        user.setRoles(roles);
        user.setPasswordHash(hashedPassword);
        user.setUsername(registerViewModel.getUsername());
        user.setDisplayName(registerViewModel.getDisplayName());

        this.authRepository.saveAndFlush(user);
    }

    public boolean authenticate(UserTokenViewModel tokenViewModel) {
        //  GeneralDataAccessException
        if (tokenViewModel.getUsername().isEmpty() || tokenViewModel.getToken().isEmpty()) {
            throw new RuntimeException("Authentication failure");
        }

        User userDb=this.authRepository
                .findByUsername(tokenViewModel.getUsername())
                .orElseThrow(() -> new RuntimeException("Authentication failed"));

        return userDb.getToken().equals(tokenViewModel.getToken());
    }

    public boolean authorize(UserTokenViewModel tokenViewModel, String role) {
        //  GeneralDataAccessException
        if (!this.authenticate(tokenViewModel)) {
            return false;
        }

        User userDb=this.authRepository
                .findByUsername(tokenViewModel.getUsername())
                .orElseThrow(() -> new RuntimeException("Authentication failed"));

        return Arrays.stream(userDb.getRoles()
                .split(";"))
                .anyMatch(r -> r.equals(role));
    }
}
