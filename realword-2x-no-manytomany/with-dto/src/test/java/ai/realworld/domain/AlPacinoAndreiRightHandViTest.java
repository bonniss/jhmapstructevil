package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoAndreiRightHandViTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AndreiRightHandViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoAndreiRightHandVi.class);
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi1 = getAlPacinoAndreiRightHandViSample1();
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi2 = new AlPacinoAndreiRightHandVi();
        assertThat(alPacinoAndreiRightHandVi1).isNotEqualTo(alPacinoAndreiRightHandVi2);

        alPacinoAndreiRightHandVi2.setId(alPacinoAndreiRightHandVi1.getId());
        assertThat(alPacinoAndreiRightHandVi1).isEqualTo(alPacinoAndreiRightHandVi2);

        alPacinoAndreiRightHandVi2 = getAlPacinoAndreiRightHandViSample2();
        assertThat(alPacinoAndreiRightHandVi1).isNotEqualTo(alPacinoAndreiRightHandVi2);
    }

    @Test
    void userTest() {
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi = getAlPacinoAndreiRightHandViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoAndreiRightHandVi.setUser(alPacinoBack);
        assertThat(alPacinoAndreiRightHandVi.getUser()).isEqualTo(alPacinoBack);

        alPacinoAndreiRightHandVi.user(null);
        assertThat(alPacinoAndreiRightHandVi.getUser()).isNull();
    }

    @Test
    void addressTest() {
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi = getAlPacinoAndreiRightHandViRandomSampleGenerator();
        AndreiRightHandVi andreiRightHandViBack = getAndreiRightHandViRandomSampleGenerator();

        alPacinoAndreiRightHandVi.setAddress(andreiRightHandViBack);
        assertThat(alPacinoAndreiRightHandVi.getAddress()).isEqualTo(andreiRightHandViBack);

        alPacinoAndreiRightHandVi.address(null);
        assertThat(alPacinoAndreiRightHandVi.getAddress()).isNull();
    }
}
