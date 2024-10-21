package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlCatalinaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlCatalinaDTO.class);
        AlCatalinaDTO alCatalinaDTO1 = new AlCatalinaDTO();
        alCatalinaDTO1.setId(1L);
        AlCatalinaDTO alCatalinaDTO2 = new AlCatalinaDTO();
        assertThat(alCatalinaDTO1).isNotEqualTo(alCatalinaDTO2);
        alCatalinaDTO2.setId(alCatalinaDTO1.getId());
        assertThat(alCatalinaDTO1).isEqualTo(alCatalinaDTO2);
        alCatalinaDTO2.setId(2L);
        assertThat(alCatalinaDTO1).isNotEqualTo(alCatalinaDTO2);
        alCatalinaDTO1.setId(null);
        assertThat(alCatalinaDTO1).isNotEqualTo(alCatalinaDTO2);
    }
}
