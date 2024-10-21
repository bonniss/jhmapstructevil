package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashRossDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashRossDTO.class);
        HashRossDTO hashRossDTO1 = new HashRossDTO();
        hashRossDTO1.setId(1L);
        HashRossDTO hashRossDTO2 = new HashRossDTO();
        assertThat(hashRossDTO1).isNotEqualTo(hashRossDTO2);
        hashRossDTO2.setId(hashRossDTO1.getId());
        assertThat(hashRossDTO1).isEqualTo(hashRossDTO2);
        hashRossDTO2.setId(2L);
        assertThat(hashRossDTO1).isNotEqualTo(hashRossDTO2);
        hashRossDTO1.setId(null);
        assertThat(hashRossDTO1).isNotEqualTo(hashRossDTO2);
    }
}
