package ai.realworld.domain;

import static ai.realworld.domain.AlPedroTaxTestSamples.*;
import static ai.realworld.domain.AlPounderTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPounderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPounder.class);
        AlPounder alPounder1 = getAlPounderSample1();
        AlPounder alPounder2 = new AlPounder();
        assertThat(alPounder1).isNotEqualTo(alPounder2);

        alPounder2.setId(alPounder1.getId());
        assertThat(alPounder1).isEqualTo(alPounder2);

        alPounder2 = getAlPounderSample2();
        assertThat(alPounder1).isNotEqualTo(alPounder2);
    }

    @Test
    void attributeTaxonomyTest() {
        AlPounder alPounder = getAlPounderRandomSampleGenerator();
        AlPedroTax alPedroTaxBack = getAlPedroTaxRandomSampleGenerator();

        alPounder.setAttributeTaxonomy(alPedroTaxBack);
        assertThat(alPounder.getAttributeTaxonomy()).isEqualTo(alPedroTaxBack);

        alPounder.attributeTaxonomy(null);
        assertThat(alPounder.getAttributeTaxonomy()).isNull();
    }

    @Test
    void applicationTest() {
        AlPounder alPounder = getAlPounderRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPounder.setApplication(johnLennonBack);
        assertThat(alPounder.getApplication()).isEqualTo(johnLennonBack);

        alPounder.application(null);
        assertThat(alPounder.getApplication()).isNull();
    }
}
