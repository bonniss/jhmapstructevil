package ai.realworld.domain;

import static ai.realworld.domain.AlPyuJokerTestSamples.*;
import static ai.realworld.domain.AlPyuThomasWayneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuThomasWayne.class);
        AlPyuThomasWayne alPyuThomasWayne1 = getAlPyuThomasWayneSample1();
        AlPyuThomasWayne alPyuThomasWayne2 = new AlPyuThomasWayne();
        assertThat(alPyuThomasWayne1).isNotEqualTo(alPyuThomasWayne2);

        alPyuThomasWayne2.setId(alPyuThomasWayne1.getId());
        assertThat(alPyuThomasWayne1).isEqualTo(alPyuThomasWayne2);

        alPyuThomasWayne2 = getAlPyuThomasWayneSample2();
        assertThat(alPyuThomasWayne1).isNotEqualTo(alPyuThomasWayne2);
    }

    @Test
    void bookingTest() {
        AlPyuThomasWayne alPyuThomasWayne = getAlPyuThomasWayneRandomSampleGenerator();
        AlPyuJoker alPyuJokerBack = getAlPyuJokerRandomSampleGenerator();

        alPyuThomasWayne.setBooking(alPyuJokerBack);
        assertThat(alPyuThomasWayne.getBooking()).isEqualTo(alPyuJokerBack);

        alPyuThomasWayne.booking(null);
        assertThat(alPyuThomasWayne.getBooking()).isNull();
    }
}
