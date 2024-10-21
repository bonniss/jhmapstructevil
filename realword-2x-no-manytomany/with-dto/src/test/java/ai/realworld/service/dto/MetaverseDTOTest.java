package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaverseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaverseDTO.class);
        MetaverseDTO metaverseDTO1 = new MetaverseDTO();
        metaverseDTO1.setId(1L);
        MetaverseDTO metaverseDTO2 = new MetaverseDTO();
        assertThat(metaverseDTO1).isNotEqualTo(metaverseDTO2);
        metaverseDTO2.setId(metaverseDTO1.getId());
        assertThat(metaverseDTO1).isEqualTo(metaverseDTO2);
        metaverseDTO2.setId(2L);
        assertThat(metaverseDTO1).isNotEqualTo(metaverseDTO2);
        metaverseDTO1.setId(null);
        assertThat(metaverseDTO1).isNotEqualTo(metaverseDTO2);
    }
}
