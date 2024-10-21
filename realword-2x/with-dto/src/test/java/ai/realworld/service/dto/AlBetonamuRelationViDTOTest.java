package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBetonamuRelationViDTO.class);
        AlBetonamuRelationViDTO alBetonamuRelationViDTO1 = new AlBetonamuRelationViDTO();
        alBetonamuRelationViDTO1.setId(1L);
        AlBetonamuRelationViDTO alBetonamuRelationViDTO2 = new AlBetonamuRelationViDTO();
        assertThat(alBetonamuRelationViDTO1).isNotEqualTo(alBetonamuRelationViDTO2);
        alBetonamuRelationViDTO2.setId(alBetonamuRelationViDTO1.getId());
        assertThat(alBetonamuRelationViDTO1).isEqualTo(alBetonamuRelationViDTO2);
        alBetonamuRelationViDTO2.setId(2L);
        assertThat(alBetonamuRelationViDTO1).isNotEqualTo(alBetonamuRelationViDTO2);
        alBetonamuRelationViDTO1.setId(null);
        assertThat(alBetonamuRelationViDTO1).isNotEqualTo(alBetonamuRelationViDTO2);
    }
}
