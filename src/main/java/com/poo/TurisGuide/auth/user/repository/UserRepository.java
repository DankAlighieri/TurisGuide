package com.poo.TurisGuide.auth.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.poo.TurisGuide.auth.user.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserDetails findByLogin(String login);
}