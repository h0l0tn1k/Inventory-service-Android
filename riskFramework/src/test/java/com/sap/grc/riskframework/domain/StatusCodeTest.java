package com.sap.grc.riskframework.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sap.grc.riskframework.web.rest.TestUtil;

public class StatusCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusCode.class);
        StatusCode statusCode1 = new StatusCode();
        statusCode1.setId(1L);
        StatusCode statusCode2 = new StatusCode();
        statusCode2.setId(statusCode1.getId());
        assertThat(statusCode1).isEqualTo(statusCode2);
        statusCode2.setId(2L);
        assertThat(statusCode1).isNotEqualTo(statusCode2);
        statusCode1.setId(null);
        assertThat(statusCode1).isNotEqualTo(statusCode2);
    }
}
