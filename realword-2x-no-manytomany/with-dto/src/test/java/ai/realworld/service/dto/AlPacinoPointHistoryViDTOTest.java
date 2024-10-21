package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoPointHistoryViDTO.class);
        AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO1 = new AlPacinoPointHistoryViDTO();
        alPacinoPointHistoryViDTO1.setId(1L);
        AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO2 = new AlPacinoPointHistoryViDTO();
        assertThat(alPacinoPointHistoryViDTO1).isNotEqualTo(alPacinoPointHistoryViDTO2);
        alPacinoPointHistoryViDTO2.setId(alPacinoPointHistoryViDTO1.getId());
        assertThat(alPacinoPointHistoryViDTO1).isEqualTo(alPacinoPointHistoryViDTO2);
        alPacinoPointHistoryViDTO2.setId(2L);
        assertThat(alPacinoPointHistoryViDTO1).isNotEqualTo(alPacinoPointHistoryViDTO2);
        alPacinoPointHistoryViDTO1.setId(null);
        assertThat(alPacinoPointHistoryViDTO1).isNotEqualTo(alPacinoPointHistoryViDTO2);
    }
}
