package com.poo.TurisGuide.auth.user.service;

import org.springframework.stereotype.Service;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public UserModel saveUser(UserModel userModel) {
        return userRepository.save(userModel);
    }
}