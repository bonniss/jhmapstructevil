package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewThetaDTO.class);
        ReviewThetaDTO reviewThetaDTO1 = new ReviewThetaDTO();
        reviewThetaDTO1.setId(1L);
        ReviewThetaDTO reviewThetaDTO2 = new ReviewThetaDTO();
        assertThat(reviewThetaDTO1).isNotEqualTo(reviewThetaDTO2);
        reviewThetaDTO2.setId(reviewThetaDTO1.getId());
        assertThat(reviewThetaDTO1).isEqualTo(reviewThetaDTO2);
        reviewThetaDTO2.setId(2L);
        assertThat(reviewThetaDTO1).isNotEqualTo(reviewThetaDTO2);
        reviewThetaDTO1.setId(null);
        assertThat(reviewThetaDTO1).isNotEqualTo(reviewThetaDTO2);
    }
}
