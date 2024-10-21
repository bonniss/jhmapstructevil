package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoAndreiRightHandTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AndreiRightHandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoAndreiRightHand.class);
        AlPacinoAndreiRightHand alPacinoAndreiRightHand1 = getAlPacinoAndreiRightHandSample1();
        AlPacinoAndreiRightHand alPacinoAndreiRightHand2 = new AlPacinoAndreiRightHand();
        assertThat(alPacinoAndreiRightHand1).isNotEqualTo(alPacinoAndreiRightHand2);

        alPacinoAndreiRightHand2.setId(alPacinoAndreiRightHand1.getId());
        assertThat(alPacinoAndreiRightHand1).isEqualTo(alPacinoAndreiRightHand2);

        alPacinoAndreiRightHand2 = getAlPacinoAndreiRightHandSample2();
        assertThat(alPacinoAndreiRightHand1).isNotEqualTo(alPacinoAndreiRightHand2);
    }

    @Test
    void userTest() {
        AlPacinoAndreiRightHand alPacinoAndreiRightHand = getAlPacinoAndreiRightHandRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoAndreiRightHand.setUser(alPacinoBack);
        assertThat(alPacinoAndreiRightHand.getUser()).isEqualTo(alPacinoBack);

        alPacinoAndreiRightHand.user(null);
        assertThat(alPacinoAndreiRightHand.getUser()).isNull();
    }

    @Test
    void addressTest() {
        AlPacinoAndreiRightHand alPacinoAndreiRightHand = getAlPacinoAndreiRightHandRandomSampleGenerator();
        AndreiRightHand andreiRightHandBack = getAndreiRightHandRandomSampleGenerator();

        alPacinoAndreiRightHand.setAddress(andreiRightHandBack);
        assertThat(alPacinoAndreiRightHand.getAddress()).isEqualTo(andreiRightHandBack);

        alPacinoAndreiRightHand.address(null);
        assertThat(alPacinoAndreiRightHand.getAddress()).isNull();
    }
}
