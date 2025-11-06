package com.poo.TurisGuide.auth.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserModel updateUser(UserModel updatedUser, UUID userId){
        UserModel existingUser = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("User not found: " + userId));

        // Atualiza apenas os campos que podem ser modificados
        existingUser.setPasswordHash(updatedUser.getPasswordHash());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDOB(updatedUser.getDOB());

        return userRepository.save(existingUser);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserModel> getUsersWithPagination(int start, int end) {
        Pageable pageable = PageRequest.of(start, end);
        return userRepository.findAll(pageable).getContent();
    }

    public UserModel getUserById(UUID userId){
        return userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("User not found: " + userId));
    }
}