package ai.realworld.domain;

import static ai.realworld.domain.AlPedroTaxViTestSamples.*;
import static ai.realworld.domain.AlPounderViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPounderViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPounderVi.class);
        AlPounderVi alPounderVi1 = getAlPounderViSample1();
        AlPounderVi alPounderVi2 = new AlPounderVi();
        assertThat(alPounderVi1).isNotEqualTo(alPounderVi2);

        alPounderVi2.setId(alPounderVi1.getId());
        assertThat(alPounderVi1).isEqualTo(alPounderVi2);

        alPounderVi2 = getAlPounderViSample2();
        assertThat(alPounderVi1).isNotEqualTo(alPounderVi2);
    }

    @Test
    void attributeTaxonomyTest() {
        AlPounderVi alPounderVi = getAlPounderViRandomSampleGenerator();
        AlPedroTaxVi alPedroTaxViBack = getAlPedroTaxViRandomSampleGenerator();

        alPounderVi.setAttributeTaxonomy(alPedroTaxViBack);
        assertThat(alPounderVi.getAttributeTaxonomy()).isEqualTo(alPedroTaxViBack);

        alPounderVi.attributeTaxonomy(null);
        assertThat(alPounderVi.getAttributeTaxonomy()).isNull();
    }

    @Test
    void applicationTest() {
        AlPounderVi alPounderVi = getAlPounderViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPounderVi.setApplication(johnLennonBack);
        assertThat(alPounderVi.getApplication()).isEqualTo(johnLennonBack);

        alPounderVi.application(null);
        assertThat(alPounderVi.getApplication()).isNull();
    }
}
