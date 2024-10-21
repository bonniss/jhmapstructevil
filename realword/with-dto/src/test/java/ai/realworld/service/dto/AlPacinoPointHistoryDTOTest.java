package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoPointHistoryDTO.class);
        AlPacinoPointHistoryDTO alPacinoPointHistoryDTO1 = new AlPacinoPointHistoryDTO();
        alPacinoPointHistoryDTO1.setId(1L);
        AlPacinoPointHistoryDTO alPacinoPointHistoryDTO2 = new AlPacinoPointHistoryDTO();
        assertThat(alPacinoPointHistoryDTO1).isNotEqualTo(alPacinoPointHistoryDTO2);
        alPacinoPointHistoryDTO2.setId(alPacinoPointHistoryDTO1.getId());
        assertThat(alPacinoPointHistoryDTO1).isEqualTo(alPacinoPointHistoryDTO2);
        alPacinoPointHistoryDTO2.setId(2L);
        assertThat(alPacinoPointHistoryDTO1).isNotEqualTo(alPacinoPointHistoryDTO2);
        alPacinoPointHistoryDTO1.setId(null);
        assertThat(alPacinoPointHistoryDTO1).isNotEqualTo(alPacinoPointHistoryDTO2);
    }
}
