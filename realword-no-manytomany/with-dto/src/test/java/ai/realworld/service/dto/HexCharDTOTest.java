package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HexCharDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HexCharDTO.class);
        HexCharDTO hexCharDTO1 = new HexCharDTO();
        hexCharDTO1.setId(1L);
        HexCharDTO hexCharDTO2 = new HexCharDTO();
        assertThat(hexCharDTO1).isNotEqualTo(hexCharDTO2);
        hexCharDTO2.setId(hexCharDTO1.getId());
        assertThat(hexCharDTO1).isEqualTo(hexCharDTO2);
        hexCharDTO2.setId(2L);
        assertThat(hexCharDTO1).isNotEqualTo(hexCharDTO2);
        hexCharDTO1.setId(null);
        assertThat(hexCharDTO1).isNotEqualTo(hexCharDTO2);
    }
}
