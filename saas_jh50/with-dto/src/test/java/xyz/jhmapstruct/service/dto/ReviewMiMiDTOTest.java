package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewMiMiDTO.class);
        ReviewMiMiDTO reviewMiMiDTO1 = new ReviewMiMiDTO();
        reviewMiMiDTO1.setId(1L);
        ReviewMiMiDTO reviewMiMiDTO2 = new ReviewMiMiDTO();
        assertThat(reviewMiMiDTO1).isNotEqualTo(reviewMiMiDTO2);
        reviewMiMiDTO2.setId(reviewMiMiDTO1.getId());
        assertThat(reviewMiMiDTO1).isEqualTo(reviewMiMiDTO2);
        reviewMiMiDTO2.setId(2L);
        assertThat(reviewMiMiDTO1).isNotEqualTo(reviewMiMiDTO2);
        reviewMiMiDTO1.setId(null);
        assertThat(reviewMiMiDTO1).isNotEqualTo(reviewMiMiDTO2);
    }
}
