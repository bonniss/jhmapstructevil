package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RihannaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RihannaDTO.class);
        RihannaDTO rihannaDTO1 = new RihannaDTO();
        rihannaDTO1.setId(1L);
        RihannaDTO rihannaDTO2 = new RihannaDTO();
        assertThat(rihannaDTO1).isNotEqualTo(rihannaDTO2);
        rihannaDTO2.setId(rihannaDTO1.getId());
        assertThat(rihannaDTO1).isEqualTo(rihannaDTO2);
        rihannaDTO2.setId(2L);
        assertThat(rihannaDTO1).isNotEqualTo(rihannaDTO2);
        rihannaDTO1.setId(null);
        assertThat(rihannaDTO1).isNotEqualTo(rihannaDTO2);
    }
}
