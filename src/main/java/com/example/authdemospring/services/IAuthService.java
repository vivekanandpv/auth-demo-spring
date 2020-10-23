package com.example.authdemospring.services;

import com.example.authdemospring.viewmodels.UserLoginViewModel;
import com.example.authdemospring.viewmodels.UserRegisterViewModel;
import com.example.authdemospring.viewmodels.UserTokenViewModel;

public interface IAuthService {
    UserTokenViewModel login(UserLoginViewModel loginViewModel);
    void logout(UserTokenViewModel tokenViewModel);
    void createUser(UserRegisterViewModel registerViewModel);
    boolean authenticate(UserTokenViewModel tokenViewModel);
    boolean authorize(UserTokenViewModel tokenViewModel, String role);
}
