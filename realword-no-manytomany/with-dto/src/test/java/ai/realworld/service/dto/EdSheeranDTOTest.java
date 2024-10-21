package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdSheeranDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EdSheeranDTO.class);
        EdSheeranDTO edSheeranDTO1 = new EdSheeranDTO();
        edSheeranDTO1.setId(1L);
        EdSheeranDTO edSheeranDTO2 = new EdSheeranDTO();
        assertThat(edSheeranDTO1).isNotEqualTo(edSheeranDTO2);
        edSheeranDTO2.setId(edSheeranDTO1.getId());
        assertThat(edSheeranDTO1).isEqualTo(edSheeranDTO2);
        edSheeranDTO2.setId(2L);
        assertThat(edSheeranDTO1).isNotEqualTo(edSheeranDTO2);
        edSheeranDTO1.setId(null);
        assertThat(edSheeranDTO1).isNotEqualTo(edSheeranDTO2);
    }
}
