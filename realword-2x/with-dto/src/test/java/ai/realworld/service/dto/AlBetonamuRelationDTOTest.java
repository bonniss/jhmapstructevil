package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBetonamuRelationDTO.class);
        AlBetonamuRelationDTO alBetonamuRelationDTO1 = new AlBetonamuRelationDTO();
        alBetonamuRelationDTO1.setId(1L);
        AlBetonamuRelationDTO alBetonamuRelationDTO2 = new AlBetonamuRelationDTO();
        assertThat(alBetonamuRelationDTO1).isNotEqualTo(alBetonamuRelationDTO2);
        alBetonamuRelationDTO2.setId(alBetonamuRelationDTO1.getId());
        assertThat(alBetonamuRelationDTO1).isEqualTo(alBetonamuRelationDTO2);
        alBetonamuRelationDTO2.setId(2L);
        assertThat(alBetonamuRelationDTO1).isNotEqualTo(alBetonamuRelationDTO2);
        alBetonamuRelationDTO1.setId(null);
        assertThat(alBetonamuRelationDTO1).isNotEqualTo(alBetonamuRelationDTO2);
    }
}
