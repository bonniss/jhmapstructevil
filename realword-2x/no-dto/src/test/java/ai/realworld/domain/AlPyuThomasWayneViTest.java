package ai.realworld.domain;

import static ai.realworld.domain.AlPyuJokerViTestSamples.*;
import static ai.realworld.domain.AlPyuThomasWayneViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuThomasWayneVi.class);
        AlPyuThomasWayneVi alPyuThomasWayneVi1 = getAlPyuThomasWayneViSample1();
        AlPyuThomasWayneVi alPyuThomasWayneVi2 = new AlPyuThomasWayneVi();
        assertThat(alPyuThomasWayneVi1).isNotEqualTo(alPyuThomasWayneVi2);

        alPyuThomasWayneVi2.setId(alPyuThomasWayneVi1.getId());
        assertThat(alPyuThomasWayneVi1).isEqualTo(alPyuThomasWayneVi2);

        alPyuThomasWayneVi2 = getAlPyuThomasWayneViSample2();
        assertThat(alPyuThomasWayneVi1).isNotEqualTo(alPyuThomasWayneVi2);
    }

    @Test
    void bookingTest() {
        AlPyuThomasWayneVi alPyuThomasWayneVi = getAlPyuThomasWayneViRandomSampleGenerator();
        AlPyuJokerVi alPyuJokerViBack = getAlPyuJokerViRandomSampleGenerator();

        alPyuThomasWayneVi.setBooking(alPyuJokerViBack);
        assertThat(alPyuThomasWayneVi.getBooking()).isEqualTo(alPyuJokerViBack);

        alPyuThomasWayneVi.booking(null);
        assertThat(alPyuThomasWayneVi.getBooking()).isNull();
    }
}
