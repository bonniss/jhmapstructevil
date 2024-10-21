package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RihannaViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RihannaViDTO.class);
        RihannaViDTO rihannaViDTO1 = new RihannaViDTO();
        rihannaViDTO1.setId(1L);
        RihannaViDTO rihannaViDTO2 = new RihannaViDTO();
        assertThat(rihannaViDTO1).isNotEqualTo(rihannaViDTO2);
        rihannaViDTO2.setId(rihannaViDTO1.getId());
        assertThat(rihannaViDTO1).isEqualTo(rihannaViDTO2);
        rihannaViDTO2.setId(2L);
        assertThat(rihannaViDTO1).isNotEqualTo(rihannaViDTO2);
        rihannaViDTO1.setId(null);
        assertThat(rihannaViDTO1).isNotEqualTo(rihannaViDTO2);
    }
}
