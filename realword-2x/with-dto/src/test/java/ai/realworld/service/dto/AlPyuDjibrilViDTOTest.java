package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuDjibrilViDTO.class);
        AlPyuDjibrilViDTO alPyuDjibrilViDTO1 = new AlPyuDjibrilViDTO();
        alPyuDjibrilViDTO1.setId(1L);
        AlPyuDjibrilViDTO alPyuDjibrilViDTO2 = new AlPyuDjibrilViDTO();
        assertThat(alPyuDjibrilViDTO1).isNotEqualTo(alPyuDjibrilViDTO2);
        alPyuDjibrilViDTO2.setId(alPyuDjibrilViDTO1.getId());
        assertThat(alPyuDjibrilViDTO1).isEqualTo(alPyuDjibrilViDTO2);
        alPyuDjibrilViDTO2.setId(2L);
        assertThat(alPyuDjibrilViDTO1).isNotEqualTo(alPyuDjibrilViDTO2);
        alPyuDjibrilViDTO1.setId(null);
        assertThat(alPyuDjibrilViDTO1).isNotEqualTo(alPyuDjibrilViDTO2);
    }
}
