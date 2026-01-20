package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
    select t from Token t inner join User u on t.user.id = u.id
    where u.id = :userId and (t.expired = 0 or t.revoked = 0)
    """)
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}
