package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlPyuJokerTestSamples.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuJokerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuJoker.class);
        AlPyuJoker alPyuJoker1 = getAlPyuJokerSample1();
        AlPyuJoker alPyuJoker2 = new AlPyuJoker();
        assertThat(alPyuJoker1).isNotEqualTo(alPyuJoker2);

        alPyuJoker2.setId(alPyuJoker1.getId());
        assertThat(alPyuJoker1).isEqualTo(alPyuJoker2);

        alPyuJoker2 = getAlPyuJokerSample2();
        assertThat(alPyuJoker1).isNotEqualTo(alPyuJoker2);
    }

    @Test
    void customerTest() {
        AlPyuJoker alPyuJoker = getAlPyuJokerRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPyuJoker.setCustomer(alPacinoBack);
        assertThat(alPyuJoker.getCustomer()).isEqualTo(alPacinoBack);

        alPyuJoker.customer(null);
        assertThat(alPyuJoker.getCustomer()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlPyuJoker alPyuJoker = getAlPyuJokerRandomSampleGenerator();
        EdSheeran edSheeranBack = getEdSheeranRandomSampleGenerator();

        alPyuJoker.setPersonInCharge(edSheeranBack);
        assertThat(alPyuJoker.getPersonInCharge()).isEqualTo(edSheeranBack);

        alPyuJoker.personInCharge(null);
        assertThat(alPyuJoker.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlPyuJoker alPyuJoker = getAlPyuJokerRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPyuJoker.setApplication(johnLennonBack);
        assertThat(alPyuJoker.getApplication()).isEqualTo(johnLennonBack);

        alPyuJoker.application(null);
        assertThat(alPyuJoker.getApplication()).isNull();
    }
}
