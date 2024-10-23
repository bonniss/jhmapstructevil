package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewBetaDTO.class);
        ReviewBetaDTO reviewBetaDTO1 = new ReviewBetaDTO();
        reviewBetaDTO1.setId(1L);
        ReviewBetaDTO reviewBetaDTO2 = new ReviewBetaDTO();
        assertThat(reviewBetaDTO1).isNotEqualTo(reviewBetaDTO2);
        reviewBetaDTO2.setId(reviewBetaDTO1.getId());
        assertThat(reviewBetaDTO1).isEqualTo(reviewBetaDTO2);
        reviewBetaDTO2.setId(2L);
        assertThat(reviewBetaDTO1).isNotEqualTo(reviewBetaDTO2);
        reviewBetaDTO1.setId(null);
        assertThat(reviewBetaDTO1).isNotEqualTo(reviewBetaDTO2);
    }
}
