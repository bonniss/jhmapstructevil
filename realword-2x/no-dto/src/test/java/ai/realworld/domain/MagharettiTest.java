package ai.realworld.domain;

import static ai.realworld.domain.MagharettiTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagharettiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magharetti.class);
        Magharetti magharetti1 = getMagharettiSample1();
        Magharetti magharetti2 = new Magharetti();
        assertThat(magharetti1).isNotEqualTo(magharetti2);

        magharetti2.setId(magharetti1.getId());
        assertThat(magharetti1).isEqualTo(magharetti2);

        magharetti2 = getMagharettiSample2();
        assertThat(magharetti1).isNotEqualTo(magharetti2);
    }
}
