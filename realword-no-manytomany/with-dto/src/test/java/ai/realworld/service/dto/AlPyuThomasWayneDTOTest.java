package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuThomasWayneDTO.class);
        AlPyuThomasWayneDTO alPyuThomasWayneDTO1 = new AlPyuThomasWayneDTO();
        alPyuThomasWayneDTO1.setId(1L);
        AlPyuThomasWayneDTO alPyuThomasWayneDTO2 = new AlPyuThomasWayneDTO();
        assertThat(alPyuThomasWayneDTO1).isNotEqualTo(alPyuThomasWayneDTO2);
        alPyuThomasWayneDTO2.setId(alPyuThomasWayneDTO1.getId());
        assertThat(alPyuThomasWayneDTO1).isEqualTo(alPyuThomasWayneDTO2);
        alPyuThomasWayneDTO2.setId(2L);
        assertThat(alPyuThomasWayneDTO1).isNotEqualTo(alPyuThomasWayneDTO2);
        alPyuThomasWayneDTO1.setId(null);
        assertThat(alPyuThomasWayneDTO1).isNotEqualTo(alPyuThomasWayneDTO2);
    }
}
