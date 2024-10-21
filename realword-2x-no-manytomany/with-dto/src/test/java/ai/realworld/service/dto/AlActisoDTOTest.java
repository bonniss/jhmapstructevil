package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlActisoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlActisoDTO.class);
        AlActisoDTO alActisoDTO1 = new AlActisoDTO();
        alActisoDTO1.setId(1L);
        AlActisoDTO alActisoDTO2 = new AlActisoDTO();
        assertThat(alActisoDTO1).isNotEqualTo(alActisoDTO2);
        alActisoDTO2.setId(alActisoDTO1.getId());
        assertThat(alActisoDTO1).isEqualTo(alActisoDTO2);
        alActisoDTO2.setId(2L);
        assertThat(alActisoDTO1).isNotEqualTo(alActisoDTO2);
        alActisoDTO1.setId(null);
        assertThat(alActisoDTO1).isNotEqualTo(alActisoDTO2);
    }
}
