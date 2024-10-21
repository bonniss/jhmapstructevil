package ai.realworld.domain;

import static ai.realworld.domain.AlActisoTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlActisoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlActiso.class);
        AlActiso alActiso1 = getAlActisoSample1();
        AlActiso alActiso2 = new AlActiso();
        assertThat(alActiso1).isNotEqualTo(alActiso2);

        alActiso2.setId(alActiso1.getId());
        assertThat(alActiso1).isEqualTo(alActiso2);

        alActiso2 = getAlActisoSample2();
        assertThat(alActiso1).isNotEqualTo(alActiso2);
    }

    @Test
    void applicationTest() {
        AlActiso alActiso = getAlActisoRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alActiso.setApplication(johnLennonBack);
        assertThat(alActiso.getApplication()).isEqualTo(johnLennonBack);

        alActiso.application(null);
        assertThat(alActiso.getApplication()).isNull();
    }
}
