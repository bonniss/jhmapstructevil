package ai.realworld.domain;

import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AlInquiryTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlInquiryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlInquiry.class);
        AlInquiry alInquiry1 = getAlInquirySample1();
        AlInquiry alInquiry2 = new AlInquiry();
        assertThat(alInquiry1).isNotEqualTo(alInquiry2);

        alInquiry2.setId(alInquiry1.getId());
        assertThat(alInquiry1).isEqualTo(alInquiry2);

        alInquiry2 = getAlInquirySample2();
        assertThat(alInquiry1).isNotEqualTo(alInquiry2);
    }

    @Test
    void customerTest() {
        AlInquiry alInquiry = getAlInquiryRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alInquiry.setCustomer(alPacinoBack);
        assertThat(alInquiry.getCustomer()).isEqualTo(alPacinoBack);

        alInquiry.customer(null);
        assertThat(alInquiry.getCustomer()).isNull();
    }

    @Test
    void agencyTest() {
        AlInquiry alInquiry = getAlInquiryRandomSampleGenerator();
        AlApple alAppleBack = getAlAppleRandomSampleGenerator();

        alInquiry.setAgency(alAppleBack);
        assertThat(alInquiry.getAgency()).isEqualTo(alAppleBack);

        alInquiry.agency(null);
        assertThat(alInquiry.getAgency()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlInquiry alInquiry = getAlInquiryRandomSampleGenerator();
        EdSheeran edSheeranBack = getEdSheeranRandomSampleGenerator();

        alInquiry.setPersonInCharge(edSheeranBack);
        assertThat(alInquiry.getPersonInCharge()).isEqualTo(edSheeranBack);

        alInquiry.personInCharge(null);
        assertThat(alInquiry.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlInquiry alInquiry = getAlInquiryRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alInquiry.setApplication(johnLennonBack);
        assertThat(alInquiry.getApplication()).isEqualTo(johnLennonBack);

        alInquiry.application(null);
        assertThat(alInquiry.getApplication()).isNull();
    }
}
