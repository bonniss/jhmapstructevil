package ai.realworld.domain;

import static ai.realworld.domain.HashRossViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashRossViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashRossVi.class);
        HashRossVi hashRossVi1 = getHashRossViSample1();
        HashRossVi hashRossVi2 = new HashRossVi();
        assertThat(hashRossVi1).isNotEqualTo(hashRossVi2);

        hashRossVi2.setId(hashRossVi1.getId());
        assertThat(hashRossVi1).isEqualTo(hashRossVi2);

        hashRossVi2 = getHashRossViSample2();
        assertThat(hashRossVi1).isNotEqualTo(hashRossVi2);
    }
}
