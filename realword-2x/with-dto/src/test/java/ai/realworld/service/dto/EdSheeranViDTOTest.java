package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdSheeranViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EdSheeranViDTO.class);
        EdSheeranViDTO edSheeranViDTO1 = new EdSheeranViDTO();
        edSheeranViDTO1.setId(1L);
        EdSheeranViDTO edSheeranViDTO2 = new EdSheeranViDTO();
        assertThat(edSheeranViDTO1).isNotEqualTo(edSheeranViDTO2);
        edSheeranViDTO2.setId(edSheeranViDTO1.getId());
        assertThat(edSheeranViDTO1).isEqualTo(edSheeranViDTO2);
        edSheeranViDTO2.setId(2L);
        assertThat(edSheeranViDTO1).isNotEqualTo(edSheeranViDTO2);
        edSheeranViDTO1.setId(null);
        assertThat(edSheeranViDTO1).isNotEqualTo(edSheeranViDTO2);
    }
}
