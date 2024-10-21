package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewViViDTO.class);
        ReviewViViDTO reviewViViDTO1 = new ReviewViViDTO();
        reviewViViDTO1.setId(1L);
        ReviewViViDTO reviewViViDTO2 = new ReviewViViDTO();
        assertThat(reviewViViDTO1).isNotEqualTo(reviewViViDTO2);
        reviewViViDTO2.setId(reviewViViDTO1.getId());
        assertThat(reviewViViDTO1).isEqualTo(reviewViViDTO2);
        reviewViViDTO2.setId(2L);
        assertThat(reviewViViDTO1).isNotEqualTo(reviewViViDTO2);
        reviewViViDTO1.setId(null);
        assertThat(reviewViViDTO1).isNotEqualTo(reviewViViDTO2);
    }
}
