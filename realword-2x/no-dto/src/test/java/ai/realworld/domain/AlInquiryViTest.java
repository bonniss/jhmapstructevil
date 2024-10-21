package ai.realworld.domain;

import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AlInquiryViTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlInquiryViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlInquiryVi.class);
        AlInquiryVi alInquiryVi1 = getAlInquiryViSample1();
        AlInquiryVi alInquiryVi2 = new AlInquiryVi();
        assertThat(alInquiryVi1).isNotEqualTo(alInquiryVi2);

        alInquiryVi2.setId(alInquiryVi1.getId());
        assertThat(alInquiryVi1).isEqualTo(alInquiryVi2);

        alInquiryVi2 = getAlInquiryViSample2();
        assertThat(alInquiryVi1).isNotEqualTo(alInquiryVi2);
    }

    @Test
    void customerTest() {
        AlInquiryVi alInquiryVi = getAlInquiryViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alInquiryVi.setCustomer(alPacinoBack);
        assertThat(alInquiryVi.getCustomer()).isEqualTo(alPacinoBack);

        alInquiryVi.customer(null);
        assertThat(alInquiryVi.getCustomer()).isNull();
    }

    @Test
    void agencyTest() {
        AlInquiryVi alInquiryVi = getAlInquiryViRandomSampleGenerator();
        AlAppleVi alAppleViBack = getAlAppleViRandomSampleGenerator();

        alInquiryVi.setAgency(alAppleViBack);
        assertThat(alInquiryVi.getAgency()).isEqualTo(alAppleViBack);

        alInquiryVi.agency(null);
        assertThat(alInquiryVi.getAgency()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlInquiryVi alInquiryVi = getAlInquiryViRandomSampleGenerator();
        EdSheeranVi edSheeranViBack = getEdSheeranViRandomSampleGenerator();

        alInquiryVi.setPersonInCharge(edSheeranViBack);
        assertThat(alInquiryVi.getPersonInCharge()).isEqualTo(edSheeranViBack);

        alInquiryVi.personInCharge(null);
        assertThat(alInquiryVi.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlInquiryVi alInquiryVi = getAlInquiryViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alInquiryVi.setApplication(johnLennonBack);
        assertThat(alInquiryVi.getApplication()).isEqualTo(johnLennonBack);

        alInquiryVi.application(null);
        assertThat(alInquiryVi.getApplication()).isNull();
    }
}
