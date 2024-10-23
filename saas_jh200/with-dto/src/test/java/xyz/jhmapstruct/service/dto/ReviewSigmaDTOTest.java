package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewSigmaDTO.class);
        ReviewSigmaDTO reviewSigmaDTO1 = new ReviewSigmaDTO();
        reviewSigmaDTO1.setId(1L);
        ReviewSigmaDTO reviewSigmaDTO2 = new ReviewSigmaDTO();
        assertThat(reviewSigmaDTO1).isNotEqualTo(reviewSigmaDTO2);
        reviewSigmaDTO2.setId(reviewSigmaDTO1.getId());
        assertThat(reviewSigmaDTO1).isEqualTo(reviewSigmaDTO2);
        reviewSigmaDTO2.setId(2L);
        assertThat(reviewSigmaDTO1).isNotEqualTo(reviewSigmaDTO2);
        reviewSigmaDTO1.setId(null);
        assertThat(reviewSigmaDTO1).isNotEqualTo(reviewSigmaDTO2);
    }
}
