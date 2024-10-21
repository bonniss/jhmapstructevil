package ai.realworld.domain;

import static ai.realworld.domain.AlPounderTestSamples.*;
import static ai.realworld.domain.AlPowerShellTestSamples.*;
import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPowerShellTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPowerShell.class);
        AlPowerShell alPowerShell1 = getAlPowerShellSample1();
        AlPowerShell alPowerShell2 = new AlPowerShell();
        assertThat(alPowerShell1).isNotEqualTo(alPowerShell2);

        alPowerShell2.setId(alPowerShell1.getId());
        assertThat(alPowerShell1).isEqualTo(alPowerShell2);

        alPowerShell2 = getAlPowerShellSample2();
        assertThat(alPowerShell1).isNotEqualTo(alPowerShell2);
    }

    @Test
    void propertyProfileTest() {
        AlPowerShell alPowerShell = getAlPowerShellRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        alPowerShell.setPropertyProfile(alProProBack);
        assertThat(alPowerShell.getPropertyProfile()).isEqualTo(alProProBack);

        alPowerShell.propertyProfile(null);
        assertThat(alPowerShell.getPropertyProfile()).isNull();
    }

    @Test
    void attributeTermTest() {
        AlPowerShell alPowerShell = getAlPowerShellRandomSampleGenerator();
        AlPounder alPounderBack = getAlPounderRandomSampleGenerator();

        alPowerShell.setAttributeTerm(alPounderBack);
        assertThat(alPowerShell.getAttributeTerm()).isEqualTo(alPounderBack);

        alPowerShell.attributeTerm(null);
        assertThat(alPowerShell.getAttributeTerm()).isNull();
    }

    @Test
    void applicationTest() {
        AlPowerShell alPowerShell = getAlPowerShellRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPowerShell.setApplication(johnLennonBack);
        assertThat(alPowerShell.getApplication()).isEqualTo(johnLennonBack);

        alPowerShell.application(null);
        assertThat(alPowerShell.getApplication()).isNull();
    }
}
