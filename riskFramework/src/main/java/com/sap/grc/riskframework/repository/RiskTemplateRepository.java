package com.sap.grc.riskframework.repository;

import com.sap.grc.riskframework.domain.RiskTemplate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RiskTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskTemplateRepository extends JpaRepository<RiskTemplate, Long> {
}
