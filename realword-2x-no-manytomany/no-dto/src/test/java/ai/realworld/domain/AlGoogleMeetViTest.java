package ai.realworld.domain;

import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AlGoogleMeetViTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoogleMeetViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoogleMeetVi.class);
        AlGoogleMeetVi alGoogleMeetVi1 = getAlGoogleMeetViSample1();
        AlGoogleMeetVi alGoogleMeetVi2 = new AlGoogleMeetVi();
        assertThat(alGoogleMeetVi1).isNotEqualTo(alGoogleMeetVi2);

        alGoogleMeetVi2.setId(alGoogleMeetVi1.getId());
        assertThat(alGoogleMeetVi1).isEqualTo(alGoogleMeetVi2);

        alGoogleMeetVi2 = getAlGoogleMeetViSample2();
        assertThat(alGoogleMeetVi1).isNotEqualTo(alGoogleMeetVi2);
    }

    @Test
    void customerTest() {
        AlGoogleMeetVi alGoogleMeetVi = getAlGoogleMeetViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alGoogleMeetVi.setCustomer(alPacinoBack);
        assertThat(alGoogleMeetVi.getCustomer()).isEqualTo(alPacinoBack);

        alGoogleMeetVi.customer(null);
        assertThat(alGoogleMeetVi.getCustomer()).isNull();
    }

    @Test
    void agencyTest() {
        AlGoogleMeetVi alGoogleMeetVi = getAlGoogleMeetViRandomSampleGenerator();
        AlAppleVi alAppleViBack = getAlAppleViRandomSampleGenerator();

        alGoogleMeetVi.setAgency(alAppleViBack);
        assertThat(alGoogleMeetVi.getAgency()).isEqualTo(alAppleViBack);

        alGoogleMeetVi.agency(null);
        assertThat(alGoogleMeetVi.getAgency()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlGoogleMeetVi alGoogleMeetVi = getAlGoogleMeetViRandomSampleGenerator();
        EdSheeranVi edSheeranViBack = getEdSheeranViRandomSampleGenerator();

        alGoogleMeetVi.setPersonInCharge(edSheeranViBack);
        assertThat(alGoogleMeetVi.getPersonInCharge()).isEqualTo(edSheeranViBack);

        alGoogleMeetVi.personInCharge(null);
        assertThat(alGoogleMeetVi.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlGoogleMeetVi alGoogleMeetVi = getAlGoogleMeetViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alGoogleMeetVi.setApplication(johnLennonBack);
        assertThat(alGoogleMeetVi.getApplication()).isEqualTo(johnLennonBack);

        alGoogleMeetVi.application(null);
        assertThat(alGoogleMeetVi.getApplication()).isNull();
    }
}
