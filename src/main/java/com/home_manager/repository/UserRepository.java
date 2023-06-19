package com.home_manager.repository;

import com.home_manager.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    User getUserByEmail(String email);

    User getUserById(long id);

    User getUserByResetPasswordToken(String token);

    @Query("SELECT u.resetPasswordToken FROM User u WHERE u.email = :email")
    String getResetPasswordTokenByEmail(@Param("email") String email);

    List<User> findAllByResetPasswordTokenNotNull();
}
