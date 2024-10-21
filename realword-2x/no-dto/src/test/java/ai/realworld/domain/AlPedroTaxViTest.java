package ai.realworld.domain;

import static ai.realworld.domain.AlPedroTaxViTestSamples.*;
import static ai.realworld.domain.AlPounderViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlPedroTaxViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPedroTaxVi.class);
        AlPedroTaxVi alPedroTaxVi1 = getAlPedroTaxViSample1();
        AlPedroTaxVi alPedroTaxVi2 = new AlPedroTaxVi();
        assertThat(alPedroTaxVi1).isNotEqualTo(alPedroTaxVi2);

        alPedroTaxVi2.setId(alPedroTaxVi1.getId());
        assertThat(alPedroTaxVi1).isEqualTo(alPedroTaxVi2);

        alPedroTaxVi2 = getAlPedroTaxViSample2();
        assertThat(alPedroTaxVi1).isNotEqualTo(alPedroTaxVi2);
    }

    @Test
    void applicationTest() {
        AlPedroTaxVi alPedroTaxVi = getAlPedroTaxViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPedroTaxVi.setApplication(johnLennonBack);
        assertThat(alPedroTaxVi.getApplication()).isEqualTo(johnLennonBack);

        alPedroTaxVi.application(null);
        assertThat(alPedroTaxVi.getApplication()).isNull();
    }

    @Test
    void attributeTermsTest() {
        AlPedroTaxVi alPedroTaxVi = getAlPedroTaxViRandomSampleGenerator();
        AlPounderVi alPounderViBack = getAlPounderViRandomSampleGenerator();

        alPedroTaxVi.addAttributeTerms(alPounderViBack);
        assertThat(alPedroTaxVi.getAttributeTerms()).containsOnly(alPounderViBack);
        assertThat(alPounderViBack.getAttributeTaxonomy()).isEqualTo(alPedroTaxVi);

        alPedroTaxVi.removeAttributeTerms(alPounderViBack);
        assertThat(alPedroTaxVi.getAttributeTerms()).doesNotContain(alPounderViBack);
        assertThat(alPounderViBack.getAttributeTaxonomy()).isNull();

        alPedroTaxVi.attributeTerms(new HashSet<>(Set.of(alPounderViBack)));
        assertThat(alPedroTaxVi.getAttributeTerms()).containsOnly(alPounderViBack);
        assertThat(alPounderViBack.getAttributeTaxonomy()).isEqualTo(alPedroTaxVi);

        alPedroTaxVi.setAttributeTerms(new HashSet<>());
        assertThat(alPedroTaxVi.getAttributeTerms()).doesNotContain(alPounderViBack);
        assertThat(alPounderViBack.getAttributeTaxonomy()).isNull();
    }
}
