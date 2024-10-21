package ai.realworld.domain;

import static ai.realworld.domain.AlMemTierTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMemTierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMemTier.class);
        AlMemTier alMemTier1 = getAlMemTierSample1();
        AlMemTier alMemTier2 = new AlMemTier();
        assertThat(alMemTier1).isNotEqualTo(alMemTier2);

        alMemTier2.setId(alMemTier1.getId());
        assertThat(alMemTier1).isEqualTo(alMemTier2);

        alMemTier2 = getAlMemTierSample2();
        assertThat(alMemTier1).isNotEqualTo(alMemTier2);
    }

    @Test
    void applicationTest() {
        AlMemTier alMemTier = getAlMemTierRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alMemTier.setApplication(johnLennonBack);
        assertThat(alMemTier.getApplication()).isEqualTo(johnLennonBack);

        alMemTier.application(null);
        assertThat(alMemTier.getApplication()).isNull();
    }
}
