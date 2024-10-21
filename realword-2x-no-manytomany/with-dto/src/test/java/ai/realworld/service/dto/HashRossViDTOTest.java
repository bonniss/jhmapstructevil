package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashRossViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashRossViDTO.class);
        HashRossViDTO hashRossViDTO1 = new HashRossViDTO();
        hashRossViDTO1.setId(1L);
        HashRossViDTO hashRossViDTO2 = new HashRossViDTO();
        assertThat(hashRossViDTO1).isNotEqualTo(hashRossViDTO2);
        hashRossViDTO2.setId(hashRossViDTO1.getId());
        assertThat(hashRossViDTO1).isEqualTo(hashRossViDTO2);
        hashRossViDTO2.setId(2L);
        assertThat(hashRossViDTO1).isNotEqualTo(hashRossViDTO2);
        hashRossViDTO1.setId(null);
        assertThat(hashRossViDTO1).isNotEqualTo(hashRossViDTO2);
    }
}
