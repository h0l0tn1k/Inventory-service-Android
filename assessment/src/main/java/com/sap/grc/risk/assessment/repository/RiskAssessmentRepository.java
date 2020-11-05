package com.sap.grc.risk.assessment.repository;

import com.sap.grc.risk.assessment.domain.RiskAssessment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RiskAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
}
