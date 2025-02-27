package org.example.letter.domain.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.example.letter.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
