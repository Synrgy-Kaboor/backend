package com.synrgy.kaboor.backend.repository;

import com.synrgy.kaboor.backend.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        SELECT t FROM Token t 
        INNER JOIN User u ON t.user.id = u.id
        WHERE u.id = :userId AND t.expired = FALSE 
    """)
    List<Token> findAllByUser(Long userId);

    Optional<Token> findByToken(String token);

}
