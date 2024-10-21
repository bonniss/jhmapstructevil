package ai.realworld.domain;

import static ai.realworld.domain.AlLadyGagaTestSamples.*;
import static ai.realworld.domain.AndreiRightHandTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLadyGagaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLadyGaga.class);
        AlLadyGaga alLadyGaga1 = getAlLadyGagaSample1();
        AlLadyGaga alLadyGaga2 = new AlLadyGaga();
        assertThat(alLadyGaga1).isNotEqualTo(alLadyGaga2);

        alLadyGaga2.setId(alLadyGaga1.getId());
        assertThat(alLadyGaga1).isEqualTo(alLadyGaga2);

        alLadyGaga2 = getAlLadyGagaSample2();
        assertThat(alLadyGaga1).isNotEqualTo(alLadyGaga2);
    }

    @Test
    void addressTest() {
        AlLadyGaga alLadyGaga = getAlLadyGagaRandomSampleGenerator();
        AndreiRightHand andreiRightHandBack = getAndreiRightHandRandomSampleGenerator();

        alLadyGaga.setAddress(andreiRightHandBack);
        assertThat(alLadyGaga.getAddress()).isEqualTo(andreiRightHandBack);

        alLadyGaga.address(null);
        assertThat(alLadyGaga.getAddress()).isNull();
    }

    @Test
    void avatarTest() {
        AlLadyGaga alLadyGaga = getAlLadyGagaRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alLadyGaga.setAvatar(metaverseBack);
        assertThat(alLadyGaga.getAvatar()).isEqualTo(metaverseBack);

        alLadyGaga.avatar(null);
        assertThat(alLadyGaga.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlLadyGaga alLadyGaga = getAlLadyGagaRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLadyGaga.setApplication(johnLennonBack);
        assertThat(alLadyGaga.getApplication()).isEqualTo(johnLennonBack);

        alLadyGaga.application(null);
        assertThat(alLadyGaga.getApplication()).isNull();
    }
}
