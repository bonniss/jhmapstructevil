package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlPyuJokerViTestSamples.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuJokerViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuJokerVi.class);
        AlPyuJokerVi alPyuJokerVi1 = getAlPyuJokerViSample1();
        AlPyuJokerVi alPyuJokerVi2 = new AlPyuJokerVi();
        assertThat(alPyuJokerVi1).isNotEqualTo(alPyuJokerVi2);

        alPyuJokerVi2.setId(alPyuJokerVi1.getId());
        assertThat(alPyuJokerVi1).isEqualTo(alPyuJokerVi2);

        alPyuJokerVi2 = getAlPyuJokerViSample2();
        assertThat(alPyuJokerVi1).isNotEqualTo(alPyuJokerVi2);
    }

    @Test
    void customerTest() {
        AlPyuJokerVi alPyuJokerVi = getAlPyuJokerViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPyuJokerVi.setCustomer(alPacinoBack);
        assertThat(alPyuJokerVi.getCustomer()).isEqualTo(alPacinoBack);

        alPyuJokerVi.customer(null);
        assertThat(alPyuJokerVi.getCustomer()).isNull();
    }

    @Test
    void personInChargeTest() {
        AlPyuJokerVi alPyuJokerVi = getAlPyuJokerViRandomSampleGenerator();
        EdSheeranVi edSheeranViBack = getEdSheeranViRandomSampleGenerator();

        alPyuJokerVi.setPersonInCharge(edSheeranViBack);
        assertThat(alPyuJokerVi.getPersonInCharge()).isEqualTo(edSheeranViBack);

        alPyuJokerVi.personInCharge(null);
        assertThat(alPyuJokerVi.getPersonInCharge()).isNull();
    }

    @Test
    void applicationTest() {
        AlPyuJokerVi alPyuJokerVi = getAlPyuJokerViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPyuJokerVi.setApplication(johnLennonBack);
        assertThat(alPyuJokerVi.getApplication()).isEqualTo(johnLennonBack);

        alPyuJokerVi.application(null);
        assertThat(alPyuJokerVi.getApplication()).isNull();
    }
}
