package ai.realworld.domain;

import static ai.realworld.domain.AlMenityViTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlMenityViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMenityVi.class);
        AlMenityVi alMenityVi1 = getAlMenityViSample1();
        AlMenityVi alMenityVi2 = new AlMenityVi();
        assertThat(alMenityVi1).isNotEqualTo(alMenityVi2);

        alMenityVi2.setId(alMenityVi1.getId());
        assertThat(alMenityVi1).isEqualTo(alMenityVi2);

        alMenityVi2 = getAlMenityViSample2();
        assertThat(alMenityVi1).isNotEqualTo(alMenityVi2);
    }

    @Test
    void applicationTest() {
        AlMenityVi alMenityVi = getAlMenityViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alMenityVi.setApplication(johnLennonBack);
        assertThat(alMenityVi.getApplication()).isEqualTo(johnLennonBack);

        alMenityVi.application(null);
        assertThat(alMenityVi.getApplication()).isNull();
    }

    @Test
    void propertyProfileTest() {
        AlMenityVi alMenityVi = getAlMenityViRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        alMenityVi.addPropertyProfile(alProProViBack);
        assertThat(alMenityVi.getPropertyProfiles()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getAmenities()).containsOnly(alMenityVi);

        alMenityVi.removePropertyProfile(alProProViBack);
        assertThat(alMenityVi.getPropertyProfiles()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getAmenities()).doesNotContain(alMenityVi);

        alMenityVi.propertyProfiles(new HashSet<>(Set.of(alProProViBack)));
        assertThat(alMenityVi.getPropertyProfiles()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getAmenities()).containsOnly(alMenityVi);

        alMenityVi.setPropertyProfiles(new HashSet<>());
        assertThat(alMenityVi.getPropertyProfiles()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getAmenities()).doesNotContain(alMenityVi);
    }
}
