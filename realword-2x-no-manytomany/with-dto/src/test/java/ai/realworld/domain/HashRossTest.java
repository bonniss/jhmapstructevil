package ai.realworld.domain;

import static ai.realworld.domain.HashRossTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashRossTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashRoss.class);
        HashRoss hashRoss1 = getHashRossSample1();
        HashRoss hashRoss2 = new HashRoss();
        assertThat(hashRoss1).isNotEqualTo(hashRoss2);

        hashRoss2.setId(hashRoss1.getId());
        assertThat(hashRoss1).isEqualTo(hashRoss2);

        hashRoss2 = getHashRossSample2();
        assertThat(hashRoss1).isNotEqualTo(hashRoss2);
    }
}
