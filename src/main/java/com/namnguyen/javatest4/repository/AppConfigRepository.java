package com.namnguyen.javatest4.repository;

import com.namnguyen.javatest4.domain.AppConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {}
