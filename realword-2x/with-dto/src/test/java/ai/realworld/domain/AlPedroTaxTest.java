package ai.realworld.domain;

import static ai.realworld.domain.AlPedroTaxTestSamples.*;
import static ai.realworld.domain.AlPounderTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlPedroTaxTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPedroTax.class);
        AlPedroTax alPedroTax1 = getAlPedroTaxSample1();
        AlPedroTax alPedroTax2 = new AlPedroTax();
        assertThat(alPedroTax1).isNotEqualTo(alPedroTax2);

        alPedroTax2.setId(alPedroTax1.getId());
        assertThat(alPedroTax1).isEqualTo(alPedroTax2);

        alPedroTax2 = getAlPedroTaxSample2();
        assertThat(alPedroTax1).isNotEqualTo(alPedroTax2);
    }

    @Test
    void applicationTest() {
        AlPedroTax alPedroTax = getAlPedroTaxRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPedroTax.setApplication(johnLennonBack);
        assertThat(alPedroTax.getApplication()).isEqualTo(johnLennonBack);

        alPedroTax.application(null);
        assertThat(alPedroTax.getApplication()).isNull();
    }

    @Test
    void attributeTermsTest() {
        AlPedroTax alPedroTax = getAlPedroTaxRandomSampleGenerator();
        AlPounder alPounderBack = getAlPounderRandomSampleGenerator();

        alPedroTax.addAttributeTerms(alPounderBack);
        assertThat(alPedroTax.getAttributeTerms()).containsOnly(alPounderBack);
        assertThat(alPounderBack.getAttributeTaxonomy()).isEqualTo(alPedroTax);

        alPedroTax.removeAttributeTerms(alPounderBack);
        assertThat(alPedroTax.getAttributeTerms()).doesNotContain(alPounderBack);
        assertThat(alPounderBack.getAttributeTaxonomy()).isNull();

        alPedroTax.attributeTerms(new HashSet<>(Set.of(alPounderBack)));
        assertThat(alPedroTax.getAttributeTerms()).containsOnly(alPounderBack);
        assertThat(alPounderBack.getAttributeTaxonomy()).isEqualTo(alPedroTax);

        alPedroTax.setAttributeTerms(new HashSet<>());
        assertThat(alPedroTax.getAttributeTerms()).doesNotContain(alPounderBack);
        assertThat(alPounderBack.getAttributeTaxonomy()).isNull();
    }
}
