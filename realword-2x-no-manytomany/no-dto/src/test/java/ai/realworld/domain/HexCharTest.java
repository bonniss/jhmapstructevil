package ai.realworld.domain;

import static ai.realworld.domain.HashRossTestSamples.*;
import static ai.realworld.domain.HexCharTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HexCharTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HexChar.class);
        HexChar hexChar1 = getHexCharSample1();
        HexChar hexChar2 = new HexChar();
        assertThat(hexChar1).isNotEqualTo(hexChar2);

        hexChar2.setId(hexChar1.getId());
        assertThat(hexChar1).isEqualTo(hexChar2);

        hexChar2 = getHexCharSample2();
        assertThat(hexChar1).isNotEqualTo(hexChar2);
    }

    @Test
    void roleTest() {
        HexChar hexChar = getHexCharRandomSampleGenerator();
        HashRoss hashRossBack = getHashRossRandomSampleGenerator();

        hexChar.setRole(hashRossBack);
        assertThat(hexChar.getRole()).isEqualTo(hashRossBack);

        hexChar.role(null);
        assertThat(hexChar.getRole()).isNull();
    }
}
