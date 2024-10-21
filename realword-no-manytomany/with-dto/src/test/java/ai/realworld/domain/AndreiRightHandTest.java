package ai.realworld.domain;

import static ai.realworld.domain.AndreiRightHandTestSamples.*;
import static ai.realworld.domain.AntonioBanderasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AndreiRightHandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AndreiRightHand.class);
        AndreiRightHand andreiRightHand1 = getAndreiRightHandSample1();
        AndreiRightHand andreiRightHand2 = new AndreiRightHand();
        assertThat(andreiRightHand1).isNotEqualTo(andreiRightHand2);

        andreiRightHand2.setId(andreiRightHand1.getId());
        assertThat(andreiRightHand1).isEqualTo(andreiRightHand2);

        andreiRightHand2 = getAndreiRightHandSample2();
        assertThat(andreiRightHand1).isNotEqualTo(andreiRightHand2);
    }

    @Test
    void countryTest() {
        AndreiRightHand andreiRightHand = getAndreiRightHandRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        andreiRightHand.setCountry(antonioBanderasBack);
        assertThat(andreiRightHand.getCountry()).isEqualTo(antonioBanderasBack);

        andreiRightHand.country(null);
        assertThat(andreiRightHand.getCountry()).isNull();
    }

    @Test
    void provinceTest() {
        AndreiRightHand andreiRightHand = getAndreiRightHandRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        andreiRightHand.setProvince(antonioBanderasBack);
        assertThat(andreiRightHand.getProvince()).isEqualTo(antonioBanderasBack);

        andreiRightHand.province(null);
        assertThat(andreiRightHand.getProvince()).isNull();
    }

    @Test
    void districtTest() {
        AndreiRightHand andreiRightHand = getAndreiRightHandRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        andreiRightHand.setDistrict(antonioBanderasBack);
        assertThat(andreiRightHand.getDistrict()).isEqualTo(antonioBanderasBack);

        andreiRightHand.district(null);
        assertThat(andreiRightHand.getDistrict()).isNull();
    }

    @Test
    void wardTest() {
        AndreiRightHand andreiRightHand = getAndreiRightHandRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        andreiRightHand.setWard(antonioBanderasBack);
        assertThat(andreiRightHand.getWard()).isEqualTo(antonioBanderasBack);

        andreiRightHand.ward(null);
        assertThat(andreiRightHand.getWard()).isNull();
    }
}
