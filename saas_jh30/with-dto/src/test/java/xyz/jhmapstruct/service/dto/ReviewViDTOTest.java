package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewViDTO.class);
        ReviewViDTO reviewViDTO1 = new ReviewViDTO();
        reviewViDTO1.setId(1L);
        ReviewViDTO reviewViDTO2 = new ReviewViDTO();
        assertThat(reviewViDTO1).isNotEqualTo(reviewViDTO2);
        reviewViDTO2.setId(reviewViDTO1.getId());
        assertThat(reviewViDTO1).isEqualTo(reviewViDTO2);
        reviewViDTO2.setId(2L);
        assertThat(reviewViDTO1).isNotEqualTo(reviewViDTO2);
        reviewViDTO1.setId(null);
        assertThat(reviewViDTO1).isNotEqualTo(reviewViDTO2);
    }
}
