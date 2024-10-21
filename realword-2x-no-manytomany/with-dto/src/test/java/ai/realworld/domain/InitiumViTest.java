package ai.realworld.domain;

import static ai.realworld.domain.InitiumViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InitiumViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InitiumVi.class);
        InitiumVi initiumVi1 = getInitiumViSample1();
        InitiumVi initiumVi2 = new InitiumVi();
        assertThat(initiumVi1).isNotEqualTo(initiumVi2);

        initiumVi2.setId(initiumVi1.getId());
        assertThat(initiumVi1).isEqualTo(initiumVi2);

        initiumVi2 = getInitiumViSample2();
        assertThat(initiumVi1).isNotEqualTo(initiumVi2);
    }
}
