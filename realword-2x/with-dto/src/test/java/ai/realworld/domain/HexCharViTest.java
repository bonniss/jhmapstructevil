package ai.realworld.domain;

import static ai.realworld.domain.HashRossViTestSamples.*;
import static ai.realworld.domain.HexCharViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HexCharViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HexCharVi.class);
        HexCharVi hexCharVi1 = getHexCharViSample1();
        HexCharVi hexCharVi2 = new HexCharVi();
        assertThat(hexCharVi1).isNotEqualTo(hexCharVi2);

        hexCharVi2.setId(hexCharVi1.getId());
        assertThat(hexCharVi1).isEqualTo(hexCharVi2);

        hexCharVi2 = getHexCharViSample2();
        assertThat(hexCharVi1).isNotEqualTo(hexCharVi2);
    }

    @Test
    void roleTest() {
        HexCharVi hexCharVi = getHexCharViRandomSampleGenerator();
        HashRossVi hashRossViBack = getHashRossViRandomSampleGenerator();

        hexCharVi.setRole(hashRossViBack);
        assertThat(hexCharVi.getRole()).isEqualTo(hashRossViBack);

        hexCharVi.role(null);
        assertThat(hexCharVi.getRole()).isNull();
    }
}
