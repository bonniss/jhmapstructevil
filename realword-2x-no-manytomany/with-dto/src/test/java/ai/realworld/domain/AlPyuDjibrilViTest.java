package ai.realworld.domain;

import static ai.realworld.domain.AlProtyViTestSamples.*;
import static ai.realworld.domain.AlPyuDjibrilViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuDjibrilVi.class);
        AlPyuDjibrilVi alPyuDjibrilVi1 = getAlPyuDjibrilViSample1();
        AlPyuDjibrilVi alPyuDjibrilVi2 = new AlPyuDjibrilVi();
        assertThat(alPyuDjibrilVi1).isNotEqualTo(alPyuDjibrilVi2);

        alPyuDjibrilVi2.setId(alPyuDjibrilVi1.getId());
        assertThat(alPyuDjibrilVi1).isEqualTo(alPyuDjibrilVi2);

        alPyuDjibrilVi2 = getAlPyuDjibrilViSample2();
        assertThat(alPyuDjibrilVi1).isNotEqualTo(alPyuDjibrilVi2);
    }

    @Test
    void propertyTest() {
        AlPyuDjibrilVi alPyuDjibrilVi = getAlPyuDjibrilViRandomSampleGenerator();
        AlProtyVi alProtyViBack = getAlProtyViRandomSampleGenerator();

        alPyuDjibrilVi.setProperty(alProtyViBack);
        assertThat(alPyuDjibrilVi.getProperty()).isEqualTo(alProtyViBack);

        alPyuDjibrilVi.property(null);
        assertThat(alPyuDjibrilVi.getProperty()).isNull();
    }

    @Test
    void applicationTest() {
        AlPyuDjibrilVi alPyuDjibrilVi = getAlPyuDjibrilViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPyuDjibrilVi.setApplication(johnLennonBack);
        assertThat(alPyuDjibrilVi.getApplication()).isEqualTo(johnLennonBack);

        alPyuDjibrilVi.application(null);
        assertThat(alPyuDjibrilVi.getApplication()).isNull();
    }
}
