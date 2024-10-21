package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewMiDTO.class);
        ReviewMiDTO reviewMiDTO1 = new ReviewMiDTO();
        reviewMiDTO1.setId(1L);
        ReviewMiDTO reviewMiDTO2 = new ReviewMiDTO();
        assertThat(reviewMiDTO1).isNotEqualTo(reviewMiDTO2);
        reviewMiDTO2.setId(reviewMiDTO1.getId());
        assertThat(reviewMiDTO1).isEqualTo(reviewMiDTO2);
        reviewMiDTO2.setId(2L);
        assertThat(reviewMiDTO1).isNotEqualTo(reviewMiDTO2);
        reviewMiDTO1.setId(null);
        assertThat(reviewMiDTO1).isNotEqualTo(reviewMiDTO2);
    }
}
