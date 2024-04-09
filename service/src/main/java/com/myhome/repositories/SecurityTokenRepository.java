package com.myhome.repositories;

import com.myhome.domain.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * extends the JpaRepository interface and provides a way to interact with SecurityTokens
 * in a Spring Data JPA environment.
 */
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {
}
