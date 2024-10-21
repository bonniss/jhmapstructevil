package ai.realworld.domain;

import static ai.realworld.domain.AlMemTierViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMemTierViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMemTierVi.class);
        AlMemTierVi alMemTierVi1 = getAlMemTierViSample1();
        AlMemTierVi alMemTierVi2 = new AlMemTierVi();
        assertThat(alMemTierVi1).isNotEqualTo(alMemTierVi2);

        alMemTierVi2.setId(alMemTierVi1.getId());
        assertThat(alMemTierVi1).isEqualTo(alMemTierVi2);

        alMemTierVi2 = getAlMemTierViSample2();
        assertThat(alMemTierVi1).isNotEqualTo(alMemTierVi2);
    }

    @Test
    void applicationTest() {
        AlMemTierVi alMemTierVi = getAlMemTierViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alMemTierVi.setApplication(johnLennonBack);
        assertThat(alMemTierVi.getApplication()).isEqualTo(johnLennonBack);

        alMemTierVi.application(null);
        assertThat(alMemTierVi.getApplication()).isNull();
    }
}
