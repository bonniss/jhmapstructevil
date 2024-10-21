package ai.realworld.domain;

import static ai.realworld.domain.AlProtyTestSamples.*;
import static ai.realworld.domain.AlPyuDjibrilTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuDjibril.class);
        AlPyuDjibril alPyuDjibril1 = getAlPyuDjibrilSample1();
        AlPyuDjibril alPyuDjibril2 = new AlPyuDjibril();
        assertThat(alPyuDjibril1).isNotEqualTo(alPyuDjibril2);

        alPyuDjibril2.setId(alPyuDjibril1.getId());
        assertThat(alPyuDjibril1).isEqualTo(alPyuDjibril2);

        alPyuDjibril2 = getAlPyuDjibrilSample2();
        assertThat(alPyuDjibril1).isNotEqualTo(alPyuDjibril2);
    }

    @Test
    void propertyTest() {
        AlPyuDjibril alPyuDjibril = getAlPyuDjibrilRandomSampleGenerator();
        AlProty alProtyBack = getAlProtyRandomSampleGenerator();

        alPyuDjibril.setProperty(alProtyBack);
        assertThat(alPyuDjibril.getProperty()).isEqualTo(alProtyBack);

        alPyuDjibril.property(null);
        assertThat(alPyuDjibril.getProperty()).isNull();
    }

    @Test
    void applicationTest() {
        AlPyuDjibril alPyuDjibril = getAlPyuDjibrilRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPyuDjibril.setApplication(johnLennonBack);
        assertThat(alPyuDjibril.getApplication()).isEqualTo(johnLennonBack);

        alPyuDjibril.application(null);
        assertThat(alPyuDjibril.getApplication()).isNull();
    }
}
