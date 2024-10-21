package ai.realworld.domain;

import static ai.realworld.domain.AlMenityTestSamples.*;
import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlMenityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMenity.class);
        AlMenity alMenity1 = getAlMenitySample1();
        AlMenity alMenity2 = new AlMenity();
        assertThat(alMenity1).isNotEqualTo(alMenity2);

        alMenity2.setId(alMenity1.getId());
        assertThat(alMenity1).isEqualTo(alMenity2);

        alMenity2 = getAlMenitySample2();
        assertThat(alMenity1).isNotEqualTo(alMenity2);
    }

    @Test
    void applicationTest() {
        AlMenity alMenity = getAlMenityRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alMenity.setApplication(johnLennonBack);
        assertThat(alMenity.getApplication()).isEqualTo(johnLennonBack);

        alMenity.application(null);
        assertThat(alMenity.getApplication()).isNull();
    }

    @Test
    void propertyProfileTest() {
        AlMenity alMenity = getAlMenityRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        alMenity.addPropertyProfile(alProProBack);
        assertThat(alMenity.getPropertyProfiles()).containsOnly(alProProBack);
        assertThat(alProProBack.getAmenities()).containsOnly(alMenity);

        alMenity.removePropertyProfile(alProProBack);
        assertThat(alMenity.getPropertyProfiles()).doesNotContain(alProProBack);
        assertThat(alProProBack.getAmenities()).doesNotContain(alMenity);

        alMenity.propertyProfiles(new HashSet<>(Set.of(alProProBack)));
        assertThat(alMenity.getPropertyProfiles()).containsOnly(alProProBack);
        assertThat(alProProBack.getAmenities()).containsOnly(alMenity);

        alMenity.setPropertyProfiles(new HashSet<>());
        assertThat(alMenity.getPropertyProfiles()).doesNotContain(alProProBack);
        assertThat(alProProBack.getAmenities()).doesNotContain(alMenity);
    }
}
