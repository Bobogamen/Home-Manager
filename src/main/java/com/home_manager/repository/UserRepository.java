package com.home_manager.repository;

import com.home_manager.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    User getUserByEmail(String email);

    User getUserByResetPasswordToken(String token);

    @Query(value = "SELECT reset_password_token FROM users WHERE users.email = ?1", nativeQuery = true)
    String getResetPasswordTokenByEmail(String email);
}
