package ai.realworld.domain;

import static ai.realworld.domain.AlActisoViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlActisoViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlActisoVi.class);
        AlActisoVi alActisoVi1 = getAlActisoViSample1();
        AlActisoVi alActisoVi2 = new AlActisoVi();
        assertThat(alActisoVi1).isNotEqualTo(alActisoVi2);

        alActisoVi2.setId(alActisoVi1.getId());
        assertThat(alActisoVi1).isEqualTo(alActisoVi2);

        alActisoVi2 = getAlActisoViSample2();
        assertThat(alActisoVi1).isNotEqualTo(alActisoVi2);
    }

    @Test
    void applicationTest() {
        AlActisoVi alActisoVi = getAlActisoViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alActisoVi.setApplication(johnLennonBack);
        assertThat(alActisoVi.getApplication()).isEqualTo(johnLennonBack);

        alActisoVi.application(null);
        assertThat(alActisoVi.getApplication()).isNull();
    }
}
