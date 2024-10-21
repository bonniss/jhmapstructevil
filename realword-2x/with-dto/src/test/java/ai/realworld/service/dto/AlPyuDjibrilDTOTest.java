package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuDjibrilDTO.class);
        AlPyuDjibrilDTO alPyuDjibrilDTO1 = new AlPyuDjibrilDTO();
        alPyuDjibrilDTO1.setId(1L);
        AlPyuDjibrilDTO alPyuDjibrilDTO2 = new AlPyuDjibrilDTO();
        assertThat(alPyuDjibrilDTO1).isNotEqualTo(alPyuDjibrilDTO2);
        alPyuDjibrilDTO2.setId(alPyuDjibrilDTO1.getId());
        assertThat(alPyuDjibrilDTO1).isEqualTo(alPyuDjibrilDTO2);
        alPyuDjibrilDTO2.setId(2L);
        assertThat(alPyuDjibrilDTO1).isNotEqualTo(alPyuDjibrilDTO2);
        alPyuDjibrilDTO1.setId(null);
        assertThat(alPyuDjibrilDTO1).isNotEqualTo(alPyuDjibrilDTO2);
    }
}
