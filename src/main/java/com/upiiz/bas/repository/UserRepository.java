package com.upiiz.bas.repository;

import com.upiiz.bas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Agrega esta línea si no estaba declarada
    Optional<User> findByEmail(String email);
}