package com.poo.TurisGuide.auth.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poo.TurisGuide.auth.user.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {}