package com.sap.grc.riskframework.repository;

import com.sap.grc.riskframework.domain.AnalysisValues;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnalysisValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisValuesRepository extends JpaRepository<AnalysisValues, Long> {
}
