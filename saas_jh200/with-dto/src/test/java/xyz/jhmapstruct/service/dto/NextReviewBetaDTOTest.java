package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewBetaDTO.class);
        NextReviewBetaDTO nextReviewBetaDTO1 = new NextReviewBetaDTO();
        nextReviewBetaDTO1.setId(1L);
        NextReviewBetaDTO nextReviewBetaDTO2 = new NextReviewBetaDTO();
        assertThat(nextReviewBetaDTO1).isNotEqualTo(nextReviewBetaDTO2);
        nextReviewBetaDTO2.setId(nextReviewBetaDTO1.getId());
        assertThat(nextReviewBetaDTO1).isEqualTo(nextReviewBetaDTO2);
        nextReviewBetaDTO2.setId(2L);
        assertThat(nextReviewBetaDTO1).isNotEqualTo(nextReviewBetaDTO2);
        nextReviewBetaDTO1.setId(null);
        assertThat(nextReviewBetaDTO1).isNotEqualTo(nextReviewBetaDTO2);
    }
}
