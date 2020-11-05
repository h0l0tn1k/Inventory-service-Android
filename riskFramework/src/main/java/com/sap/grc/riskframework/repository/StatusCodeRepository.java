package com.sap.grc.riskframework.repository;

import com.sap.grc.riskframework.domain.StatusCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the StatusCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusCodeRepository extends JpaRepository<StatusCode, Long> {
}
