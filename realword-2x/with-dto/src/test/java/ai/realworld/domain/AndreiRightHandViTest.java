package ai.realworld.domain;

import static ai.realworld.domain.AndreiRightHandViTestSamples.*;
import static ai.realworld.domain.AntonioBanderasViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AndreiRightHandViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AndreiRightHandVi.class);
        AndreiRightHandVi andreiRightHandVi1 = getAndreiRightHandViSample1();
        AndreiRightHandVi andreiRightHandVi2 = new AndreiRightHandVi();
        assertThat(andreiRightHandVi1).isNotEqualTo(andreiRightHandVi2);

        andreiRightHandVi2.setId(andreiRightHandVi1.getId());
        assertThat(andreiRightHandVi1).isEqualTo(andreiRightHandVi2);

        andreiRightHandVi2 = getAndreiRightHandViSample2();
        assertThat(andreiRightHandVi1).isNotEqualTo(andreiRightHandVi2);
    }

    @Test
    void countryTest() {
        AndreiRightHandVi andreiRightHandVi = getAndreiRightHandViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        andreiRightHandVi.setCountry(antonioBanderasViBack);
        assertThat(andreiRightHandVi.getCountry()).isEqualTo(antonioBanderasViBack);

        andreiRightHandVi.country(null);
        assertThat(andreiRightHandVi.getCountry()).isNull();
    }

    @Test
    void provinceTest() {
        AndreiRightHandVi andreiRightHandVi = getAndreiRightHandViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        andreiRightHandVi.setProvince(antonioBanderasViBack);
        assertThat(andreiRightHandVi.getProvince()).isEqualTo(antonioBanderasViBack);

        andreiRightHandVi.province(null);
        assertThat(andreiRightHandVi.getProvince()).isNull();
    }

    @Test
    void districtTest() {
        AndreiRightHandVi andreiRightHandVi = getAndreiRightHandViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        andreiRightHandVi.setDistrict(antonioBanderasViBack);
        assertThat(andreiRightHandVi.getDistrict()).isEqualTo(antonioBanderasViBack);

        andreiRightHandVi.district(null);
        assertThat(andreiRightHandVi.getDistrict()).isNull();
    }

    @Test
    void wardTest() {
        AndreiRightHandVi andreiRightHandVi = getAndreiRightHandViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        andreiRightHandVi.setWard(antonioBanderasViBack);
        assertThat(andreiRightHandVi.getWard()).isEqualTo(antonioBanderasViBack);

        andreiRightHandVi.ward(null);
        assertThat(andreiRightHandVi.getWard()).isNull();
    }
}
