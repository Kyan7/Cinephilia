package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);
}
