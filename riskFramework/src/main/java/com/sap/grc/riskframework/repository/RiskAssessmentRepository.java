package com.sap.grc.riskframework.repository;

import com.sap.grc.riskframework.domain.RiskAssessment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RiskAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
}
