package com.sap.grc.riskframework.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sap.grc.riskframework.web.rest.TestUtil;

public class AnalysisValuesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisValues.class);
        AnalysisValues analysisValues1 = new AnalysisValues();
        analysisValues1.setId(1L);
        AnalysisValues analysisValues2 = new AnalysisValues();
        analysisValues2.setId(analysisValues1.getId());
        assertThat(analysisValues1).isEqualTo(analysisValues2);
        analysisValues2.setId(2L);
        assertThat(analysisValues1).isNotEqualTo(analysisValues2);
        analysisValues1.setId(null);
        assertThat(analysisValues1).isNotEqualTo(analysisValues2);
    }
}
