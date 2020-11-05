package com.sap.grc.risk.template.repository;

import com.sap.grc.risk.template.domain.RiskTemplate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RiskTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskTemplateRepository extends JpaRepository<RiskTemplate, Long> {
}
