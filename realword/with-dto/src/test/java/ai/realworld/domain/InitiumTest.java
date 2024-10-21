package ai.realworld.domain;

import static ai.realworld.domain.InitiumTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InitiumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Initium.class);
        Initium initium1 = getInitiumSample1();
        Initium initium2 = new Initium();
        assertThat(initium1).isNotEqualTo(initium2);

        initium2.setId(initium1.getId());
        assertThat(initium1).isEqualTo(initium2);

        initium2 = getInitiumSample2();
        assertThat(initium1).isNotEqualTo(initium2);
    }
}
