package com.sap.grc.risk.template.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sap.grc.risk.template.web.rest.TestUtil;

public class RiskTemplateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiskTemplate.class);
        RiskTemplate riskTemplate1 = new RiskTemplate();
        riskTemplate1.setId(1L);
        RiskTemplate riskTemplate2 = new RiskTemplate();
        riskTemplate2.setId(riskTemplate1.getId());
        assertThat(riskTemplate1).isEqualTo(riskTemplate2);
        riskTemplate2.setId(2L);
        assertThat(riskTemplate1).isNotEqualTo(riskTemplate2);
        riskTemplate1.setId(null);
        assertThat(riskTemplate1).isNotEqualTo(riskTemplate2);
    }
}
