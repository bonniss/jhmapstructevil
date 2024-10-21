package ai.realworld.domain;

import static ai.realworld.domain.AlPounderViTestSamples.*;
import static ai.realworld.domain.AlPowerShellViTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPowerShellViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPowerShellVi.class);
        AlPowerShellVi alPowerShellVi1 = getAlPowerShellViSample1();
        AlPowerShellVi alPowerShellVi2 = new AlPowerShellVi();
        assertThat(alPowerShellVi1).isNotEqualTo(alPowerShellVi2);

        alPowerShellVi2.setId(alPowerShellVi1.getId());
        assertThat(alPowerShellVi1).isEqualTo(alPowerShellVi2);

        alPowerShellVi2 = getAlPowerShellViSample2();
        assertThat(alPowerShellVi1).isNotEqualTo(alPowerShellVi2);
    }

    @Test
    void propertyProfileTest() {
        AlPowerShellVi alPowerShellVi = getAlPowerShellViRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        alPowerShellVi.setPropertyProfile(alProProViBack);
        assertThat(alPowerShellVi.getPropertyProfile()).isEqualTo(alProProViBack);

        alPowerShellVi.propertyProfile(null);
        assertThat(alPowerShellVi.getPropertyProfile()).isNull();
    }

    @Test
    void attributeTermTest() {
        AlPowerShellVi alPowerShellVi = getAlPowerShellViRandomSampleGenerator();
        AlPounderVi alPounderViBack = getAlPounderViRandomSampleGenerator();

        alPowerShellVi.setAttributeTerm(alPounderViBack);
        assertThat(alPowerShellVi.getAttributeTerm()).isEqualTo(alPounderViBack);

        alPowerShellVi.attributeTerm(null);
        assertThat(alPowerShellVi.getAttributeTerm()).isNull();
    }

    @Test
    void applicationTest() {
        AlPowerShellVi alPowerShellVi = getAlPowerShellViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPowerShellVi.setApplication(johnLennonBack);
        assertThat(alPowerShellVi.getApplication()).isEqualTo(johnLennonBack);

        alPowerShellVi.application(null);
        assertThat(alPowerShellVi.getApplication()).isNull();
    }
}
