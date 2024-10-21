package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuThomasWayneViDTO.class);
        AlPyuThomasWayneViDTO alPyuThomasWayneViDTO1 = new AlPyuThomasWayneViDTO();
        alPyuThomasWayneViDTO1.setId(1L);
        AlPyuThomasWayneViDTO alPyuThomasWayneViDTO2 = new AlPyuThomasWayneViDTO();
        assertThat(alPyuThomasWayneViDTO1).isNotEqualTo(alPyuThomasWayneViDTO2);
        alPyuThomasWayneViDTO2.setId(alPyuThomasWayneViDTO1.getId());
        assertThat(alPyuThomasWayneViDTO1).isEqualTo(alPyuThomasWayneViDTO2);
        alPyuThomasWayneViDTO2.setId(2L);
        assertThat(alPyuThomasWayneViDTO1).isNotEqualTo(alPyuThomasWayneViDTO2);
        alPyuThomasWayneViDTO1.setId(null);
        assertThat(alPyuThomasWayneViDTO1).isNotEqualTo(alPyuThomasWayneViDTO2);
    }
}
