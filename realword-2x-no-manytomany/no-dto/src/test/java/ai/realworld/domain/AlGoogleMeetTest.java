package ai.realworld.domain;

import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AlGoogleMeetTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoogleMeetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoogleMeet.class);
        AlGoogleMeet alGoogleMeet1 = getAlGoogleMeetSample1();
        AlGoogleMeet alGoogleMeet2 = new AlGoogleMeet();
        assertThat(alGoogleMeet1).isNotEqualTo(alGoogleMeet2);

        alGoogleMeet2.setId(alGoogleMeet1.getId());
        assertThat(alGoogleMeet1).isEqualTo(alGoogleMeet2);

        alGoogleMeet2 = getAlGoogleMeetSample2();
        assertThat(alGoogleMeet1).isNotEqualTo(alGoogleMeet2);
    }

    @Test
    void customerTest() {
        AlGoogleMeet alGoogleMeet = getAlGoogleMeetRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alGoogleMeet.setCustomer(alPacinoBack);
        assertThat(alGoogleMeet.getCustomer()).isEqualTo(alPacinoBack);

        alGoogleMeet.customer(null);
        assertThat(alGoogleMeet.getCustomer()).isNull();
    }

    @Test
    void agencyTest() {
        AlGoogleMeet alGoogleMeet = getAlGoogleMeetRandomSampleGenerator();
        AlApple alAppleBack = getAlAppleRandomSampleGenerator();

        alGoogleMeet.setAgency(alAppleBack);
        assertThat(alGoogleMeet.getAgency()).isEqualTo(alAppleBack);

        alGoogleMeet.agency(null);
        assertThat(alGoogleMeet.getAgency()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlGoogleMeet alGoogleMeet = getAlGoogleMeetRandomSampleGenerator();
        EdSheeran edSheeranBack = getEdSheeranRandomSampleGenerator();

        alGoogleMeet.setPersonInCharge(edSheeranBack);
        assertThat(alGoogleMeet.getPersonInCharge()).isEqualTo(edSheeranBack);

        alGoogleMeet.personInCharge(null);
        assertThat(alGoogleMeet.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlGoogleMeet alGoogleMeet = getAlGoogleMeetRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alGoogleMeet.setApplication(johnLennonBack);
        assertThat(alGoogleMeet.getApplication()).isEqualTo(johnLennonBack);

        alGoogleMeet.application(null);
        assertThat(alGoogleMeet.getApplication()).isNull();
    }
}
