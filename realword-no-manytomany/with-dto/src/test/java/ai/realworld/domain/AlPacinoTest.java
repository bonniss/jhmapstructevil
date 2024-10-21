package ai.realworld.domain;

import static ai.realworld.domain.AlMemTierTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlVueVueUsageTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacino.class);
        AlPacino alPacino1 = getAlPacinoSample1();
        AlPacino alPacino2 = new AlPacino();
        assertThat(alPacino1).isNotEqualTo(alPacino2);

        alPacino2.setId(alPacino1.getId());
        assertThat(alPacino1).isEqualTo(alPacino2);

        alPacino2 = getAlPacinoSample2();
        assertThat(alPacino1).isNotEqualTo(alPacino2);
    }

    @Test
    void applicationTest() {
        AlPacino alPacino = getAlPacinoRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPacino.setApplication(johnLennonBack);
        assertThat(alPacino.getApplication()).isEqualTo(johnLennonBack);

        alPacino.application(null);
        assertThat(alPacino.getApplication()).isNull();
    }

    @Test
    void membershipTierTest() {
        AlPacino alPacino = getAlPacinoRandomSampleGenerator();
        AlMemTier alMemTierBack = getAlMemTierRandomSampleGenerator();

        alPacino.setMembershipTier(alMemTierBack);
        assertThat(alPacino.getMembershipTier()).isEqualTo(alMemTierBack);

        alPacino.membershipTier(null);
        assertThat(alPacino.getMembershipTier()).isNull();
    }

    @Test
    void alVueVueUsageTest() {
        AlPacino alPacino = getAlPacinoRandomSampleGenerator();
        AlVueVueUsage alVueVueUsageBack = getAlVueVueUsageRandomSampleGenerator();

        alPacino.setAlVueVueUsage(alVueVueUsageBack);
        assertThat(alPacino.getAlVueVueUsage()).isEqualTo(alVueVueUsageBack);

        alPacino.alVueVueUsage(null);
        assertThat(alPacino.getAlVueVueUsage()).isNull();
    }
}
