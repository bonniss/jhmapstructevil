package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HexCharViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HexCharViDTO.class);
        HexCharViDTO hexCharViDTO1 = new HexCharViDTO();
        hexCharViDTO1.setId(1L);
        HexCharViDTO hexCharViDTO2 = new HexCharViDTO();
        assertThat(hexCharViDTO1).isNotEqualTo(hexCharViDTO2);
        hexCharViDTO2.setId(hexCharViDTO1.getId());
        assertThat(hexCharViDTO1).isEqualTo(hexCharViDTO2);
        hexCharViDTO2.setId(2L);
        assertThat(hexCharViDTO1).isNotEqualTo(hexCharViDTO2);
        hexCharViDTO1.setId(null);
        assertThat(hexCharViDTO1).isNotEqualTo(hexCharViDTO2);
    }
}
