package com.sap.grc.riskframework.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sap.grc.riskframework.web.rest.TestUtil;

public class RiskAssessmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiskAssessment.class);
        RiskAssessment riskAssessment1 = new RiskAssessment();
        riskAssessment1.setId(1L);
        RiskAssessment riskAssessment2 = new RiskAssessment();
        riskAssessment2.setId(riskAssessment1.getId());
        assertThat(riskAssessment1).isEqualTo(riskAssessment2);
        riskAssessment2.setId(2L);
        assertThat(riskAssessment1).isNotEqualTo(riskAssessment2);
        riskAssessment1.setId(null);
        assertThat(riskAssessment1).isNotEqualTo(riskAssessment2);
    }
}
