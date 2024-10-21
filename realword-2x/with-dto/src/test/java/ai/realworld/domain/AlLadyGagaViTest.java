package ai.realworld.domain;

import static ai.realworld.domain.AlLadyGagaViTestSamples.*;
import static ai.realworld.domain.AndreiRightHandViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLadyGagaViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLadyGagaVi.class);
        AlLadyGagaVi alLadyGagaVi1 = getAlLadyGagaViSample1();
        AlLadyGagaVi alLadyGagaVi2 = new AlLadyGagaVi();
        assertThat(alLadyGagaVi1).isNotEqualTo(alLadyGagaVi2);

        alLadyGagaVi2.setId(alLadyGagaVi1.getId());
        assertThat(alLadyGagaVi1).isEqualTo(alLadyGagaVi2);

        alLadyGagaVi2 = getAlLadyGagaViSample2();
        assertThat(alLadyGagaVi1).isNotEqualTo(alLadyGagaVi2);
    }

    @Test
    void addressTest() {
        AlLadyGagaVi alLadyGagaVi = getAlLadyGagaViRandomSampleGenerator();
        AndreiRightHandVi andreiRightHandViBack = getAndreiRightHandViRandomSampleGenerator();

        alLadyGagaVi.setAddress(andreiRightHandViBack);
        assertThat(alLadyGagaVi.getAddress()).isEqualTo(andreiRightHandViBack);

        alLadyGagaVi.address(null);
        assertThat(alLadyGagaVi.getAddress()).isNull();
    }

    @Test
    void avatarTest() {
        AlLadyGagaVi alLadyGagaVi = getAlLadyGagaViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alLadyGagaVi.setAvatar(metaverseBack);
        assertThat(alLadyGagaVi.getAvatar()).isEqualTo(metaverseBack);

        alLadyGagaVi.avatar(null);
        assertThat(alLadyGagaVi.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlLadyGagaVi alLadyGagaVi = getAlLadyGagaViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLadyGagaVi.setApplication(johnLennonBack);
        assertThat(alLadyGagaVi.getApplication()).isEqualTo(johnLennonBack);

        alLadyGagaVi.application(null);
        assertThat(alLadyGagaVi.getApplication()).isNull();
    }
}
